package com.ishow.ischool.bean.student;

import android.os.Parcel;
import android.os.Parcelable;

import com.ishow.ischool.bean.user.Avatar;

/**
 * Created by wqf on 16/8/14.
 */
public class Student implements Parcelable {
    public ApplyInfo applyInfo;
    public StudentInfo studentInfo;
    public OpenclassInfo openclassInfo;
    public StudentRelationInfo studentRelationInfo;
    public Avatar avatarInfo;


    protected Student(Parcel in) {
        applyInfo = in.readParcelable(ApplyInfo.class.getClassLoader());
        studentInfo = in.readParcelable(StudentInfo.class.getClassLoader());
        openclassInfo = in.readParcelable(OpenclassInfo.class.getClassLoader());
        studentRelationInfo = in.readParcelable(StudentRelationInfo.class.getClassLoader());
        avatarInfo = in.readParcelable(Avatar.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(applyInfo, flags);
        dest.writeParcelable(studentInfo, flags);
        dest.writeParcelable(openclassInfo, flags);
        dest.writeParcelable(studentRelationInfo, flags);
        dest.writeParcelable(avatarInfo, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Student> CREATOR = new Creator<Student>() {
        @Override
        public Student createFromParcel(Parcel in) {
            return new Student(in);
        }

        @Override
        public Student[] newArray(int size) {
            return new Student[size];
        }
    };
}
