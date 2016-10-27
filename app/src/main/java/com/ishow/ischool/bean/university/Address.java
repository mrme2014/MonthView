package com.ishow.ischool.bean.university;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by abel on 16/10/25.
 */

public class Address implements Parcelable {
    public int id;//: 42,
    public int parent_id;//: 3,
    public String name;// "淮北",
    public int type;//: 2

    protected Address(Parcel in) {
        id = in.readInt();
        parent_id = in.readInt();
        name = in.readString();
        type = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(parent_id);
        dest.writeString(name);
        dest.writeInt(type);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Address> CREATOR = new Creator<Address>() {
        @Override
        public Address createFromParcel(Parcel in) {
            return new Address(in);
        }

        @Override
        public Address[] newArray(int size) {
            return new Address[size];
        }
    };
}
