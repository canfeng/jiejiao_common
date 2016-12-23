package com.jiejiao.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class DateUtil {

	/**
	 * 随机生成一个日期（今天之前）
	 * @author shizhiguo
	 * @date 2016年12月9日 下午2:25:10
	 * @param num 生成的范围
	 * @return 
	 */
	public static Date randomDate(int num){
		Date now = new Date();
		Random random=new Random();
		int days = random.nextInt(num);
		Date date = getSpecifiedDayBefore(now,days);
		return date;
	}
	/** 
     * 获得指定日期的前几天 
     *  
     * @param specifiedDay 
     * @return 
     * @throws Exception 
     */  
    public static Date getSpecifiedDayBefore(Date olddate,int days) {//可以用new Date().toLocalString()传递参数  
        Calendar c = Calendar.getInstance();  
        Date date = null;  
        try {  
            date = new SimpleDateFormat("yy-MM-dd").parse(olddate.toLocaleString());  
        } catch (ParseException e) {  
            e.printStackTrace();  
        }  
        c.setTime(date);  
        int day = c.get(Calendar.DATE);  
        c.set(Calendar.DATE, day - days);  
       
        return  c.getTime();
    }  
	
}
