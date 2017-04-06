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
		map.put("city", "闸北");
		map.put("nickname", "Gggg");
		map.put("province", "上海");
		map.put("sex", "2");
		map.put("token", "9A009B9DF22C833687CB3E6D5C05ED9E");
		map.put("timestamp", "1491448373711");
		  
		String genesign = getSign(map, "biubiu_app_7D&jj#@w");
		//
		System.out.println("签名1：4FCD2A7557B8B66DA23FF676A7B49418");
		System.out.println("签名2："+genesign);
		
		map.put("sign", genesign);
		
		boolean verifySign = verifySign(map, "biubiu_app_7D&jj#@w");
		System.err.println(verifySign);
		
		
		System.out.println(Md5Util.getMD5("city=舟山&nickname=Vvv&province=浙江&sex=2&timestamp=1491450601572&token=A16198CE92EE24A58ECB82EBECD21840&secret=biubiu_app_7D&jj#@w").toUpperCase());
	}
	
}
