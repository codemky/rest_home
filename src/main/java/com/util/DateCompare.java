package com.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/*判断开始时间和结束时间是否相同,
 *返回1:begin大于end;
 *返回0:begin等于end;
 *返回-1:begin小于end
*/
public class DateCompare {
	public static int compareDate(String endDate){
		DateFormat fmt = new SimpleDateFormat("yyyyMMdd"); 
		int result = 0;
		try {
			Date begin = fmt.parse(StrToDate.FormatDateToStr1(new Date()));    //当前时间
			Date end = fmt.parse(endDate);   //结束时间	
			result = begin.compareTo(end);
		} catch (ParseException e) {
			e.printStackTrace();
		} //开始时间
		return result;
	}
}
