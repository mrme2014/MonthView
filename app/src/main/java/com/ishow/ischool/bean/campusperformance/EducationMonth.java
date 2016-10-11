package com.ishow.ischool.bean.campusperformance;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mini on 16/10/9.
 */

public class EducationMonth implements Parcelable {

    public Integer campusid;
    public String full_base;
    public String full_challenge;
    public String permonth_real;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.campusid);
        dest.writeString(this.full_base);
        dest.writeString(this.full_challenge);
        dest.writeString(this.permonth_real);
    }

    public EducationMonth() {
    }

    protected EducationMonth(Parcel in) {
        this.campusid = in.readInt();
        this.full_base = in.readString();
        this.full_challenge = in.readString();
        this.permonth_real = in.readString();
    }

    public static final Creator<EducationMonth> CREATOR = new Creator<EducationMonth>() {
        @Override
        public EducationMonth createFromParcel(Parcel source) {
            return new EducationMonth(source);
        }

        @Override
        public EducationMonth[] newArray(int size) {
            return new EducationMonth[size];
        }
    };
}
