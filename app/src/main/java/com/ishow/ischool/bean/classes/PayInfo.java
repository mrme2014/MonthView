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


    protected PayInfo(Parcel in) {
        id = in.readInt();
        apply_id = in.readInt();
        student_id = in.readInt();
        class_cost = in.readString();
        payed = in.readString();
        arrearage = in.readString();
        teacher_id = in.readInt();
        advisor_id = in.readInt();
        pay_time = in.readInt();
        is_del = in.readInt();
        pay_info = in.readString();
        receipt_no = in.readString();
        memo = in.readString();
        cheap = in.readString();
        preferential_course_id = in.readInt();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(apply_id);
        dest.writeInt(student_id);
        dest.writeString(class_cost);
        dest.writeString(payed);
        dest.writeString(arrearage);
        dest.writeInt(teacher_id);
        dest.writeInt(advisor_id);
        dest.writeInt(pay_time);
        dest.writeInt(is_del);
        dest.writeString(pay_info);
        dest.writeString(receipt_no);
        dest.writeString(memo);
        dest.writeString(cheap);
        dest.writeInt(preferential_course_id);
    }
}
