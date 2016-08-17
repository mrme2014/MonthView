package com.ishow.ischool.bean.user;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by abel on 16/8/12.
 */
public class Campus implements Parcelable {

    /**
     * id : 1
     * name : 杭州校区
     */

    public int id;
    public String name;
    public ArrayList<String> positions;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
    }

    public Campus() {
    }

    protected Campus(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
    }

    public static final Creator<Campus> CREATOR = new Creator<Campus>() {
        @Override
        public Campus createFromParcel(Parcel source) {
            return new Campus(source);
        }

        @Override
        public Campus[] newArray(int size) {
            return new Campus[size];
        }
    };
}
