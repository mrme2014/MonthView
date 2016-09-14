package com.ishow.ischool.bean.saleprocess;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by MrS on 2016/9/13.
 */
public class ChartBean implements Parcelable {

    public List<String> date;
    public List<String> full_amount;
    public List<String> apply_number;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(this.date);
        dest.writeStringList(this.full_amount);
        dest.writeStringList(this.apply_number);
    }

    public ChartBean() {
    }

    protected ChartBean(Parcel in) {
        this.date = in.createStringArrayList();
        this.full_amount = in.createStringArrayList();
        this.apply_number = in.createStringArrayList();
    }

    public static final Parcelable.Creator<ChartBean> CREATOR = new Parcelable.Creator<ChartBean>() {
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
