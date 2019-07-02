
package com.gome.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Random;

import android.text.TextUtils;

public class StringUtil {

    /**
     * Convert null to empty string ""
     *
     * @param str
     * @return
     */
    public final static String toSafeString(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        return str;
    }

    public static String getStrTime(String cc_time) {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        if (!TextUtils.isEmpty(cc_time)) {
            return sDateFormat.format(Long.valueOf(cc_time));
        } else {
            return null;
        }

    }

    public static String readContent(InputStream is) {
        return readContent(is, "UTF-8");
    }

    public static String readContent(InputStream is, String charsetName) {
        StringBuffer buffer = new StringBuffer();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(is, charsetName));
            String json = null;
            while ((json = br.readLine()) != null) {
                buffer.append(json);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return buffer.toString();
    }

    /**
     * 得到固定长度的随机字符串
     */
    public static String getRandomChar(int length) {
        char[] chr = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
        Random random = new Random();
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < length; i++) {
            buffer.append(chr[random.nextInt(36)]);
        }
        return buffer.toString();

    }

}
