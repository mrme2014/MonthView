package com.ishow.ischool.bean.classes;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mini on 16/10/24.
 */

public class PayInfo implements Parcelable {
    /**
     * id : 20
     * apply_id : 19
     * student_id : 21
     * class_cost : 4980.00
     * payed : 100.00
     * arrearage : 4780.00
     * teacher_id : null
     * advisor_id : 31
     * pay_time : 1473038112
     * is_del : 1
     * pay_info : [{"method":"现金","method_id":2,"account_id":0,"account":"","balance":100}]
     * receipt_no : 1
     * memo : null
     * cheap : null
     * preferential_course_id : null
     */

    private int id;
    private int apply_id;
    private int student_id;
    private String class_cost;
    private String payed;
    private String arrearage;
    private int teacher_id;
    private int advisor_id;
    private int pay_time;
    private int is_del;
    private String pay_info;
    private String receipt_no;
    private String memo;
    private String cheap;
    private int preferential_course_id;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.apply_id);
        dest.writeInt(this.student_id);
        dest.writeString(this.class_cost);
        dest.writeString(this.payed);
        dest.writeString(this.arrearage);
        dest.writeInt(this.teacher_id);
        dest.writeInt(this.advisor_id);
        dest.writeInt(this.pay_time);
        dest.writeInt(this.is_del);
        dest.writeString(this.pay_info);
        dest.writeString(this.receipt_no);
        dest.writeString(this.memo);
        dest.writeString(this.cheap);
        dest.writeInt(this.preferential_course_id);
    }

    public PayInfo() {
    }

    protected PayInfo(Parcel in) {
        this.id = in.readInt();
        this.apply_id = in.readInt();
        this.student_id = in.readInt();
        this.class_cost = in.readString();
        this.payed = in.readString();
        this.arrearage = in.readString();
        this.teacher_id = in.readInt();
        this.advisor_id = in.readInt();
        this.pay_time = in.readInt();
        this.is_del = in.readInt();
        this.pay_info = in.readString();
        this.receipt_no = in.readString();
        this.memo = in.readString();
        this.cheap = in.readString();
        this.preferential_course_id = in.readInt();
    }

    public static final Creator<PayInfo> CREATOR = new Creator<PayInfo>() {
        @Override
        public PayInfo createFromParcel(Parcel in) {
            return new PayInfo(in);
        }

        @Override
        public PayInfo[] newArray(int size) {
            return new PayInfo[size];
        }
    };


}
