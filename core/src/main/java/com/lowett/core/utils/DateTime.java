package com.lowett.core.utils;

import android.text.TextUtils;

import java.math.BigDecimal;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Date Time 
 * @author Hyu
 *
 */
public class DateTime {

	public static final String[] WEEKS = new String[]{ "周日","周一", "周二", "周三", "周四", "周五", "周六"};

	public enum DateTimeFiled{YEAR, MONTH, DAY, HOUR, MINUTE};
	//Time scope types
	public static final int TIME_TODAY = 0;
	public static final int TIME_YESTERDAY = 1;
	public static final int TIME_THISWEEK = 2;
	public static final int TIME_LASTWEEK = 3;
	public static final int TIME_THISMONTH = 4;
	public static final int TIME_LASTMONTH = 5;
	public static final int TIME_THISQUARTER = 6;
	public static final int TIME_LASTQUARTER = 7;
	public static final int TIME_THISYEAR = 8;
	public static final int TIME_LASTYEAR = 9;
	public static final int TIME_RECENT7DAYS = 10;
	public static final int TIME_RECENT30DAYS = 11;
	public static final int TIME_RECENT3DAYS = 12;
	public static final int TIME_RECENT2DAYS = 13;
	
	public static final int[] DAYS_OF_MONTH_NORMAL = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
	public static final int[] DAYS_OF_MONTH_LEAP = {31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    public static final String FORMAT_COMMON                =  "yyyy-MM-dd HH:mm:ss";
    public static final String FORMAT_YMD_HOUR_MINUTE_ZERO  =  "yyyy-MM-dd HH:mm";
    public static final String FORMAT_HOUR_MINUTE_ZERO      =  "HH:mm";
    public static final String FORMAT_YMD_ZERO              =  "yyyy-MM-dd";
    public static final String FORMAT_YM_CH                 =  "yyyy年MM月";
    public static final String FORMAT_MD_CH                 =  "MM月dd日";
    public static final String FORMAT_MD_HOUR_MINUTE_CH     =  "MM月dd日  HH:mm";
    public static final String FORMAT_MD_HOUR_MINUTE_WEEK   =  "MM月dd日  HH:mm";
    public static final String FORMAT_MD_HOUR_ONLY_WEEK     =  " HH:mm";
    public static final String FORMAT_HOUR_MINITE			=  "HH:mm";
    public static final String FORMAT_MD_HM                 =  "MM-dd HH:mm";
    public static final String FORMAT_Y_M_D                 =  "yyyy.MM.dd";
	public static final String FORMAT_YM_CH_DH				=  "yyyy年MM月dd日";
	public static final String FORMAT_M_D					=  "MM-dd";




    public static final long TIME_MILLS_ONEDAY 	= 24*60*60*1000;
    public static final int TIME_SECONDS_ONEDAY = 24*60*60;
    public static final long TIME_MILLS_ONEHOUR = 60*60*1000;
	private int year;
	private int month;
	private int day;
	private int hour;
	private int minute;
	private int second;

	private int weekDay;
	
	private DateTime() {
		
	}


    private void init(Date time){
        if (time != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(time);
            this.year = cal.get(Calendar.YEAR);
            this.month = cal.get(Calendar.MONTH) + 1;
            this.day= cal.get(Calendar.DAY_OF_MONTH);
            this.hour = cal.get(Calendar.HOUR_OF_DAY);
            this.minute = cal.get(Calendar.MINUTE);
            this.second = cal.get(Calendar.SECOND);
			weekDay = cal.get(Calendar.DAY_OF_WEEK) - 1;

        }

    }

    public DateTime(String dateString, String format){
        this();
        Date date = new Date();
        if (!TextUtils.isEmpty(dateString)){
            SimpleDateFormat df = new SimpleDateFormat(format, Locale.getDefault());
            try {
                date = df.parse(dateString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        init(date);
    }

	
	public DateTime(Date time){
		this();
        init(time);
	}

	public DateTime(long time){
		this(new Date(time));
	}
	
	public Date toDate()
	{
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month - 1);
		cal.set(Calendar.DAY_OF_MONTH, day);
		cal.set(Calendar.HOUR_OF_DAY, hour);
		cal.set(Calendar.MINUTE, minute);
		cal.set(Calendar.SECOND, second);
		cal.set(Calendar.MILLISECOND, 0);
		
		return cal.getTime();
	}
	
	public static long getYMD(long time) {
		return new DateTime(time).getYMD();
	}
	
	public  boolean isThanOneHour(long current){
		return (toTime() - current) > TIME_MILLS_ONEHOUR;
	}
	
	public int getPastDay(){
		 Calendar targetCal = Calendar.getInstance();
	     Calendar sourceCal = Calendar.getInstance();
	     targetCal.setTimeInMillis(System.currentTimeMillis());
	     sourceCal.setTimeInMillis(this.toTime());
		return targetCal.get(Calendar.DAY_OF_YEAR) - sourceCal.get(Calendar.DAY_OF_YEAR);
	}
	
	public long getYMD() {
		long ymd = 0;
		ymd = year * 10000 + month * 100 + day;
		
		return ymd;
	}
	
	public long toTime() {
		return toDate().getTime();
	}

	public boolean isDay() {
		return getPastDay() == 0;
	}
	
	public void nextMonth(){
		if (month < 12){
			month++;
		} else{
			year += 1;
			month = 1;
		}
	}
	
	public void preMonth(){
		if (month > 1){
			month--;
		} else {
			year -= 1;
			month = 12;
		}
	}

    public void add(DateTimeFiled filed, int time){
       Calendar cal = Calendar.getInstance();
        cal.setTime(toDate());
        if (filed == DateTimeFiled.YEAR){
            cal.add(Calendar.YEAR, time);
        }else if (filed == DateTimeFiled.MONTH){
            cal.add(Calendar.MONTH, time);
        }else if (filed == DateTimeFiled.DAY){
            cal.add(Calendar.DAY_OF_MONTH, time);
        }else if (filed == DateTimeFiled.HOUR){
            cal.add(Calendar.HOUR_OF_DAY, time);
        }else if (filed == DateTimeFiled.MINUTE){
            cal.add(Calendar.MINUTE, time);
        }

        init(cal.getTime());
    }

    public String getStringDates(String format){
        return new SimpleDateFormat(format).format(toDate());
    }

	public String getWeek() {
		return WEEKS[weekDay];
	}
    
    public String getStringDateWeek(String format){
    	DateFormatSymbols symbols = new DateFormatSymbols(Locale.CHINA);  
	    symbols.setShortWeekdays(new String[]{"", "周日", "周一", "周二", "周三", "周四", "周五", "周六"});
        return new SimpleDateFormat(format,symbols).format(toDate());
    }
	
	public static boolean isLastDayOfMonth(Date date) {
		if (date == null)
			return false;
		
		return date.getDate() == getDaysCount(date.getYear(), date.getMonth() + 1);
	}
	
	/**
	 * To get the days of the month, it's also the last day of the month.
	 * @param nYear: The year, must be the value 'year' in calendar.
	 * @param nMonth: The month, must start with '1', which means 'date' of calendar + 1.
	 * @return
	 */
	public static int getDaysCount(int nYear, int nMonth) {
		int[] nDaysEachMonth = (new GregorianCalendar().isLeapYear(nYear)) ? DAYS_OF_MONTH_LEAP : DAYS_OF_MONTH_NORMAL;
		if (nMonth > nDaysEachMonth.length)
			return -1;

		return nDaysEachMonth[nMonth-1];
	}

	
	public static void InitDate(int type, Date startDate, Date endDate) {
		Calendar c = Calendar.getInstance();
		Date curDate = c.getTime();
		DateTime dt = null;
		long MINISECPH = 3600000;
		switch (type) {
		case TIME_YESTERDAY:
			curDate.setTime(curDate.getTime() - MINISECPH * 24);
		case TIME_TODAY:
			dt = new DateTime(curDate.getTime());
			dt.hour = 0;
			dt.minute = 0;
			dt.second = 0;
			if (startDate != null) {
				startDate.setTime(dt.toTime());
			}
			if (endDate != null) {
				endDate.setTime(dt.toTime() + MINISECPH * 24 - 1000);
			}
			break;
		case TIME_LASTWEEK:
			curDate.setTime(curDate.getTime() - MINISECPH * 7 * 24);
		case TIME_THISWEEK:
			c.setTime(curDate);
			int day = chinaWeekDay(c.get(Calendar.DAY_OF_WEEK));
			curDate.setTime(curDate.getTime() - MINISECPH * day * 24);
			dt = new DateTime(curDate.getTime());
			dt.hour = 0;
			dt.minute = 0;
			dt.second = 0;
			if (startDate != null) {
				startDate.setTime(dt.toTime());
			}
			if (endDate != null) {
				endDate.setTime(dt.toTime() + MINISECPH * 7 * 24 - 1000);
			}
			break;
		case TIME_LASTMONTH:
		case TIME_THISMONTH:
			dt = new DateTime(curDate.getTime());
			if (type == TIME_LASTMONTH) {
				dt.preMonth();
			}
			dt.day = 1;
			dt.hour = 0;
			dt.minute = 0;
			dt.second = 0;
			if (startDate != null) {
				startDate.setTime(dt.toTime());
			}
			
			dt.day = getDaysCount(dt.year, dt.month);
			dt.hour = 23;
			dt.minute = 59;
			dt.second = 59;
			if (endDate != null) {
				endDate.setTime(dt.toTime());
			}
			break;
		case TIME_LASTQUARTER:
		case TIME_THISQUARTER:
			dt = new DateTime(curDate.getTime());
			int month = dt.month;
			int term = month % 3;
			if (term != 0) {
				dt.month = month - month % 3 + 1;
			} else {
				dt.month = month - 2;
			}
			
			if (type == TIME_LASTQUARTER) {
				dt.preMonth();
				dt.preMonth();
				dt.preMonth();
			}
			dt.day = 1;
			dt.hour = 0;
			dt.minute = 0;
			dt.second = 0;
			if (startDate != null) {
				startDate.setTime(dt.toTime());
			}
			
			dt.nextMonth();
			dt.nextMonth();
			dt.day = getDaysCount(dt.year, dt.month);
			dt.hour = 23;
			dt.minute = 59;
			dt.second = 59;
			if (endDate != null) {
				endDate.setTime(dt.toTime());
			}
			break;
		case TIME_THISYEAR:
			dt = new DateTime(curDate.getTime());
			dt.month = 1;
			dt.day = 1;
			dt.hour = 0;
			dt.minute = 0;
			dt.second = 0;
			if (startDate != null) {
				startDate.setTime(dt.toTime());
			}
			dt.year += 1;
			if (endDate != null) {
				endDate.setTime(dt.toTime() - 1000);
			}
			break;
		case TIME_LASTYEAR:
			dt = new DateTime(curDate.getTime());
			dt.month = 1;
			dt.day = 1;
			dt.hour = 0;
			dt.minute = 0;
			dt.second = 0;
			if (endDate != null) {
				endDate.setTime(dt.toTime() - 1000);
			}
			dt.year -= 1;
			if (startDate != null) {
				startDate.setTime(dt.toTime());
			}
			break;
		case TIME_RECENT7DAYS:
			dt = new DateTime(curDate.getTime());
			dt.hour = 23;
			dt.minute = 59;
			dt.second = 59;
			if (endDate != null) {
				endDate.setTime(dt.toTime());
			}
			dt.hour = 0;
			dt.minute = 0;
			dt.second = 0;
			if (startDate != null) {
				startDate.setTime(dt.toTime() - MINISECPH * 6 * 24);
			}
			break;
		case TIME_RECENT30DAYS:
			dt = new DateTime(curDate.getTime());
			dt.hour = 23;
			dt.minute = 59;
			dt.second = 59;
			if (endDate != null) {
				endDate.setTime(dt.toTime());
			}
			dt.hour = 0;
			dt.minute = 0;
			dt.second = 0;
			if (startDate != null) {
				startDate.setTime(dt.toTime() - MINISECPH * 29 * 24);
			}
			break;
		case TIME_RECENT3DAYS:
			dt = new DateTime(curDate.getTime());
			dt.hour = 23;
			dt.minute = 59;
			dt.second = 59;
			if (endDate != null) {
				endDate.setTime(dt.toTime());
			}
			dt.hour = 0;
			dt.minute = 0;
			dt.second = 0;
			if (startDate != null) {
				startDate.setTime(dt.toTime() - MINISECPH * 2 * 24);
			}
			break;
		case TIME_RECENT2DAYS:
			dt = new DateTime(curDate.getTime());
			dt.hour = 23;
			dt.minute = 59;
			dt.second = 59;
			if (endDate != null) {
				endDate.setTime(dt.toTime());
			}
			dt.hour = 0;
			dt.minute = 0;
			dt.second = 0;
			if (startDate != null) {
				startDate.setTime(dt.toTime() - MINISECPH * 24);
			}
			break;
		default:
			break;
		}
	}
	
	public static int chinaWeekDay(int dayOfWeek) {
		int day = 0;
		switch (dayOfWeek) {
		case Calendar.MONDAY:
			day = 0;
			break;
		case Calendar.TUESDAY:
			day = 1;
			break;
		case Calendar.WEDNESDAY:
			day = 2;
			break;
		case Calendar.THURSDAY:
			day = 3;
			break;
		case Calendar.FRIDAY:
			day = 4;
			break;
		case Calendar.SATURDAY:
			day = 5;
			break;
		case Calendar.SUNDAY:
			day = 6;
			break;
		}

		return day;
	}
	
	public static long ymdToTime(long ymd) {
		return ymdToTime((int)(ymd / 10000), 
				(int)(ymd % 10000 / 100), (int)(ymd % 100)).toTime();
	}
	
	public static DateTime createDateTimeByYMD(long ymd) {
		return ymdToTime((int) (ymd / 10000),
				(int) (ymd % 10000 / 100), (int) (ymd % 100));
	}
	
	public static DateTime ymdToTime(int y, int m, int d) {
		DateTime dt = new DateTime(0);
		dt.year = y;
		dt.month = m;
		dt.day = d;
		dt.hour = 0;
		dt.minute = 0;
		dt.second = 0;	
		return dt;
	}
	
	/**
	 * @param nYear the year
	 * @param nMonth the month must start from 1 to 12
	 * @param nDay the day of the month start from 1 to the end
	 * @return the resource index according to the week day (0 ~ 6), which start from Monday.
	 */
	public static int getWeekDayIndex(int nYear, int nMonth, int nDay) {
		Calendar cal = Calendar.getInstance();
		cal.set(nYear, nMonth - 1, nDay);
		return chinaWeekDay(cal.get(Calendar.DAY_OF_WEEK));
	}


    private static boolean isSameDay(Calendar now, Calendar msg) {
        int nowDay = now.get(Calendar.DAY_OF_YEAR);
        int msgDay = msg.get(Calendar.DAY_OF_YEAR);

        return nowDay == msgDay;
    }

    public static boolean isSameDay(long target, long source){
        Calendar targetCal = Calendar.getInstance();
        Calendar sourceCal = Calendar.getInstance();
        targetCal.setTimeInMillis(target);
        sourceCal.setTimeInMillis(source);

        int sourceDay = sourceCal.get(Calendar.DAY_OF_YEAR);
        int targetDay = targetCal.get(Calendar.DAY_OF_YEAR);

        return sourceDay == targetDay;
    }
    
    public static String getTimeNear3Day(long timeMills){
		String timeText = "";
		
		Calendar calendar= Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00"));
		int cyear = calendar.get(Calendar.YEAR);
		int ctoday = calendar.get(Calendar.DAY_OF_YEAR);
		int ctotalDay = calendar.getMaximum(Calendar.DAY_OF_YEAR);
		calendar.setTimeInMillis(timeMills);
		int year = calendar.get(Calendar.YEAR);
		int day = calendar.get(Calendar.DAY_OF_YEAR);
		switch(year - cyear){
		case -1:
		default:
			timeText = getNear3Day(2,8,timeMills);
			break;
		case 0:
			timeText = getNear3Day(day,ctoday,timeMills);
			break;
		case 1:
			int vday = day+(ctotalDay-ctoday)-1;
			timeText = getNear3Day(day+vday,day,timeMills);
			break;
		}
		
		return timeText;
	}
	
	private static String getNear3Day(int day,int ctoday,long timeMills){
		String timeText= "";
		switch (day - ctoday) {
		case 0:
			timeText= "今天";
			timeText += getWeekWithHour(timeMills);
			break;
		case 1:
			timeText = "明天";
			timeText += getWeekWithHour(timeMills);
			break;
		case 2:
			timeText = "后天";
			timeText += getWeekWithHour(timeMills);
			break;
		default:
			timeText = new DateTime(timeMills).getStringDateWeek(DateTime.FORMAT_MD_HOUR_MINUTE_WEEK);
			break;
		}
		return timeText;
	}
	
	/**
	 * "MM月dd日(E) HH:mm"
	 */
	public  static String getMouthDayWithWeek(long timeMills){
		return new DateTime(timeMills).getStringDateWeek(DateTime.FORMAT_MD_HOUR_MINUTE_WEEK);
	}
	
	/**
	 * "(E) HH:mm"
	 */
	public static String getWeekWithHour(long timeMills){
		return new DateTime(timeMills).getStringDateWeek(DateTime.FORMAT_MD_HOUR_ONLY_WEEK);
	}
	
	/**
	 *"HH:mm"
	 */
	public static String getHourOnly(long timeMills){
		return new DateTime(timeMills).getStringDateWeek(DateTime.FORMAT_HOUR_MINITE);
	}
	
	/**
	 * 根据秒得到日期
	 * @param times
	 * @return 2015.09.09
	 */
	public static String getDateBym(long times){
		GregorianCalendar gc = new GregorianCalendar(); 
	    gc.setTimeInMillis(times);//* 1000
	    SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");
	    return format.format(gc.getTime()); 
	}
	
	/**
	 * 根据秒得到日期
	 * @param str（秒 服务器直接传入 ）
	 * @return 2015.09.09
	 */
	public static String getDateBym(String str){
		
		BigDecimal db = new BigDecimal(Double.valueOf(str)+"");
		long times =Long.valueOf(db.toPlainString());
	    return getDateBym(times);
		
	}
	
	/**
	 * 根据秒得到日期
	 * @param times
	 * @return 2015年09月09日
	 */
	public static String getDateByStr(long times){
		GregorianCalendar gc = new GregorianCalendar(); 
	    gc.setTimeInMillis(times);//* 1000
	    SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
	    return format.format(gc.getTime()); 
	}

	// 1小时30风中
	public static String getTimeByStr(long times){
		GregorianCalendar gc = new GregorianCalendar();
	    gc.setTimeInMillis(times);//* 1000
	    SimpleDateFormat format = new SimpleDateFormat("HH小时mm分钟ss秒");
	    return format.format(gc.getTime());
	}
	
	
	/**
	 * 根据秒得到日期
	 * @return 2015年09月09日
	 */
	public static String getDateByStr(String str){
		
		BigDecimal db = new BigDecimal(Double.valueOf(str)+"");
		long times =Long.valueOf(db.toPlainString());
	    return getDateByStr(times);
	}
	
	
	/**
	 * 根据秒得到日期
	 * @param times
	 * @return 2015-09-09 09:09:09
	 */
	public static String getDateBya(long times){
		GregorianCalendar gc = new GregorianCalendar(); 
	    gc.setTimeInMillis(times);//* 1000
	    SimpleDateFormat format = new SimpleDateFormat(FORMAT_COMMON);
	    return format.format(gc.getTime()); 
	}
	
	/**
	 * 根据秒得到日期
	 * @param str(秒 服务器直接传入)
	 * @return 2015-09-09 09:09:09
	 */
	public static String getDateBya(String str){
		BigDecimal db = new BigDecimal(Double.valueOf(str)+"");
		long times =Long.valueOf(db.toPlainString());
	    return getDateBya(times);
	}
	
	
	/**
	 * 根据秒得到日期
	 * @param times
	 * @return 2015-09-09
	 */
	public static String getDateByb(long times){
		GregorianCalendar gc = new GregorianCalendar(); 
	    gc.setTimeInMillis(times);//* 1000
	    SimpleDateFormat format = new SimpleDateFormat(FORMAT_YMD_ZERO);
	    return format.format(gc.getTime()); 
	}
	
	/**
	 * 根据秒得到日期
	 * @param str(秒 服务器直接传入)
	 * @return 2015-09-09 09:09:09
	 */
	public static String getDateByb(String str){
		BigDecimal db = new BigDecimal(Double.valueOf(str)+"");
		long times =Long.valueOf(db.toPlainString());
	    return getDateByb(times);
	}

	/**
	 *  根据秒得到日期(自定义类型)
	 * @param str
	 * @param dateType
	 * @return
	 */
	public static String getDateWithCustomType(String str,String dateType){//
		BigDecimal db = new BigDecimal(Double.valueOf(str)+"");
		long times =Long.valueOf(db.toPlainString());
//		return getDateByb(times);
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTimeInMillis(times);//* 1000
		SimpleDateFormat format = new SimpleDateFormat(dateType);
		return format.format(gc.getTime());
	}
	
	/**
	 * 获取两个日期的时间差(data到当前的时间)
	 * @param data(秒)
	 * @return 天数
	 *
//	 * 到期时间 的511点9分59秒市，自动加一
	 *
	 */
	public static int getTimeInterval(String data) {

		BigDecimal enddb = new BigDecimal(Double.valueOf(data)+"");
		data=DateTime.getDateBya(Long.valueOf(enddb.toPlainString()));
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		int interval = 0;
		try {
			Date currentTime = new Date();// 获取现在的时间
			Date beginTime = dateFormat.parse(data);

			interval = (int) (((beginTime.getTime()) - currentTime.getTime()) / (1000*60*60*24));// 时间差

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return interval;
	}
	
}

