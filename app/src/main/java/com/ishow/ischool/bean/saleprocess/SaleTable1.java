package com.ishow.ischool.bean.saleprocess;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by MrS on 2016/9/14.
 */
public class SaleTable1 implements Parcelable {
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

    public SaleTable1() {
    }

    protected SaleTable1(Parcel in) {
        this.table = in.readParcelable(Table.class.getClassLoader());
        this.tablehead = in.createStringArrayList();
        this.tablebody = in.createTypedArrayList(TableBody.CREATOR);
    }

    public static final Parcelable.Creator<SaleTable1> CREATOR = new Parcelable.Creator<SaleTable1>() {
        @Override
        public SaleTable1 createFromParcel(Parcel source) {
            return new SaleTable1(source);
        }

        @Override
        public SaleTable1[] newArray(int size) {
            return new SaleTable1[size];
        }
    };
}
