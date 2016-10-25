package com.ishow.ischool.bean.classes;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 班级状态类
 * Created by mini on 16/10/21.
 */

public class ClassDynamic implements Parcelable {
    public int checkin_status;          //0是今天未，1是今天已签到


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.checkin_status);
    }

    public ClassDynamic() {
    }

    protected ClassDynamic(Parcel in) {
        this.checkin_status = in.readInt();
    }

    public static final Parcelable.Creator<ClassDynamic> CREATOR = new Parcelable.Creator<ClassDynamic>() {
        @Override
        public ClassDynamic createFromParcel(Parcel source) {
            return new ClassDynamic(source);
        }

        @Override
        public ClassDynamic[] newArray(int size) {
            return new ClassDynamic[size];
        }
    };
}
