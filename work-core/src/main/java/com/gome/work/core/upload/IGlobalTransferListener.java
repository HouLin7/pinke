package com.gome.work.core.upload;

import com.gome.work.core.model.dao.FileTransferTaskInfo;

/**
 * 上传下载的全局监听接口
 */
public interface IGlobalTransferListener {

    /**
     * @param taskInfo
     * @param totalBytes    总字节数
     * @param transferBytes 已传输的字节数
     */
    public void onProcess(FileTransferTaskInfo taskInfo, long totalBytes, long transferBytes);

    public void onStateChanged(FileTransferTaskInfo taskInfo);

}
