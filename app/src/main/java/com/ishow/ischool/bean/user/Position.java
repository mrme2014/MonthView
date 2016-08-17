package com.ishow.ischool.bean.user;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by MrS on 2016/8/17.
 */
public class Position implements Parcelable {

    /**
     * id : 10
     * title : 市场副部长
     * campus_id : 3
     */

    private int id;
    private String title;
    private int campus_id;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeInt(this.campus_id);
    }

    public Position() {
    }

    protected Position(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.campus_id = in.readInt();
    }

    public static final Parcelable.Creator<Position> CREATOR = new Parcelable.Creator<Position>() {
        @Override
        public Position createFromParcel(Parcel source) {
            return new Position(source);
        }

        @Override
        public Position[] newArray(int size) {
            return new Position[size];
        }
    };
}
