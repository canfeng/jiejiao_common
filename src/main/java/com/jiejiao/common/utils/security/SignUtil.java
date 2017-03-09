package com.jiejiao.common.utils.security;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.jiejiao.common.tencent.Util;

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
	
	
	
	@Test
	public void signTest() throws Exception {
		Map<String, Object> map=new HashMap<>();
		map.put("id", 123);
		map.put("name", "史治国");
		map.put("sex", 1);
		map.put("time",1459650390);
		map.put("is_del", false);
		String str = getSign(map,"jiejiao");
	}
	
	@Test
	public void verifySignTest() throws Exception {
		Map<String, Object> map=new HashMap<>();
		map.put("id", 123);
		map.put("name", "史治国");
		map.put("sex", 1);
		map.put("time", 1459650390);
		map.put("is_del", false);
		map.put("sign", "45F13FAC039F15A6C63237C355F1D413");
		boolean verifySign = verifySign(map, "jiejiao");
		System.err.println(verifySign);
	}
}
