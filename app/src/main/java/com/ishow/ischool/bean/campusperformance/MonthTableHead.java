package com.ishow.ischool.bean.campusperformance;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by mini on 16/9/27.
 */

public class MonthTableHead implements Parcelable {
    public String title;
    public ArrayList<String> subtitle;
    public int monthPosition;       // 跳转到weektable时对应的数据位置


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeStringList(this.subtitle);
        dest.writeInt(this.monthPosition);
    }

    public MonthTableHead() {
    }

    protected MonthTableHead(Parcel in) {
        this.title = in.readString();
        this.subtitle = in.createStringArrayList();
        this.monthPosition = in.readInt();
    }

    public static final Creator<MonthTableHead> CREATOR = new Creator<MonthTableHead>() {
        @Override
        public MonthTableHead createFromParcel(Parcel source) {
            return new MonthTableHead(source);
        }

        @Override
        public MonthTableHead[] newArray(int size) {
            return new MonthTableHead[size];
        }
    };
}
