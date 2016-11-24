package com.ishow.ischool.bean.registrationform;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by MrS on 2016/11/23.
 */

public class CheapType implements Parcelable {

                 /*"id": 95,
                "campus_id": 20,
                "name": "长期折扣2折",
                "start_time": 1476806400,
                "end_time": 1480521599,
                "preferential_type": 1,
                "preferential": 2,
                "info": "只是测试而已哈",
                "status": 1*/

    public int id;
    public int campus_id;
    public String name;
    public int start_time;
    public int end_time;
    public String preferential_type;
    public String preferential;
    public String info;
    public int status;
    public int create_time;
    public int update_time;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.campus_id);
        dest.writeString(this.name);
        dest.writeInt(this.start_time);
        dest.writeInt(this.end_time);
        dest.writeString(this.preferential_type);
        dest.writeString(this.preferential);
        dest.writeString(this.info);
        dest.writeInt(this.status);
        dest.writeInt(this.create_time);
        dest.writeInt(this.update_time);
    }

    public CheapType() {
    }

    protected CheapType(Parcel in) {
        this.id = in.readInt();
        this.campus_id = in.readInt();
        this.name = in.readString();
        this.start_time = in.readInt();
        this.end_time = in.readInt();
        this.preferential_type = in.readString();
        this.preferential = in.readString();
        this.info = in.readString();
        this.status = in.readInt();
        this.create_time = in.readInt();
        this.update_time = in.readInt();
    }

    public static final Parcelable.Creator<CheapType> CREATOR = new Parcelable.Creator<CheapType>() {
        @Override
        public CheapType createFromParcel(Parcel source) {
            return new CheapType(source);
        }

        @Override
        public CheapType[] newArray(int size) {
            return new CheapType[size];
        }
    };
}
