package com.ishow.ischool.bean.saleprocess;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by MrS on 2016/9/14.
 */
public class TableTotal implements Parcelable,Serializable {
    public String openclass_sign_number;
    public String apply_number;
    public String full_amount_number;
    public String full_amount_rate;
    public String apply_rate;
    public String full_amount_apply_rate;
    public String referrer_full_amount_number;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.openclass_sign_number);
        dest.writeString(this.apply_number);
        dest.writeString(this.full_amount_number);
        dest.writeString(this.full_amount_rate);
        dest.writeString(this.apply_rate);
        dest.writeString(this.full_amount_apply_rate);
        dest.writeString(this.referrer_full_amount_number);
    }

    public TableTotal() {
    }

    protected TableTotal(Parcel in) {
        this.openclass_sign_number = in.readString();
        this.apply_number = in.readString();
        this.full_amount_number = in.readString();
        this.full_amount_rate = in.readString();
        this.apply_rate = in.readString();
        this.full_amount_apply_rate = in.readString();
        this.referrer_full_amount_number = in.readString();
    }

    public static final Creator<TableTotal> CREATOR = new Creator<TableTotal>() {
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
