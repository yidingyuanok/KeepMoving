package com.yuenan.shame.util;

import android.text.TextUtils;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间格式
 * Created by Liuhuacheng.
 * Created time 16/9/5.
 */

public class DateUtils {
    public static final String FORMAT_SEC = "yyyy.MM.dd HH:mm";
    public static final String FORMAT = "yyyy.MM.dd";
    public static final String FORMAT_DATE = "yyyy-MM-dd";
    public static final String FORMAT_TIME = "HH:mm";
    public static final String FORMAT_MONTH_TIME = "MM-dd HH:mm";

    /**
     * 获取时间格式
     */
    public static long getCurrTime() {
        return System.currentTimeMillis();
    }

    /**
     * 获取时间格式
     */
    public static String getCurrDate() {
        long time = System.currentTimeMillis();
        return getTime(time, FORMAT_DATE);
    }

    /**
     * 获取时间格式
     */
    public static String getTime(String time, String format) {
        String result = "";
        if (!TextUtils.isEmpty(time)) {
            long date = Long.valueOf(time);
            result = getTime(date, format);
        }
        return result;
    }

    /**
     * 获取时间格式
     */
    public static String getTime(long time, String format) {
        time = checkDate(time);
        String result = "";
        SimpleDateFormat df = new SimpleDateFormat(format);
        result = df.format(time);
        return result;
    }

    /**
     * 将短时间格式字符串转换为时间 yyyy-MM-dd
     *
     * @param format
     * @return
     */
    public static Date strToDate(String time, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(time, pos);
        return strtodate;
    }


    /**
     * 按时间HH:mm 获取时间戳
     */
    public static long getTime(String timeStr) {
        long time = getCurrTime();
        Date date = strToDate(getCurrDate() + " " + timeStr, FORMAT);
        time = date.getTime();
        return time;
    }

    /**
     * 转化时间输入时间与当前时间的间隔
     *
     * @param timestamp
     * @return
     */
    public static String converTime(long timestamp) {
        long currentSeconds = System.currentTimeMillis() / 1000;
        long timeGap = currentSeconds - timestamp;// 与现在时间相差秒数
        String timeStr = null;
        if (timeGap > 24 * 60 * 60) {// 1天以上
            timeStr = timeGap / (24 * 60 * 60) + "天前";
        } else if (timeGap > 60 * 60) {// 1小时-24小时
            timeStr = timeGap / (60 * 60) + "小时前";
        } else if (timeGap > 60) {// 1分钟-59分钟
            timeStr = timeGap / 60 + "分钟前";
        } else {// 1秒钟-59秒钟
            timeStr = "刚刚";
        }
        return timeStr;
    }

    //检验时间的长度
    public static long checkDate(long time) {
        String str = String.valueOf(time);
        if (str.length() == 10) {
            time = time * 1000;
        }
        if (str.length() == 11) {
            time = time * 100;
        }
        if (str.length() == 12) {
            time = time * 10;
        }
        return time;
    }


    public static String getWeek(int week) {
        switch (week) {
            case 1:
                return "星期日";
            case 2:
                return "星期一";
            case 3:
                return "星期二";
            case 4:
                return "星期三";
            case 5:
                return "星期四";
            case 6:
                return "星期五";

        }
        return "星期六";
    }

    public static String getLunarMonthWeek(int month) {
        String str = "";
        switch (month) {
            case 1:
                str = "一月";
                break;
            case 2:
                str = "二月";
                break;
            case 3:
                str = "三月";
                break;
            case 4:
                str = "四月";
                break;
            case 5:
                str = "五月";
                break;
            case 6:
                str = "六月";
                break;
            case 7:
                str = "七月";
                break;
            case 8:
                str = "八月";
                break;
            case 9:
                str = "九月";
                break;
            case 10:
                str = "十月";
                break;
            case 11:
                str = "十一月";
                break;
            case 12:
                str = "十二月";
                break;
            case 13:
                str = "十二月";

        }
        return str;
    }

}
