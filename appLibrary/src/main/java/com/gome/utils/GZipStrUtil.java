package com.gome.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipInputStream;

/**
 * GZip压缩工具类
 *
 * @author Zhang.Mingji
 * @date 2014年6月27日下午3:57:00
 * @Copyright(c) gome inc Gome Co.,LTD
 */
public class GZipStrUtil {

	/**
	 * 字符串压缩
	 *
	 * @param str
	 *            待压缩的字符串
	 * @return 返回压缩后的字符串
	 * @throws java.io.IOException
	 */
	public static String compress(String str) throws IOException {
		if (null == str || str.length() <= 0) {
			return str;
		}
		// 创建一个新的 byte 数组输出流
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		// 使用默认缓冲区大小创建新的输出流
		GZIPOutputStream gzip = new GZIPOutputStream(out);
		// 将 b.length 个字节写入此输出流
		gzip.write(str.getBytes());
		gzip.close();
		// 使用指定的 charsetName，通过解码字节将缓冲区内容转换为字符串
		return out.toString("ISO-8859-1");
	}

//	/**
//	 * 字符串解压
//	 *
//	 * @param str
//	 *            对字符串解压
//	 * @return 返回解压缩后的字符串
//	 * @throws java.io.IOException
//	 */
//	public static String unCompress(String str) throws IOException {
//		return  unCompress(str, "ISO-8859-1");
//	}
	
	
	/**
	 * 字符串解压
	 *
	 * @param str
	 *            对字符串解压
	 * @return 返回解压缩后的字符串
	 * @throws java.io.IOException
	 */
	public static String unCompress(String str) throws IOException {
		if (null == str || str.length() <= 0) {
			return str;
		}
		// 创建一个新的 byte 数组输出流
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		// 创建一个 ByteArrayInputStream，使用 buf 作为其缓冲区数组
		ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes("ISO-8859-1"));
		// 使用默认缓冲区大小创建新的输入流
		GZIPInputStream gzip = new GZIPInputStream(in);
		byte[] buffer = new byte[256];
		int n = 0;
		while ((n = gzip.read(buffer)) >= 0) {// 将未压缩数据读入字节数组
			// 将指定 byte 数组中从偏移量 off 开始的 len 个字节写入此 byte数组输出流
			out.write(buffer, 0, n);
		}
		gzip.close();
		// 使用指定的 charsetName，通过解码字节将缓冲区内容转换为字符串
		return out.toString("UTF-8");
	}

	public static byte[] unZip(byte[] bContent) {
		byte[] b = null;
		try {
			ByteArrayInputStream bis = new ByteArrayInputStream(bContent);
			ZipInputStream zip = new ZipInputStream(bis);
			while (zip.getNextEntry() != null) {
				byte[] buf = new byte[1024];
				int num = -1;
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				while ((num = zip.read(buf, 0, buf.length)) != -1) {
					baos.write(buf, 0, num);
				}
				b = baos.toByteArray();
				baos.flush();
				baos.close();
			}
			zip.close();
			bis.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return b;
	}
}
