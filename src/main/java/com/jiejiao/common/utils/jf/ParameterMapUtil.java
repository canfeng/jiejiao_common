package com.jiejiao.common.utils.jf;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.jiejiao.common.utils.StringUtil;

public class ParameterMapUtil {

	/**
	 * 复制一份request map
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> parameter2Map(HttpServletRequest request) {
		return parameter2Map(request, true);
	}
	/**
	 * 复制一份request map
	 * @param removeEmptyItem:是否去除空项
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> parameter2Map(HttpServletRequest request,boolean removeEmptyItem) {
		
		Enumeration<String> enu=request.getParameterNames();  
		
		Map<String, Object> map = new HashMap<>();
		
		while(enu.hasMoreElements()){  
			String paraName=(String)enu.nextElement(); 
			String value = request.getParameter(paraName);
			if(removeEmptyItem){
				if (!StringUtil.isNullOrEmpty(value)) {
					map.put(paraName, request.getParameter(paraName));
				}
			}else{
				map.put(paraName, request.getParameter(paraName));
			}
		}
		return map;
	}
}
