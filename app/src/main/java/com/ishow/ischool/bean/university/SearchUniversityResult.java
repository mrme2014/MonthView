package com.ishow.ischool.bean.university;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by wqf on 16/8/16.
 */
public class SearchUniversityResult implements Parcelable {
    public int total;
    public int page;
    public int pagesize;
    public ArrayList<UniversityInfo> list;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.total);
        dest.writeInt(this.page);
        dest.writeInt(this.pagesize);
        dest.writeTypedList(this.list);
    }

    public SearchUniversityResult() {
    }

    protected SearchUniversityResult(Parcel in) {
        this.total = in.readInt();
        this.page = in.readInt();
        this.pagesize = in.readInt();
        this.list = in.createTypedArrayList(UniversityInfo.CREATOR);
    }

    public static final Creator<SearchUniversityResult> CREATOR = new Creator<SearchUniversityResult>() {
        @Override
        public SearchUniversityResult createFromParcel(Parcel source) {
            return new SearchUniversityResult(source);
        }

        @Override
        public SearchUniversityResult[] newArray(int size) {
            return new SearchUniversityResult[size];
        }
    };
}
