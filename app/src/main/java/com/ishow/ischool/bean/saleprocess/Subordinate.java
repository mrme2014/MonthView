package com.ishow.ischool.bean.saleprocess;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by MrS on 2016/9/23.
 */

public class Subordinate implements Parcelable {
    public  ArrayList<SubordinateObject>  Subordinate;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.Subordinate);
    }

    public Subordinate() {
    }

    protected Subordinate(Parcel in) {
        this.Subordinate = in.createTypedArrayList(SubordinateObject.CREATOR);
    }

    public static final Parcelable.Creator<Subordinate> CREATOR = new Parcelable.Creator<Subordinate>() {
        @Override
        public Subordinate createFromParcel(Parcel source) {
            return new Subordinate(source);
        }

        @Override
        public Subordinate[] newArray(int size) {
            return new Subordinate[size];
        }
    };
}
