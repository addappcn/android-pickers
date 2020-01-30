package cn.addapp.pickers.util;

import android.text.format.DateFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.SimpleTimeZone;

public class DateDeal {
    private static final ThreadLocal<SimpleDateFormat> SDF_THREAD_LOCAL = new ThreadLocal<>();
    /**
     * HH 24
     * hh 12
     */
    public static String[] weekArray = new String[]{"一", "二", "三", "四", "五", "六", "日"};
    /**
     * 默认时间格式 24
     */
    public static String MM_dd = "MM-dd";
    public static String YYYY_MM = "yyyy-MM";
    public static String YYYY_MM_dd = "yyyy-MM-dd";
    public static String MM_dd_HH_MM = "MM-dd HH:mm";
    public static String MM_dd__HH_MM = "MM-dd  HH:mm";//中间多一个空格
    public static String MM_dd_HH_MM_SS = "MM-dd HH:mm:ss";
    public static String YYYY_MM_dd_HH_MM = "yyyy-MM-dd HH:mm";
    public static String YYYY_MM_dd_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public static String MM_dd_EEEE_HH_MM = "MM月dd日 EEEE HH:mm";
    public static String HH_MM = "HH:mm"; //0-23
    public static String HH_MM_SS = "HH:mm:ss";
    public static String EEEE_HH_mm = "EEEE HH:mm";

    /**
     * 英文时间格式 12小时
     */
    public static String MM_dd_EN = "MM/dd";
    public static String YYYY_MM_EN = "yyyy/MM";
    public static String YYYY_MM_dd_EN = "yyyy/MM/dd";
    public static String MM_dd_HH_MM_EN = "MM/dd hh:mm";
    public static String MM_dd_HH_MM_SS_EN = "MM/dd hh:mm:ss";
    public static String YYYY_MM_dd_HH_MM_EN = "yyyy/MM/dd hh:mm";

    /**
     * 中文时间格式
     */
    public static String MM_CN = "MM月";
    public static String MM_dd_CN = "MM月dd日";
    public static String YYYY_MM_CN = "yyyy年MM月";
    public static String YYYY_MM_dd_CN = "yyyy年MM月dd日";
    public static String MM_dd_HH_MM_CN = "MM月dd日 HH:mm";
    public static String MM_dd_HH_MM_SS_CN = "MM月dd日 HH:mm:ss";
    public static String YYYY_MM_dd_HH_MM_CN = "yyyy年MM月dd日 HH:mm";
    public static String YYYY_MM_dd_HH_MM_SS_CN = "yyyy年MM月dd日 hh:mm:ss";
    public static String MM_dd_EEEE_CN = "MM月dd日 EEEE";

    public static SimpleDateFormat getDateFormat(String pattern) {
        SimpleDateFormat simpleDateFormat = SDF_THREAD_LOCAL.get();
        if (simpleDateFormat == null) {
            simpleDateFormat = new SimpleDateFormat(pattern, Locale.getDefault());
            SDF_THREAD_LOCAL.set(simpleDateFormat);
        } else {
            simpleDateFormat.applyPattern(pattern);
        }
        return simpleDateFormat;
    }

    public static String formatDate(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    public static String formatDate(long date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    public static Date formatDate(String strDate, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = new Date();
        try {
            date = sdf.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static long getTimeMill(String time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhhmmss");
        Date date = new Date();
        try {
            date = format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }

    public static long getTimeMillFrom(String time) {
        SimpleDateFormat format = new SimpleDateFormat(YYYY_MM_dd_HH_MM_CN);
        Date date = new Date();
        try {
            date = format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }
    //  获取某天，-1是昨天，0是今天，1是明天依次类推
    public static String getDay(int i) {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat(YYYY_MM_dd);
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        String dateString="";
        try {
            calendar.add(Calendar.DATE,i);//把日期往后增加一天.整数往后推,负数往前移动
            date=calendar.getTime(); //这个时间就是日期往后推一天的结果
            dateString = formatter.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dateString;

    }
    public static long getStartTime() {
        Calendar todayStart = Calendar.getInstance();
        todayStart.set(Calendar.HOUR_OF_DAY, 0);
        todayStart.set(Calendar.MINUTE, 0);
        todayStart.set(Calendar.SECOND, 0);
        todayStart.set(Calendar.MILLISECOND, 0);
        return todayStart.getTimeInMillis();
    }

    public static String formatDate(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = new Date();
        return sdf.format(date);
    }


    public static long StringTolong(String str_date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long time = 0;
        try {
            Date date = sdf.parse(str_date);
            time = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }

    public static long StringTolong(String str_date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        long time = 0;
        try {
            Date date = sdf.parse(str_date);
            time = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }

    public static String changeFormat(String str_date, String before, String after) {
        SimpleDateFormat sdf = new SimpleDateFormat(before);
        long time = 0;
        try {
            Date date = sdf.parse(str_date);
            time = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formatDate(time,after);
    }

    public static long UTCStringToLong(String str_date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
        long time = 0;
        try {
            sdf.setTimeZone(new SimpleTimeZone(0, "GMT"));
            Date date = sdf.parse(str_date);
            time = date.getTime();
            return time;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Function 获取当前时间，24小时制
     */
    public static String getCurrentTime() {
        return DateFormat.format("yyyy-MM-dd HH:mm:ss", new Date()).toString();
    }

    /**
     * 获取某天是星期几 星期一是1
     */
    public static int getWeekNumber(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int time = calendar.get(Calendar.DAY_OF_WEEK);
        if (time == Calendar.SUNDAY) {
            time = 7;
        } else {
            time -= 1;
        }
        return time;
    }


    /**
     * 获取某天是这个月第几周
     */
    public static int getMonthNumer(String str_date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = sdf.parse(str_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int forign = calendar.get(Calendar.WEEK_OF_MONTH);

        if (calendar.get(Calendar.DAY_OF_MONTH) == Calendar.SUNDAY) {//如果今天就是一号
            return 1;
        }
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
            forign += 1;
        return forign;
    }


    /**
     * 获取某月有几天
     * position 偏移多少月
     */
    public static int getDayOfMonth(int position) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, position);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取某天是第几个月
     */
    public static int getMonthOfYear(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH) + 1;
    }

    /**
     * 获取是多少号
     */
    public static int getDayNumber(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取是哪一年
     */
    public static int getYearNumber(String time) {
        String year = time.split(" ")[0].split("-")[0];
        return Integer.valueOf(year);
    }

    /**
     * 判断时间是不是今天
     * @param date
     * @return    是返回true，不是返回false
     */
    public static boolean isNow(Date date) {
        //当前时间
        Date now = new Date();
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
        //获取今天的日期
        String nowDay = sf.format(now);
        //对比的时间
        String day = sf.format(date);
        return day.equals(nowDay);

    }

}
