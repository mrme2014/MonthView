package com.ishow.ischool.bean.classes;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mini on 16/10/21.
 */

public class CoursetypeInfo implements Parcelable {
    public int id;
    public String name;
    @SerializedName("default")
    public int defaultValue;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeInt(this.defaultValue);
    }

    public CoursetypeInfo() {
    }

    protected CoursetypeInfo(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.defaultValue = in.readInt();
    }

    public static final Creator<CoursetypeInfo> CREATOR = new Creator<CoursetypeInfo>() {
        @Override
        public CoursetypeInfo createFromParcel(Parcel source) {
            return new CoursetypeInfo(source);
        }

        @Override
        public CoursetypeInfo[] newArray(int size) {
            return new CoursetypeInfo[size];
        }
    };
}
