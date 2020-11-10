package com.test.util;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import org.apache.commons.lang3.StringUtils;

/**
 * LocalDateTime 转换工具
 *
 * @author LinBin
 * @create 2018-10-27 15:08
 **/

public class DateTimeUtils {

    public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HHmmss");
    public static final DateTimeFormatter MONTH_FORMATTER = DateTimeFormatter.ofPattern("yyyyMM");
    public static final DateTimeFormatter SHORT_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyMMdd");
    public static final DateTimeFormatter SHORT_DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyMMddHHmmss");
    public static final DateTimeFormatter DATETIME_FORMATTER =  DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");
    public static final DateTimeFormatter FULL_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
    public static final DateTimeFormatter YYYY_MM_DD_HH_MM_SS = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final DateTimeFormatter YYYY_MM_DD= DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final DateTimeFormatter HH_MM_SS = DateTimeFormatter.ofPattern("HH:mm:ss");
    

    
    /**
     * LocalDateTime转换为Date
     * @param localDateTime
     */
    public static Date localDateTime2Date( LocalDateTime localDateTime){
    	if(localDateTime==null){
    		return null;
    	}
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt = localDateTime.atZone(zoneId);//Combines this date-time with a time-zone to create a  ZonedDateTime.
        Date date = Date.from(zdt.toInstant());
        return date;
      }

    
    
    public static LocalDateTime parseDate(String dateStr) {
    	LocalDateTime date = null;
        if (StringUtils.isNotBlank(dateStr) && dateStr.length() == 8) {
            date =  parseLocalDateTime(dateStr + "235959", "yyyyMMddHHmmss");
        } else if (StringUtils.isNotBlank(dateStr) && dateStr.length() == 10) {
            date = parseLocalDateTime(dateStr + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
        } else if (StringUtils.isNotBlank(dateStr) && dateStr.length() == 14) {
            date = parseLocalDateTime(dateStr, "yyyyMMddHHmmss");
        } else if (StringUtils.isNotBlank(dateStr) && dateStr.length() == 19) {
            date = parseLocalDateTime(dateStr, "yyyy-MM-dd HH:mm:ss");
        }
        return date;
    }


    /**
     * 返回当前的日期
     * @return
     */
    public static LocalDate getCurrentLocalDate() {
        return LocalDate.now();
    }

  //Date转换为LocalDateTime
    public static LocalDateTime convert(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }
    
  //获取一天的开始时间，2017,7,22 00:00
    public static LocalDateTime getDayStart(LocalDateTime time) {
        return time.withHour(0)
                .withMinute(0)
                .withSecond(0)
                .withNano(0);
    }
    
    /**
     * 获取一年的开始时间
     * @param time
     * @return
     */
    public static LocalDateTime getYearStart(LocalDateTime time) {
    	return time.withMonth(1)
    			.withDayOfMonth(1)
    			.withHour(0)
    			.withMinute(0)
    			.withSecond(0)
    			.withNano(0);
    }
    
    public static LocalDateTime getYearEnd(LocalDateTime time) {
    	return time.withMonth(12)
    			.withDayOfMonth(31)
    			.withHour(23)
    			.withMinute(59)
    			.withSecond(59).withNano(0);
    }
    
    public static LocalDateTime getHourEnd(LocalDateTime time){
    	return time.withMinute(59).withSecond(59).withNano(0);
    }
    
    /**
     * 获取一小时的开始时间
     * @param time
     * @return
     */
    public static LocalDateTime getHourStart(LocalDateTime time){
    	return time.withMinute(0)
    			.withSecond(0)
    			.withNano(0);
    }

    //获取一天的结束时间，2017,7,22 23:59:59
    public static LocalDateTime getDayEnd(LocalDateTime time) {
        return time.withHour(23)
                .withMinute(59)
                .withSecond(59)
                .withNano(0);
    }
    

    
    
    
    //获取一天的开始时间，2017,7,22 00:00
    public static LocalDateTime getMonthStart(LocalDateTime time) {
        return time.withDayOfMonth(1)
        		.withHour(0)
                .withMinute(0)
                .withSecond(0)
                .withNano(00);
    }
    
    //根据时间获取该月的 自然月月底时间
    public static LocalDateTime getNaturalMonthEnd(LocalDateTime time){
    	 return time.withDayOfMonth(1)
         		.withHour(0)
                 .withMinute(0)
                 .withSecond(0)
                 .withNano(00).minusSeconds(1);
    }

    //获取一天的结束时间，2017,7,22 23:59:59.999999999
    public static LocalDateTime getMonthEnd(LocalDateTime time) {
        return getDayEnd(time.with(TemporalAdjusters.lastDayOfMonth()));
    }
    
    
    public static LocalDateTime currentMonthStart() {
    	return  getMonthStart(getCurrentLocalDateTime());
    }
    public static LocalDateTime currentMonthEnd() {
        return getMonthEnd(getCurrentLocalDateTime());
    }

    
    /**
     * 返回当前时间
     * @return
     */
    public static LocalTime getCurrentLocalTime() {
        return LocalTime.now();
    }

    /**
     * 返回当前日期时间
     * @return
     */
    public static LocalDateTime getCurrentLocalDateTime() {
        return LocalDateTime.now();
    }
    
    

    /**
     * yyyy_MM_dd
     *
     * @return
     */
    public static String getCurrentDateStr() {
        return LocalDate.now().format(YYYY_MM_DD);
    }
    
    public static String getCurrentDateStr8() {
    	return LocalDate.now().format(DATE_FORMATTER);
    }

    /**
     * yyMMdd
     *
     * @return
     */
    public static String getCurrentShortDateStr() {
        return LocalDate.now().format(SHORT_DATE_FORMATTER);
    }

    public static String getCurrentMonthStr() {
        return LocalDate.now().format(MONTH_FORMATTER);
    }

    /**
     * yyyyMMddHHmmss
     * @return
     */
    public static String getCurrentDateTimeStr() {
        return LocalDateTime.now().format(DATETIME_FORMATTER);
    }

    /**
     * yyMMddHHmmss
     * @return
     */
    public static String getCurrentShortDateTimeStr() {
        return LocalDateTime.now().format(SHORT_DATETIME_FORMATTER);
    }

    public static String getCurrentDateTimeStr19() {
        return LocalDateTime.now().format(YYYY_MM_DD_HH_MM_SS);
    }

    /**
     * HHmmss
     * @return
     */
    public static String getCurrentTimeStr() {
        return LocalTime.now().format(TIME_FORMATTER);
    }

    public static String getCurrentDateStr(String pattern) {
        return LocalDate.now().format(DateTimeFormatter.ofPattern(pattern));
    }

    public static String getCurrentDateTimeStr(String pattern) {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(pattern));
    }

    public static String getCurrentTimeStr(String pattern) {
        return LocalTime.now().format(DateTimeFormatter.ofPattern(pattern));
    }

    public static LocalDate parseLocalDate(String dateStr, String pattern) {
        return LocalDate.parse(dateStr, DateTimeFormatter.ofPattern(pattern));
    }

    public static LocalDateTime parseLocalDateTime(String dateTimeStr, String pattern) {
        return LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ofPattern(pattern));
    }

    public static LocalTime parseLocalTime(String timeStr, String pattern) {
        return LocalTime.parse(timeStr, DateTimeFormatter.ofPattern(pattern));
    }

    public static String formatLocalDate(LocalDate date, String pattern) {
        return date.format(DateTimeFormatter.ofPattern(pattern));
    }

    public static String formatLocalDateTime(LocalDateTime datetime, String pattern) {
        return datetime.format(DateTimeFormatter.ofPattern(pattern));
    }

    public static String formatLocalTime(LocalTime time, String pattern) {
        return time.format(DateTimeFormatter.ofPattern(pattern));
    }

    public static LocalDate parseLocalDate(String dateStr) {
        return LocalDate.parse(dateStr, YYYY_MM_DD);
    }

    public static LocalDateTime parseLocalDateTime(String dateTimeStr) {
        return LocalDateTime.parse(dateTimeStr, DATETIME_FORMATTER);
    }

    public static LocalTime parseLocalTime(String timeStr) {
        return LocalTime.parse(timeStr, TIME_FORMATTER);
    }

    public static String formatLocalDate(LocalDate date) {
        return date.format(YYYY_MM_DD);
    }

    public static String formatLocalDateTime(LocalDateTime datetime) {
    	if(datetime==null){
    		return null;
    	}
        return datetime.format(YYYY_MM_DD_HH_MM_SS);
    }

    public static String formatLocalTime(LocalTime time) {
        return time.format(TIME_FORMATTER);
    }
    
    /**
     * 日期相隔月
     * notice:只计算当年月份差，慎用
     * 只能计算同月的天数、同年的月数，不能计算隔月的天数以及隔年的月数
     * @param startDateInclusive
     * @param endDateExclusive
     * @return
     */
    public static int periodMonths(LocalDateTime startDateInclusive, LocalDateTime endDateExclusive) {
        return Period.between(startDateInclusive.toLocalDate(), endDateExclusive.toLocalDate()).getMonths();
    }

    public static int periodNatureMonths(LocalDateTime startDateInclusive, LocalDateTime endDateExclusive) {
        int year = endDateExclusive.toLocalDate().getYear()-startDateInclusive.toLocalDate().getYear();
        int i = endDateExclusive.toLocalDate().getMonthValue()-startDateInclusive.toLocalDate().getMonthValue();
        return year*12+i;
    }

    /**
     * 日期相隔天数
     * notice:只计算当月日期差，慎用
     * 只能计算同月的天数、同年的月数，不能计算隔月的天数以及隔年的月数
     * @param startDateInclusive
     * @param endDateExclusive
     * @return
     */
    public static int periodDays(LocalDate startDateInclusive, LocalDate endDateExclusive) {
        return Period.between(startDateInclusive, endDateExclusive).getDays();
    }
    //日期相隔天数
    public static int periodDays(LocalDateTime startDateInclusive, LocalDateTime endDateExclusive) {
        return (int)(endDateExclusive.toLocalDate().toEpochDay() - startDateInclusive.toLocalDate().toEpochDay());
    }

    /**
     * 日期相隔小时
     * @param startInclusive
     * @param endExclusive
     * @return
     */
    public static long durationHours(Temporal startInclusive, Temporal endExclusive) {
        return Duration.between(startInclusive, endExclusive).toHours();
    }

    /**
     * 日期相隔分钟
     * @param startInclusive
     * @param endExclusive
     * @return
     */
    public static long durationMinutes(Temporal startInclusive, Temporal endExclusive) {
        return Duration.between(startInclusive, endExclusive).toMinutes();
    }

    /**
     * 日期相隔毫秒数
     * @param startInclusive
     * @param endExclusive
     * @return
     */
    public static long durationMillis(Temporal startInclusive, Temporal endExclusive) {
        return Duration.between(startInclusive, endExclusive).toMillis();
    }

    /**
     * 是否当天
     * @param date
     * @return
     */
    public static boolean isToday(LocalDate date) {
        return getCurrentLocalDate().equals(date);
    }

    public static Long toEpochMilli(LocalDateTime dateTime) {
        return dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }
    
    public static boolean isValid(LocalDateTime startTime,LocalDateTime endTime,boolean canNull){
    	LocalDateTime now = LocalDateTime.now();
    	if(!canNull){
    		return startTime!=null&&endTime!=null&&now.isAfter(startTime)&&now.isBefore(endTime);
    	}
    	if(startTime!=null&&endTime!=null){
    		return now.isAfter(startTime)&&now.isBefore(endTime);
    	}else if(startTime!=null){
    		return now.isAfter(startTime);
    	}else if(endTime!=null){
    		return now.isBefore(endTime);
    	}
    	return true;
    }
    
    public static boolean isValid(LocalDateTime startTime,LocalDateTime endTime){
    	return isValid(startTime, endTime, true);
    }
    
    /**
     * 根据入参时间获取到第二天中午十二点的时间
     * @param startTime
     * @return
     */
    public static LocalDateTime getNextDayMiddleTime(LocalDateTime startTime){
//    	startTime = startTime.plusDays(1);
    	String nextDay = formatLocalDateTime(startTime,DateUtils.SHORT_DATE_FORAT);
    	nextDay = nextDay + "120000";
    	return parseLocalDateTime(nextDay, DateUtils.FORMAT14);
    }


	/**
	 * 计算日期相隔月份
	 * @param startDateInclusive    开始日期
	 * @param endDateExclusive  结束日期
	 * @return
	 */
	public static long calculateMonthsInterval(LocalDateTime startDateInclusive, LocalDateTime endDateExclusive) {
		return ChronoUnit.MONTHS.between(startDateInclusive.toLocalDate(), endDateExclusive.toLocalDate());
	}

	/**
	 * 计算日期相隔月份
	 * @param startDateInclusive    开始日期
	 * @param endDateExclusive  结束日期
	 * @return
	 */
	public static int calculateMonthsInterval(LocalDate startDateInclusive, LocalDate endDateExclusive) {
		return Long.valueOf(ChronoUnit.MONTHS.between(startDateInclusive, endDateExclusive)).intValue();
	}
    
}
