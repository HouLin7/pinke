package com.gome.work.common.utils.apkverify;

import java.io.UnsupportedEncodingException;

/**
 * Created by chenhang01 on 2016/11/21.
 */

public class EncryptionUtils {

    private static final int BIT_CNT = 16;
    private static final String STUB = "0";

    /**
     *
     * @param key 要加密的字符串
     * @param seed 与后台约定的私钥
     * @return
     */
    public static String getSecurity(String key, int seed) {
        if (key == null) {
            throw new IllegalArgumentException("key is not allowed to be null");
        }
        byte[] bytes = null;
        try {
            bytes = key.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        MurmurHash3.LongPair longPair = new MurmurHash3.LongPair();
        MurmurHash3.murmurhash3_x64_128(bytes, 0, bytes.length, seed, longPair);

        String high = Long.toHexString(longPair.val1);
        String low = Long.toHexString(longPair.val2);

        if (high.length() < BIT_CNT) {
            high = getZero(BIT_CNT - high.length()) + high;
        }
        if (low.length() < BIT_CNT) {
            low = getZero(BIT_CNT - low.length()) + low;
        }

        return high + low;

    }

    private static String getZero(int n) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            sb.append(STUB);
        }
        return sb.toString();
    }

}
