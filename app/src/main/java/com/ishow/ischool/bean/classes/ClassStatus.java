package com.ishow.ischool.bean.classes;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mini on 16/10/21.
 */

public class ClassStatus implements Parcelable {
    public int id;
    public String name;
    @SerializedName("default")
    public int defaultValue;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeInt(this.defaultValue);
    }

    public ClassStatus() {
    }

    protected ClassStatus(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.defaultValue = in.readInt();
    }

    public static final Parcelable.Creator<ClassStatus> CREATOR = new Parcelable.Creator<ClassStatus>() {
        @Override
        public ClassStatus createFromParcel(Parcel source) {
            return new ClassStatus(source);
        }

        @Override
        public ClassStatus[] newArray(int size) {
            return new ClassStatus[size];
        }
    };
}
