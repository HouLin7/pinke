package com.gome.work.common.utils;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.gome.work.common.imageloader.ImageLoader;


/**
 *
 */

public class FileBindingUtilsInterface {

    @BindingAdapter({"android:bindImageSrc"})
    public static void bindImageSrc(final ImageView view, Object path) {
        ImageLoader.loadImage(view.getContext(), path, view);
    }


}
