package com.test.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public final class DateUtils {
    public static final String DATE_TIME_FORAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORAT = "yyyy-MM-dd";
    public static final String TIME_FORMAT = "HH:mm:ss";

    public static final String SHORT_DATE_TIME_FORAT2 = "yyyyMMdd HH:mm:ss";
    public static final String SHORT_DATE_FORAT = "yyyyMMdd";

    public static final String MONTH_FORAT = "MM月dd日 HH:mm";
    public static final String HOUR_FORAT = "HH:mm";
    public static final String DEFAULT_BEGIN = " 00:00:00";
    public static final String DEFAULT_END = " 23:59:59";

    public static final String FORMAT14 = "yyyyMMddHHmmss";

    private DateUtils() {

    }

    public static String getDateStart(Date date) {
        return dateToString(date) + DEFAULT_BEGIN;
    }

    public static String getDateEnd(Date date) {
        return dateToString(date) + DEFAULT_END;
    }

    public static String formatDate(Date date, String sdf) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(sdf);
            return simpleDateFormat.format(date);
        } catch (Exception e) {
            return "";
        }

    }

    public static Date parseDate(String dateStr) {
        Date date = null;
        if (StringUtils.isNotBlank(dateStr) && dateStr.length() == 8) {
            date = DateUtils.parseDate(dateStr + "235959", "yyyyMMddHHmmss");
        } else if (StringUtils.isNotBlank(dateStr) && dateStr.length() == 10) {
            date = DateUtils.parseDate(dateStr + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
        } else if (StringUtils.isNotBlank(dateStr) && dateStr.length() == 14) {
            date = DateUtils.parseDate(dateStr, "yyyyMMddHHmmss");
        } else if (StringUtils.isNotBlank(dateStr) && dateStr.length() == 19) {
            date = DateUtils.parseDate(dateStr, "yyyy-MM-dd HH:mm:ss");
        }
        return date;
    }

    public static Date parseStartDate(String dateStr) {
        Date date = null;
        if (StringUtils.isNotBlank(dateStr) && dateStr.length() == 8) {
            date = DateUtils.parseDate(dateStr + "000000", "yyyyMMddHHmmss");
        } else if (StringUtils.isNotBlank(dateStr) && dateStr.length() == 10) {
            date = DateUtils.parseDate(dateStr + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
        }
        return date;
    }

    public static Date parseDate(String dateStr, String sdf) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(sdf);
        try {
            return simpleDateFormat.parse(dateStr);
        } catch (ParseException e) {
            throw new RuntimeException("Parse date failed", e);
        }
    }

    public static long convertDateToUnix(Date date) {
        long result = -1;
        if (date == null) {
            return result;
        }
        result = date.getTime() / 1000;
        return result;
    }


    /**
     * 获得今天日期
     *
     * @return String
     */
    public static String getToday(String formart) {
        SimpleDateFormat sdf = new SimpleDateFormat(formart);
        return sdf.format(new Date());
    }

    /**
     * 获得N天日期     *
     *
     * @return String
     */
    public static String getBeforeDayShort(int days) {
        SimpleDateFormat sdf = new SimpleDateFormat(SHORT_DATE_FORAT);
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, days);
        return sdf.format(c.getTime());
    }

    /**
     * 获得N天日期     *
     *
     * @return String
     */
    public static String getBeforeDay(int days) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORAT);
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, days);
        return sdf.format(c.getTime());
    }

    public static Date getBeforeDayDate(int days) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, days);
        return c.getTime();
    }

    /**
     * @param strDate 待转化的日期字符串
     * @return 日期对象, 如果字符串格式非法，则返回null
     */
    public static Date toDate2(String strDate) {
        if (StringUtils.isEmpty(strDate)) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(SHORT_DATE_TIME_FORAT2);
        try {
            return sdf.parse(strDate);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param strDate 待转化的日期字符串
     * @return 日期对象, 如果字符串格式非法，则返回null
     */
    public static Date toDateTime(String strDate) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_FORAT);
        try {
            return sdf.parse(strDate);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 日期转换为字符串 短日期
     *
     * @param date 参数
     * @return String
     */
    public static String dateToString(Date date) {
        SimpleDateFormat dateFormat2 = new SimpleDateFormat(DATE_FORAT);
        if (date != null) {
            return dateFormat2.format(date);
        } else {
            return null;
        }
    }

    public static String dateToString(Date date, String format) {
        SimpleDateFormat dateFormat2 = new SimpleDateFormat(format);
        if (date != null) {
            return dateFormat2.format(date);
        } else {
            return null;
        }
    }


    /**
     * 日期转换为字符串 短日期
     *
     * @param date 参数
     * @return String
     */
    public static String dateToMinuteString(String date) {
        SimpleDateFormat dateFormat2 = new SimpleDateFormat(MONTH_FORAT);
        if (StringUtils.isNotBlank(date)) {
            return dateFormat2.format(toDateTime(date));
        } else {
            return "";
        }
    }

    public static String dateToHourString(String date) {
        SimpleDateFormat dateFormat2 = new SimpleDateFormat(HOUR_FORAT);
        if (StringUtils.isNotBlank(date)) {
            return dateFormat2.format(toDateTime(date));
        } else {
            return "";
        }
    }

    /**
     * 日期转换为字符串 长日期
     *
     * @param date 参数
     * @return String
     */
    public static String dateTimeToString(Date date) {
        SimpleDateFormat timeFormat2 = new SimpleDateFormat(DATE_TIME_FORAT);
        if (date != null) {
            return timeFormat2.format(date);
        } else {
            return null;
        }
    }


    public static String unixTimeStampToDateTimeString(String timeStampString) {
        Long timestamp = Long.parseLong(timeStampString) * 1000;
        String date = new SimpleDateFormat(DATE_TIME_FORAT).format(new Date(timestamp));
        return date;
    }

    public static List<String> getDayWeek() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date currentDate = new Date();
        List<Date> days = new ArrayList<>();
        days.add(currentDate);
        days.add(getBeforeDayDate(-1));
        days.add(getBeforeDayDate(-2));
        days.add(getBeforeDayDate(-3));
        days.add(getBeforeDayDate(-4));
        days.add(getBeforeDayDate(-5));
        days.add(getBeforeDayDate(-6));
        List<String> result = new ArrayList<>();
        for (Date date : days) {
            result.add(sdf.format(date));
        }
        Collections.reverse(result);
        return result;
    }

    /**
     * 获取当月月末
     *
     * @return 下月一号
     */
    public static Date nextMonthFirstDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar.getTime();
    }

    public static Date addMonth(Date date, int offset) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MONTH, offset);
        return c.getTime();
    }

    public static Date addDay(Date date, int offset) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DAY_OF_MONTH, offset);
        return c.getTime();
    }

    public static Date addHour(Date date, int offset) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.HOUR, offset);
        return c.getTime();
    }

    public static Date localDateTime2Date(LocalDateTime localDateTime) {
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt = localDateTime.atZone(zoneId);
        Date date = Date.from(zdt.toInstant());
        return date;
    }

    /**
     * 判断当前日期是星期几(1:星期一,2:星期二,3:星期三,4:星期四,5:星期五,6:星期五，7:星期天)<br>
     */
    public static int dayForWeek() {
        Calendar c = Calendar.getInstance();
        int dayForWeek = 0;
        if (c.get(Calendar.DAY_OF_WEEK) == 1) {
            dayForWeek = 7;
        } else {
            dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
        }
        return dayForWeek;
    }

    public static int dayForMonth() {
        LocalDateTime now = LocalDateTime.now();
        return now.getDayOfMonth();
    }

    /**
     * 判断当前日期是星期几(1:星期一,2:星期二,3:星期三,4:星期四,5:星期五,6:星期五，7:星期天)<br>
     */
    public static int dayForWeek(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int dayForWeek = 0;
        if (c.get(Calendar.DAY_OF_WEEK) == 1) {
            dayForWeek = 7;
        } else {
            dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
        }
        return dayForWeek;
    }

    /**
     * 取得当月剩余天数
     */
    public static int getCurrentMonthRemainDay() {
        return getCurrentMonthLastDay() - getCurrentdayForMonth() + 1;
    }

    /**
     * 取得当月天数
     */
    public static int getCurrentMonthLastDay() {
        Calendar a = Calendar.getInstance();
        a.set(Calendar.DATE, 1);//把日期设置为当月第一天
        a.roll(Calendar.DATE, -1);//日期回滚一天，也就是最后一天
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }

    /**
     * 取得当年天数
     */
    public static int getDaysOfYear() {
        Calendar c = Calendar.getInstance();
        int days = c.getActualMaximum(Calendar.DAY_OF_YEAR);
        return days;
    }

    /**
     * 获取当天在本地是第多少天
     */
    public static int getCurrentdayForMonth() {
        Calendar a = Calendar.getInstance();
        int day = a.get(Calendar.DAY_OF_MONTH);
        return day;
    }

    /**
     * 获取两个时间之间的String类型日期List列表
     *
     * @param dBegin
     * @param dEnd
     * @param format
     * @return
     */
    public static List<String> getBetweenListStringDates(Date dBegin, Date dEnd, String format) {
        List<String> lDate = new ArrayList<String>();
        SimpleDateFormat sd = null;
        try {
            sd = new SimpleDateFormat(format);
        } catch (Exception e) {
            log.error("Method:getBetweenListStringDates error:{},format:{}", "时间格式化异常", format, e);
        }
        lDate.add(sd.format(dBegin));
        Calendar calBegin = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        calBegin.setTime(dBegin);
        Calendar calEnd = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        calEnd.setTime(dEnd);
        // 测试此日期是否在指定日期之后
        while (dEnd.after(calBegin.getTime())) {
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
            calBegin.add(Calendar.DAY_OF_MONTH, 1);
            lDate.add(sd.format(calBegin.getTime()));
        }
        return lDate;
    }

    /**
     * @param nowDate   要比较的时间
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return true在时间段内，false不在时间段内
     * @throws Exception
     */
    public static boolean isEffectiveDate(String nowDate, String startDate, String endDate) throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date now = format.parse(nowDate);
        Date start = format.parse(startDate);
        Date end = format.parse(endDate);
        long nowTime = now.getTime();
        long startTime = start.getTime();
        long endTime = end.getTime();
        return nowTime >= startTime && nowTime <= endTime;
    }

}
