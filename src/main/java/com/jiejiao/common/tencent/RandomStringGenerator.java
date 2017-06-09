package com.jiejiao.common.tencent;

import java.util.Random;

/**
 * User: rizenguo
 * Date: 2014/10/29
 * Time: 14:18
 */
public class RandomStringGenerator {

    /**
     * 获取一定长度的随机字符串
     * @param length 指定字符串长度
     * @return 一定长度的字符串
     */
    public static String getRandomStringByLength(int length) {
        String baseStr = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(baseStr.length());
            sb.append(baseStr.charAt(number));
        }
        return sb.toString();
    }
    
    /**
     * 获取一定长度的随机字符串（包括大写字母）
     * @param length 指定字符串长度
     * @return 一定长度的字符串
     */
    public static String getRandomStringHasUpperCharactorByLength(int length) {
    	String baseStr = "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    	Random random = new Random();
    	StringBuffer sb = new StringBuffer();
    	for (int i = 0; i < length; i++) {
    		int number = random.nextInt(baseStr.length());
    		sb.append(baseStr.charAt(number));
    	}
    	return sb.toString();
    }
    
    /**
     * 获取一定长度的随机数（纯数字）
     * @author shizhiguo
     * @date 2017年4月6日 下午5:13:22
     * @param length
     * @return
     */
    public static String getRandomNumberStringByLength(int length){
    	 String baseStr = "123456789";
         Random random = new Random();
         StringBuffer sb = new StringBuffer();
         for (int i = 0; i < length; i++) {
             int number = random.nextInt(baseStr.length());
             sb.append(baseStr.charAt(number));
         }
         return sb.toString();
    }
    
    /**
     * 获取一定长度的随机数（包括大写和特殊字符，用于生成随机密码）
     * @author shizhiguo
     * @date 2017年4月6日 下午5:13:48
     * @param length
     * @return
     */
    public static String getRandomPwdStringByLength(int length){
    	  String baseStr = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789*&$#?!";
          Random random = new Random();
          StringBuffer sb = new StringBuffer();
          for (int i = 0; i < length; i++) {
              int number = random.nextInt(baseStr.length());
              sb.append(baseStr.charAt(number));
          }
          return sb.toString();
    }

}
