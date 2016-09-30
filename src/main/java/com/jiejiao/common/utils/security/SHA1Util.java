package com.jiejiao.common.utils.security;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

/**
 * SHA1签名算法加密工具类
 * @author shizhiguo
 * @date 2016年9月8日 下午6:05:11
 */
public class SHA1Util {
	
	/**
	 * sha1加密
	 * @author shizhiguo
	 * @date 2016年9月8日 下午6:06:12
	 * @param str 要加密的字符串
	 * @return
	 */
	public static String SHA1(String str){
		try {
			MessageDigest crypt = MessageDigest.getInstance("SHA-1");
			crypt.reset();
			crypt.update(str.getBytes("UTF-8"));
			return byteToHex(crypt.digest());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 字节数组转16进制字符串
	 * @author shizhiguo
	 * @date 2016年9月8日 下午6:09:37
	 * @param hash
	 * @return
	 */
	private static String byteToHex(final byte[] hash) {
		Formatter formatter = new Formatter();
		for (byte b : hash) {
			formatter.format("%02x", b);
		}
		String result = formatter.toString();
		formatter.close();
		return result;
	}
}
