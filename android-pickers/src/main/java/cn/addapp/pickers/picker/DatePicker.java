package cn.addapp.pickers.picker;

import android.app.Activity;

/**
 * 日期选择器
 * @author matt
 * blog: addapp.cn
 */
public class DatePicker extends DateTimePicker {

    public DatePicker(Activity activity) {
        this(activity, YEAR_MONTH_DAY);
    }

    /**
     * @see #YEAR_MONTH_DAY
     * @see #YEAR_MONTH
     * @see #MONTH_DAY
     */
    public DatePicker(Activity activity, @DateMode int mode) {
        super(activity, mode, NONE);
    }

    /**
     * 设置年月日的单位
     */
    public void setLabel(String yearLabel, String monthLabel, String dayLabel) {
        super.setLabel(yearLabel, monthLabel, dayLabel, "", "");
    }

    /**
     * 设置范围：开始的年月日
     */
    public void setRangeStart(int startYear, int startMonth, int startDay) {
        super.setDateRangeStart(startYear, startMonth, startDay);
    }

    /**
     * 设置范围：结束的年月日
     */
    public void setRangeEnd(int endYear, int endMonth, int endDay) {
        super.setDateRangeEnd(endYear, endMonth, endDay);
    }

    /**
     * 设置范围：开始的年月日
     */
    public void setRangeStart(int startYearOrMonth, int startMonthOrDay) {
        super.setDateRangeStart(startYearOrMonth, startMonthOrDay);
    }

    /**
     * 设置范围：结束的年月日
     */
    public void setRangeEnd(int endYearOrMonth, int endMonthOrDay) {
        super.setDateRangeEnd(endYearOrMonth, endMonthOrDay);
    }



    /**
     * 设置默认选中的年月日
     */
    public void setSelectedItem(int year, int month, int day) {
        super.setSelectedItem(year, month, day, 0, 0);
    }

    /**
     * 设置默认选中的年月或者月日
     */
    public void setSelectedItem(int yearOrMonth, int monthOrDay) {
        super.setSelectedItem(yearOrMonth, monthOrDay, 0, 0);
    }


    public void setOnWheelListener(final OnWheelListener listener) {
        if (null == listener) {
            return;
        }
        super.setOnWheelListener(new DateTimePicker.OnWheelListener() {
            @Override
            public void onYearWheeled(int index, String year) {
                listener.onYearWheeled(index, year);
            }

            @Override
            public void onMonthWheeled(int index, String month) {
                listener.onMonthWheeled(index, month);
            }

            @Override
            public void onDayWheeled(int index, String day) {
                listener.onDayWheeled(index, day);
            }

            @Override
            public void onHourWheeled(int index, String hour) {

            }

            @Override
            public void onMinuteWheeled(int index, String minute) {

            }
        });
    }

    public void setOnDatePickListener(final OnDatePickListener listener) {
        if (null == listener) {
            return;
        }
        if (listener instanceof OnYearMonthDayPickListener) {
            super.setOnDateTimePickListener(new OnYearMonthDayTimePickListener() {
                @Override
                public void onDateTimePicked(String year, String month, String day, String hour, String minute) {
                    ((OnYearMonthDayPickListener) listener).onDatePicked(year, month, day);
                }
            });
        } else if (listener instanceof OnYearMonthPickListener) {
            super.setOnDateTimePickListener(new OnYearMonthTimePickListener() {
                @Override
                public void onDateTimePicked(String year, String month, String hour, String minute) {
                    ((OnYearMonthPickListener) listener).onDatePicked(year, month);
                }
            });
        } else if (listener instanceof OnMonthDayPickListener) {
            super.setOnDateTimePickListener(new OnMonthDayTimePickListener() {
                @Override
                public void onDateTimePicked(String month, String day, String hour, String minute) {
                    ((OnMonthDayPickListener) listener).onDatePicked(month, day);
                }
            });
        }
    }

    protected interface OnDatePickListener {

    }

    public interface OnYearMonthDayPickListener extends OnDatePickListener {

        void onDatePicked(String year, String month, String day);

    }

    public interface OnYearMonthPickListener extends OnDatePickListener {

        void onDatePicked(String year, String month);

    }

    public interface OnMonthDayPickListener extends OnDatePickListener {

        void onDatePicked(String month, String day);

    }

    public interface OnWheelListener {

        void onYearWheeled(int index, String year);

        void onMonthWheeled(int index, String month);

        void onDayWheeled(int index, String day);

    }

}
