package com.stackstech.honeybee.core.util;

import org.apache.commons.lang3.StringUtils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 日期工具类
 */
public class DateUtils {
    //默认格式
    private static final String DEFAULT = "yyyy-MM-dd HH:mm:ss";
    private static final String DEFAULT_DATE = "yyyy-MM-dd";
    //默认格式化格式
    private static SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT);

    private DateUtils() {
    }

    /**
     * 得到指定格式的 SimpleDateFormat
     *
     * @param formatString 指定格式的format 如: yyyy-MM-dd
     * @return SimpleDateFormat sdf
     */
    private static SimpleDateFormat getSimpleDateFormat(String formatString) {
        sdf = new SimpleDateFormat(formatString);
        return sdf;
    }

    /**
     * 得到当前时间的字符串格式 如 2016-09-09 23:12:12
     *
     * @return 2016-09-09 23:12:12
     */
    public static String getDefaultCurrentStrDate() {
        return getCurrentStrDate(DEFAULT);
    }

    /**
     * 指定格式的日期字符串
     *
     * @param formatString yyyy-MM-dd
     * @return 指定格式的string
     */
    public static String getCurrentStrDate(String formatString) {
        SimpleDateFormat sdf = getSimpleDateFormat(formatString);
        return sdf.format(new Date());
    }

    /**
     * 由字符串日期转成 date日期
     *
     * @param dateString
     * @return Date日期
     */
    public static Date getCurrentUtilDate(String dateString) {
        try {
            return sdf.parse(dateString);
        } catch (ParseException e) {
            //TODO log this ?
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 得到默认字符串格式的日期对象
     *
     * @return 日期对象
     */
    public static Date getDefalutCurrentUtilDate() {
        return getCurrentUtilDate(getDefaultCurrentStrDate());
    }

    /**
     * 得到当前指定字符串格式的日期对象
     *
     * @param formatString
     * @return 日期对象
     */
    public static Date getDefaultCurrentUtilDate(String formatString) {
        return getCurrentUtilDate(getCurrentStrDate(formatString));
    }

    /**
     * 指定格式转timestamp对象
     *
     * @param formatString
     * @return
     */
    public static Timestamp getDefaultCurrentTimeStamp(String formatString) {

        return Timestamp.valueOf(formatString);
    }

    /**
     * 指定格式转timestamp对象
     *
     * @param date
     * @return
     */
    public static Timestamp getDefaultCurrentTimeStamp(Date date) {
        return new Timestamp(date.getTime());
    }

    /**
     * 将Date类型转化为String
     *
     * @param date
     * @return
     */
    public static String date2String(Date date) {
        return new SimpleDateFormat(DEFAULT_DATE).format(date);
    }

    /**
     * 将Long类型转化为String
     *
     * @param date
     * @return
     */
    public static String date2String(Long date) {
        return new SimpleDateFormat(DEFAULT_DATE).format(new Date(date));
    }

    /**
     * 获取当前时间
     *
     * @return
     */
    public static String date2String() {
        return new SimpleDateFormat(DEFAULT_DATE).format(new Date());
    }

    /**
     * 获取指定日期
     *
     * @param startTime
     * @param days
     * @return
     */
    public static String getSpacedDays(String startTime, int days) throws Exception {
        Calendar cal = Calendar.getInstance();
        if (StringUtils.isBlank(startTime)) {
            cal.add(Calendar.DATE, -days);
        } else {
            cal.setTime(new SimpleDateFormat(DEFAULT_DATE).parse(startTime));
            cal.add(Calendar.DATE, -days);
        }
        return date2String(cal.getTime());
    }

    /**
     * 获取指定时间<返回二元组>
     *
     * @param startTime
     * @param days
     * @return
     * @throws Exception
     */
    public static Map<String, Object> getTupleDays(String startTime, int days) throws Exception {
        Map<String, Object> map = new HashMap<>();

        Calendar cal = Calendar.getInstance();
        if (StringUtils.isBlank(startTime)) {
            cal.add(Calendar.DATE, -days);
            map.put("startTime", date2String(cal.getTime()));
            if (days == 0) {
                cal.add(Calendar.DATE, +1);
                map.put("endTime", date2String(cal.getTime()));
            } else {
                map.put("endTime", date2String(new Date()));
            }
        } else {
            cal.setTime(new SimpleDateFormat(DEFAULT_DATE).parse(startTime));
            cal.add(Calendar.DATE, -days);
            map.put("startTime", date2String(cal.getTime()));
            if (days == 0) {
                cal.add(Calendar.DATE, +1);
                map.put("endTime", date2String(cal.getTime()));
            } else {
                map.put("endTime", startTime);
            }
        }
        return map;
    }

    /**
     * 字符串计算两月相差天数
     *
     * @throws ParseException
     * @result List<String> list时间
     */
    public static Integer daysBetween(String smdate, String bdate) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar cal = Calendar.getInstance();
            cal.setTime(sdf.parse(smdate));
            long time1 = cal.getTimeInMillis();
            cal.setTime(sdf.parse(bdate));
            long time2 = cal.getTimeInMillis();
            long between_days = (time2 - time1) / (1000 * 3600 * 24);

            return Integer.parseInt(String.valueOf(between_days)); //获得的天数
        } catch (Exception e) {
            //TODO log this ?
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 字符串计算两月相差天数
     *
     * @throws ParseException
     * @result List<String> list时间
     */
    public static List<String> daysList(String smdate, String bdate) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar cal = Calendar.getInstance();
            cal.setTime(sdf.parse(smdate));
            long time1 = cal.getTimeInMillis();
            cal.setTime(sdf.parse(bdate));
            long time2 = cal.getTimeInMillis();
            long between_days = (time2 - time1) / (1000 * 3600 * 24);

            List<String> listdate = new ArrayList<String>();
            Date date = null;
            date = sdf.parse(smdate);//开始时间
            listdate.add(smdate);
            for (; ; ) {
                if (date.getTime() >= sdf.parse(bdate).getTime()) {
                    break;
                }
                Calendar calendar = new GregorianCalendar();
                calendar.setTime(date);
                calendar.add(Calendar.DATE, 1);                        //把日期往后增加一天.整数往后推,负数往前移动
                date = calendar.getTime();                            //这个时间就是日期往后推一天的结果
                listdate.add(sdf.format(date));
            }
            return listdate; //获得的天数
        } catch (Exception e) {
            //TODO log this ?
            e.printStackTrace();
        }
        return null;
    }

}
