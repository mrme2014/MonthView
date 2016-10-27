package com.ishow.ischool.bean.classes;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mini on 16/10/21.
 */

public class TeacherInfo implements Parcelable {
    public int id;
    public String user_name;
    @SerializedName("default")
    public int defaultValue;
    public String avatar;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.user_name);
        dest.writeInt(this.defaultValue);
        dest.writeString(this.avatar);
    }

    public TeacherInfo() {
    }

    protected TeacherInfo(Parcel in) {
        this.id = in.readInt();
        this.user_name = in.readString();
        this.defaultValue = in.readInt();
        this.avatar = in.readString();
    }

    public static final Creator<TeacherInfo> CREATOR = new Creator<TeacherInfo>() {
        @Override
        public TeacherInfo createFromParcel(Parcel source) {
            return new TeacherInfo(source);
        }

        @Override
        public TeacherInfo[] newArray(int size) {
            return new TeacherInfo[size];
        }
    };
}
