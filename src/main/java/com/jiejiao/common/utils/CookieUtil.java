package com.jiejiao.common.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import com.jiejiao.common.utils.config.ConfigUtil;
import com.jiejiao.common.utils.security.DesUtil;

public class CookieUtil {
	/**
	 * 根据cookie名称获取cookie
	 * @param cookies 当前请求中的cookie数组
	 * @param name cookie名称
	 * @return
	 */
	public static Cookie getCookieByName(Cookie[] cookies, String name) {
		if (cookies != null && cookies.length != 0) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(name)) {
					return cookie;
				}
			}
		}
		return null;
	}
	
	/**
	 * 从cookie中取得解密后的token
	 * @author SZG
	 * 2016年4月29日 上午10:14:29
	 * @param cookie
	 * @return
	 */
	public static String getTokenFromCookie(Cookie cookie){
		String cookieValue=cookie.getValue();
		if (cookie!=null&&cookieValue.contains("@")) {
			String tokenEncrypt=cookieValue.split("@")[1];
			return DesUtil.decrypt(tokenEncrypt, ConfigUtil.get("loginCookieDesKey"));
		}
		return null;
	}
	
	
	/**
	 * 设置cookie
	 * @author SZG
	 * 2016年5月3日 下午1:35:24
	 * @param response
	 * @param value
	 */
	public static void setCookie(HttpServletResponse response,String value){
		//添加cookie
		Cookie cookie=new Cookie(ConfigUtil.get("loginCookieName"), value);
		//设置路径，这个路径即该工程下都可以访问该cookie 如果不设置路径，那么只有设置该cookie路径及其子路径可以访问
		cookie.setPath("/");
		//设置有效期30分钟
		cookie.setMaxAge(30 * 60);
		response.addCookie(cookie);
	}
}
