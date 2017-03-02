package com.jiejiao.common.utils;


/**
 * 域名相关
 * @author shizhiguo
 * @date 2017年2月13日 上午11:38:04
 */
public class DomainUtil {

	/**
	 * 检测传入url是否为可信域名之一
	 * @author shizhiguo
	 * @date 2017年2月13日 上午11:38:41
	 * @param url 要检测的url
	 * @param trust_domain 可信域名列表
	 * @return
	 */
	public static boolean checkTrust(String url,String trust_domain){
		
		if(url.startsWith("http://") || url.startsWith("https://")){
			
			String urlDomain = url.split("://")[1];
			
			String[] domains = trust_domain.split(",");
			for (String domain : domains) {
				boolean trust = urlDomain.startsWith(domain);
				if (trust) {
					return true;
				}
			}
			return false;
		}
		
		//没有前置域名，内部跳转，直接通过
		return true;
		
	}
}
