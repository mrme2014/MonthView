package com.ishow.ischool.bean.saleprocess;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by MrS on 2016/9/14.
 */
public class TableTotal implements Parcelable,Serializable {
    public String apply_numbers;
    public String full_amount;
    public String full_amount_rate;
    public String apply_rate;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.apply_numbers);
        dest.writeString(this.full_amount);
        dest.writeString(this.full_amount_rate);
        dest.writeString(this.apply_rate);
    }

    public TableTotal() {
    }

    protected TableTotal(Parcel in) {
        this.apply_numbers = in.readString();
        this.full_amount = in.readString();
        this.full_amount_rate = in.readString();
        this.apply_rate = in.readString();
    }

    public static final Parcelable.Creator<TableTotal> CREATOR = new Parcelable.Creator<TableTotal>() {
        @Override
        public TableTotal createFromParcel(Parcel source) {
            return new TableTotal(source);
        }

        @Override
        public TableTotal[] newArray(int size) {
            return new TableTotal[size];
        }
    };
}
