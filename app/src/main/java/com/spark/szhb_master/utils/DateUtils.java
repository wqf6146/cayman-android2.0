package com.spark.szhb_master.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Administrator on 2017/9/1.
 * 日期格式化工具类
 */

public class DateUtils {
    /**
     * 将时间戳转化成固定格式（默认 yyyy-MM-dd HH:mm:ss 当前时间 ）
     */
    public static String getFormatTime(String format, Date date) {
        if (StringUtils.isEmpty(format)) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        if (date == null) {
            date = new Date();
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String formatTime = sdf.format(date);
        return formatTime;
    }

    /**
     * 将固定格式转化成时间戳（默认 yyyy-MM-dd HH:mm:ss）
     */
    public static long getTimeMillis(String format, String dateString) {
        if (StringUtils.isEmpty(format)) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            Date date = sdf.parse(dateString);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 将时间戳转date
     */
    public static Date getDate(String pattern, Long dateString) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        String d = format.format(dateString);
        Date date = null;
        try {
            date = format.parse(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * string转date
     *
     * @param strTime
     * @param formatType
     * @return
     */
    public static Date getDateTransformString(String strTime, String formatType) {
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        Date date = null;
        try {
            date = formatter.parse(strTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    private static ThreadLocal<SimpleDateFormat> DateLocal = new ThreadLocal<SimpleDateFormat>();

    /**
     * 判断是否为今天(效率比较高)
     *
     * @param day 传入的 时间  "2016-06-28 10:10:30" "2016-06-28" 都可以
     * @return true今天 false不是
     * @throws ParseException
     */
    public static boolean IsToday(String day) throws ParseException {
        if (StringUtils.isEmpty(day))
            return false;
        Calendar pre = Calendar.getInstance();
        Date predate = new Date(System.currentTimeMillis());
        pre.setTime(predate);

        Calendar cal = Calendar.getInstance();
        Date date = getDateFormat().parse(day);
        cal.setTime(date);

        if (cal.get(Calendar.YEAR) == (pre.get(Calendar.YEAR))) {
            int diffDay = cal.get(Calendar.DAY_OF_YEAR)
                    - pre.get(Calendar.DAY_OF_YEAR);

            if (diffDay == 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否为昨天(效率比较高)
     *
     * @param day 传入的 时间  "2016-06-28 10:10:30" "2016-06-28" 都可以
     * @return true今天 false不是
     * @throws ParseException
     */
    public static boolean IsYesterday(String day) throws ParseException {

        Calendar pre = Calendar.getInstance();
        Date predate = new Date(System.currentTimeMillis());
        pre.setTime(predate);

        Calendar cal = Calendar.getInstance();
        Date date = getDateFormat().parse(day);
        cal.setTime(date);

        if (cal.get(Calendar.YEAR) == (pre.get(Calendar.YEAR))) {
            int diffDay = cal.get(Calendar.DAY_OF_YEAR)
                    - pre.get(Calendar.DAY_OF_YEAR);

            if (diffDay == -1) {
                return true;
            }
        }
        return false;
    }

    public static SimpleDateFormat getDateFormat() {
        if (null == DateLocal.get()) {
            DateLocal.set(new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA));
        }
        return DateLocal.get();
    }

    /**
     * 将毫秒转化成固定格式的时间
     * 时间格式: yyyy-MM-dd HH:mm:ss
     *
     * @param millisecond
     * @return
     */
    public static String getDateTimeFromMillisecond(Long millisecond) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(millisecond);
        String dateStr = simpleDateFormat.format(date);
        return dateStr;
    }

    //获取当前时间
    public static Long getSimpleDateFormat() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());
        String format = df.format(curDate);
        StringBuffer buffer = new StringBuffer();
        String substring = format.substring(0, 11);
        long s2 =  DateUtils.getTimeMillis(null,format);
        return s2;
    }


    //获取当前时间
    public static Long getSimpleDate(String data) {
        SimpleDateFormat df = new SimpleDateFormat(data);
        Date curDate = new Date(System.currentTimeMillis());
        String format = df.format(curDate);
        long s2 =  DateUtils.getTimeMillis(null,format);
        return s2;
    }

    //将毫秒转为时间
    public static String getSFM(long time) {
        long hours = (time ) / (1000 * 60 * 60);
//            long minute = (defferenttime - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)) / (1000 * 60);
        long minute = (time  - hours * (1000 * 60 * 60)) / (1000 * 60);
        long seconds = time % 60000;
        long second = Math.round((float) seconds / 1000);
        String sfm = hours+":"+minute +":"+ second;
        return sfm;
    }

    //将毫秒转为时间
    public static String getSFMtime(long time) {
        long minute = (time) / (1000 * 60);
        long seconds = time % 60000;
        long second = Math.round((float) seconds / 1000);
        String sfm = minute +":"+ second;
        return sfm;
    }


}
