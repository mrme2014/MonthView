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

    public int total_apply_number;
    public int total_full_amount;
    public int full_amount_rate;

    public List<String> date;
    public List<Integer> full_amount;
    public List<Integer> apply_number;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.total_apply_number);
        dest.writeInt(this.total_full_amount);
        dest.writeInt(this.full_amount_rate);
        dest.writeStringList(this.date);
        dest.writeList(this.full_amount);
        dest.writeList(this.apply_number);
    }

    public ChartBean() {
    }

    protected ChartBean(Parcel in) {
        this.total_apply_number = in.readInt();
        this.total_full_amount = in.readInt();
        this.full_amount_rate = in.readInt();
        this.date = in.createStringArrayList();
        this.full_amount = new ArrayList<Integer>();
        in.readList(this.full_amount, Integer.class.getClassLoader());
        this.apply_number = new ArrayList<Integer>();
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
