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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.campus_id);
        dest.writeInt(this.province_id);
        dest.writeInt(this.city_id);
        dest.writeInt(this.college_id);
        dest.writeString(this.name);
        dest.writeString(this.password);
        dest.writeString(this.mobile);
        dest.writeString(this.qq);
        dest.writeString(this.major);
        dest.writeString(this.idcard);
        dest.writeInt(this.create_time);
        dest.writeInt(this.update_time);
        dest.writeInt(this.class_state);
        dest.writeInt(this.pay_state);
        dest.writeInt(this.upgrade_state);
        dest.writeInt(this.apply_state);
        dest.writeInt(this.student_id);
        dest.writeInt(this.birthday);
        dest.writeString(this.grade);
        dest.writeString(this.english_name);
        dest.writeInt(this.source);
        dest.writeString(this.notes);
    }

    public StudentInfo() {
    }

    protected StudentInfo(Parcel in) {
        this.id = in.readInt();
        this.campus_id = in.readInt();
        this.province_id = in.readInt();
        this.city_id = in.readInt();
        this.college_id = in.readInt();
        this.name = in.readString();
        this.password = in.readString();
        this.mobile = in.readString();
        this.qq = in.readString();
        this.major = in.readString();
        this.idcard = in.readString();
        this.create_time = in.readInt();
        this.update_time = in.readInt();
        this.class_state = in.readInt();
        this.pay_state = in.readInt();
        this.upgrade_state = in.readInt();
        this.apply_state = in.readInt();
        this.student_id = in.readInt();
        this.birthday = in.readInt();
        this.grade = in.readString();
        this.english_name = in.readString();
        this.source = in.readInt();
        this.notes = in.readString();
    }

    public static final Creator<StudentInfo> CREATOR = new Creator<StudentInfo>() {
        @Override
        public StudentInfo createFromParcel(Parcel source) {
            return new StudentInfo(source);
        }

        @Override
        public StudentInfo[] newArray(int size) {
            return new StudentInfo[size];
        }
    };


    @Override
    public String toString() {
        return "studentinfo{" +
                "id=" + id +
                ", campus_id=" + campus_id +
                ", province_id=" + province_id +
                ", city_id=" + city_id +
                ", college_id=" + college_id +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", mobile='" + mobile + '\'' +
                ", qq='" + qq + '\'' +
                ", major='" + major + '\'' +
                ", idcard=" + idcard +
                ", create_time=" + create_time +
                ", update_time=" + update_time +
                ", class_sate=" + class_state +
                ", pay_sate=" + pay_state +
                ", upgrade_stae=" + upgrade_state +
                ", apply_stae=" + apply_state +
                ", student_id=" + student_id +
                ", birthday=" + birthday +
                ", grade=" + grade +
                ", english_name='" + english_name + '\'' +
                ", source=" + source +
                ", notes='" + notes + '\'' +
                '}';
    }
}
