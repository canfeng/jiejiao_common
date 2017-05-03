package com.jiejiao.common.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class DateUtil {

	/**
	 * 随机生成一个日期（今天之前）
	 * 
	 * @author shizhiguo
	 * @date 2016年12月9日 下午2:25:10
	 * @param num
	 *            生成的范围
	 * @return
	 */
	public static Date randomDate(int num) {
		Date now = new Date();
		Random random = new Random();
		int days = random.nextInt(num);
		Date date = getSpecifiedDayBefore(now, days);
		return date;
	}

	/**
	 * 获得指定日期的前几天
	 * 
	 * @param specifiedDay
	 * @return
	 * @throws Exception
	 */
	public static Date getSpecifiedDayBefore(Date olddate, int days) {// 可以用new
																		// Date().toLocalString()传递参数
		Calendar c = Calendar.getInstance();
		Date date = null;
		try {
			String format = DateFormat.getInstance().format(date);
			date = new SimpleDateFormat("yy-MM-dd").parse(format);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		c.setTime(date);
		int day = c.get(Calendar.DATE);
		c.set(Calendar.DATE, day - days);

		return c.getTime();
	}
	
	/**
	 * 获取两个日期之间的相差的毫秒数
	 * @author shizhiguo
	 * @date 2017年4月21日 上午10:20:19
	 * @param end
	 * @param start
	 * @return
	 */
	public static long getDateMillsDiff(Date end, Date start) {
		Calendar calendar1 = Calendar.getInstance();
		calendar1.setTime(end);
		long time1 = calendar1.getTimeInMillis();
		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTime(start);
		long time2 = calendar2.getTimeInMillis();
		return time1 - time2;
	}

	/**
	 * 计算两个日期之间的间隔天数
	 * 
	 * @author shizhiguo
	 * @date 2017年3月20日 上午10:44:22
	 * @param end
	 * @param start
	 * @return
	 */
	public static int getDateDiff(Date end, Date start) {
		Calendar calendar1 = Calendar.getInstance();
		calendar1.setTime(end);
		long time1 = calendar1.getTimeInMillis();
		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTime(start);
		long time2 = calendar2.getTimeInMillis();
		long diff = (time1 - time2) / (1000 * 60 * 60 * 24);
		return Integer.parseInt(String.valueOf(diff));
	}

	/**
	 * 计算两个日期之间的间隔月数
	 * 
	 * @author shizhiguo
	 * @date 2017年3月20日 上午10:44:22
	 * @param end
	 * @param start
	 * @return
	 */
	public static int getMonthDiff(Date end, Date start) {
		Calendar calendar1 = Calendar.getInstance();
		calendar1.setTime(end);
		int month1 = calendar1.get(Calendar.MONTH);
		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTime(start);
		int month2 = calendar2.get(Calendar.MONTH);

		int months = (calendar1.get(Calendar.YEAR) - calendar2.get(Calendar.YEAR)) * 12;

		return month1 - month2 + months;
	}

	/**
	 * 计算两个日期之间的间隔周数
	 * 
	 * @author shizhiguo
	 * @date 2017年3月20日 上午10:44:22
	 * @param end
	 * @param start
	 * @return
	 */
	public static int getWeekDiff(Date end, Date start) {
		Calendar calendar1 = Calendar.getInstance();
		calendar1.setTime(end);
		long time1 = calendar1.getTimeInMillis();
		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTime(start);
		long time2 = calendar2.getTimeInMillis();
		Long diff = (time1 - time2) / (1000 * 60 * 60 * 24 * 7);
		return diff.intValue();
	}

}
