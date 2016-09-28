package com.ishow.ischool.bean.user;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mrs on 16/8/22.
 */
public class CampusInfo implements Parcelable {
    public Integer id;
    public String name;
    public int prov_id;
    public int sort;
    public int status;
    public long create_time;
    public String prov;


    public CampusInfo(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeInt(this.prov_id);
        dest.writeInt(this.sort);
        dest.writeInt(this.status);
        dest.writeLong(this.create_time);
        dest.writeString(this.prov);
    }

    public CampusInfo() {
    }

    protected CampusInfo(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.prov_id = in.readInt();
        this.sort = in.readInt();
        this.status = in.readInt();
        this.create_time = in.readLong();
        this.prov = in.readString();
    }

    public static final Creator<CampusInfo> CREATOR = new Creator<CampusInfo>() {
        @Override
        public CampusInfo createFromParcel(Parcel source) {
            return new CampusInfo(source);
        }

        @Override
        public CampusInfo[] newArray(int size) {
            return new CampusInfo[size];
        }
    };
}
