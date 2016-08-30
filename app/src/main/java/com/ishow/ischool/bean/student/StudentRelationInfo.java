package com.ishow.ischool.bean.student;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * guider_id	Int	0			晨读讲师id	0
 * school_chat_attache_id	Int	0			校聊专员id	0
 * school_chat_charge_id	Int	0			校聊主管id	0
 * student_id	Int	0			学员id	0
 * referrer_id	Int	0			推荐人id	0
 * campus_manager_id	Int	0			校园经理id	0
 * charge_id	Int	0			主管id	0
 * saler_id	Int	0			销讲师id	0
 * advisor_id	Int	0			课程顾问id	0
 * Created by wqf on 16/8/14.
 */
public class StudentRelationInfo implements Parcelable {

    public int guider_id;
    public int school_chat_attache_id;
    public int school_chat_charge_id;
    public int student_id;
    public int referrer_id;
    public int campus_manager_id;
    public int charge_id;
    public int saler_id;
    public int advisor_id;


    protected StudentRelationInfo(Parcel in) {
        guider_id = in.readInt();
        school_chat_attache_id = in.readInt();
        school_chat_charge_id = in.readInt();
        student_id = in.readInt();
        referrer_id = in.readInt();
        campus_manager_id = in.readInt();
        charge_id = in.readInt();
        saler_id = in.readInt();
        advisor_id = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(guider_id);
        dest.writeInt(school_chat_attache_id);
        dest.writeInt(school_chat_charge_id);
        dest.writeInt(student_id);
        dest.writeInt(referrer_id);
        dest.writeInt(campus_manager_id);
        dest.writeInt(charge_id);
        dest.writeInt(saler_id);
        dest.writeInt(advisor_id);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<StudentRelationInfo> CREATOR = new Creator<StudentRelationInfo>() {
        @Override
        public StudentRelationInfo createFromParcel(Parcel in) {
            return new StudentRelationInfo(in);
        }

        @Override
        public StudentRelationInfo[] newArray(int size) {
            return new StudentRelationInfo[size];
        }
    };
}
