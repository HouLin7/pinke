package com.gome.work.common.imageloader;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;

/**
 * Created by tanxingchun on 2016/4/27.
 */
public class ImageLoader {

    private static final String TAG = "GlideImp";

    private static int mDefaultDrawableId;

    private static int mErrorDrawableId;

    private static int DURATION = 300;

    public static void init(Context context, int mDefaultResId, int errorResID) {
        mDefaultDrawableId = mDefaultResId;
        mErrorDrawableId = errorResID;
    }

    public static <T> void loadImage(Context context, T uri, ImageView imageView) {
        loadImage(context, uri, imageView, mDefaultDrawableId);
    }

    public static <T> void loadImage(Context context, T uri, ImageView imageView, int defaultDrawableId) {
        if (context == null) {
            return;
        }
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            if (activity.isFinishing()) {
                return;
            }
        }
        doLoadImage(Glide.with(context), uri, imageView, defaultDrawableId, defaultDrawableId);
    }


    public static <T> void loadImage(Fragment fragment, T uri, ImageView imageView) {
        doLoadImage(Glide.with(fragment), uri, imageView, mDefaultDrawableId, mDefaultDrawableId);
    }


    private static <T> void doLoadImage(RequestManager requestManager, T uri, ImageView imageView, int defaultResId, int errResId) {
        if (defaultResId <= 0) {
            defaultResId = mDefaultDrawableId;
        }
        if (errResId <= 0) {
            errResId = mErrorDrawableId;
        }
        requestManager.load(uri).crossFade(DURATION).placeholder(defaultResId)
                .error(errResId)
                .into(imageView);
    }

    public static <T> void loadRoundImage(Activity activity, T uri, ImageView imageView, int corners) {
        loadRoundImage(activity, uri, imageView, corners, mDefaultDrawableId);
    }

    public static <T> void loadRoundImage(Activity activity, T uri, ImageView imageView, int corners, int defaultResId) {
        if (activity != null && !activity.isFinishing()) {
            Glide.with(activity).load(uri).transform(new CenterCrop(activity), new GlideRoundTransform(activity, corners))
                    .placeholder(defaultResId)
                    .crossFade(DURATION)
                    .into(imageView);
        }

    }

    public static <T> void loadImageAsIMMessage(Context context, T uri, ImageView imageView, int corners) {
        Glide.with(context).load(uri).transform(new GlideRoundTransform(context, corners), new GlideIMTransform(context)).placeholder(mDefaultDrawableId).dontAnimate().fallback(mDefaultDrawableId).error(mDefaultDrawableId).into(imageView);
    }

    public static void loadMediaUri(Context context, Uri mediaStoreUri, ImageView view) {
        Glide.with(context).loadFromMediaStore(mediaStoreUri).into(view);
    }

}
