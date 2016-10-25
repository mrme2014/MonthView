package com.ishow.ischool.bean.classes;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 排课时间类
 * Created by mini on 16/10/21.
 */

public class ClassTime implements Parcelable {
    public int classes_id;
    public int begin_time;
    public int end_time;
    public int week;            //周几（1-7）表示


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.classes_id);
        dest.writeInt(this.begin_time);
        dest.writeInt(this.end_time);
        dest.writeInt(this.week);
    }

    public ClassTime() {
    }

    protected ClassTime(Parcel in) {
        this.classes_id = in.readInt();
        this.begin_time = in.readInt();
        this.end_time = in.readInt();
        this.week = in.readInt();
    }

    public static final Parcelable.Creator<ClassTime> CREATOR = new Parcelable.Creator<ClassTime>() {
        @Override
        public ClassTime createFromParcel(Parcel source) {
            return new ClassTime(source);
        }

        @Override
        public ClassTime[] newArray(int size) {
            return new ClassTime[size];
        }
    };
}
