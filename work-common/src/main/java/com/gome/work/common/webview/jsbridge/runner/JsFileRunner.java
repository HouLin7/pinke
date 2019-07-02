package com.gome.work.common.webview.jsbridge.runner;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.text.TextUtils;
import com.gome.core.greendao.AppItemBeanDao;
import com.gome.utils.GsonUtil;
import com.gome.work.common.webview.CommonWebActivity;
import com.gome.work.common.webview.jsbridge.JsActions;
import com.gome.work.common.webview.jsbridge.JsTask;
import com.gome.work.common.webview.model.JsDownloadInfo;
import com.gome.work.core.event.EventDispatcher;
import com.gome.work.core.event.model.BaseParamInfo;
import com.gome.work.core.event.model.EventInfo;
import com.gome.work.core.model.FileDownTaskInfo;
import com.gome.work.core.model.UploadFileResultInfo;
import com.gome.work.core.model.appmarket.AppItemBean;
import com.gome.work.core.net.WebApi;
import com.gome.work.core.persistence.DaoUtil;
import com.gome.work.core.upload.IUploadListener;
import com.liulishuo.okdownload.SpeedCalculator;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

/**
 * 文件相关操作的处理类
 */

public class JsFileRunner extends MyBaseJsRunner {

    private CommonWebActivity mActivity;

    private String mAppID;

    private AppItemBean mAppItem;

    private DaoUtil mDaoUtil;

    private File mImgPickFile = null;

    private UploadFileResultInfo mUploadFileResultInfo;

    private String[] jsActionList = new String[]{
            JsActions.ACTION_DOWNLOAD_FILE,
            JsActions.ACTION_UPLOAD_FILE,
            JsActions.ACTION_TAKE_PHOTO
    };

    private ProgressDialog mProgressDlg;

    public JsFileRunner(CommonWebActivity activity, String appId) {
        super(activity);
        mActivity = activity;
        mAppID = appId;
        mDaoUtil = new DaoUtil(activity);
        if (!TextUtils.isEmpty(mAppID)) {
            mAppItem = mDaoUtil.getAppItemBeanDao().queryBuilder()
                    .where(AppItemBeanDao.Properties.AppId.eq(appId)).unique();
        }
    }

    @Override
    public String execute(final JsTask task) throws InterruptedException, JSONException {
        final JSONObject jsonObject = new JSONObject();
        switch (task.action) {
            case JsActions.ACTION_TAKE_PHOTO:
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mActivity.showImagePickerWindow(false, new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                synchronized (JsFileRunner.this) {
                                    JsFileRunner.this.notify();
                                }
                                try {
                                    jsonObject.put("errorMsg", "用户未选择");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, true);
                    }
                });

                synchronized (JsFileRunner.this) {
                    JsFileRunner.this.wait();
                }
                if (mImgPickFile != null) {
                    WebApi.getInstance().uploadFile(mImgPickFile, new IUploadListener<UploadFileResultInfo>() {

                        @Override
                        public void onProcess(final long totalBytes, final long transferBytes, SpeedCalculator speed) {
                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (mProgressDlg != null) {
                                        mProgressDlg.setMax((int) totalBytes);
                                        mProgressDlg.setProgress((int) transferBytes);
                                    }
                                }
                            });
                        }

                        @Override
                        public void onStart() {
                            showProgressDlg();
                        }

                        @Override
                        public void onEnd() {

                        }

                        @Override
                        public void onError(String code, String message) {
                            mUploadFileResultInfo = null;
                            synchronized (JsFileRunner.this) {
                                JsFileRunner.this.notify();
                            }
                            dismissProgressDlg();
                        }

                        @Override
                        public void onSuccess(UploadFileResultInfo result) {
                            dismissProgressDlg();
                            mUploadFileResultInfo = result;
                            synchronized (JsFileRunner.this) {
                                JsFileRunner.this.notify();
                            }
                        }
                    });

                    synchronized (JsFileRunner.this) {
                        JsFileRunner.this.wait();
                    }

                    if (mUploadFileResultInfo != null) {
                        try {
                            jsonObject.put("fileUrl", mUploadFileResultInfo.url);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            jsonObject.put("errorMsg", "上传失败");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    try {
                        jsonObject.put("errorMsg", "用户未选择");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case JsActions.ACTION_UPLOAD_FILE:
                if (TextUtils.isEmpty(task.param)) {
                    jsonObject.put("errorMsg", "param 不能为空");
                    return jsonObject.toString();
                }
                JsDownloadInfo jsDownloadInfo = GsonUtil.jsonToObject(JsDownloadInfo.class, task.param);
                if (jsDownloadInfo == null || TextUtils.isEmpty(jsDownloadInfo.mimeType) || !jsDownloadInfo.mimeType.contains("image")) {
                    jsonObject.put("errorMsg", "无法识别的mimeType类型");
                    return jsonObject.toString();
                }
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mActivity.showImagePickerWindow(false, new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                synchronized (JsFileRunner.this) {
                                    JsFileRunner.this.notify();
                                }
                                try {
                                    jsonObject.put("errorMsg", "用户未选择");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                });

                synchronized (JsFileRunner.this) {
                    JsFileRunner.this.wait();
                }
                if (mImgPickFile != null) {
                    WebApi.getInstance().uploadFile(mImgPickFile, new IUploadListener<UploadFileResultInfo>() {
                        @Override
                        public void onProcess(final long totalBytes, final long transferBytes, SpeedCalculator speed) {
                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (mProgressDlg != null) {
                                        mProgressDlg.setMax((int) totalBytes);
                                        mProgressDlg.setProgress((int) transferBytes);
                                    }
                                }
                            });
                        }

                        @Override
                        public void onStart() {
                            showProgressDlg();
                        }

                        @Override
                        public void onEnd() {

                        }

                        @Override
                        public void onError(String code, String message) {
                            mUploadFileResultInfo = null;
                            synchronized (JsFileRunner.this) {
                                JsFileRunner.this.notify();
                            }
                            dismissProgressDlg();
                        }

                        @Override
                        public void onSuccess(UploadFileResultInfo result) {
                            dismissProgressDlg();
                            mUploadFileResultInfo = result;
                            synchronized (JsFileRunner.this) {
                                JsFileRunner.this.notify();
                            }
                        }
                    });

                    synchronized (JsFileRunner.this) {
                        JsFileRunner.this.wait();
                    }

                    if (mUploadFileResultInfo != null) {
                        try {
                            jsonObject.put("fileUrl", mUploadFileResultInfo.url);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            jsonObject.put("errorMsg", "上传失败");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    try {
                        jsonObject.put("errorMsg", "用户未选择");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case JsActions.ACTION_DOWNLOAD_FILE:
                if (!TextUtils.isEmpty(task.param)) {
                    jsDownloadInfo = GsonUtil.jsonToObject(JsDownloadInfo.class, task.param);
                    BaseParamInfo baseParamInfo = new BaseParamInfo(mActivity);
                    FileDownTaskInfo downInfo = new FileDownTaskInfo();
                    baseParamInfo.extraData = downInfo;
                    downInfo.url = jsDownloadInfo.url;
                    downInfo.contentLength = jsDownloadInfo.contentLength;
                    downInfo.fromSource = mAppItem == null ? "未知来源" : mAppItem.name;
                    downInfo.fromSourceType = mAppID;
                    if (!TextUtils.isEmpty(jsDownloadInfo.fileName)) {
                        downInfo.fileName = jsDownloadInfo.fileName;
                    } else {
                        downInfo.fileName = Uri.parse(jsDownloadInfo.url).getLastPathSegment();
                    }
                    downInfo.mimeType = jsDownloadInfo.mimeType;
                    EventDispatcher.postEvent(new EventInfo(EventInfo.FLAG_REQUEST_FILE_DOWNLOAD, baseParamInfo));
                }
                break;
            default:
                break;
        }
        return jsonObject.toString();
    }


    @Override
    public String[] getActionList() {
        return jsActionList;
    }


    public void onImageGetResult(boolean isSuccess, Uri uri, File file) {
        if (isSuccess) {
            mImgPickFile = file;
        } else {
            mImgPickFile = null;
        }
        synchronized (JsFileRunner.this) {
            JsFileRunner.this.notify();
        }
    }

    private void showProgressDlg() {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mProgressDlg = new ProgressDialog(mActivity);
                mProgressDlg.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                mProgressDlg.show();
            }
        });

    }


    private void dismissProgressDlg() {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mProgressDlg != null) {
                    mProgressDlg.dismiss();
                }
            }
        });
    }


}
