package com.ishow.ischool.bean.classes;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 课程时间段类
 * Created by mini on 16/10/24.
 */

public class ClassTimeSlot implements Parcelable {
    public String start_time;
    public String end_time;
    public int week;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.start_time);
        dest.writeString(this.end_time);
        dest.writeInt(this.week);
    }

    public ClassTimeSlot() {
    }

    protected ClassTimeSlot(Parcel in) {
        this.start_time = in.readString();
        this.end_time = in.readString();
        this.week = in.readInt();
    }

    public static final Parcelable.Creator<ClassTimeSlot> CREATOR = new Parcelable.Creator<ClassTimeSlot>() {
        @Override
        public ClassTimeSlot createFromParcel(Parcel source) {
            return new ClassTimeSlot(source);
        }

        @Override
        public ClassTimeSlot[] newArray(int size) {
            return new ClassTimeSlot[size];
        }
    };
}
