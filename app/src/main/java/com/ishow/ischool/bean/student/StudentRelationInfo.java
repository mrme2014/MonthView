package com.ishow.ischool.bean.student;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by wqf on 16/8/14.
 */
public class StudentRelationInfo implements Parcelable {
    public int student_id;
    public int referrer_id;
    public int campus_manager_id;
    public int charge_id;
    public int saler_id;
    public int advisor_id;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.student_id);
        dest.writeInt(this.referrer_id);
        dest.writeInt(this.campus_manager_id);
        dest.writeInt(this.charge_id);
        dest.writeInt(this.saler_id);
        dest.writeInt(this.advisor_id);
    }

    public StudentRelationInfo() {
    }

    protected StudentRelationInfo(Parcel in) {
        this.student_id = in.readInt();
        this.referrer_id = in.readInt();
        this.campus_manager_id = in.readInt();
        this.charge_id = in.readInt();
        this.saler_id = in.readInt();
        this.advisor_id = in.readInt();
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
