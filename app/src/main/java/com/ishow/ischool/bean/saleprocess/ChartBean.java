package com.ishow.ischool.bean.saleprocess;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by MrS on 2016/9/13.
 */
public class ChartBean implements Parcelable ,Serializable{

    public String total_apply_number;
    public String total_full_amount;
    public String full_amount_rate;

    public List<String> date;
    public List<String> full_amount;
    public List<String> apply_number;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.total_apply_number);
        dest.writeString(this.total_full_amount);
        dest.writeString(this.full_amount_rate);
        dest.writeStringList(this.date);
        dest.writeList(this.full_amount);
        dest.writeList(this.apply_number);
    }

    public ChartBean() {
    }

    protected ChartBean(Parcel in) {
        this.total_apply_number = in.readString();
        this.total_full_amount = in.readString();
        this.full_amount_rate = in.readString();
        this.date = in.createStringArrayList();
        this.full_amount = new ArrayList<String>();
        in.readList(this.full_amount, Integer.class.getClassLoader());
        this.apply_number = new ArrayList<String>();
        in.readList(this.apply_number, Integer.class.getClassLoader());
    }

    public static final Creator<ChartBean> CREATOR = new Creator<ChartBean>() {
        @Override
        public ChartBean createFromParcel(Parcel source) {
            return new ChartBean(source);
        }

        @Override
        public ChartBean[] newArray(int size) {
            return new ChartBean[size];
        }
    };
}
