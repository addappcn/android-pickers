package cn.addapp.pickers.util;

import android.support.annotation.IntDef;
import android.support.annotation.NonNull;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * 日期时间工具类
 * @author matt
 * blog: addapp.cn
 */
public class DateUtils extends android.text.format.DateUtils {
    public static final int Second = 0;
    public static final int Minute = 1;
    public static final int Hour = 2;
    public static final int Day = 3;

    @IntDef(value = {Second, Minute, Hour, Day})
    @Retention(RetentionPolicy.SOURCE)
    public @interface DifferenceMode {
    }

    public static long calculateDifferentSecond(Date startDate, Date endDate) {
        return calculateDifference(startDate, endDate, Second);
    }

    public static long calculateDifferentMinute(Date startDate, Date endDate) {
        return calculateDifference(startDate, endDate, Minute);
    }

    public static long calculateDifferentHour(Date startDate, Date endDate) {
        return calculateDifference(startDate, endDate, Hour);
    }

    public static long calculateDifferentDay(Date startDate, Date endDate) {
        return calculateDifference(startDate, endDate, Day);
    }

    public static long calculateDifferentSecond(long startTimeMillis, long endTimeMillis) {
        return calculateDifference(startTimeMillis, endTimeMillis, Second);
    }

    public static long calculateDifferentMinute(long startTimeMillis, long endTimeMillis) {
        return calculateDifference(startTimeMillis, endTimeMillis, Minute);
    }

    public static long calculateDifferentHour(long startTimeMillis, long endTimeMillis) {
        return calculateDifference(startTimeMillis, endTimeMillis, Hour);
    }

    public static long calculateDifferentDay(long startTimeMillis, long endTimeMillis) {
        return calculateDifference(startTimeMillis, endTimeMillis, Day);
    }

    /**
     * 计算两个时间戳之间相差的时间戳数
     */
    public static long calculateDifference(long startTimeMillis, long endTimeMillis, @DifferenceMode int mode) {
        return calculateDifference(new Date(startTimeMillis), new Date(endTimeMillis), mode);
    }

    /**
     * 计算两个日期之间相差的时间戳数
     */
    public static long calculateDifference(Date startDate, Date endDate, @DifferenceMode int mode) {
        long[] different = calculateDifference(startDate, endDate);
        if (mode == Minute) {
            return different[2];
        } else if (mode == Hour) {
            return different[1];
        } else if (mode == Day) {
            return different[0];
        } else {
            return different[3];
        }
    }

    private static long[] calculateDifference(Date startDate, Date endDate) {
        return calculateDifference(endDate.getTime() - startDate.getTime());
    }

    private static long[] calculateDifference(long differentMilliSeconds) {
        long secondsInMilli = 1000;//1s==1000ms
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;
        long elapsedDays = differentMilliSeconds / daysInMilli;
        differentMilliSeconds = differentMilliSeconds % daysInMilli;
        long elapsedHours = differentMilliSeconds / hoursInMilli;
        differentMilliSeconds = differentMilliSeconds % hoursInMilli;
        long elapsedMinutes = differentMilliSeconds / minutesInMilli;
        differentMilliSeconds = differentMilliSeconds % minutesInMilli;
        long elapsedSeconds = differentMilliSeconds / secondsInMilli;
        LogUtils.verbose(String.format(Locale.CHINA, "different: %d ms, %d days, %d hours, %d minutes, %d seconds",
                differentMilliSeconds, elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds));
        return new long[]{elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds};
    }

    /**
     * 计算每月的天数
     */
    public static int calculateDaysInMonth(int month) {
        return calculateDaysInMonth(0, month);
    }

    /**
     * 根据年份及月份计算每月的天数
     */
    public static int calculateDaysInMonth(int year, int month) {
        // 添加大小月月份并将其转换为list,方便之后的判断
        String[] bigMonths = {"1", "3", "5", "7", "8", "10", "12"};
        String[] littleMonths = {"4", "6", "9", "11"};
        List<String> bigList = Arrays.asList(bigMonths);
        List<String> littleList = Arrays.asList(littleMonths);
        // 判断大小月及是否闰年,用来确定"日"的数据
        if (bigList.contains(String.valueOf(month))) {
            return 31;
        } else if (littleList.contains(String.valueOf(month))) {
            return 30;
        } else {
            if (year <= 0) {
                return 29;
            }
            // 是否闰年
            if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
                return 29;
            } else {
                return 28;
            }
        }
    }

    /**
     * 月日时分秒，0-9前补0
     */
    @NonNull
    public static String fillZero(int number) {
        return number < 10 ? "0" + number : "" + number;
    }

    /**
     * 截取掉前缀0以便转换为整数
     *
     * @see #fillZero(int)
     */
    public static int trimZero(@NonNull String text) {
        try {
            if (text.startsWith("0")) {
                text = text.substring(1);
            }
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            LogUtils.warn(e);
            return 0;
        }
    }

    /**
     * 功能：判断日期是否和当前date对象在同一天。
     * 参见：http://www.cnblogs.com/myzhijie/p/3330970.html
     *
     * @param date 比较的日期
     * @return boolean 如果在返回true，否则返回false。
     */
    public static boolean isSameDay(Date date) {
        if (date == null) {
            throw new IllegalArgumentException("date is null");
        }
        Calendar nowCalendar = Calendar.getInstance();
        Calendar newCalendar = Calendar.getInstance();
        newCalendar.setTime(date);
        return (nowCalendar.get(Calendar.ERA) == newCalendar.get(Calendar.ERA) &&
                nowCalendar.get(Calendar.YEAR) == newCalendar.get(Calendar.YEAR) &&
                nowCalendar.get(Calendar.DAY_OF_YEAR) == newCalendar.get(Calendar.DAY_OF_YEAR));
    }

    /**
     * 将yyyy-MM-dd HH:mm:ss字符串转换成日期<br/>
     *
     * @param dateStr    时间字符串
     * @param dataFormat 当前时间字符串的格式。
     * @return Date 日期 ,转换异常时返回null。
     */
    public static Date parseDate(String dateStr, String dataFormat) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(dataFormat, Locale.PRC);
            Date date = dateFormat.parse(dateStr);
            return new Date(date.getTime());
        } catch (ParseException e) {
            LogUtils.warn(e);
            return null;
        }
    }

    /**
     * 将yyyy-MM-dd HH:mm:ss字符串转换成日期<br/>
     *
     * @param dateStr yyyy-MM-dd HH:mm:ss字符串
     * @return Date 日期 ,转换异常时返回null。
     */
    public static Date parseDate(String dateStr) {
        return parseDate(dateStr, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 将指定的日期转换为一定格式的字符串
     */
    public static String formatDate(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.PRC);
        return sdf.format(date);
    }

    /**
     * 将当前日期转换为一定格式的字符串
     */
    public static String formatDate(String format) {
        return formatDate(Calendar.getInstance(Locale.CHINA).getTime(), format);
    }


    public static String getDataShow(long date) {

        SimpleDateFormat df = new SimpleDateFormat(
                "yyyy-MM-dd ");
        java.util.Calendar calendar = java.util.Calendar.getInstance();

        long today = DateFormat(df.format(calendar.getTime()) + " 00:00");
        System.out.println("今天： " + today);

        calendar.add(java.util.Calendar.DAY_OF_YEAR, 1);
        long twoday = DateFormat(df.format(calendar.getTime()) + " 00:00");
        System.out.println("明天： " + twoday);

        calendar.add(java.util.Calendar.DAY_OF_YEAR, 1);

        long threeday = DateFormat(df.format(calendar.getTime()) + " 00:00");
        long threeday2 = DateFormat(df.format(calendar.getTime()) + " 24:00");
        System.out.println("后天： " + threeday);

        if (date < today) {
            return StringUtils.getFormat_s1(date) + "  "
                    + StringUtils.getDateFors(date);
        } else if (date < twoday) {
            return "今天  " + StringUtils.getOnlyHHMM(date) + "  "
                    + StringUtils.getDateFors(date);
        } else if (date < threeday) {
            return "明天 " + StringUtils.getOnlyHHMM(date) + "  "
                    + StringUtils.getDateFors(date);
        } else if (date <= threeday2) {
            return "后天" + StringUtils.getOnlyHHMM(date) + "  "
                    + StringUtils.getDateFors(date);
        } else {
            return StringUtils.getFormat_s1(date) + "  "
                    + StringUtils.getDateFors(date);
        }

    }

    /**
     * yyyy-MM-dd HH:mm
     * @param date
     * @return
     */
    public static String getDateFormatRoute(long date) {
        SimpleDateFormat df = new SimpleDateFormat(
                "yyyy-MM-dd ");
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        long today = DateFormat(df.format(calendar.getTime()) + " 00:00");
        calendar.add(java.util.Calendar.DAY_OF_YEAR, 1);
        long twoday = DateFormat(df.format(calendar.getTime()) + " 00:00");
        calendar.add(java.util.Calendar.DAY_OF_YEAR, 1);
        long threeday = DateFormat(df.format(calendar.getTime()) + " 00:00");
        long threeday2 = DateFormat(df.format(calendar.getTime()) + " 24:00");
        if (date < today) {
            return StringUtils.getFormat_s1(date);
        } else if (date < twoday) {
            return "今天 " + StringUtils.getOnlyHHMM(date);
        } else if (date < threeday) {
            return "明天 " + StringUtils.getOnlyHHMM(date);
        } else if (date <= threeday2) {
            return "后天 " + StringUtils.getOnlyHHMM(date);
        } else {
            return StringUtils.getFormat_s1(date);
        }

    }


    public static String getDataShowForCoupon(long date) {

        SimpleDateFormat df = new SimpleDateFormat(
                "yyyy-MM-dd ");
        java.util.Calendar calendar = java.util.Calendar.getInstance();

        long today = DateFormat(df.format(calendar.getTime()) + " 00:00");
        System.out.println("今天： " + today);

        calendar.add(java.util.Calendar.DAY_OF_YEAR, 1);
        long twoday = DateFormat(df.format(calendar.getTime()) + " 00:00");
        System.out.println("明天： " + twoday);

        calendar.add(java.util.Calendar.DAY_OF_YEAR, 1);

        long threeday = DateFormat(df.format(calendar.getTime()) + " 00:00");
        long threeday2 = DateFormat(df.format(calendar.getTime()) + " 24:00");
        System.out.println("后天： " + threeday);

        if (date < today) {
            return StringUtils.getFormatYYMMDD(date);
        } else if (date < twoday) {
            return "今天";
        } else if (date < threeday) {
            return "明天";
        } else if (date <= threeday2) {
            return "后天";
        } else {
            return StringUtils.getFormatYYMMDD(date);
        }

    }

    public static String getDataShow_one(long date) {

        SimpleDateFormat df = new SimpleDateFormat(
                "yyyy-MM-dd ");
        java.util.Calendar calendar = java.util.Calendar.getInstance();

        long today = DateFormat(df.format(calendar.getTime()) + " 00:00");
        System.out.println("今天： " + today);

        calendar.add(java.util.Calendar.DAY_OF_YEAR, 1);
        long twoday = DateFormat(df.format(calendar.getTime()) + " 00:00");
        System.out.println("明天： " + twoday);

        calendar.add(java.util.Calendar.DAY_OF_YEAR, 1);

        long threeday = DateFormat(df.format(calendar.getTime()) + " 00:00");
        long threeday2 = DateFormat(df.format(calendar.getTime()) + " 24:00");
        System.out.println("后天： " + threeday);

        if (date < today) {
            return StringUtils.getFormat_s1(date) + "  ";
        } else if (date < twoday) {
            return "今天  " + StringUtils.getOnlyHHMM(date) + "  ";
        } else if (date < threeday) {
            return "明天 " + StringUtils.getOnlyHHMM(date);

        } else if (date <= threeday2) {
            return "后天 " + StringUtils.getOnlyHHMM(date);
        } else {
            return StringUtils.getFormat_s1(date) + "  ";
        }

    }

    public static long get2NextTime() {
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.add(java.util.Calendar.HOUR_OF_DAY, 2);

        return calendar.getTimeInMillis();
    }

    public static long getNowMiliTime(){
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        long nowTimeLong = calendar.getTimeInMillis();
        return nowTimeLong;
    }

    public static String getDataShow1(long date) {

        SimpleDateFormat df = new SimpleDateFormat(
                "yyyy-MM-dd ");
        java.util.Calendar calendar = java.util.Calendar.getInstance();

        long today = DateFormat(df.format(calendar.getTime()) + " 00:00");
        System.out.println("今天： " + today);

        calendar.add(java.util.Calendar.DAY_OF_YEAR, 1);
        long twoday = DateFormat(df.format(calendar.getTime()) + " 00:00");
        System.out.println("明天： " + twoday);

        calendar.add(java.util.Calendar.DAY_OF_YEAR, 1);

        long threeday = DateFormat(df.format(calendar.getTime()) + " 00:00");
        long threeday2 = DateFormat(df.format(calendar.getTime()) + " 24:00");
        System.out.println("后天： " + threeday);

        if (date < today) {
            return StringUtils.getFormat_s1(date);
        } else if (date < twoday) {
            return "今天  " + StringUtils.getOnlyHHMM(date);
        } else if (date < threeday) {
            return "明天 " + StringUtils.getOnlyHHMM(date);
        } else if (date <= threeday2) {
            return "后天" + StringUtils.getOnlyHHMM(date);
        } else {
            return "" + StringUtils.getFormat_s1(date);
        }

    }

    public static String getDate02dYMD(long mill){
        SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日");
        Date date = new Date(mill);
        String dateStr = df.format(date);
        return dateStr;
    }

    public static String getDataShow2(long date) {
        SimpleDateFormat df = new SimpleDateFormat(
                "yyyy-MM-dd ");
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        long today = DateFormat(df.format(calendar.getTime()) + " 00:00");

        calendar.add(java.util.Calendar.DAY_OF_YEAR, 1);
        long twoday = DateFormat(df.format(calendar.getTime()) + " 00:00");

        calendar.add(java.util.Calendar.DAY_OF_YEAR, 1);

        long threeday = DateFormat(df.format(calendar.getTime()) + " 00:00");
        long threeday2 = DateFormat(df.format(calendar.getTime()) + " 24:00");

        if (date < today) {
            return StringUtils.getFormat_s1(date);
        } else if (date < twoday) {
            return StringUtils.getOnlyHHMM(date);
        } else if (date < threeday) {
            return "明天 " + StringUtils.getOnlyHHMM(date);
        } else if (date <= threeday2) {
            return "后天" + StringUtils.getOnlyHHMM(date);
        } else {
            return "" + StringUtils.getFormat_s1(date);
        }

    }

    public static long DateFormatWithYearMonthDay(String dates) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = format.parse(dates);
            return date.getTime();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return 0;
    }
    public static long DateFormat(String dates) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            Date date = format.parse(dates);
            return date.getTime();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return 0;
    }
    public static long DateFormatToSS(String dates) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = format.parse(dates);
            return date.getTime();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return 0;
    }
    /**
     * 根据毫秒时间差计算相差多少天，多少小时，多少分
     * */

    public static Map<String,Long> computeDiff(long startTime, long endTime){
        Map<String,Long> data=new HashMap();
        String diff = "";
        //开始
        long days = 0;
        long hours = 0;
        long minutes = 0;
        long seconds = 0;
        //结束
        long dayn = 0;
        long hourn = 0;
        long minuten = 0;
        long secondn = 0;

        days = startTime / (24 * 60 * 60 * 1000);
        hours = (startTime / (60 * 60 * 1000) - days * 24);
        minutes = ((startTime / (60 * 1000)) - days * 24 * 60 - hours * 60);
        seconds = (startTime / 1000 - days*24*60*60 - hours*60*60 - minutes*60);



        dayn = endTime / (24 * 60 * 60 * 1000);
        hourn = (endTime / (60 * 60 * 1000) - dayn * 24);
        minuten = ((endTime / (60 * 1000)) - dayn * 24 * 60 - hourn * 60);
        secondn = (endTime / 1000 - dayn*24*60*60 - hourn*60*60 - minuten*60);
        startTime= startTime-seconds*1000;
        endTime= endTime-secondn*1000;
        if(seconds>0){
            startTime = startTime+60*1000;
        }
        if(secondn>0){
            endTime = endTime+60*1000;
        }
        data.put("startTime",startTime);
        data.put("endTime",endTime);
        return data;
    }
    /**
     * 根据毫秒时间差计算相差多少天，多少小时，多少分
     * */

    public static  String computeDiff(long diffTime){
        String diff = "";
        long days = 0;
        long hours = 0;
        long minutes = 0;
        long seconds = 0;

        days = diffTime / (24 * 60 * 60 * 1000);
        hours = (diffTime / (60 * 60 * 1000) - days * 24);
        minutes = ((diffTime / (60 * 1000)) - days * 24 * 60 - hours * 60);
        seconds = (diffTime / 1000 - days*24*60*60 - hours*60*60 - minutes*60);
        if(seconds>0){
            minutes=minutes+1;
        }
        diff = days + "天"+ hours +"小时"+ minutes+"分钟";
        return diff;
    }
}
