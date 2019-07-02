package com.gome.work.common.utils;

import android.content.Context;

/**
 * Created by tanxingchun on 2017/3/17.
 */

public class ImageScaleUtils {
    private static int MAX_WIDTH = 130;
    private static int MAX_HEIGHT = 150;
    private static int MIN_SIZE = 20;

    public static int[] scaleImage(Context context, int width, int height) {
        int[] sizes = new int[2];
        if (width == 0 || height == 0) {
            sizes[0] = width;
            sizes[1] = height;
            return sizes;
        }
        int maxWidth = UiUtils.dip2px(context, MAX_WIDTH);
        int maxHeight = UiUtils.dip2px(context, MAX_HEIGHT);
        int minSize = UiUtils.dip2px(context, MIN_SIZE);

        float scale;
        if (height > maxHeight || width > maxWidth) {

            if (width * maxHeight > maxWidth * height) {
                scale = (float) maxWidth / (float) width;
            } else {
                scale = (float) maxHeight / (float) height;
            }
            sizes[0] = (int) ((width * scale) + 0.5f);
            sizes[1] = (int) ((height * scale) + 0.5f);
        } else if (height < minSize && width < minSize) {
            if (width > height) {
                scale = (float) minSize / (float) width ;
            } else {
                scale = (float)  minSize  / (float) height;
            }
            sizes[0] = (int) ((width * scale) + 0.5f);
            sizes[1] = (int) ((height * scale) + 0.5f);
        } else {
            sizes[0] = width;
            sizes[1] = height;
        }
        return sizes;
    }
}
