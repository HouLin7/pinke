package com.gome.work.core.upload;

import android.content.Context;

import com.gome.core.greendao.FileUploadRecordInfoDao;
import com.gome.utils.GsonUtil;
import com.gome.work.core.model.dao.FileTransferTaskInfo;
import com.gome.work.core.model.dao.FileUploadRecordInfo;
import com.gome.work.core.net.IResponseListener;
import com.gome.work.core.persistence.DaoUtil;
import com.gome.work.core.persistence.ThreadPoolFactory;
import com.liulishuo.okdownload.SpeedCalculator;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

public class FileUploadManager {

    private static final int TIME_OUT = 1000 * 30;

    /**
     * 测试地址
     */
    private static final String FILE_URL_TEST = "http://gfs.ds.gfsuat.com.cn/api/uploadFile";
    private static final String IMAGE_URL_TEST = "http://gfs.ds.gfsuat.com.cn/api/fileup";

    private static final String FILE_URL = "http://gfs.ds.gome.com.cn/api/uploadFile";
    private static final String IMAGE_URL = "http://gfs.ds.gome.com.cn/api/fileup";

    /**
     * appKey定义 测试环境
     */
    private static final String APP_TOKEN_IMG_TEST = "d350c4b43ec44fcdaf33a24e5128609b";
    private static final String APP_TOKEN_FILE_TEST = "d94fa4cf5efc4d37bf00b1fa18c2a019";
    /**
     * appKey定义  生产环境
     */
    private static final String APP_TOKEN_IMG = "1bc225b7e47e4c24bc14e4c11dfeb2d1";
    private static final String APP_TOKEN_FILE = "88dea35c41764f40b53b4ffc660335a6";

    private OkHttpClient okHttpClientFile;

    private OkHttpClient okHttpClientImage;

    private Context mContext;

    private static FileUploadManager instance;

    private DaoUtil mDaoUtil;

    private Set<IGlobalTransferListener> mUploadListeners = new HashSet<>();

    public void addUploadListener(IGlobalTransferListener listener) {
        mUploadListeners.add(listener);
    }

    public void removeUploadListener(IGlobalTransferListener listener) {
        mUploadListeners.remove(listener);
    }

    private FileUploadManager(Context context) {
        this.mContext = context;
        mDaoUtil = new DaoUtil(context);
        okHttpClientFile = getOkHttpClient(FileType.file);
        okHttpClientImage = getOkHttpClient(FileType.image);
    }

    public static FileUploadManager getInstance(Context context) {
        if (instance == null) {
            synchronized (FileUploadManager.class) {
                if (instance == null) {
                    instance = new FileUploadManager(context.getApplicationContext());
                }
            }
        }
        return instance;
    }

    /**
     * 记录一次上传文件的记录
     *
     * @param filePath
     */

    public void tagEvent(final String filePath) {
        ThreadPoolFactory.getIoExecutor().submit(new Runnable() {
            @Override
            public void run() {
                List<FileUploadRecordInfo> result = mDaoUtil.getFileUploadRecordInfoDao().queryBuilder()
                        .where(FileUploadRecordInfoDao.Properties.FilePath.eq(filePath)).limit(1).list();
                if (result != null && !result.isEmpty()) {
                    FileUploadRecordInfo info = result.get(0);
                    info.setUpdateTime(System.currentTimeMillis());
                    mDaoUtil.getFileUploadRecordInfoDao().update(info);
                } else {
                    FileUploadRecordInfo info = new FileUploadRecordInfo();
                    info.setUpdateTime(System.currentTimeMillis());
                    info.setFilePath(filePath);
                    mDaoUtil.getFileUploadRecordInfoDao().save(info);
                }
            }
        });
    }

    static class MyCallback implements Callback {

        public IResponseListener<UploadResult> listener;

        public MyCallback(IResponseListener<UploadResult> listener) {
            this.listener = listener;
        }

        @Override
        public void onFailure(Call call, IOException e) {
            UploadResult result = new UploadResult();
            result.result = "N";
            result.msg = e.getMessage();
            listener.onSuccess(result);
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            if (response.isSuccessful()) {
                String data = response.body().string();
                UploadResult result = GsonUtil.jsonToObject(UploadResult.class, data);
                listener.onSuccess(result);
            } else {
                String data = response.body().string();
                UploadResult result = new UploadResult();
                result.result = "N";
                result.errorCode = String.valueOf(response.code());
                result.msg = data;
                listener.onSuccess(result);
            }
        }
    }

    private FileTransferTaskInfo createTransferTask(File file) {
        FileTransferTaskInfo task = new FileTransferTaskInfo();
        task.setCreateDate(System.currentTimeMillis());
        task.setDirect(1);
        task.setFileName(file.getName());
        task.setFilePath(file.getAbsolutePath());
        task.setState(FileTransferTaskInfo.STATE_IDLE);
        return task;
    }

    public void uploadFile(File file, final IUploadListener<UploadResult> listener) {
        doUpload(file, false, listener);
    }

    /**
     * 仅限于上传图片
     *
     * @param file
     * @param listener
     */
    public void uploadImage(File file, final IUploadListener<UploadResult> listener) {
        doUpload(file, true, listener);
    }

    private void doUpload(File file, boolean isImage, final IUploadListener<UploadResult> listener) {
        String fileName = file.getName();
        RequestBody requestBody = RequestBody.create(MediaType.parse(fileName), file);
        FileTransferTaskInfo taskInfo = createTransferTask(file);
        long id = mDaoUtil.getFileTransferTaskInfoDao().insert(taskInfo);
        taskInfo.setId(id);
        RequestBody body = new FileRequestBody(requestBody, new MyGlobalListenerWrapper(taskInfo, listener));
        String url = isImage ? IMAGE_URL : FILE_URL;
        final Request request = new Request.Builder().url(assembleFileName(url, fileName))
                .post(body).build();
        Call call;
        if (isImage) {
            call = okHttpClientImage.newCall(request);
        } else {
            call = okHttpClientFile.newCall(request);
        }
        call.enqueue(new MyCallback(listener));
        tagEvent(file.getPath());
    }


    class MyGlobalListenerWrapper implements IResponseListener, IProgressChangeListener {

        private FileTransferTaskInfo taskInfo;
        private IProgressChangeListener listener;

        public MyGlobalListenerWrapper(FileTransferTaskInfo taskInfo, IProgressChangeListener listener) {
            this.listener = listener;
            this.taskInfo = taskInfo;
        }

        @Override
        public void onProcess(long totalBytes, long transferBytes, SpeedCalculator speed) {
            for (IGlobalTransferListener item : mUploadListeners) {
                taskInfo.setState(FileTransferTaskInfo.STATE_FAIL);
                mDaoUtil.getFileTransferTaskInfoDao().insertOrReplace(taskInfo);
                item.onStateChanged(taskInfo);
            }
            if (listener != null) {
                listener.onProcess(totalBytes, transferBytes, speed);
            }
        }

        @Override
        public void onStart() {

        }

        @Override
        public void onEnd() {

        }


        @Override
        public void onError(String code, String message) {
            for (IGlobalTransferListener item : mUploadListeners) {
                taskInfo.setState(FileTransferTaskInfo.STATE_FAIL);
                mDaoUtil.getFileTransferTaskInfoDao().insertOrReplace(taskInfo);
                item.onStateChanged(taskInfo);
            }
        }

        @Override
        public void onSuccess(Object result) {
            for (IGlobalTransferListener item : mUploadListeners) {
                taskInfo.setState(FileTransferTaskInfo.STATE_FAIL);
                mDaoUtil.getFileTransferTaskInfoDao().insertOrReplace(taskInfo);
                item.onStateChanged(taskInfo);
            }
        }
    }


    private String assembleFileName(String url, String fileName) {
        StringBuilder builder = new StringBuilder(url);
        builder.append("?").append("fileName").append("=").append(fileName);
        return builder.toString();
    }

    private OkHttpClient getOkHttpClient(FileType fileType) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(TIME_OUT, TimeUnit.MILLISECONDS);
        builder.readTimeout(TIME_OUT, TimeUnit.MILLISECONDS);
        builder.writeTimeout(TIME_OUT, TimeUnit.MILLISECONDS);
        if (fileType == FileType.image) {
            builder.networkInterceptors().add(new HeaderParamsInterceptor(APP_TOKEN_IMG));
        } else {
            builder.networkInterceptors().add(new HeaderParamsInterceptor(APP_TOKEN_FILE));
        }

        builder.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });

        return builder.build();
    }


    class HeaderParamsInterceptor implements Interceptor {

        private String token = "";

        public HeaderParamsInterceptor(String token) {
            this.token = token;
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            request = request
                    .newBuilder()
                    .addHeader("token", token)
                    .build();
            Response response = chain.proceed(request);
            return response;
        }
    }



    public enum FileType {
        image,
        file
    }

}
