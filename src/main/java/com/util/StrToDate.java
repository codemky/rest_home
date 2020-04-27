package com.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
/*
 * 字符串转时间
 */
public class StrToDate {

	public static Date FormatStrToDate(String str) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = format.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	
	public static Date FormatStrToDateTime(String str) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = format.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	
	public static String getStringDate(String s){
    	Date date = new Date();
    	date.setTime(new Long(s));
    	SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	return sdf.format(date);
    }

    public static Date stampToDate(String s){
        long lt = new Long(s);
        Date date = new Date(lt);
        return date;
    }
	
	public static Date FormatStrToHour(String str) {
		SimpleDateFormat format = new SimpleDateFormat("HH:mm");
		Date date = null;
		try {
			date = format.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	
	public static String FormatDateToStr(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String str = format.format(date);  
		return str;
	}
	
	public static String FormatDateToStr1(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		String str = format.format(date);  
		return str;
	}
}
