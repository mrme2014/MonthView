package com.ishow.ischool.bean.saleprocess;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by MrS on 2016/9/14.
 */
public class TableTotal implements Parcelable,Serializable {
    public int apply_numbers;
    public int full_amount;
    public int full_amount_rate;
    public int apply_rate;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.apply_numbers);
        dest.writeInt(this.full_amount);
        dest.writeInt(this.full_amount_rate);
        dest.writeInt(this.apply_rate);
    }

    public TableTotal() {
    }

    protected TableTotal(Parcel in) {
        this.apply_numbers = in.readInt();
        this.full_amount = in.readInt();
        this.full_amount_rate = in.readInt();
        this.apply_rate = in.readInt();
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
