package com.jiejiao.common.utils.config;

import java.util.ResourceBundle;

/**
 * 获取properties配置信息,默认读取config.properties
 * 需要读取其它配置文件,可通过构造器传入
 * @author shizhiguo
 * @date 2016年9月8日 下午4:24:46
 */
public class ConfigUtil {
	
	private static ResourceBundle bundle=ResourceBundle.getBundle("config");
	
	/**
	 * @param propFileName 配置文件的路径和名称 ，不要后缀 如 spring/redis
	 */
	public ConfigUtil(String propFileName){
		bundle=ResourceBundle.getBundle(propFileName);
	}
	
	
	/**
	 * 根据key获取值
	 * @author shizhiguo
	 * @date 2016年9月8日 下午4:29:01
	 * @param key
	 * @return
	 */
	public static String get(String key){
		return bundle.getString(key);
	}
	
}
