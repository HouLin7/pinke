package com.gome.work.core.download;

import com.gome.work.core.upload.IProgressChangeListener;

public interface IDownloadListener extends IProgressChangeListener {
    void onSuccess();

    void onError(String code, String message);

}
