package com.ishow.ischool.bean.user;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by MrS on 2016/8/17.
 */
public class PositionInfo implements Parcelable {

    /**
     * id : 10
     * title : 市场副部长
     */

    public int id;
    public String title;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
    }

    public PositionInfo() {
    }

    protected PositionInfo(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
    }

    public static final Parcelable.Creator<PositionInfo> CREATOR = new Parcelable.Creator<PositionInfo>() {
        @Override
        public PositionInfo createFromParcel(Parcel source) {
            return new PositionInfo(source);
        }

        @Override
        public PositionInfo[] newArray(int size) {
            return new PositionInfo[size];
        }
    };
}
