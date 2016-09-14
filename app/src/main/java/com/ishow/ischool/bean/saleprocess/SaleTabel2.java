package com.ishow.ischool.bean.saleprocess;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by MrS on 2016/9/14.
 */
public class SaleTabel2 implements Parcelable {
    public Table table;
    public List<String> tablehead;
    public List<TableBody> tablebody;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.table, flags);
        dest.writeStringList(this.tablehead);
        dest.writeTypedList(this.tablebody);
    }

    public SaleTabel2() {
    }

    protected SaleTabel2(Parcel in) {
        this.table = in.readParcelable(Table.class.getClassLoader());
        this.tablehead = in.createStringArrayList();
        this.tablebody = in.createTypedArrayList(TableBody.CREATOR);
    }

    public static final Parcelable.Creator<SaleTabel2> CREATOR = new Parcelable.Creator<SaleTabel2>() {
        @Override
        public SaleTabel2 createFromParcel(Parcel source) {
            return new SaleTabel2(source);
        }

        @Override
        public SaleTabel2[] newArray(int size) {
            return new SaleTabel2[size];
        }
    };
}
