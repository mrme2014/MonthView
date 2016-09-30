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
    public String avatar;
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
        dest.writeString(this.avatar);
        dest.writeInt(this.id);
        dest.writeInt(this.defaultX);
    }

    public SubordinateObject() {
    }

    protected SubordinateObject(Parcel in) {
        this.user_name = in.readString();
        this.avatar = in.readString();
        this.id = in.readInt();
        this.defaultX = in.readInt();
    }

    public static final Creator<SubordinateObject> CREATOR = new Creator<SubordinateObject>() {
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
