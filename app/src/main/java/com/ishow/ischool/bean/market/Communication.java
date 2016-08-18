package com.ishow.ischool.bean.market;

import android.os.Parcel;
import android.os.Parcelable;

import com.ishow.ischool.bean.student.StudentInfo;
import com.ishow.ischool.bean.user.UserInfo;

/**
 * Created by abel on 16/8/15.
 */
public class Communication implements Parcelable {
    public CommunicationInfo communicationInfo;//	CommunicationInfo	1				0
    public StudentInfo studentInfo;//		1			学生信息	0
    public UserInfo userInfo;//	UserInfo

    protected Communication(Parcel in) {
        communicationInfo = in.readParcelable(CommunicationInfo.class.getClassLoader());
        studentInfo = in.readParcelable(StudentInfo.class.getClassLoader());
        userInfo = in.readParcelable(UserInfo.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(communicationInfo, flags);
        dest.writeParcelable(studentInfo, flags);
        dest.writeParcelable(userInfo, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Communication> CREATOR = new Creator<Communication>() {
        @Override
        public Communication createFromParcel(Parcel in) {
            return new Communication(in);
        }

        @Override
        public Communication[] newArray(int size) {
            return new Communication[size];
        }
    };
}
