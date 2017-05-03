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
		  
		String genesign = getSign(map, "biubiu_app_7D&jj#@w");
		//
		System.out.println("签名2："+genesign);
		
	/*	map.put("sign", genesign);
		
		boolean verifySign = verifySign(map, "biubiu_app_7D&jj#@w");
		System.err.println(verifySign);
		
		
		
	*/
		
		System.out.println(Md5Util.getMD5("budget=55.0&deposit_cycle=1&exact_date=0&name=出来了&per_money=1.0&state=0&timestamp=1493719054495&token=4850D8E01D4C9DAB827C2FB1BB048956&url=http://api.zq.jiejiaohui.com/upload/wish/default/9eef71bd-b264-4c4e-8e41-58df4f27440f.png&secret=biubiu_app_7D&jj#@w").toUpperCase());
	}
	
}
