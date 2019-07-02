package com.gome.utils;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

public class ClipBoardUtil {
	/**
	 * 实现文本复制功能 
	 *
	 * @param content
	 */
	@SuppressLint("NewApi")
	public static void copy(Context context, String content) {
		// 得到剪贴板管理器
		ClipboardManager cmb = (ClipboardManager) context
				.getSystemService(Context.CLIPBOARD_SERVICE);
		ClipData myClip = ClipData.newPlainText("text", content);
		cmb.setPrimaryClip(myClip);
	}

	/**
	 * 实现粘贴功能
	 *
	 * @param context
	 * @return
	 */
	@SuppressLint("NewApi")
	public static String paste(Context context) {
		// 得到剪贴板管理器
		ClipboardManager cmb = (ClipboardManager) context
				.getSystemService(Context.CLIPBOARD_SERVICE);
		return cmb.getText().toString().trim();
	}

}
