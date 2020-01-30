package cn.addapp.pickers.picker;

import android.app.Activity;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

import cn.addapp.pickers.adapter.ArrayWheelAdapter;
import cn.addapp.pickers.listeners.OnItemPickListener;
import cn.addapp.pickers.util.DateUtils;
import cn.addapp.pickers.util.LogUtils;
import cn.addapp.pickers.wheelpicker.R;
import cn.addapp.pickers.widget.WheelView;

/**
 * 时间选择器，只包含天，小时，分，另见{@link DatePicker}和{@link TimePicker}
 * @author matt
 * blog: addapp.cn
 * @since 2015/9/29
 */
public class DayTimePicker extends WheelPicker {
    /**
     * 不显示
     */
    public static final int NONE = -1;
    /**
     * 年月日
     */
    public static final int YEAR_MONTH_DAY = 0;
    /**
     * 年月
     */
    public static final int YEAR_MONTH = 1;
    /**
     * 月日
     */
    public static final int MONTH_DAY = 2;

    /**
     * 24小时
     */
    public static final int HOUR_24 = 3;
    /**
     * 12小时
     */
    public static final int HOUR_12 = 4;

    Calendar c;
    private int preDays = 3;//最大预约天数
    private int driverHour = 0;//开车时间
    private long serverTime = 0;
    private ArrayList<String> hours = new ArrayList<>();
    private ArrayList<String> days = new ArrayList<>();
    private ArrayList<String> minutes = new ArrayList<>();
    private String  dayLabel = "日";
    private String hourLabel = "时", minuteLabel = "分";
    private int selectedDayIndex = 0,selectedHourIndex = 0,selectedMinuteIndex = 0;
    private String selectedHour = "", selectedMinute = "";
    private OnWheelListener onWheelListener;
    private OnDateTimePickListener onDateTimePickListener;
    private int  startDay = -1;
    private int endDay = -1;
    private int startHour, startMinute = 0;
    private int endHour, endMinute = 59;
    private int stepMinute=1,stepHour=1;//时间间隔
    private String [] dayLabels;

    public DayTimePicker(Activity activity) {
        super(activity);
        //根据时间模式初始化小时范围
        startHour = 0;
        endHour = 23;
        if (preDays <= 0) {
            preDays = 3;
        }
    }

    public DayTimePicker(Activity activity,long serverTime) {
        super(activity);
        //根据时间模式初始化小时范围
        startHour = 0;
        endHour = 23;
        this.serverTime = serverTime;
        if (preDays <= 0) {
            preDays = 3;
        }
        initCalendar();
    }

    public DayTimePicker(Activity activity,int preDays,long serverTime) {
        super(activity);
        //根据时间模式初始化小时范围
        startHour = 0;
        endHour = 23;
        this.preDays = preDays;
        this.serverTime = serverTime;
        initCalendar();
    }
    public DayTimePicker(Activity activity,int preDays,long serverTime,@IntRange(from = 1, to = 24) int driverHour) {
        super(activity);
        //根据时间模式初始化小时范围
        startHour = 0;
        endHour = 23;
        this.preDays = preDays;
        this.serverTime = serverTime;
        this.driverHour = driverHour;
        initCalendar();
    }
//    public void setStepMinute(int stepMinute) {
//        this.stepMinute = stepMinute;
//        minutes.clear();
//        if (timeMode != NONE) {
//            changeMinuteData(DateUtils.trimZero(selectedHour));
//        }
//    }
//
//    public void setStepHour(int stepHour) {
//        this.stepHour = stepHour;
//        hours.clear();
//        initHourData();
//    }


    /**
     * 设置范围：开始的时分
     */
    public void setTimeRangeStart(int startHour, int startMinute) {
        boolean illegal = false;
        if (startHour <= 0 || startHour >= 24 || startMinute < 0 || startMinute > 59) {
            illegal = true;
        }
        if (illegal) {
            throw new IllegalArgumentException("Time out of range");
        }
        this.startHour = startHour;
        this.startMinute = startMinute;
    }

    /**
     * 设置范围：结束的时分
     */
    public void setTimeRangeEnd(int endHour, int endMinute) {
        boolean illegal = false;
        if (endHour < 0 || endHour >= 24 || endMinute < 0 || endMinute > 59) {
            illegal = true;
        }
        if (illegal) {
            throw new IllegalArgumentException("Time out of range");
        }
        this.endHour = endHour;
        this.endMinute = endMinute;
        initHourData();
    }

    /**
     * 设置年月日时分的显示单位
     */
    public void setLabel(String dayLabel, String hourLabel, String minuteLabel) {
        this.dayLabel = dayLabel;
        this.hourLabel = hourLabel;
        this.minuteLabel = minuteLabel;
    }

    /**
     * 设置默认选中的年月日时分
     */
    public void setSelectedItem(int day, int hour, int minute) {
        selectedDayIndex = findItemIndex(days, day);
        selectedHour = DateUtils.fillZero(hour);
        selectedMinute = DateUtils.fillZero(minute);
        if ( hours.size() == 0) {
            LogUtils.verbose(this, "init hours before make view");
            initHourData();
        }
        selectedHourIndex = findItemIndex(hours, hour);
        changeMinuteData(hour);
        selectedMinuteIndex = findItemIndex(minutes, minute);

    }


    public void setOnWheelListener(OnWheelListener onWheelListener) {
        this.onWheelListener = onWheelListener;
    }

    public void setOnDateTimePickListener(OnDateTimePickListener listener) {
        this.onDateTimePickListener = listener;
    }


    public String getSelectedDay() {
        if (days.size() <= selectedDayIndex) {
            selectedDayIndex = days.size() - 1;
        }
        return days.get(selectedDayIndex);
    }

    public String getSelectedHour() {
        return selectedHour;
    }

    public String getSelectedMinute() {
        return selectedMinute;
    }

    @NonNull
    @Override
    protected View makeCenterView() {
        // 如果未设置默认项，则需要在此初始化数据
        dayLabels = activity.getResources().getStringArray(R.array.day_labels);
        changeDayData();
        if (hours.size() == 0) {
            LogUtils.verbose(this, "init hours before make view");
            initHourData();
        }
        if ( minutes.size() == 0) {
            LogUtils.verbose(this, "init minutes before make view");
            changeMinuteData(DateUtils.trimZero(selectedHour));
        }

        LinearLayout layout = new LinearLayout(activity);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setGravity(Gravity.CENTER);
        layout.setWeightSum(3);
        LinearLayout.LayoutParams wheelViewParams;
        LinearLayout.LayoutParams labelViewParams;
        if(weightEnable){
            wheelViewParams = new LinearLayout.LayoutParams(0,WRAP_CONTENT);
            wheelViewParams.weight = 1.0f;
//            labelViewParams= new LinearLayout.LayoutParams(0,WRAP_CONTENT);
//            labelViewParams.weight = 0.5f;
        }else{
            wheelViewParams = new LinearLayout.LayoutParams(WRAP_CONTENT,WRAP_CONTENT);
        }
        labelViewParams = new LinearLayout.LayoutParams(WRAP_CONTENT,WRAP_CONTENT);
        final WheelView dayView = new WheelView(activity);
        final WheelView hourView = new WheelView(activity);
        final WheelView minuteView = new WheelView(activity);
        dayView.setCanLoop(canLoop);
        dayView.setTextSize(textSize);//must be called before setDateList
        dayView.setSelectedTextColor(textColorFocus);
        dayView.setUnSelectedTextColor(textColorNormal);
        dayView.setAdapter(new ArrayWheelAdapter<>(days));
        dayView.setCurrentItem(selectedDayIndex);
        dayView.setLineConfig(lineConfig);
        dayView.setDividerType(lineConfig.getDividerType());
        dayView.setLayoutParams(wheelViewParams);
        dayView.setOnItemPickListener(new OnItemPickListener<String>() {
            @Override
            public void onItemPicked(int index, String item) {
                selectedDayIndex = index;
                if (onWheelListener != null) {
                    onWheelListener.onDayWheeled(index, item);
                }
            }
        });
        layout.addView(dayView);
        if (!TextUtils.isEmpty(dayLabel) ){
            if(isOuterLabelEnable()){
                TextView labelView = new TextView(activity);
                labelView.setLayoutParams(labelViewParams);
                labelView.setTextColor(textColorFocus);
                labelView.setTextSize(textSize);
                labelView.setText(dayLabel);
                layout.addView(labelView);
            }else{
                dayView.setLabel(dayLabel);
            }
        }
//                layout.setWeightSum(5);
        //小时
        hourView.setCanLoop(canLoop);
        hourView.setTextSize(textSize);//must be called before setDateList
        hourView.setSelectedTextColor(textColorFocus);
        hourView.setUnSelectedTextColor(textColorNormal);
        hourView.setDividerType(lineConfig.getDividerType());
        hourView.setAdapter(new ArrayWheelAdapter<>(hours));
        hourView.setCurrentItem(selectedHourIndex);
        hourView.setLineConfig(lineConfig);
        hourView.setLayoutParams(wheelViewParams);
        hourView.setOnItemPickListener(new OnItemPickListener<String>() {
            @Override
            public void onItemPicked(int index, String item) {
                selectedHourIndex = index;
                selectedMinuteIndex = 0;
                selectedHour = item;
                if (onWheelListener != null) {
                    onWheelListener.onHourWheeled(index, item);
                }
                if (!canLinkage) {
                    return;
                }
                changeMinuteData(DateUtils.trimZero(item));
                minuteView.setAdapter(new ArrayWheelAdapter<>(minutes));
                minuteView.setCurrentItem(selectedMinuteIndex);
            }
        });
        layout.addView(hourView);
        if (!TextUtils.isEmpty(hourLabel) ){
            if(isOuterLabelEnable()){
                TextView labelView = new TextView(activity);
                labelView.setLayoutParams(labelViewParams);
                labelView.setTextColor(textColorFocus);
                labelView.setTextSize(textSize);
                labelView.setText(hourLabel);
                layout.addView(labelView);
            }else{
                hourView.setLabel(hourLabel);
            }
        }
        //分钟
        minuteView.setCanLoop(canLoop);
        minuteView.setTextSize(textSize);//must be called before setDateList
        minuteView.setSelectedTextColor(textColorFocus);
        minuteView.setUnSelectedTextColor(textColorNormal);
        minuteView.setAdapter(new ArrayWheelAdapter<>(minutes));
        minuteView.setCurrentItem(selectedMinuteIndex);
        minuteView.setDividerType(lineConfig.getDividerType());
        minuteView.setLineConfig(lineConfig);
        minuteView.setLayoutParams(wheelViewParams);
        layout.addView(minuteView);
        minuteView.setOnItemPickListener(new OnItemPickListener<String>() {
            @Override
            public void onItemPicked(int index, String item) {
                selectedMinuteIndex = index;
                selectedMinute = item;
                if (onWheelListener != null) {
                    onWheelListener.onMinuteWheeled(index, item);
                }
            }
        });
        if (!TextUtils.isEmpty(minuteLabel)){
            if(isOuterLabelEnable()){
                TextView labelView = new TextView(activity);
                labelView.setLayoutParams(labelViewParams);
                labelView.setTextColor(textColorFocus);
                labelView.setTextSize(textSize);
                labelView.setText(minuteLabel);
                layout.addView(labelView);
            }else{
                minuteView.setLabel(minuteLabel);
            }
        }


        return layout;
    }

    @Override
    protected void onSubmit() {
        if (onDateTimePickListener == null) {
            return;
        }
        if(c==null){
            initCalendar();
        }
        String day,selectDay;
        String year = String.valueOf(c.get(Calendar.YEAR));
        String month = String.valueOf(c.get(Calendar.MONTH)+1);
        int today = c.get(Calendar.DATE);
        selectDay = getSelectedDay();
        if(dayLabels[0].equals(selectDay)){
            day = String.valueOf(today);
        }else if(dayLabels[1].equals(selectDay)){
            day = String.valueOf(today+1);
        }else if(dayLabels[2].equals(selectDay)){
            day = String.valueOf(today+2);
        }else{
            day = selectDay;
        }
        String hour = getSelectedHour();
        String minute = getSelectedMinute();
        //要转换的时间字符串 要和dataFormat 对应 没有秒就不加秒 不然转换报错
        if(!isOuterLabelEnable()){
            if(selectDay.contains(dayLabel)){
                selectDay = selectDay.substring(0,selectDay.lastIndexOf(dayLabel));
            }
            if(hour.contains(hourLabel)){
                hour = hour.substring(0,hour.lastIndexOf(hourLabel));
            }
            if(minute.contains(minuteLabel)){
                minute = minute.substring(0,minute.lastIndexOf(minuteLabel));
            }
        }
        String selectTime = year + "-" + month + "-" + day + " " + hour + ":" + minute;
        Date date = DateUtils.parseDate(selectTime,"yyyy-MM-dd HH:mm");
        ((OnDayTimePickListener) onDateTimePickListener).onDateTimePicked(date==null? 0 : date.getTime(),selectDay, hour, minute);
    }

    private int findItemIndex(ArrayList<String> items, int item) {
        //折半查找有序元素的索引
        int index = Collections.binarySearch(items, item, new Comparator<Object>() {
            @Override
            public int compare(Object lhs, Object rhs) {
                String lhsStr = lhs.toString();
                String rhsStr = rhs.toString();
                lhsStr = lhsStr.startsWith("0") ? lhsStr.substring(1) : lhsStr;
                rhsStr = rhsStr.startsWith("0") ? rhsStr.substring(1) : rhsStr;
                return Integer.parseInt(lhsStr) - Integer.parseInt(rhsStr);
            }
        });
        if (index < 0) {
            throw new IllegalArgumentException("Item[" + item + "] out of range");
        }
        return index;
    }
    /*
     * 初始化日历
     * */
    private void changeDayData() {
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH)+1;
        int maxDays = DateUtils.calculateDaysInMonth(year, month);
        days.clear();
//        if(startDay!=-1){
//            if(endDay!=-1){
//                for (int i = startDay; i <= endDay; i++) {
//                    days.add(DateUtils.fillZero(i));
//                }
//            }else{
//                for (int i = startDay; i <= maxDays; i++) {
//                    days.add(DateUtils.fillZero(i));
//                }
//            }
//        }else {
//            for (int i = 1; i <= maxDays; i++) {
//                days.add(DateUtils.fillZero(i));
//            }
//        }
        //24只针对添加行程获取当前时间

        boolean includeToday = driverHour <= 24;//判断能不能登上今天最晚一班车，不能的话等明天了。
        switch (preDays) {
            case 1:
                if (includeToday) {
                    days.add(dayLabels[0]);
                }
                break;
            case 2:
                if (includeToday) {
                    days.add(dayLabels[0]);
                    days.add(dayLabels[1]);
                } else {
                    days.add(dayLabels[1]);
                }
                break;
            case 3:
                if (includeToday) {
                    days.add(dayLabels[0]);
                    days.add(dayLabels[1]);
                    days.add(dayLabels[2]);
                } else {
                    days.add(dayLabels[1]);
                    days.add(dayLabels[2]);
                }
                break;
            default:
                if (includeToday) {
                    days.add(dayLabels[0]);
                    days.add(dayLabels[1]);
                    days.add(dayLabels[2]);
                } else {
                    days.add(dayLabels[1]);
                    days.add(dayLabels[2]);
                    preDays = preDays-1;
                }
                for (int i = preDays; i <= maxDays; i++) {
                    days.add(dealDayLabel(DateUtils.fillZero(i)));
                }
//                for (int i = preDays - 3; i >= 0; i--) {
//                    initCalendar();
//                    c.add(Calendar.DAY_OF_MONTH, predays - i);
//                    daysList.add(getDateFormatMMDD(c));
//                }
                break;
        }

    }
    private void initCalendar() {
        c = Calendar.getInstance(Locale.CHINA);
        if (serverTime != 0) {
            c.setTimeInMillis(serverTime);
        }
    }
    public Calendar getC(){
        if(null==c){
            initCalendar();
        }
        return c;
    }
    private void initHourData() {
        for (int i = startHour; i <= endHour; i += stepHour) {
            String hour = dealHourLabel(DateUtils.fillZero(i));
            hours.add(hour);
        }
        if (hours.indexOf(selectedHour) == -1) {
            //当前设置的小时不在指定范围，则默认选中范围开始的小时
            selectedHour = hours.get(0);
        }
    }

    private void changeMinuteData(int selectedHour) {
        if (startHour == endHour) {
            if (startMinute > endMinute) {
                int temp = startMinute;
                startMinute = endMinute;
                endMinute = temp;
            }
            for (int i = startMinute; i <= endMinute; i+= stepMinute) {
                minutes.add(dealMinLabel(DateUtils.fillZero(i)));
            }
        } else if (selectedHour == startHour) {
            for (int i = startMinute; i <= 59; i+= stepMinute) {
                minutes.add(dealMinLabel(DateUtils.fillZero(i)));
            }
        } else if (selectedHour == endHour) {
            for (int i = 0; i <= endMinute; i+= stepMinute) {
                minutes.add(dealMinLabel(DateUtils.fillZero(i)));
            }
        } else {
            for (int i = 0; i <= 59; i+= stepMinute) {
                minutes.add(dealMinLabel(DateUtils.fillZero(i)));
            }
        }
        if (minutes.indexOf(selectedMinute) == -1) {
            //当前设置的分钟不在指定范围，则默认选中范围开始的分钟
            selectedMinute = minutes.get(0);
        }
    }
    private String dealDayLabel(String itemDay){
        if(isOuterLabelEnable()){
            return itemDay;
        }
        return itemDay+dayLabel;
    }
    private String dealHourLabel(String itemHour){
        if(isOuterLabelEnable()){
            return itemHour;
        }
        return itemHour+hourLabel;
    }
    private String dealMinLabel(String itemMin){
        if(isOuterLabelEnable()){
            return itemMin;
        }
        return itemMin+minuteLabel;
    }
    public interface OnWheelListener {

        void onYearWheeled(int index, String year);

        void onMonthWheeled(int index, String month);

        void onDayWheeled(int index, String day);

        void onHourWheeled(int index, String hour);

        void onMinuteWheeled(int index, String minute);

    }

    protected interface OnDateTimePickListener {

    }

    public interface OnYearMonthDayTimePickListener extends OnDateTimePickListener {

        void onDateTimePicked(String year, String month, String day, String hour, String minute);

    }

    public interface OnYearMonthTimePickListener extends OnDateTimePickListener {

        void onDateTimePicked(String year, String month, String hour, String minute);

    }

    public interface OnMonthDayTimePickListener extends OnDateTimePickListener {

        void onDateTimePicked(String month, String day, String hour, String minute);
    }

    public interface OnDayTimePickListener extends OnDateTimePickListener {

        void onDateTimePicked(long time,String day, String hour, String minute);
    }
    public interface OnTimePickListener extends OnDateTimePickListener {

        void onDateTimePicked(long time,String hour, String minute);
    }

}
