package com.jiejiao.common.tencent;

/**
 * 模版消息属性
 * 
 * @author shizhiguo
 * @date 2017年2月8日 下午12:02:54
 */
public class WXTemplateMsgAttr {
	
	private String value;
	private String color;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public WXTemplateMsgAttr(String value, String color) {
		this.value = value;
		this.color = color;
	}
}
