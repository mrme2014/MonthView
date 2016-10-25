package com.ishow.ischool.bean.classes;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mini on 16/10/21.
 */

public class TeacherInfo implements Parcelable {
    public int id;
    public String user_name;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.user_name);
    }

    public TeacherInfo() {
    }

    protected TeacherInfo(Parcel in) {
        this.id = in.readInt();
        this.user_name = in.readString();
    }

    public static final Parcelable.Creator<TeacherInfo> CREATOR = new Parcelable.Creator<TeacherInfo>() {
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
