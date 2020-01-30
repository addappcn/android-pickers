package cn.addapp.pickers.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 项目名称：UtoApp
 * 类描述：封装服务器返回的时间
 * 创建人：matt
 */
public class TimeWrapperEntity implements Parcelable {
    private long slotBegin;//开始时间
    private long slotEnd;//结束时间
    private String startTime;//HH:mm
    private String endTime;//HH:mm
    private String showTime;//用于界面显示的字符串

    public TimeWrapperEntity(long slotBegin, String startTime, long slotEnd, String endTime) {
        this.slotBegin = slotBegin;
        this.slotEnd = slotEnd;
        this.startTime = startTime;
        this.endTime = endTime;
    }
    public TimeWrapperEntity(long slotBegin, String startTime, long slotEnd, String endTime,String showTime) {
        this.slotBegin = slotBegin;
        this.slotEnd = slotEnd;
        this.startTime = startTime;
        this.endTime = endTime;
        this.showTime = showTime;
    }

    public String getShowTime() {
        return showTime;
    }

    public void setShowTime(String showTime) {
        this.showTime = showTime;
    }


    public TimeWrapperEntity(String showTime) {
        this.showTime = showTime;
    }
    public long getSlotBegin() {
        return slotBegin;
    }

    public void setSlotBegin(long slotBegin) {
        this.slotBegin = slotBegin;
    }

    public long getSlotEnd() {
        return slotEnd;
    }

    public void setSlotEnd(long slotEnd) {
        this.slotEnd = slotEnd;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.slotBegin);
        dest.writeLong(this.slotEnd);
        dest.writeString(this.startTime);
        dest.writeString(this.endTime);
        dest.writeString(this.showTime);
    }

    protected TimeWrapperEntity(Parcel in) {
        this.slotBegin = in.readLong();
        this.slotEnd = in.readLong();
        this.startTime = in.readString();
        this.endTime = in.readString();
        this.showTime = in.readString();
    }

    public static final Creator<TimeWrapperEntity> CREATOR = new Creator<TimeWrapperEntity>() {
        @Override
        public TimeWrapperEntity createFromParcel(Parcel source) {
            return new TimeWrapperEntity(source);
        }

        @Override
        public TimeWrapperEntity[] newArray(int size) {
            return new TimeWrapperEntity[size];
        }
    };
}
