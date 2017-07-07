package com.jiejiao.common.utils;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Test;

import com.jiejiao.common.tencent.RandomStringGenerator;
import com.jiejiao.common.utils.log.LogKit;
import com.jiejiao.common.utils.security.Md5Util;

public class Md5Test {

	
	@Test
	public void test1() throws Exception {
		String pwd="123456";
		System.out.println("原密码："+pwd);
		String md5pwd = Md5Util.getMD5(pwd);
		System.out.println("普通MD5加密:"+md5pwd);
		Random r = new Random();
		String salt= RandomStringGenerator.getRandomStringByLength(16);
		System.out.println("salt:"+salt);
		String md5Final = Md5Util.getMD5(pwd+salt);
		System.out.println("加盐后的MD5加密:"+Md5Util.getMD5(pwd+salt));
		
		
		  char[] cs = new char[48];  
	      for (int i = 0; i < 48; i += 3) {  
	          cs[i] = md5Final.charAt(i / 3 * 2);  
	          char c = salt.charAt(i / 3);  
	          cs[i + 1] = c;  
	          cs[i + 2] = md5Final.charAt(i / 3 * 2 + 1);  
	      }  
	      String rs = new String(cs);  
	      System.out.println("最终的密文结果："+rs);
	}
	
	
	@Test
	public void test2() throws Exception {
		String savePwd="c928yc12c5r9622c37dc5epf7y343b7w0216120c4acm91s8";
		char[] saltByte=new char[16]; 
		char[] pwdByte=new char[32]; 
		 for (int i = 0; i < 48; i += 3) {  
			 pwdByte[i / 3 * 2] = savePwd.charAt(i);  
			 pwdByte[i / 3 * 2 + 1] = savePwd.charAt(i + 2);  
			 saltByte[i / 3] = savePwd.charAt(i + 1);  
         }  
		 
		 System.out.println("md5："+new String(pwdByte));
		 System.out.println("salt："+new String(saltByte));
	}
	
	
	@Test
	public void test3() throws Exception {
		
		String md5 = Md5Util.getSaltMd5("123456");
		System.out.println("md5:"+md5);
		boolean b = Md5Util.verifySaltMd5("123456", md5);
		LogKit.info("123");
	}
}
