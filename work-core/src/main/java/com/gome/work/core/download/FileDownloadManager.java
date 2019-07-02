package com.gome.work.core.download;


import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AndroidRuntimeException;

import com.gome.core.greendao.FileItemInfoDao;
import com.gome.utils.CommonUtils;
import com.gome.utils.FileUtil;
import com.gome.work.core.model.FileDownTaskInfo;
import com.gome.work.core.model.dao.FileItemInfo;
import com.gome.work.core.model.dao.FileTransferTaskInfo;
import com.gome.work.core.persistence.DaoUtil;
import com.gome.work.core.upload.IGlobalTransferListener;
import com.liulishuo.okdownload.DownloadTask;
import com.liulishuo.okdownload.SpeedCalculator;
import com.liulishuo.okdownload.StatusUtil;
import com.liulishuo.okdownload.core.breakpoint.BlockInfo;
import com.liulishuo.okdownload.core.breakpoint.BreakpointInfo;
import com.liulishuo.okdownload.core.cause.EndCause;
import com.liulishuo.okdownload.core.listener.DownloadListener4WithSpeed;
import com.liulishuo.okdownload.core.listener.assist.Listener4SpeedAssistExtend;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 文件下载管理类
 */

public class FileDownloadManager {

    private static FileDownloadManager instance;

    private Context mContext;

    private Map<Long, DownloadTask> mDownloadTasks = new HashMap<>();

//    private Map<Long, Set<DownloadListener>> mDownloadListener = new HashMap<>();

    private Set<IGlobalTransferListener> mUploadListeners = new HashSet<>();

    public void addGlobalTransferListener(IGlobalTransferListener listener) {
        mUploadListeners.add(listener);
    }

    public void removeGlobalTransferListener(IGlobalTransferListener listener) {
        mUploadListeners.remove(listener);
    }

    private ExecutorService mExecutor = Executors.newSingleThreadExecutor();

    private FileDownloadManager(Context context) {
        mContext = context;
        daoUtil = new DaoUtil(context);
    }

    private DaoUtil daoUtil;

    public static FileDownloadManager getInstance(Context context) {
        if (instance == null) {
            synchronized (FileDownloadManager.class) {
                if (instance == null) {
                    instance = new FileDownloadManager(context);
                }
            }
        }
        return instance;
    }


    public void initState(FileTransferTaskInfo info) {
        DownloadTask task = mDownloadTasks.get(info.getId());
        if (task != null) {
            final StatusUtil.Status status = StatusUtil.getStatus(task);
            switch (status) {
                case IDLE:
                case UNKNOWN:
                    info.setState(FileTransferTaskInfo.STATE_IDLE);
                    break;
                case PENDING:
                    info.setState(FileTransferTaskInfo.STATE_IDLE);
                    break;
                case RUNNING:
                    info.setState(FileTransferTaskInfo.STATE_DOING);
                    break;
                case COMPLETED:
                    info.setState(FileTransferTaskInfo.STATE_OK);
                    break;
                default:
                    break;
            }
        } else if (FileTransferTaskInfo.STATE_DOING.equals(info.getState())) {
            info.setState(FileTransferTaskInfo.STATE_FAIL);
        }
    }

    public void stopTask(FileTransferTaskInfo info) {
        DownloadTask task = mDownloadTasks.get(info.getId());
        if (task != null) {
            task.cancel();
        }
    }


    private void completeInfoIfNeed(FileDownTaskInfo downTaskInfo) {
        if (TextUtils.isEmpty(downTaskInfo.fileName)) {
            downTaskInfo.fileName = Uri.parse(downTaskInfo.url).getLastPathSegment();
        }
        if (TextUtils.isEmpty(downTaskInfo.mimeType)) {
            downTaskInfo.mimeType = CommonUtils.getMIMEType(downTaskInfo.url);
        }
    }

    /**
     * 无论是否有改url的下载任务，都会强制下载
     *
     * @param downTaskInfo
     */
    public FileTransferTaskInfo startTaskIgnoreIfExist(FileDownTaskInfo downTaskInfo) {
        completeInfoIfNeed(downTaskInfo);
        String filePath = getCacheFilePath(downTaskInfo.fileName);

        File file = new File(filePath);
        downTaskInfo.fileName = file.getName();

        final FileTransferTaskInfo taskInfo = new FileTransferTaskInfo();
        taskInfo.setState(FileTransferTaskInfo.STATE_IDLE);
        taskInfo.setDownloadUrl(downTaskInfo.url);
        taskInfo.setCreateDate(System.currentTimeMillis());
        taskInfo.setFileName(downTaskInfo.fileName);
        taskInfo.setDirect(0);
        taskInfo.setFilePath(filePath);
        taskInfo.setContentLength(downTaskInfo.contentLength);
        taskInfo.setFromSourceCode(downTaskInfo.fromSourceType);
        taskInfo.setFromSourceName(downTaskInfo.fromSource);
        taskInfo.setMimeType(downTaskInfo.mimeType);

        final long taskId = daoUtil.getFileTransferTaskInfoDao().insert(taskInfo);
        taskInfo.setId(taskId);

        startTask(taskInfo, null);
        return taskInfo;
    }


    public void startTask(FileTransferTaskInfo taskInfo) {
        startTask(taskInfo, null);
    }

    public void startTask(FileTransferTaskInfo taskInfo, IDownloadListener listener) {
        DownloadTask task = download(taskInfo, listener);
        mDownloadTasks.put(taskInfo.getId(), task);
    }

    /**
     * 记录一次文件下载完毕传文件的记录
     *
     * @param filePath
     */

    public void tagEvent(final String filePath) {
        mExecutor.submit(new Runnable() {
            @Override
            public void run() {
                List<FileItemInfo> result = daoUtil.getFileItemInfoDao().queryBuilder()
                        .where(FileItemInfoDao.Properties.Path.eq(filePath)).limit(1).list();
                if (result != null && !result.isEmpty()) {
                    FileItemInfo info = result.get(0);
                    info.setUpdateDate(System.currentTimeMillis());
                    daoUtil.getFileItemInfoDao().update(info);
                } else {
                    FileItemInfo info = new FileItemInfo();
                    info.setUpdateDate(System.currentTimeMillis());
                    info.setCreateDate(System.currentTimeMillis());
                    File file = new File(filePath);
                    info.setName(file.getName());
                    info.setFromSourceName("来自聊天的文件");
                    info.setFromSourceCode("im");
                    info.setPath(filePath);
                    info.setContentLength(file.length());
                    info.setType(FileItemInfo.getRecognizeFileType(file.getName()));
                    daoUtil.getFileItemInfoDao().save(info);
                }
            }
        });
    }


    public FileTransferTaskInfo startTask(FileDownTaskInfo taskInfo) {
        for (Map.Entry<Long, DownloadTask> item : mDownloadTasks.entrySet()) {
            if (item.getValue().getUrl().equals(taskInfo.url)) {
                return null;
            }
        }
        return startTaskIgnoreIfExist(taskInfo);
    }

    private DownloadTask download(FileTransferTaskInfo info, final IDownloadListener downloadListener) {
        String fileName = info.getFileName();
        if (TextUtils.isEmpty(fileName)) {
            throw new AndroidRuntimeException("要保存的文件名不能为空");
        }
        File file = new File(info.getFilePath());
        if (file.exists()) {
            file.delete();
        }
        DownloadTask task = new DownloadTask.Builder(info.getDownloadUrl(), file.getParentFile())
                .setMinIntervalMillisCallbackProcess(200)
                .setPassIfAlreadyCompleted(false)
                .setAutoCallbackToUIThread(true)
                .setFilename(fileName)
                .setConnectionCount(1)
                .build();

        task.enqueue(new MyDownloadListenerProxy(info, downloadListener));
        return task;
    }

    public static String getDate() {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss", Locale.getDefault());
        Date date = new Date(System.currentTimeMillis());
        return sf.format(date);
    }

    private String getCacheFilePath(String name) {
        File baseFile = mContext.getExternalCacheDir();
        File baseFileFolder = new File(baseFile, "Download");
        if (!baseFileFolder.exists()) {
            baseFile.mkdirs();
        }
        File file = new File(baseFileFolder, name);
        if (file.exists()) {
            String suffix = FileUtil.getFileExtension(name);
            String realName = name;
            if (!TextUtils.isEmpty(suffix)) {
                realName = name.replace("." + suffix, "");
            }
            for (int i = 1; i < 10000; i++) {
                String newName = "";
                if (TextUtils.isEmpty(suffix)) {
                    newName = realName + "(" + i + ")";
                } else {
                    newName = realName + "(" + i + ")" + "." + suffix;
                }
                file = new File(baseFileFolder, newName);
                if (!file.exists()) {
                    break;
                }
            }
        }
        if (file.exists()) {
            file.delete();
        }
        return file.getAbsolutePath();
    }

    class MyDownloadListenerProxy extends DownloadListener4WithSpeed {

        public FileTransferTaskInfo taskInfo;

        public IDownloadListener progressChangeListener;

        public MyDownloadListenerProxy(FileTransferTaskInfo taskInfo, IDownloadListener progressChangeListener) {
            this.taskInfo = taskInfo;
            this.progressChangeListener = progressChangeListener;
        }

        public Set<IGlobalTransferListener> getThreadSafeArray() {
            return new HashSet<>(mUploadListeners);
        }

        @Override
        public void taskStart(@NonNull DownloadTask task) {
            taskInfo.setState(FileTransferTaskInfo.STATE_DOING);
            daoUtil.getFileTransferTaskInfoDao().update(taskInfo);

            final Set<IGlobalTransferListener> listeners = getThreadSafeArray();
            for (final IGlobalTransferListener realOne : listeners) {
                if (realOne != null) {
                    realOne.onStateChanged(taskInfo);
                }
            }
            if (progressChangeListener != null) {
                progressChangeListener.onStart();
            }
        }

        @Override
        public void connectStart(@NonNull DownloadTask task, int blockIndex, @NonNull Map<String, List<String>> requestHeaderFields) {

        }

        @Override
        public void connectEnd(@NonNull DownloadTask task, int blockIndex, int responseCode,
                               @NonNull Map<String, List<String>> responseHeaderFields) {
        }

        @Override
        public void fetchStart(@NonNull DownloadTask task, int blockIndex, long contentLength) {
            if (taskInfo.getContentLength() == 0) {
                taskInfo.setContentLength(contentLength);
                daoUtil.getFileTransferTaskInfoDao().update(taskInfo);
            }
        }


        @Override
        public void fetchEnd(@NonNull DownloadTask task, int blockIndex, long contentLength) {

        }

        @Override
        public void infoReady(@NonNull DownloadTask task, @NonNull BreakpointInfo info, boolean fromBreakpoint, @NonNull Listener4SpeedAssistExtend.Listener4SpeedModel model) {

        }

        @Override
        public void progressBlock(@NonNull DownloadTask task, int blockIndex, long currentBlockOffset, @NonNull SpeedCalculator blockSpeed) {

        }

        @Override
        public void progress(@NonNull DownloadTask task, long currentOffset, @NonNull SpeedCalculator taskSpeed) {
            final Set<IGlobalTransferListener> listeners = getThreadSafeArray();
            for (final IGlobalTransferListener realOne : listeners) {
                if (realOne != null) {
                    realOne.onProcess(taskInfo, taskInfo.getContentLength(), currentOffset);
                }
            }
            if (progressChangeListener != null) {
                progressChangeListener.onProcess(taskInfo.getContentLength(), currentOffset, taskSpeed);
            }
        }

        @Override
        public void blockEnd(@NonNull DownloadTask task, int blockIndex, BlockInfo info, @NonNull SpeedCalculator blockSpeed) {

        }

        @Override
        public void taskEnd(@NonNull DownloadTask task, @NonNull EndCause cause, @Nullable Exception realCause, @NonNull SpeedCalculator taskSpeed) {
            switch (cause) {
                case COMPLETED:
                    taskInfo.setState(FileTransferTaskInfo.STATE_OK);
                    FileItemInfo fileItemInfo = new FileItemInfo();
                    fileItemInfo.setName(taskInfo.getFileName());
                    fileItemInfo.setType(fileItemInfo.getRecognizeFileType(taskInfo.getFileName()));
                    fileItemInfo.setContentLength(taskInfo.getContentLength());
                    fileItemInfo.setCreateDate(System.currentTimeMillis());
                    fileItemInfo.setUpdateDate(System.currentTimeMillis());
                    fileItemInfo.setPath(taskInfo.getFilePath());
                    fileItemInfo.setFromSourceName(taskInfo.getFromSourceName());
                    fileItemInfo.setFromSourceCode(taskInfo.getFromSourceCode());
                    long id = daoUtil.getFileItemInfoDao().insert(fileItemInfo);
                    taskInfo.setFileId(id);
                    if (progressChangeListener != null) {
                        progressChangeListener.onSuccess();
                    }
                    break;
                case ERROR:
                case FILE_BUSY:
                case SAME_TASK_BUSY:
                case PRE_ALLOCATE_FAILED:
                    taskInfo.setState(FileTransferTaskInfo.STATE_FAIL);
                    if (progressChangeListener != null) {
                        progressChangeListener.onSuccess();
                    }
                    if (progressChangeListener != null) {
                        String msg = realCause == null ? "下载失败" : realCause.getMessage();
                        progressChangeListener.onError("1", msg);
                    }
                    break;
                case CANCELED:
                    taskInfo.setState(FileTransferTaskInfo.STATE_CANCEL);
                    if (progressChangeListener != null) {
                        progressChangeListener.onError("1", "用户取消");
                    }
                    break;
                default:
                    break;
            }
            daoUtil.getFileTransferTaskInfoDao().update(taskInfo);
            final Set<IGlobalTransferListener> listeners = getThreadSafeArray();
            for (final IGlobalTransferListener realOne : listeners) {
                if (realOne != null) {
                    realOne.onStateChanged(taskInfo);
                }
            }
        }
    }

}
