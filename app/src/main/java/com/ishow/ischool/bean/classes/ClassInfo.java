package com.ishow.ischool.bean.classes;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * 班级信息类
 * Created by mini on 16/10/21.
 */

public class ClassInfo implements Parcelable {
    @SerializedName("teacher")          //上课老师id
    public int teacherId;
    @SerializedName("advisor")          //学习顾问id
    public int advisorId;
    public String teacher_name;         //上课老师名字
    public String advisor_name;         //学习顾问名字
    public String open_date;            //开班日期
    public String location;             //教室地点
    public int teaching_time;           //课时数
    public String create_time;
    public String update_time;
    public String course_type;            //课程类型标题
    public int id;
    public int campus_id;
    public String name;
    public int type;                    //课程类型。1 ishow初级,10 ishow中级,20 ishow高级,30 ishow影视
    public int numbers;                 //开班人数
    public int current_numbers;         //当前人数
    public int status;                  //班级状态 1 未 2 中 3已
    public int lesson_times;            //所有课时
    public int lessoned_times;          //当前课时
    public ArrayList<ClassTimeSlot> timeslot;                 //上课时间段
    public String nextClassTime;        //下节课时间


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.teacherId);
        dest.writeInt(this.advisorId);
        dest.writeString(this.teacher_name);
        dest.writeString(this.advisor_name);
        dest.writeString(this.open_date);
        dest.writeString(this.location);
        dest.writeInt(this.teaching_time);
        dest.writeString(this.create_time);
        dest.writeString(this.update_time);
        dest.writeString(this.course_type);
        dest.writeInt(this.id);
        dest.writeInt(this.campus_id);
        dest.writeString(this.name);
        dest.writeInt(this.type);
        dest.writeInt(this.numbers);
        dest.writeInt(this.status);
        dest.writeInt(this.lesson_times);
        dest.writeInt(this.current_numbers);
        dest.writeInt(this.lessoned_times);
        dest.writeList(this.timeslot);
        dest.writeString(this.nextClassTime);
    }

    public ClassInfo() {
    }

    protected ClassInfo(Parcel in) {
        this.teacherId = in.readInt();
        this.advisorId = in.readInt();
        this.teacher_name = in.readString();
        this.advisor_name = in.readString();
        this.open_date = in.readString();
        this.location = in.readString();
        this.teaching_time = in.readInt();
        this.create_time = in.readString();
        this.update_time = in.readString();
        this.course_type = in.readString();
        this.id = in.readInt();
        this.campus_id = in.readInt();
        this.name = in.readString();
        this.type = in.readInt();
        this.numbers = in.readInt();
        this.status = in.readInt();
        this.lesson_times = in.readInt();
        this.current_numbers = in.readInt();
        this.lessoned_times = in.readInt();
        this.timeslot = new ArrayList<ClassTimeSlot>();
        in.readList(this.timeslot, ClassTimeSlot.class.getClassLoader());
        this.nextClassTime = in.readString();
    }

    public static final Creator<ClassInfo> CREATOR = new Creator<ClassInfo>() {
        @Override
        public ClassInfo createFromParcel(Parcel source) {
            return new ClassInfo(source);
        }

        @Override
        public ClassInfo[] newArray(int size) {
            return new ClassInfo[size];
        }
    };
}
