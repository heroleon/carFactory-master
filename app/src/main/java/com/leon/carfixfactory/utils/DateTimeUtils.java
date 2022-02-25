package com.leon.carfixfactory.utils;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * org.joda.time.DateTime
 * 联合DateTime类时间操作工具
 */
public class DateTimeUtils {

    /**
     * 文件名日期格式：yyyy-MM-dd HH:mm:ss
     **/
    public static final String YYYY_MM_DD_HH_MM_SS_FILE = "yyyy-MM-dd-HH-mm-ss";
    /**
     * 日期格式：yyyy-MM-dd HH:mm:ss
     **/
    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    /**
     * 日期格式：yyyy-MM-dd HH:mm
     **/
    public static final String YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";

    /**
     * 日期格式：yyyy-MM-dd HH:mm
     **/
    public static final String MM_DD_HH_MM = "MM-dd HH:mm";

    /**
     * 日期格式：yyyy-MM-dd
     **/
    public static final String YYYY_MM_DD = "yyyy-MM-dd";

    /**
     * 日期格式：yyyyMMdd
     */
    public static final String yyyyMMdd = "yyyyMMdd";

    /**
     * 日期格式：yyyy-MM
     **/
    public static final String YYYY_MM = "yyyy-MM";

    /**
     * 日期格式：MM-dd
     **/
    public static final String MM_DD = "MM-dd";

    /**
     * 日期格式：HH:mm:ss
     **/
    public static final String HH_MM_SS = "HH:mm:ss";

    /**
     * 日期格式：HH:mm
     **/
    public static final String HH_MM = "HH:mm";

    private final static long minute = 60 * 1000;// 1分钟
    private final static long hour = 60 * minute;// 1小时
    private final static long day = 24 * hour;// 1天
    private final static long month = 31 * day;// 月
    private final static long year = 12 * month;// 年

    /**
     * date 转 String
     *
     * @param date
     * @param type：2：年月 3：年月日
     * @return
     */
    public static String dateToString(int[] date, int type) {
        String dateString;
        if (date[1] < 10) {
            dateString = date[0] + "-0" + date[1];
        } else {
            dateString = date[0] + "-" + date[1];
        }
        if (type == 2) {
            return dateString;
        }
        if (date[2] < 10) {
            dateString = dateString + "-0" + date[2];
        } else {
            dateString = dateString + "-" + date[2];
        }
        return dateString;
    }

    /**
     * @param dayOfWeek 根据 DateTime.now().getDayOfWeek() 获取int类型星期几
     * @return 返回china字符串格式的星期
     */
    public static String getDayOfWeekString(int dayOfWeek) {
        StringBuilder sb = new StringBuilder();
        sb.append("星期");
        switch (dayOfWeek) {
            case 1:
                sb.append("一");
                break;
            case 2:
                sb.append("二");
                break;
            case 3:
                sb.append("三");
                break;
            case 4:
                sb.append("四");
                break;
            case 5:
                sb.append("五");
                break;
            case 6:
                sb.append("六");
                break;
            case 7:
                sb.append("日");
                break;
        }
        return sb.toString();
    }

    @SuppressLint("SimpleDateFormat")
    public static String millisecondToDate(long createDate, String pattern) {
        Date date = new Date(createDate);
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(date);
    }
}


