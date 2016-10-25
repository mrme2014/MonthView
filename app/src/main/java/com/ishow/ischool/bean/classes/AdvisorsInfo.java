package com.ishow.ischool.bean.classes;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 学习顾问类
 * Created by mini on 16/10/21.
 */

public class AdvisorsInfo implements Parcelable {
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

    public AdvisorsInfo() {
    }

    protected AdvisorsInfo(Parcel in) {
        this.id = in.readInt();
        this.user_name = in.readString();
    }

    public static final Parcelable.Creator<AdvisorsInfo> CREATOR = new Parcelable.Creator<AdvisorsInfo>() {
        @Override
        public AdvisorsInfo createFromParcel(Parcel source) {
            return new AdvisorsInfo(source);
        }

        @Override
        public AdvisorsInfo[] newArray(int size) {
            return new AdvisorsInfo[size];
        }
    };
}
