package com.ishow.ischool.bean.campusperformance;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by mini on 16/9/27.
 */

public class WeekTableHead implements Parcelable {
    public String title;
    public ArrayList<String> subtitle;
    public String date;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeStringList(this.subtitle);
        dest.writeString(this.date);
    }

    public WeekTableHead() {
    }

    protected WeekTableHead(Parcel in) {
        this.title = in.readString();
        this.subtitle = in.createStringArrayList();
        this.date = in.readString();
    }

    public static final Creator<WeekTableHead> CREATOR = new Creator<WeekTableHead>() {
        @Override
        public WeekTableHead createFromParcel(Parcel source) {
            return new WeekTableHead(source);
        }

        @Override
        public WeekTableHead[] newArray(int size) {
            return new WeekTableHead[size];
        }
    };
}
