package com.gome.work.core.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**MD5加密工具
 * @author tanxingchun
 *
 */
public class MD5Utils {
	private static final String TAG = MD5Utils.class.getSimpleName();
	/**32位小写加密
	 * md5加密的工具方法
	 * @param password
	 * @return
	 */
	public static String encrypt32Lower(String password){
		try {
			MessageDigest digest = MessageDigest.getInstance("md5");
			byte[] result = digest.digest(password.getBytes());
			StringBuilder sb = new StringBuilder();
			//16进制的方式  把结果集byte数组 打印出来
			for(byte b :result){
				int number = (b&0xff);//加盐.
				String str =Integer.toHexString(number);
				if(str.length()==1){
					sb.append("0");
				}
				sb.append(str);
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return "";
		}
	}

	 /**32位大写加密
	 * @param s
	 * @return
	 */
	public static String encrypt32Upper(String s) {
			char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
					'A', 'B', 'C', 'D', 'E', 'F' };
			try {
				byte[] btInput = s.getBytes();
				// 获得MD5摘要算法的 MessageDigest 对象
				MessageDigest mdInst = MessageDigest.getInstance("MD5");
				// 使用指定的字节更新摘要
				mdInst.update(btInput);
				// 获得密文
				byte[] md = mdInst.digest();
				// 把密文转换成十六进制的字符串形式
				int j = md.length;
				char str[] = new char[j * 2];
				int k = 0;
				for (int i = 0; i < j; i++) {
					byte byte0 = md[i];
					str[k++] = hexDigits[byte0 >>> 4 & 0xf];
					str[k++] = hexDigits[byte0 & 0xf];
				}
				return new String(str);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}

}
