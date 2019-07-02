package com.gome.work.common.webview;


import android.app.Activity;
import android.net.Uri;
import android.text.TextUtils;
import android.webkit.DownloadListener;

import com.gome.work.core.event.EventDispatcher;
import com.gome.work.core.event.model.BaseParamInfo;
import com.gome.work.core.event.model.EventInfo;
import com.gome.work.core.model.FileDownTaskInfo;
import com.gome.work.core.persistence.DaoUtil;

/**
 * Created by chaergongzi on 2018/8/16.
 */

public class MyDownloadListener implements DownloadListener {

    private Activity mActivity;

    /**
     * 文件来源
     */
    private String fromSourceCode;

    private DaoUtil daoUtil;

    public MyDownloadListener(Activity context, String fromSourceCode) {
        this.mActivity = context;
        this.fromSourceCode = fromSourceCode;
        daoUtil = new DaoUtil(context);
    }

    private String getFileName(String contentDisposition) {
        String result = "";
        if (!TextUtils.isEmpty(contentDisposition)) {
            String[] parts = contentDisposition.split(";");
            if (parts != null) {
                for (String item : parts) {
                    String[] header = item.split("=");
                    if (header != null && header.length == 2) {
                        if (header[0] != null && header[0].contains("filename")) {
                            result = header[1];
                            break;
                        }
                    }
                }
            }
        }
        result = result.replace("\"", "");
        return result;
    }

    @Override
    public void onDownloadStart(final String url, final String userAgent, final String contentDisposition, final String mimeType, final long contentLength) {
//        url = "http://kss.ksyun.com/gomeapp/im/plugin/latest/h5App-release.apk";
        String name = Uri.parse(url).getLastPathSegment();
        StringBuilder builder = new StringBuilder();
        builder.append("文件名：" + name).append("\n");
        float sizeMb = contentLength / (1024f * 1024f);
        sizeMb = Math.round(sizeMb * 100) / 100f;
        builder.append("文件大小:" + sizeMb + "M");
        startDownload(url, mimeType, contentDisposition, contentLength);
    }


    private void startDownload(String url, String mimeType, String contentDisposition, long contentLength) {
        BaseParamInfo downInfo = new BaseParamInfo(mActivity);
        FileDownTaskInfo taskInfo = new FileDownTaskInfo();
        downInfo.extraData = taskInfo;
        taskInfo.url = url;
        taskInfo.contentLength = contentLength;
        taskInfo.fromSourceType = fromSourceCode;
        String fileName = getFileName(contentDisposition);
        if (TextUtils.isEmpty(fileName)) {
            fileName = Uri.parse(url).getLastPathSegment();
        }
        taskInfo.fileName = fileName;
        taskInfo.mimeType = mimeType;

        EventDispatcher.postEvent(new EventInfo(EventInfo.FLAG_REQUEST_FILE_DOWNLOAD, downInfo));

    }

}
