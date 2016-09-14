package com.ishow.ischool.bean.saleprocess;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by MrS on 2016/9/14.
 */
public class Table implements Parcelable {
    public int apply_numbers;
    public int full_amount;
    public double full_amount_rate;
    public double apply_rate;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.apply_numbers);
        dest.writeInt(this.full_amount);
        dest.writeDouble(this.full_amount_rate);
        dest.writeDouble(this.apply_rate);
    }

    public Table() {
    }

    protected Table(Parcel in) {
        this.apply_numbers = in.readInt();
        this.full_amount = in.readInt();
        this.full_amount_rate = in.readDouble();
        this.apply_rate = in.readDouble();
    }

    public static final Parcelable.Creator<Table> CREATOR = new Parcelable.Creator<Table>() {
        @Override
        public Table createFromParcel(Parcel source) {
            return new Table(source);
        }

        @Override
        public Table[] newArray(int size) {
            return new Table[size];
        }
    };
}
