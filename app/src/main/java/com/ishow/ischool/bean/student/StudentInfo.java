package com.ishow.ischool.bean.student;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by MrS on 2016/7/24.
 */
public class StudentInfo implements Parcelable {

    /**
     * id : 11
     * campus_id : 1
     * province_id : 0
     * city_id : 0
     * college_id : 0
     * name : 里咯噢噢噢
     * password : 043285
     * mobile : 15555043285
     * qq : 863597280
     * major : 物联网
     * idcard : null
     * create_time : 1469330606
     * update_time : 1469330606
     * class_sate : 0
     * pay_sate : 1
     * upgrade_stae : 0
     * apply_stae : 0
     * student_id : 11
     * birthday : 0
     * grade : null
     * english_name :
     * source : 1
     * notes :
     * "college_name": "浙江大学",
     * "province_name": "浙江",
     * "referrer_id": 4,
     * "referrer_name": "王晓斌",
     * "pay_state_name": "未报名",
     * "class_hour_total": 28,
     * "class_hour": 0,
     * "payed": 0
     */

    public int id;
    public int campus_id;
    public int province_id;
    public int city_id;
    public int college_id;
    public String name;
    public String password;
    public String mobile;
    public String qq;
    public String wechat;
    public String major;
    public String idcard;
    public int create_time;
    public int update_time;
    public int class_state;
    public int pay_state;
    public int upgrade_state;
    public int apply_state;
    public int student_id;
    public int birthday;
    public String grade;
    public String english_name;
    public int source;
    public String notes;
    public String college_name;
    public String province_name;
    public int referrer_id;
    public String referrer_name;
    public String pay_state_name;
    public int class_hour_total;
    public int class_hour;
    public int payed;

    public StudentInfo() {
    }

    protected StudentInfo(Parcel in) {
        id = in.readInt();
        campus_id = in.readInt();
        province_id = in.readInt();
        city_id = in.readInt();
        college_id = in.readInt();
        name = in.readString();
        password = in.readString();
        mobile = in.readString();
        qq = in.readString();
        wechat = in.readString();
        major = in.readString();
        idcard = in.readString();
        create_time = in.readInt();
        update_time = in.readInt();
        class_state = in.readInt();
        pay_state = in.readInt();
        upgrade_state = in.readInt();
        apply_state = in.readInt();
        student_id = in.readInt();
        birthday = in.readInt();
        grade = in.readString();
        english_name = in.readString();
        source = in.readInt();
        notes = in.readString();
        college_name = in.readString();
        province_name = in.readString();
        referrer_id = in.readInt();
        referrer_name = in.readString();
        pay_state_name = in.readString();
        class_hour_total = in.readInt();
        class_hour = in.readInt();
        payed = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(campus_id);
        dest.writeInt(province_id);
        dest.writeInt(city_id);
        dest.writeInt(college_id);
        dest.writeString(name);
        dest.writeString(password);
        dest.writeString(mobile);
        dest.writeString(qq);
        dest.writeString(wechat);
        dest.writeString(major);
        dest.writeString(idcard);
        dest.writeInt(create_time);
        dest.writeInt(update_time);
        dest.writeInt(class_state);
        dest.writeInt(pay_state);
        dest.writeInt(upgrade_state);
        dest.writeInt(apply_state);
        dest.writeInt(student_id);
        dest.writeInt(birthday);
        dest.writeString(grade);
        dest.writeString(english_name);
        dest.writeInt(source);
        dest.writeString(notes);
        dest.writeString(college_name);
        dest.writeString(province_name);
        dest.writeInt(referrer_id);
        dest.writeString(referrer_name);
        dest.writeString(pay_state_name);
        dest.writeInt(class_hour_total);
        dest.writeInt(class_hour);
        dest.writeInt(payed);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<StudentInfo> CREATOR = new Creator<StudentInfo>() {
        @Override
        public StudentInfo createFromParcel(Parcel in) {
            return new StudentInfo(in);
        }

        @Override
        public StudentInfo[] newArray(int size) {
            return new StudentInfo[size];
        }
    };
}
