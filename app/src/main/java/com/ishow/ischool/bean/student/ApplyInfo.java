package com.ishow.ischool.bean.student;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by wqf on 16/8/14.
 */
public class ApplyInfo implements Parcelable {
    public int id;
    public int student_id;
    public int status;
    public int openclass_id;
    public int refund_id;
    public long refund_money;
    public int preferential_course_id;
    public long payable;
    public long cheap;
    public long practical;
    public long arrearage;
    public int is_use;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.student_id);
        dest.writeInt(this.status);
        dest.writeInt(this.openclass_id);
        dest.writeInt(this.refund_id);
        dest.writeLong(this.refund_money);
        dest.writeInt(this.preferential_course_id);
        dest.writeLong(this.payable);
        dest.writeLong(this.cheap);
        dest.writeLong(this.practical);
        dest.writeLong(this.arrearage);
        dest.writeInt(this.is_use);
    }

    public ApplyInfo() {
    }

    protected ApplyInfo(Parcel in) {
        this.id = in.readInt();
        this.student_id = in.readInt();
        this.status = in.readInt();
        this.openclass_id = in.readInt();
        this.refund_id = in.readInt();
        this.refund_money = in.readLong();
        this.preferential_course_id = in.readInt();
        this.payable = in.readLong();
        this.cheap = in.readLong();
        this.practical = in.readLong();
        this.arrearage = in.readLong();
        this.is_use = in.readInt();
    }

    public static final Creator<ApplyInfo> CREATOR = new Creator<ApplyInfo>() {
        @Override
        public ApplyInfo createFromParcel(Parcel source) {
            return new ApplyInfo(source);
        }

        @Override
        public ApplyInfo[] newArray(int size) {
            return new ApplyInfo[size];
        }
    };
}
