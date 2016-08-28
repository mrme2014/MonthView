package com.ishow.ischool.bean.user;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by MrS on 2016/8/28.
 */
public class CampusInfo implements Parcelable {
    public int id;
    public String name;
    public int prov_id;
    public int sort;
    public int status;
    public int create_time;

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
        dest.writeInt(this.create_time);
    }

    public CampusInfo() {
    }

    protected CampusInfo(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.prov_id = in.readInt();
        this.sort = in.readInt();
        this.status = in.readInt();
        this.create_time = in.readInt();
    }

    public static final Parcelable.Creator<CampusInfo> CREATOR = new Parcelable.Creator<CampusInfo>() {
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
