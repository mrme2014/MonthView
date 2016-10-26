package com.ishow.ischool.bean.student;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mini on 16/10/25.
 */

public class BaseUserInfo implements Parcelable {
    public String user_name;
    public String user_id;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.user_name);
        dest.writeString(this.user_id);
    }

    public BaseUserInfo() {
    }

    protected BaseUserInfo(Parcel in) {
        this.user_name = in.readString();
        this.user_id = in.readString();
    }

    public static final Parcelable.Creator<BaseUserInfo> CREATOR = new Parcelable.Creator<BaseUserInfo>() {
        @Override
        public BaseUserInfo createFromParcel(Parcel source) {
            return new BaseUserInfo(source);
        }

        @Override
        public BaseUserInfo[] newArray(int size) {
            return new BaseUserInfo[size];
        }
    };
}
