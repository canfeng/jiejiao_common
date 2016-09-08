package com.jiejiao.common.utils;

import java.util.UUID;

/**
 * uuid 工具类
 * @author shizhiguo
 * @date 2016年9月8日 下午4:10:47
 */
public class UUIDUtils {

	

	/**
	 * 获取36位 uuid
	 * @author shizhiguo
	 * @date 2016年9月8日 下午4:06:27
	 * @return
	 */
	public static String getUuid(){
		return UUID.randomUUID().toString();
	}
	
	/**
	 * 获取32位uuid
	 * @author shizhiguo
	 * @date 2016年9月8日 下午4:07:06
	 * @return
	 */
	public static String getShortUuid(){
		return getUuid().replaceAll("-", "");
	}
	/**
	 * 获取36位uuid 大写形式
	 * @author shizhiguo
	 * @date 2016年9月8日 下午4:07:41
	 * @return
	 */
	public static String getUpperUuid(){
		return getUuid().toUpperCase();
	}
	
	/**
	 * 获取32位uuid 大写形式
	 * @author shizhiguo
	 * @date 2016年9月8日 下午4:08:12
	 * @return
	 */
	public static String getUpperShortUuid(){
		return getUuid().toUpperCase().replaceAll("-", "");
	}
}
