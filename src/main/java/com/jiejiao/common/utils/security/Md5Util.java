package com.jiejiao.common.utils.security;

import java.security.MessageDigest;

/**
 * @author ShiZhiGuo
 *
 */
public class Md5Util {

	/**
	 * md5加密
	 * 
	 * @param input
	 *            要加密的字符串
	 * @return md5加密后的字符串
	 */
	public static String getMD5(String input) {

		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			// System.out.println(e.toString());
			e.printStackTrace();
			return "";
		}
		char[] charArray = input.toCharArray();
		byte[] inputArray = new byte[charArray.length];
		for (int i = 0; i < charArray.length; i++) {
			inputArray[i] = (byte) charArray[i];
		}
		byte[] outputArray = md5.digest(inputArray);
		StringBuffer hexValue = new StringBuffer();
		for (byte b : outputArray) {
			int val = (int) b & 0xff;
			if (val < 16) {
				hexValue.append("0");
			}
			hexValue.append(Integer.toHexString(val));
		}
		return hexValue.toString();
	}

}
