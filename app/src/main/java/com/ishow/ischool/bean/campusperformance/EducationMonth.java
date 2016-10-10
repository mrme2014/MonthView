package com.ishow.ischool.bean.campusperformance;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mini on 16/10/9.
 */

public class EducationMonth implements Parcelable {

    public int campusid;
    public int full_base;
    public int full_challenge;
    public int permonth_real;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.campusid);
        dest.writeInt(this.full_base);
        dest.writeInt(this.full_challenge);
        dest.writeInt(this.permonth_real);
    }

    public EducationMonth() {
    }

    protected EducationMonth(Parcel in) {
        this.campusid = in.readInt();
        this.full_base = in.readInt();
        this.full_challenge = in.readInt();
        this.permonth_real = in.readInt();
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
