package com.ishow.ischool.bean.campusperformance;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by wqf on 16/9/19.
 */
public class SignPerformance implements Parcelable {

    public int campusid;
    public String perweek_full_base;
    public String perweek_full_challenge;
    public String perweek_real;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.campusid);
        dest.writeString(this.perweek_full_base);
        dest.writeString(this.perweek_full_challenge);
        dest.writeString(this.perweek_real);
    }

    public SignPerformance() {
    }

    protected SignPerformance(Parcel in) {
        this.campusid = in.readInt();
        this.perweek_full_base = in.readString();
        this.perweek_full_challenge = in.readString();
        this.perweek_real = in.readString();
    }

    public static final Parcelable.Creator<SignPerformance> CREATOR = new Parcelable.Creator<SignPerformance>() {
        @Override
        public SignPerformance createFromParcel(Parcel source) {
            return new SignPerformance(source);
        }

        @Override
        public SignPerformance[] newArray(int size) {
            return new SignPerformance[size];
        }
    };
}
