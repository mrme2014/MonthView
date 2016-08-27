package com.ishow.ischool.bean.university;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by wqf on 16/8/16.
 */
public class UniversityInfo implements Parcelable {
    public int id;
    public String name;
    public int prov_id;
    public int city_id;
    public int status;
    public long create_time;
    public String prov;
    public String city;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeInt(this.prov_id);
        dest.writeInt(this.city_id);
        dest.writeInt(this.status);
        dest.writeLong(this.create_time);
        dest.writeString(this.prov);
        dest.writeString(this.city);
    }

    public UniversityInfo() {
    }

    protected UniversityInfo(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.prov_id = in.readInt();
        this.city_id = in.readInt();
        this.status = in.readInt();
        this.create_time = in.readLong();
        this.prov = in.readString();
        this.city = in.readString();
    }

    public static final Creator<UniversityInfo> CREATOR = new Creator<UniversityInfo>() {
        @Override
        public UniversityInfo createFromParcel(Parcel source) {
            return new UniversityInfo(source);
        }

        @Override
        public UniversityInfo[] newArray(int size) {
            return new UniversityInfo[size];
        }
    };
}
