package com.gome.work.common.utils;

import android.content.Context;
import android.graphics.Bitmap;

import com.gome.work.core.utils.DensityUtil;

import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;

/**
 * Create by liupeiquan on 2018/11/7
 */
public class QrCodeUtils {


    public static Bitmap getQrcodeByString(Context context,String content, float size){

        return QRCodeEncoder.syncEncodeQRCode(content,DensityUtil.dip2px(context,size));
    }
}
