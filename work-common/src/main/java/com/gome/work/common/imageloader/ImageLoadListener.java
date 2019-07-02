package com.gome.work.common.imageloader;

import android.graphics.drawable.Drawable;


/**
 * Created by tanxingchun on 2016/4/27.
 */
public interface ImageLoadListener {

    void onLoadingFailed(String var1, Object view, String reason);

    void onLoadingComplete(String var1, Object view, Drawable bitmap, boolean isgif);

}
