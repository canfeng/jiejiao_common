package com.jiejiao.common.utils;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.alibaba.fastjson.JSON;

public class UrlUtil {

	/**
	 * 将url参数对格式的字符串 转为 map键值对形式
	 * 
	 * @param urlStr
	 * @return
	 */
	public static Map<String, String> urlToMap(String urlStr) {
		Map<String, String> map = new HashMap<String, String>();
		String[] urlArr = urlStr.split("&");
		for (String url : urlArr) {
			String[] param = url.split("=");
			map.put(param[0], param[1]);
		}
		return map;
	}

	/**
	 * 对象转url参数对 shizhiguo 2016年9月19日 下午11:33:19
	 */
	public static String objToUrlStr1(Object object) {
		String str = "";
		Class<? extends Object> clazz = object.getClass();
		Field[] declaredFields = clazz.getDeclaredFields();
		for (Field field : declaredFields) {
			PropertyDescriptor pd;
			Method getMethod;
			try {
				String name = field.getName();
				if ("praiseType".equals(name)) {
					continue;
				}
				pd = new PropertyDescriptor(name, clazz);
				getMethod = pd.getReadMethod();// 获得get方法
				Object o = getMethod.invoke(object);// 执行get方法返回一个Object
				if (o != null) {
					str += "&" + name + "=" + o;
				}
			} catch (IntrospectionException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		str = str.substring(1);
		return str;
	}

	/**
	 * 对象转url参数对 shizhiguo 2016年9月19日 下午11:33:19
	 */
	public static String objToUrlStr(Object object) {
		String jsonStr = JSON.toJSONString(object);
		jsonStr = jsonStr.replace("\"", "").replace("{", "").replace("}", "").replace(":", "=").replace(",", "&");
		System.out.println(jsonStr);
		return jsonStr;
	}

	public static String mapToUrlStr(Map map) {
		Iterator entries = map.entrySet().iterator();
		String url = "";
		Map.Entry entry;
		String name = "";
		String value = "";
		while (entries.hasNext()) {
			entry = (Map.Entry) entries.next();
			name = (String) entry.getKey();
			if ("posturl".equals(name)) {
				continue;
			}
			Object valueObj = entry.getValue();
			if (null == valueObj) {
				value = "";
			} else if (valueObj instanceof String[]) {
				String[] values = (String[]) valueObj;
				for (int i = 0; i < values.length; i++) {
					value = values[i] + ",";
				}
				value = value.substring(0, value.length() - 1);
			} else {
				value = valueObj.toString();
			}
			url += "&" + name + "=" + value;
		}
		return url.substring(1);
	}

	public static void main(String[] args) {
		Map<String, Object> map = new HashMap<>();
		map.put("name", "http://21szg");
		map.put("age", "123");
		String objToUrlStr = mapToUrlStr(map);
		System.out.println(objToUrlStr);
	}
}
