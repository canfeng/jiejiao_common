package com.jiejiao.common.utils.weixin.entity;

import com.alibaba.fastjson.annotation.JSONField;

public class WXConfig {

	/**
	 * app Ӧ��id
	 */
	private String appId;
	/**
	 * appӦ����Կ
	 */
	@JSONField(serialize = false)
	private String appSecret;
	/**
	 * access_token ��2Сʱ��Ч��
	 */
	@JSONField(serialize = false)
	private String access_token;
	/**
	 * js�ӿ���ʱƱ�� ��2Сʱ��Ч��
	 */
	@JSONField(serialize = false)
	private String jsapi_ticket;
	/**
	 * ����ַ���
	 */
	private String nonceStr;
	/**
	 * ʱ���
	 */
	private String timestamp;
	/**
	 * ǩ��
	 */
	private String signature;

	/**
	 * ���ýӿڵ�url
	 */
	private String url;

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getAppSecret() {
		return appSecret;
	}

	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}

	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

	public String getJsapi_ticket() {
		return jsapi_ticket;
	}

	public void setJsapi_ticket(String jsapi_ticket) {
		this.jsapi_ticket = jsapi_ticket;
	}

	public String getNonceStr() {
		return nonceStr;
	}

	public void setNonceStr(String nonceStr) {
		this.nonceStr = nonceStr;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String toString() {
		return "WXConfig [appId=" + appId + ", appSecret=" + appSecret + ", access_token=" + access_token
				+ ", jsapi_ticket=" + jsapi_ticket + ", nonceStr=" + nonceStr + ", timestamp=" + timestamp
				+ ", signature=" + signature + ", url=" + url + "]";
	}
	
	

}
