package com.jiejiao.common.utils;

import org.junit.Test;

import com.jiejiao.common.utils.config.ConfigUtils;

public class ConfigUtilsTest {

	
	@Test
	public void test1() throws Exception {
		
		String username = ConfigUtils.get("username");
		System.out.println(username);
	}
	
	@Test
	public void test2() throws Exception {
		
		ConfigUtils configUtils = new ConfigUtils("conf/test");
		String string = configUtils.get("pwd");
		System.out.println(string);
	}
}
