package com.gome.utils;

import android.net.Uri;


public class UrlUtil {

    /**
     * 根据指定的url，提取host信息，并返回url字符串
     * 如 "http://baidu.com?chanel=13",返回结果是  "http://baidu.com"
     *
     * @param url
     * @return
     */
    public static String getHostUrl(String url) {
        Uri uri = Uri.parse(url);
        String scheme = uri.getScheme();
        String authority = uri.getEncodedAuthority();
        Uri newUri = new Uri.Builder().scheme(scheme).encodedAuthority(authority).build();
        return newUri.toString();
    }
}

