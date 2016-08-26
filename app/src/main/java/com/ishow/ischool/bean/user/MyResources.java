package com.ishow.ischool.bean.user;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MrS on 2016/8/26.
 */
public class MyResources implements Parcelable {

    public List<Integer> myResources;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.myResources);
    }

    public MyResources() {
    }

    protected MyResources(Parcel in) {
        this.myResources = new ArrayList<Integer>();
        in.readList(this.myResources, Integer.class.getClassLoader());
    }

    public static final Parcelable.Creator<MyResources> CREATOR = new Parcelable.Creator<MyResources>() {
        @Override
        public MyResources createFromParcel(Parcel source) {
            return new MyResources(source);
        }

        @Override
        public MyResources[] newArray(int size) {
            return new MyResources[size];
        }
    };
}
