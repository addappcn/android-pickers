package cn.addapp.pickers.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;

import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.Reader;
import java.math.RoundingMode;
import java.net.NetworkInterface;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
    public static boolean isStringDataNull(String checkData) {
        if ("".equals(checkData) || checkData == null
                || "null".equals(checkData) || checkData == null) {
            return true;
        }
        return false;
    }
    public static boolean isListDataNull(List list) {
        if (list == null || list.size()<=0) {
            return true;
        }
        return false;
    }
    public static String replaceBlankCharacter(String string) {
        if (string != null) {
            string = string.replace(" ", "");
            return string;
        }
        return null;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue
     * @param  （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }


    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     * @param  （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }


    public static boolean isConnect(Context context) {

        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）

        try {

            ConnectivityManager connectivity = (ConnectivityManager) context

                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            if (connectivity != null) {

                // 获取网络连接管理的对象

                NetworkInfo info = connectivity.getActiveNetworkInfo();

                if (info != null && info.isConnected()) {

                    // 判断当前网络是否已经连接

                    if (info.getState() == NetworkInfo.State.CONNECTED) {

                        return true;

                    }

                }

            }

        } catch (Exception e) {

            // TODO: handle exception

            Log.v("error", e.toString());

        }

        return false;

    }

    /**
     * 获取手机的MAC地址
     *
     * @return
     */
//        public static String getMac() {
//            String str = "";
//            String macSerial = "";
//            try {
//                Process pp = Runtime.getRuntime().exec("cat /sys/class/net/wlan0/address ");
//                InputStreamReader ir = new InputStreamReader(pp.getInputStream());
//                LineNumberReader input = new LineNumberReader(ir);
//
//                for (; null != str;) {
//                    str = input.readLine();
//                    if (str != null) {
//                        macSerial = str.trim();// 去空格
//                        break;
//                    }
//                }
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
//            if (null == macSerial  || "".equals(macSerial)) {
//                try {
//                    return loadFileAsString("/sys/class/net/eth0/address")
//                            .toUpperCase().substring(0, 17);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    macSerial="02:00:00:00:00:00";
//                }
//            }
//            return macSerial;
//        }
    public static String getMacAddress() {
        String str = "";
        String macSerial = "";
        try {
            Process pp = Runtime.getRuntime().exec(
                    "cat /sys/class/net/wlan0/address ");
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);

            for (; null != str;) {
                str = input.readLine();
                if (str != null) {
                    macSerial = str.trim();// 去空格
                    break;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (macSerial == null || "".equals(macSerial)) {
            try {
                return loadFileAsString("/sys/class/net/eth0/address")
                        .toUpperCase().substring(0, 17);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        if(macSerial == null || "".equals(macSerial)){
            try {
                List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
                for (NetworkInterface nif : all) {
                    if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                    byte[] macBytes = nif.getHardwareAddress();
                    if (macBytes == null) {
                        return "";
                    }

                    StringBuilder res1 = new StringBuilder();
                    for (byte b : macBytes) {
                        res1.append(String.format("%02X:",b));
                    }

                    if (res1.length() > 0) {
                        res1.deleteCharAt(res1.length() - 1);
                    }
                    return res1.toString();
                }
            } catch (Exception ex) {
                macSerial= "02:00:00:00:00:00";
            }
        }
        return macSerial;
    }
    public static String loadFileAsString(String fileName) throws Exception {
        FileReader reader = new FileReader(fileName);
        String text = loadReaderAsString(reader);
        reader.close();
        return text;
    }

    public static String loadReaderAsString(Reader reader) throws Exception {
        StringBuilder builder = new StringBuilder();
        char[] buffer = new char[4096];
        int readLength = reader.read(buffer);
        while (readLength >= 0) {
            builder.append(buffer, 0, readLength);
            readLength = reader.read(buffer);
        }
        return builder.toString();
    }


    public static boolean isNull(String str) {
        if (str == null || str.equals("")  ) {
            return true;
        } else {
            return false;
        }
    }

    public static String get2XXDa(double data) {
        DecimalFormat df = new DecimalFormat("###.00");
        df.setGroupingSize(0);
        df.setRoundingMode(RoundingMode.UP);
        String d = df.format(data);

        if (d.indexOf(".") == 0) {
            d = "0" + d;
        }

        System.out.println(d);
        return d;
    }

    /**
     * 向下取整
     *
     * @param data
     * @return
     */
    public static String get2XXDown(double data) {
        DecimalFormat df = new DecimalFormat("###.00");
        df.setGroupingSize(0);
//        df.setRoundingMode(RoundingMode.DOWN);
        String d = df.format(data);
        if (d.indexOf(".") == 0) {
            d = "0" + d;
        }
        System.out.println(d);
        return d;
    }

    public static String get2XXDat(double data) {


        System.out.println(data);
        DecimalFormat df = new DecimalFormat("###.00");
        df.setGroupingSize(0);
        df.setRoundingMode(RoundingMode.DOWN);
        String d = df.format(data);

        if (d.indexOf(".") == 0) {
            d = "0" + d;
        }

        System.out.println(d);
        return d;
    }

    public static double get2XX(double data) {
        double f = data;
//
//		String s = String.valueOf(f);
//
//		s = s.substring(s.indexOf('.') + 1);
//
//		System.out.println(s.length());
//
//		if (s.length() > 2) {
//			data += 0.01;
//		}
//
//		System.out.println(data);
        DecimalFormat df = new DecimalFormat("###.00");

        df.setGroupingSize(0);
        df.setRoundingMode(RoundingMode.UP);
        String d = df.format(data);

        System.out.println(d);
        return Double.valueOf(d);
    }

    public static String getFormat_s1(long sd) {

        Date dat = new Date(sd);
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(dat);
        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat(
                "yyyy-MM-dd HH:mm");
        String sb = format.format(gc.getTime());

        return sb;
    }

    public static String getFormatYYMMDD(long sd) {
        Date dat = new Date(sd);
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(dat);
        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat(
                "yyyy-MM-dd");
        String sb = format.format(gc.getTime());

        return sb;
    }

    public static String getFormat(long sd) {

        Date dat = new Date(sd);
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(dat);
        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        String sb = format.format(gc.getTime());
        return sb;
    }
    public static String getFormatWithoutSS(long sd) {

        Date dat = new Date(sd);
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(dat);
        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat(
                "yyyy-MM-dd HH:mm");
        String sb = format.format(gc.getTime());
        return sb;
    }
    /**
     * 2月2日 下午17:49
     *
     * @param times
     * @return
     */
    public static String getFormatForAMPM(long times) {
        Calendar c = Calendar.getInstance();
        Date dat = new Date(times);
        c.setTime(dat);
        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat(
                "MM月dd日");
        String sb = format.format(c.getTime());
        String morning = "  上午 ";
        String afternoon = "  下午 ";
        String amOrpm = "";
        int hour = 0;
        if (c.get(c.HOUR_OF_DAY) > 12) {
            amOrpm = afternoon;
            hour = c.get(c.HOUR_OF_DAY) - 12;
            return sb + amOrpm + hour + ":" + String.format("%02d", c.get(c.MINUTE));
        } else {
            amOrpm = morning;
            hour = c.get(c.HOUR_OF_DAY);
            return sb + amOrpm + String.format("%02d", hour) + ":" + String.format("%02d", c.get(c.MINUTE));
        }
    }

    public static String getFormatHHMMSS(long sd) {
        Date dat = new Date(sd);
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(dat);
        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat(
                "HH:mm:ss");
        String sb = format.format(gc.getTime());
        return sb;
    }

    public static String getFormatHHMM(long sd) {
        Date dat = new Date(sd);
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(dat);
        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat(
                "MM-dd HH:mm");
        String sb = format.format(gc.getTime());

        return sb;
    }

    public static String getOnlyHHMM(long sd) {
        Date dat = new Date(sd);
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(dat);
        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat(
                "HH:mm");
        String sb = format.format(gc.getTime());

        return sb;
    }

    private final static long hourLevelValue = 60 * 60 * 1000;
    private final static long minuteLevelValue = 60 * 1000;
    private final static long secondLevelValue = 1000;

    /**
     * 剩余的小时,精确到分钟
     *
     * @param sd
     * @return
     */
    public static String getRemainHHMM(long sd) {
        String remainHM = "请等待上车";
        GregorianCalendar gc = new GregorianCalendar();
        long now = gc.getTimeInMillis();
        long remainMills = sd - now;
        if (remainMills >= 0) {
            int hour = (int) (remainMills / hourLevelValue);
            int minute = (int) ((remainMills - hour * hourLevelValue) / minuteLevelValue);
            int second = (int) ((remainMills - hour * hourLevelValue - minute * minuteLevelValue) / secondLevelValue);
            remainHM = getStringFromat02d(hour) + " : " + getStringFromat02d(minute) + " : " + getStringFromat02d(second);
        } else {
            remainHM = "请等待上车";
        }
        return remainHM;
    }

    /**
     * 数字带0两位
     *
     * @param num
     * @return
     */
    public static String getStringFromat02d(int num) {
        return String.format("%02d", num);
    }

    public static String getFormat1(long sd) {
        Date dat = new Date(sd);
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(dat);
        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat(
                "yyyy.MM.dd", Locale.US);
        String sb = format.format(gc.getTime());

        return sb;
    }

    public static String getFormat3(long sd) {

        Date dat = new Date(sd);
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(dat);
        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat(
                "MM-dd  HH:mm", Locale.CHINA);
        String sb = format.format(dat);

        return sb;
    }

    public static String getFormat4(long sd) {

        Date dat = new Date(sd);
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(dat);
        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat(
                "HH:mm", Locale.US);
        String sb = format.format(gc.getTime());

        return sb;
    }

    public static SpannableString getSpannableString(String keyword,
                                                     String content, int color) {
        Pattern pattern;// = Pattern.compile("abc");
        Matcher matcher;// = p.matcher(s);
        SpannableString spannableString;
        if (!StringUtils.isStringDataNull(keyword)
                && !StringUtils.isStringDataNull(content)) {
            spannableString = new SpannableString(content);
            pattern = Pattern.compile(keyword);
            matcher = pattern.matcher(content);
            while (matcher.find()) {
                int start = matcher.start();
                int end = matcher.end();
                spannableString.setSpan(new ForegroundColorSpan(color), start,
                        end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
//            Log.e("", "spannableString=======" + spannableString);
            return spannableString;
        } else {
            return new SpannableString(content);
        }

    }

    public static boolean isAvilible(Context context, String packageName) {
        final PackageManager packageManager = context.getPackageManager();
        // 获取所有已安装程序的包信息
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        for (int i = 0; i < pinfo.size(); i++) {
            if (pinfo.get(i).packageName.equalsIgnoreCase(packageName))
                return true;
        }
        return false;
    }

    public static long getLastWeek() {
        // TODO Auto-generated method stub
        Calendar calendar = Calendar.getInstance();


        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        calendar.set(Calendar.WEEK_OF_YEAR,
                calendar.get(Calendar.WEEK_OF_YEAR) - 1);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        Log.e("", "calendar=======" + calendar.getTimeInMillis() + "=====");

        return calendar.getTimeInMillis();

    }

    public static long getThisWeek() {
        // TODO Auto-generated method stub
        Calendar calendar = Calendar.getInstance();

        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        Log.e("", "calendar=======" + calendar.getTimeInMillis() + "=====");

        return calendar.getTimeInMillis();

    }

    /**
     * 检查当前网络是否可用
     *
     * @param
     * @return
     */

    public static boolean isNetworkAvailable(Context c) {
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager) c
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null) {
            return false;
        } else {
            // 获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

            if (networkInfo != null && networkInfo.length > 0) {
                for (int i = 0; i < networkInfo.length; i++) {
                    System.out.println(i + "===状态==="
                            + networkInfo[i].getState());
                    System.out.println(i + "===类型==="
                            + networkInfo[i].getTypeName());
                    // 判断当前网络状态是否为连接状态
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 获取版本号
     * @param c
     * @return
     */
    public static String getVersion(Context c) {
        try {
            PackageManager manager = c.getPackageManager();
            PackageInfo info = manager.getPackageInfo(c.getPackageName(), 0);
            String version = info.versionName;
            return TextUtils.isEmpty(version) ? "" : version;
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    public static String getVer(Context c) {
        try {
            PackageManager manager = c.getPackageManager();
            PackageInfo info = manager.getPackageInfo(c.getPackageName(), 0);
            String version = info.versionName;
            return "version：" + version;
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    /**
     * 获取当前系统的android版本号
     * @param c
     * @return
     */
    public static String getPlatform(Context c){
        int sdk_int = android.os.Build.VERSION.SDK_INT;
        String version = android.os.Build.VERSION.RELEASE;
        return "android platform: "+version+"，sdk: "+sdk_int;
    }

    public static int getVersionCode(Context context)// 获取版本号(内部识别号)
    {
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);
            return pi.versionCode;
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return 0;
        }
    }

    private static String mYear;
    private static String mMonth;
    private static String mDay;
    private static String mWay;

    public static String StringData(String sd) {
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        mYear = String.valueOf(c.get(Calendar.YEAR)); // 获取当前年份
        mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);// 获取当前月份
        mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码
        mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
        if ("1".equals(mWay)) {
            mWay = "天";
        } else if ("2".equals(mWay)) {
            mWay = "一";
        } else if ("3".equals(mWay)) {
            mWay = "二";
        } else if ("4".equals(mWay)) {
            mWay = "三";
        } else if ("5".equals(mWay)) {
            mWay = "四";
        } else if ("6".equals(mWay)) {
            mWay = "五";
        } else if ("7".equals(mWay)) {
            mWay = "六";
        }
        return mYear + "-" + mMonth + "-" + mDay + "日" + " 星期" + mWay;
    }

    public static String getDateFors(long times) {
        final Calendar c = Calendar.getInstance();
        Date dat = new Date(times);
        c.setTime(dat);
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        mYear = String.valueOf(c.get(Calendar.YEAR)); // 获取当前年份
        mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);// 获取当前月份
        mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码
        mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
        if ("1".equals(mWay)) {
            mWay = "天";
        } else if ("2".equals(mWay)) {
            mWay = "一";
        } else if ("3".equals(mWay)) {
            mWay = "二";
        } else if ("4".equals(mWay)) {
            mWay = "三";
        } else if ("5".equals(mWay)) {
            mWay = "四";
        } else if ("6".equals(mWay)) {
            mWay = "五";
        } else if ("7".equals(mWay)) {
            mWay = "六";
        }
        return " 星期" + mWay;
    }

    public static String getFormatYMDW(long times) {
        final Calendar c = Calendar.getInstance();
        Date dat = new Date(times);
        c.setTime(dat);
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        mYear = String.valueOf(c.get(Calendar.YEAR)); // 获取当前年份
        mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);// 获取当前月份
        mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码
        mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
        if ("1".equals(mWay)) {
            mWay = "天";
        } else if ("2".equals(mWay)) {
            mWay = "一";
        } else if ("3".equals(mWay)) {
            mWay = "二";
        } else if ("4".equals(mWay)) {
            mWay = "三";
        } else if ("5".equals(mWay)) {
            mWay = "四";
        } else if ("6".equals(mWay)) {
            mWay = "五";
        } else if ("7".equals(mWay)) {
            mWay = "六";
        }
        return mYear + "-" + mMonth + "-" + mDay + "  星期" + mWay;
    }

    public static String getDateFors2(long times) {
        final Calendar c = Calendar.getInstance();
        Date dat = new Date(times);
        c.setTime(dat);
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        mYear = String.valueOf(c.get(Calendar.YEAR)); // 获取当前年份
        mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);// 获取当前月份
        mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码
        mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
        if ("1".equals(mWay)) {
            mWay = "天";
        } else if ("2".equals(mWay)) {
            mWay = "一";
        } else if ("3".equals(mWay)) {
            mWay = "二";
        } else if ("4".equals(mWay)) {
            mWay = "三";
        } else if ("5".equals(mWay)) {
            mWay = "四";
        } else if ("6".equals(mWay)) {
            mWay = "五";
        } else if ("7".equals(mWay)) {
            mWay = "六";
        }
        return mWay;
    }

    public static String getDateMMSS(long times) {
        final Calendar c = Calendar.getInstance();
        Date dat = new Date(times);
        c.setTime(dat);
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));

        int sec = c.get(Calendar.MINUTE);
        if (c.get(Calendar.HOUR_OF_DAY) > 12) {
            return "下午" + c.get(Calendar.HOUR) + ":" + sec;
        } else {
            return "上午" + c.get(Calendar.HOUR) + ":" + sec;
        }
    }

    public static String getDateMMSS12(long times) {
        final Calendar c = Calendar.getInstance();
        Date dat = new Date(times);
        c.setTime(dat);
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        int miao = c.get(Calendar.SECOND);
        int sec = c.get(Calendar.MINUTE);
        if (c.get(Calendar.HOUR_OF_DAY) > 12) {
            return "下午" + c.get(Calendar.HOUR) + ":" + sec + ":" + miao;
        } else {
            return "上午" + c.get(Calendar.HOUR) + ":" + sec + ":" + miao;

        }
    }

    public static String getLocalMacAddress(Context context) {
        WifiManager wifi = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        return info.getMacAddress();
    }
}
