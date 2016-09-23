package com.ishow.ischool.bean.saleprocess;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by MrS on 2016/9/23.
 */

public class SubordinateObject implements Parcelable {

    /**
     * user_name : 高燕A晨读讲师1
     * id : 65
     * default : 0
     */

    public String user_name;
    public String file_name;
    public int id;
    @SerializedName("default")
    public int defaultX;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.user_name);
        dest.writeInt(this.id);
        dest.writeInt(this.defaultX);
    }

    public SubordinateObject() {
    }

    protected SubordinateObject(Parcel in) {
        this.user_name = in.readString();
        this.id = in.readInt();
        this.defaultX = in.readInt();
    }

    public static final Parcelable.Creator<SubordinateObject> CREATOR = new Parcelable.Creator<SubordinateObject>() {
        @Override
        public SubordinateObject createFromParcel(Parcel source) {
            return new SubordinateObject(source);
        }

        @Override
        public SubordinateObject[] newArray(int size) {
            return new SubordinateObject[size];
        }
    };
}
