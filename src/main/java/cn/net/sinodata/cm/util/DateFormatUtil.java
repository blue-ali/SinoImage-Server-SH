package cn.net.sinodata.cm.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * @author manan
 *
 */
public class DateFormatUtil {
		public static String RULE_DATE_DAUFLAT = "yyyy-MM-dd";
		public static String NO_SPLIT_SIGN_DATE = "yyyyMMdd";
		public static String NO_SPLIT_SIGN_TIME = "HHmmss";
		public static String NO_SPLIT_SIGN_DATETIME = "yyyyMMddHHmmss";
		public static String RULE_TIME_DAUFLAT = "yyyy-MM-dd HH:mm:ss";
		
		public static Date parseStringDate(String strDate) throws ParseException{
			SimpleDateFormat sdf = new SimpleDateFormat(RULE_TIME_DAUFLAT);
			return sdf.parse(strDate);
		}
		
		public static String formatDate(Date date) throws ParseException{
			SimpleDateFormat sdf = new SimpleDateFormat(RULE_TIME_DAUFLAT);
			return sdf.format(date);
		}
		
		public static String formatStringDate(String strDate, String fmtFrom, String fmtTo) throws ParseException{
			SimpleDateFormat sdfFrom = new SimpleDateFormat(fmtFrom);
			SimpleDateFormat sdfTo = new SimpleDateFormat(fmtTo);
			return sdfTo.format(sdfFrom.parse(strDate));
		}
		
		public static String formatIntDate2Str(int intDate, int intTime, String pattern) throws ParseException{
			String strDate = String.valueOf(intDate) + String.valueOf(intTime);
			return formatStringDate(strDate, NO_SPLIT_SIGN_DATETIME, pattern);
		}
		
		public static Date formatIntDate(int intDate, int intTime) throws ParseException{
			String strDate = String.valueOf(intDate) + String.valueOf(intTime);
			SimpleDateFormat sdf = new SimpleDateFormat(NO_SPLIT_SIGN_DATETIME);
			return sdf.parse(strDate);
		}
		
		public static int getIntDateFromDate(Date date) throws ParseException{
			SimpleDateFormat sdf = new SimpleDateFormat(NO_SPLIT_SIGN_DATE);
			return Integer.valueOf(sdf.format(date));
		}
		
		public static int getIntTimeFromDate(Date date) throws ParseException{
			SimpleDateFormat sdf = new SimpleDateFormat(NO_SPLIT_SIGN_TIME);
			return Integer.valueOf(sdf.format(date));
		}
		/**
		 * 转为yyyy-MM-dd HH:mm:ss格式日期
		 * @param intDate
		 * @param intTime
		 * @return
		 * @throws ParseException
		 */
		public static String formatIntDate2Str(int intDate, int intTime) throws ParseException{
			String strDate = String.valueOf(intDate) + String.valueOf(intTime);
			return formatStringDate(strDate, NO_SPLIT_SIGN_DATE, RULE_TIME_DAUFLAT);
		}
		
		
		/**
		 * 字符串转化为日期对象
		 * @param str
		 * @return
		 * @throws ParseException
		 */
		public static Calendar getDate(String str) throws ParseException{
			DateFormat sf = new SimpleDateFormat(RULE_DATE_DAUFLAT);
			Calendar cal = Calendar.getInstance();
			cal.setTime(sf.parse(str));
			return cal;
		}
		
		/**
		 * 字符串转化为日期对象
		 * @param str
		 * @return
		 * @throws ParseException
		 */
		public static Calendar getTime(String str) throws ParseException{
			DateFormat sf = new SimpleDateFormat(RULE_TIME_DAUFLAT);
			Calendar cal = Calendar.getInstance();
			cal.setTime(sf.parse(str));
			return cal;
		}
		
		/**
		 * 将当前日期转化为
		 * @return
		 */
		public static String getCurrentDateStr(){
			DateFormat sf = new SimpleDateFormat(NO_SPLIT_SIGN_DATE);
			return sf.format(new Date());
		}
		
		/**
		 * 获取系统当前时间，格式 hh:MM:ss
		 * @return
		 */
		public static String getCurrentTimeStr(){
			return DateFormat.getTimeInstance(2).format(new Date());
		}
		
		
		/**
		 *   @生成时间： 2012-8-27 下午1:06:18
		 *   @方法说明： 获取当前日期和时间
		 *   @参数：
		 *   @返回值： 
		 *   @异常：
		 **/
		public static String getCurrentDateAndTimeStr(){
			DateFormat sf = new SimpleDateFormat(RULE_TIME_DAUFLAT);
			return sf.format(new Date());
		}
		
		public static void main(String[] args) throws ParseException {
			System.out.println(getCurrentDateAndTimeStr());
			
			System.out.println(getDate("1981-01-02").get(Calendar.YEAR));
			System.out.println(getDate("1981-01-02").get(Calendar.MONTH));
			System.out.println(getDate("1981-01-02").get(Calendar.DATE));
			
			System.out.println(getTime("1981-01-02 09:08:03").get(Calendar.YEAR));
			System.out.println(getTime("1981-01-02 09:08:03").get(Calendar.MONTH));
			System.out.println(getTime("1981-01-02 09:08:03").get(Calendar.DATE));
			System.out.println(getTime("1981-01-02 09:08:03").get(Calendar.HOUR));
			System.out.println(getTime("1981-01-02 09:08:03").get(Calendar.MINUTE));
			System.out.println(getTime("1981-01-02 09:08:03").get(Calendar.SECOND));
		}
}
