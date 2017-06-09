package com.jiejiao.common.utils.security;

import java.util.HashMap;
import java.util.Map;


/**
 * 签名
 * @author shizhiguo
 * @date 2017年3月9日 下午3:03:28
 */
public class SignUtil {

	/**
	 * 获得签名  (大写)
	 * @author shizhiguo
	 * @date 2017年3月9日 下午3:19:24
	 * @param map 参数对
	 * @param secret 验证密钥
	 * @return
	 */
	public static String getSign(Map<String, Object> map,String secret){
		String sortLinkString = ParamUtil.sortLinkString(map);
		sortLinkString += "secret=" + secret;
        System.out.println("Sign Before MD5:" + sortLinkString);
        String result = Md5Util.getMD5(sortLinkString).toUpperCase();
        System.out.println("Sign Result:" + result);
        return result;
	}
	
	/**
	 * 验证签名的正确
	 * @author shizhiguo
	 * @date 2017年3月9日 下午3:20:59
	 * @param paramMap 传入参数map (直接传入request.getParamterMap())
	 * @param secret
	 * @return
	 */
	public static boolean verifySign(Map<String, Object> paramMap,String secret){
		String sign=paramMap.get("sign").toString();//获取签名
		Map<String, Object> map=new HashMap<>();
		map.putAll(paramMap);
		map.remove("sign");
		if(map.containsKey("token") && map.get("token").toString().length() != 32){
			map.remove("token");
		}
		String genesign = getSign(map, secret);
		if (sign.toUpperCase().equals(genesign)) {
			return true;
		}
		return false;
	}
	
	public static void main(String[] args) {
		Map<String, Object> map=new HashMap<>();
		//map.put("city", "怀柔");
		//map.put("nickname", "Dfff");
		//map.put("userid", "");
		//map.put("type", "0");
		//map.put("token", "BBE5073346DB45EB18B3554C62362172");
		map.put("timestamp", System.currentTimeMillis());
		map.put("token", ""); 
		
		System.out.println(map.get("token").equals(map.get("token").toString()));
	}
}
