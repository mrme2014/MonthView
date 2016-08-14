package com.ishow.ischool.bean.student;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by wqf on 16/8/14.
 */
public class OpenclassInfo implements Parcelable {
    public int id;
    public int begin_time;
    public int end_time;
    public int time_type;
    public String position;
    public int saler_id;
    public int create_time;
    public int update_time;
    public int campus_id;
    public int file_id;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.begin_time);
        dest.writeInt(this.end_time);
        dest.writeInt(this.time_type);
        dest.writeString(this.position);
        dest.writeInt(this.saler_id);
        dest.writeInt(this.create_time);
        dest.writeInt(this.update_time);
        dest.writeInt(this.campus_id);
        dest.writeInt(this.file_id);
    }

    public OpenclassInfo() {
    }

    protected OpenclassInfo(Parcel in) {
        this.id = in.readInt();
        this.begin_time = in.readInt();
        this.end_time = in.readInt();
        this.time_type = in.readInt();
        this.position = in.readString();
        this.saler_id = in.readInt();
        this.create_time = in.readInt();
        this.update_time = in.readInt();
        this.campus_id = in.readInt();
        this.file_id = in.readInt();
    }

    public static final Creator<OpenclassInfo> CREATOR = new Creator<OpenclassInfo>() {
        @Override
        public OpenclassInfo createFromParcel(Parcel source) {
            return new OpenclassInfo(source);
        }

        @Override
        public OpenclassInfo[] newArray(int size) {
            return new OpenclassInfo[size];
        }
    };
}
