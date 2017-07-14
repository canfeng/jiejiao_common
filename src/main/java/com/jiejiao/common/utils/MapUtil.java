package com.jiejiao.common.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.jiejiao.common.utils.log.LogKit;

public class MapUtil {

	public static Object mapToObject(Map<String, Object> map, Class<?> beanClass) {
		if (map == null)
			return null;

		Object obj = null;
		try {
			obj = beanClass.newInstance();

			Field[] fields = obj.getClass().getDeclaredFields();
			for (Field field : fields) {
				int mod = field.getModifiers();
				if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
					continue;
				}

				field.setAccessible(true);
				field.set(obj, map.get(field.getName()));
			}
		} catch (Exception e) {
			LogKit.error(e + "\tEXception Line==>" + e.getStackTrace()[0]);
		}

		return obj;
	}

	public static Map<String, Object> objectToMap(Object obj) {
		if (obj == null) {
			return null;
		}

		Map<String, Object> map = new HashMap<String, Object>();

		Field[] declaredFields = obj.getClass().getDeclaredFields();
		for (Field field : declaredFields) {
			field.setAccessible(true);
			Object object = null;
			try {
				object = field.get(obj);
			} catch (Exception e) {
				LogKit.error(e + "\tEXception Line==>" + e.getStackTrace()[0]);
			}
			map.put(field.getName(), object);
		}

		return map;
	}

	public static Map<String, String> objectToStrMap(Object obj) {
		if (obj == null) {
			return null;
		}

		Map<String, String> map = new HashMap<String, String>();

		Field[] declaredFields = obj.getClass().getDeclaredFields();
		for (Field field : declaredFields) {
			field.setAccessible(true);
			Object object = null;
			try {
				object = field.get(obj);
			} catch (Exception e) {
				LogKit.error(e + "\tEXception Line==>" + e.getStackTrace()[0]);
			}
			if (object != null) {
				map.put(field.getName(), JSON.toJSONString(object));
			}
		}
		return map;
	}
}
