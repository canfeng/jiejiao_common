package com.jiejiao.common.utils.security;

import java.security.MessageDigest;
import java.util.Random;

import com.jiejiao.common.tencent.RandomStringGenerator;

/**
 * @author ShiZhiGuo
 *
 */
public class Md5Util {

	private final static String[] hexDigits = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d",
			"e", "f" };

	/**
	 * 转换字节数组为16进制字串
	 * 
	 * @param b
	 *            字节数组
	 * @return 16进制字串
	 */
	public static String byteArrayToHexString(byte[] b) {
		StringBuilder resultSb = new StringBuilder();
		for (byte aB : b) {
			resultSb.append(byteToHexString(aB));
		}
		return resultSb.toString();
	}

	/**
	 * 转换byte到16进制
	 * 
	 * @param b
	 *            要转换的byte
	 * @return 16进制格式
	 */
	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0) {
			n = 256 + n;
		}
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}

	/**
	 * md5加密
	 * 
	 * @param input
	 *            要加密的字符串
	 * @return md5加密后的字符串
	 */
	public static String getMD5(String input) {

		/*
		 * MessageDigest md5 = null; try { md5 =
		 * MessageDigest.getInstance("MD5"); } catch (Exception e) { //
		 * System.out.println(e.toString()); e.printStackTrace(); return ""; }
		 * char[] charArray = input.toCharArray(); byte[] inputArray = new
		 * byte[charArray.length]; for (int i = 0; i < charArray.length; i++) {
		 * inputArray[i] = (byte) charArray[i]; } byte[] outputArray =
		 * md5.digest(inputArray); StringBuffer hexValue = new StringBuffer();
		 * for (byte b : outputArray) { int val = (int) b & 0xff; if (val < 16)
		 * { hexValue.append("0"); } hexValue.append(Integer.toHexString(val));
		 * } return hexValue.toString();
		 */

		String resultString = null;

		try {
			resultString = input;
			MessageDigest md = MessageDigest.getInstance("MD5");
			resultString = byteArrayToHexString(md.digest(resultString.getBytes()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultString;

	}

	/**
	 * 加盐的md5输出
	 * 
	 * @author shizhiguo
	 * @date 2017年3月9日 下午2:58:12
	 * @param input
	 * @return
	 */
	public static String getSaltMd5(String input) {
		Random r = new Random();
		String salt = RandomStringGenerator.getRandomStringByLength(16);
		System.out.println("salt:" + salt);
		String md5Final = getMD5(input + salt);
		System.out.println("加盐后的MD5加密:" + Md5Util.getMD5(input + salt));
		// 保存盐值 到md5中
		char[] cs = new char[48];
		for (int i = 0; i < 48; i += 3) {
			cs[i] = md5Final.charAt(i / 3 * 2);
			char c = salt.charAt(i / 3);
			cs[i + 1] = c;
			cs[i + 2] = md5Final.charAt(i / 3 * 2 + 1);
		}
		String rs = new String(cs);
		return rs;
	}

	/**
	 * 加盐的md5验证
	 * 
	 * @author shizhiguo
	 * @date 2017年3月9日 下午2:58:24
	 * @param input
	 * @param md5str
	 * @return
	 */
	public static boolean verifySaltMd5(String input, String md5str) {
		md5str = md5str.toLowerCase();// 全部转小写
		char[] saltByte = new char[16];
		char[] pwdByte = new char[32];
		for (int i = 0; i < 48; i += 3) {
			pwdByte[i / 3 * 2] = md5str.charAt(i);
			pwdByte[i / 3 * 2 + 1] = md5str.charAt(i + 2);
			saltByte[i / 3] = md5str.charAt(i + 1);
		}
		String salt = new String(saltByte);
		String pwdMd5 = new String(pwdByte);
		if (pwdMd5.equals(getMD5(input + salt))) {
			return true;
		}
		return false;
	}

}
