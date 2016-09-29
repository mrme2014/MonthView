package com.ishow.ischool.bean.campusperformance;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by mini on 16/9/27.
 */

public class MonthTableData implements Parcelable {
    public ArrayList<MonthTableHead> _title;
    public ArrayList<MonthTableBodyRow> _data;
    public ArrayList<Week4MonthTableHead> _week_title;
    public ArrayList<Week4MonthTableData> _week_data;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this._title);
        dest.writeList(this._data);
        dest.writeList(this._week_title);
        dest.writeList(this._week_data);
    }

    public MonthTableData() {
    }

    protected MonthTableData(Parcel in) {
        this._title = in.createTypedArrayList(MonthTableHead.CREATOR);
        this._data = new ArrayList<MonthTableBodyRow>();
        in.readList(this._data, MonthTableBodyRow.class.getClassLoader());
        this._week_title = new ArrayList<Week4MonthTableHead>();
        in.readList(this._week_title, Week4MonthTableHead.class.getClassLoader());
        this._week_data = new ArrayList<Week4MonthTableData>();
        in.readList(this._week_data, Week4MonthTableData.class.getClassLoader());
    }

    public static final Creator<MonthTableData> CREATOR = new Creator<MonthTableData>() {
        @Override
        public MonthTableData createFromParcel(Parcel source) {
            return new MonthTableData(source);
        }

        @Override
        public MonthTableData[] newArray(int size) {
            return new MonthTableData[size];
        }
    };
}
