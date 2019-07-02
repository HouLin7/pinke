package com.gome.work.core.utils;

import android.text.InputFilter;
import android.text.Spanned;

import java.util.regex.Pattern;

/**
 * Created by liuletao on 2016/9/29.
 */
public class EmojiFilter implements InputFilter {

    private Pattern mEmojiPattern = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",
            Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
//        Matcher emojiMatcher = mEmojiPattern.matcher (source) ;
//        if (emojiMatcher.find( )) {
//            UiUtils.showToast("不支持输入Emoji表情符号");
//            return "";
//        }
        return source;
    }

}