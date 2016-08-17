package com.ishow.ischool.bean.student;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by wqf on 16/8/14.
 */
public class StudentStatistics implements Parcelable {
    public ApplyInfo applyInfo;
    public StudentInfo studentInfo;
    public OpenclassInfo openclassInfo;
    public StudentRelationInfo studentRelationInfo;

    protected StudentStatistics(Parcel in) {
        applyInfo = in.readParcelable(ApplyInfo.class.getClassLoader());
        studentInfo = in.readParcelable(StudentInfo.class.getClassLoader());
        openclassInfo = in.readParcelable(OpenclassInfo.class.getClassLoader());
        studentRelationInfo = in.readParcelable(StudentRelationInfo.class.getClassLoader());
    }

    public static final Creator<StudentStatistics> CREATOR = new Creator<StudentStatistics>() {
        @Override
        public StudentStatistics createFromParcel(Parcel in) {
            return new StudentStatistics(in);
        }

        @Override
        public StudentStatistics[] newArray(int size) {
            return new StudentStatistics[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(applyInfo, i);
        parcel.writeParcelable(studentInfo, i);
        parcel.writeParcelable(openclassInfo, i);
        parcel.writeParcelable(studentRelationInfo, i);
    }
}
