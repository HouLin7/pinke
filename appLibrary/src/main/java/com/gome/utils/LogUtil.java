package com.gome.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;

import android.os.Environment;

import com.gome.applibrary.BuildConfig;

public class LogUtil {

	public static String logTag = "NewsGroup";
	private static String log_fullpath = "";
	private static String path_name = "log";
	private static String file_name = "";
	private static String rootdir = Environment.getExternalStorageDirectory().getAbsolutePath();
	private static final String file_idle = "/";
	private static boolean isLog = true;// 设置当前是否输出log日志到本地，true输出，false不输出。
	private static boolean isLogmobile = true;// 设置当前是否输出log日志到手机，true输出，false不输出。

	static {
		isLogmobile = BuildConfig.DEBUG;
	}

	private static String GetLogPath() {
		String temp = "";
		if (!path_name.equals("")) {
			temp = rootdir + file_idle + logTag + file_idle + path_name + file_idle;
			try {
				File path = new File(temp);

				if (!path.exists()) {
					path.mkdirs();
				}
			} catch (Exception ex) {
				return rootdir + file_idle;
			}
		} else {
			return rootdir + file_idle;
		}

		return temp;
	}

	private static boolean hasSdcard() {
		try {
			return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
		} catch (Exception ex) {
			return false;
		}
	}

	private static boolean InitEnvironment(String preFix) {
		if (!hasSdcard()) {
			return false;
		}
		String path = GetLogPath();
		if (file_name.equals("")) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String date = sdf.format(new java.util.Date());
			file_name = preFix + "." + date + ".txt";
		}

		File file = new File(path + file_name);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				return false;
			}
		}
		log_fullpath = path + file_name;
		return true;
	}

	private static void WriteFileLog(String content, String type) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
		String date = sdf.format(new java.util.Date());
		FileWriter fw = null;
		try {
			fw = new FileWriter(log_fullpath, true);
			// fw.write(date + " " + type + " " + content + "\r\n");
			fw.write(date + " " + content + "\r\n");
			fw.close();
		} catch (Exception ex) {

		} finally {
			if (fw != null) {
				try {
					fw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	// 以下为输出系统日志到手机

	public static void d(String tag, String content) {
		if (isLogmobile) {
			android.util.Log.d(tag, content);
		}
	}

	public static void i(String tag, String content) {
		if (isLogmobile) {
			android.util.Log.i(tag, content);
		}
	}

	public static void w(String tag, String content) {
		if (isLogmobile) {
			android.util.Log.w(tag, content);
		}
	}

	public static void e(String tag, String content) {
		if (isLogmobile) {
			android.util.Log.e(tag, content);
		}
	}

	public static void dd(String tag, String content) {
		if (isLog) {
			if (!InitEnvironment(tag)) {
				return;
			}
			WriteFileLog(content, "d");
		}
	}

	public static void ii(String tag, String content) {
		if (isLog) {
			if (!InitEnvironment(tag)) {
				return;
			}
			WriteFileLog(content, "i");
		}
	}

	public static void ww(String tag, String content) {
		if (isLog) {
			if (!InitEnvironment(tag)) {
				return;
			}
			WriteFileLog(content, "w");
		}
	}

	public static void ee(String tag, String content) {
		if (isLog) {
			if (!InitEnvironment(tag)) {
				return;
			}
			WriteFileLog(content, "e");
		}
	}
}
