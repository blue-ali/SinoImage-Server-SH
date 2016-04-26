/**
 * 
 */
package cn.net.sinodata.cm.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Administrator
 *
 */
public class DateUtil {
	
	public static Date addTime(final Date date , int hour, int minute, int second){
		int time = (hour * 60 * 60 + minute * 60 + second) * 1000;
		date.setTime(date.getTime() + time);
		return date;
	}
	
	public static Date parse(String str, String pattern) throws ParseException{
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.parse(str);
	}
	
	public static String format(Date date, String pattern){
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(date);
	}
	
	public static String formatStringDate(String strDate, String fmtFrom, String fmtTo) throws ParseException{
		SimpleDateFormat sdfFrom = new SimpleDateFormat(fmtFrom);
		SimpleDateFormat sdfTo = new SimpleDateFormat(fmtTo);
		return sdfTo.format(sdfFrom.parse(strDate));
	}
	
	public static void main(String[] args) {
	}

}
