package com.ishow.ischool.bean.student;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by wqf on 16/10/25.
 */
public class StudentRelationInfo implements Parcelable {

    public BaseUserInfo referrer_id;
    public BaseUserInfo saler_id;
    public BaseUserInfo advisor_id;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.referrer_id, flags);
        dest.writeParcelable(this.saler_id, flags);
        dest.writeParcelable(this.advisor_id, flags);
    }

    public StudentRelationInfo() {
    }

    protected StudentRelationInfo(Parcel in) {
        this.referrer_id = in.readParcelable(BaseUserInfo.class.getClassLoader());
        this.saler_id = in.readParcelable(BaseUserInfo.class.getClassLoader());
        this.advisor_id = in.readParcelable(BaseUserInfo.class.getClassLoader());
    }

    public static final Creator<StudentRelationInfo> CREATOR = new Creator<StudentRelationInfo>() {
        @Override
        public StudentRelationInfo createFromParcel(Parcel source) {
            return new StudentRelationInfo(source);
        }

        @Override
        public StudentRelationInfo[] newArray(int size) {
            return new StudentRelationInfo[size];
        }
    };
}
