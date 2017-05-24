package com.jiejiao.common.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;

public class MapUtil {

	 public static Object mapToObject(Map<String, Object> map, Class<?> beanClass) throws Exception {      
	        if (map == null)    
	            return null;      
	     
	        Object obj = beanClass.newInstance();    
	     
	        Field[] fields = obj.getClass().getDeclaredFields();     
	        for (Field field : fields) {      
	            int mod = field.getModifiers();      
	            if(Modifier.isStatic(mod) || Modifier.isFinal(mod)){      
	                continue;      
	            }      
	     
	            field.setAccessible(true);      
	            field.set(obj, map.get(field.getName()));     
	        }     
	     
	        return obj;      
	    }      
	     
	    public static Map<String, Object> objectToMap(Object obj) throws Exception {      
	        if(obj == null){      
	            return null;      
	        }     
	     
	        Map<String, Object> map = new HashMap<String, Object>();      
	     
	        Field[] declaredFields = obj.getClass().getDeclaredFields();      
	        for (Field field : declaredFields) {      
	            field.setAccessible(true);    
	            map.put(field.getName(), field.get(obj));    
	        }      
	     
	        return map;    
	    }     
	    
	    public static Map<String, String> objectToStrMap(Object obj) throws Exception {      
	    	if(obj == null){      
	    		return null;      
	    	}     
	    	
	    	Map<String, String> map = new HashMap<String, String>();      
	    	
	    	Field[] declaredFields = obj.getClass().getDeclaredFields();      
	    	for (Field field : declaredFields) {      
	    		field.setAccessible(true);    
	    		Object object = field.get(obj);
	    		if (object != null) {
	    			map.put(field.getName(), JSON.toJSONString(object));    
				}
	    	}      
	    	
	    	return map;    
	    }     
}
