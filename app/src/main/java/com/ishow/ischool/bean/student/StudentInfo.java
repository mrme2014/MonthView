package com.ishow.ischool.bean.student;

import android.os.Parcel;
import android.os.Parcelable;

import com.ishow.ischool.bean.classes.PayInfo;
import com.ishow.ischool.bean.market.CommunicationInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MrS on 2016/7/24.
 */
public class StudentInfo implements Parcelable {



    /**
     * id : 21
     * campus_id : 4
     * province_id : 31
     * city_id : 383
     * college_id : 3180
     * name : 麦兜
     * password : 353ffa299141a1d44617
     * mobile : 18678367654
     * wechat : 18678367654
     * qq :
     * major : 英语
     * idcard : 330683199010256116
     * create_time : 1472888522
     * update_time : 1472888522
     * class_state : 1
     * pay_state : 2
     * upgrade_state : 0
     * apply_state : 1
     * course_type : 0
     * is_sgin : 1
     * avatar : 0
     * student_id : 21
     * birthday : 0
     * grade : 0
     * english_name :
     * source : 1
     * app_permission : -1
     * notes :
     * entering_school_year : 0
     * hometown_pid : 0
     * hometown_cid : 0
     * parents_call :
     * intention_class : 0
     * intention_time : 0
     * college_name : 浙江大学
     * province_name : 浙江
     * city_name : 杭州
     * hometown_pid_name :
     * hometown_cid_name :
     * source_name : 晨读
     * guider_id : 33
     * guider_name : 张志慧08晨读讲师1
     * campus_manager_id : 30
     * campus_manager_name : 张志慧05校园经理1
     * charge_id : 27
     * charge_name : 张志慧02市场主管1
     * saler_id : 31
     * saler_name : 张志慧06销讲师1
     * advisor_id : 31
     * advisor_name : 张志慧06销讲师1
     * pay_state_name : 欠款
     * class_hour_total : 28
     * class_hour : 0
     * payed : 300
     * arrearage : 4380.00
     * all_user_ids : [33,30,27,26,25,31,28]
     */

    public int id;
    public int campus_id;
    public int province_id;
    public int city_id;
    public int college_id;
    public String name;
    public String password;
    public String mobile;
    public String wechat;
    public String qq;
    public String major;
    public String idcard;
    public int create_time;
    public int update_time;
    public int class_state;     //班级状态。1.上课；2.停课；3.结果；4.升学中；5.退费
    public int pay_state;
    public int upgrade_state;
    public int apply_state;
    public int course_type;
    public int is_sgin;
    public String avatar;
    public int student_id;
    public int birthday;
    public int grade;
    public String english_name;
    public int source;
    public int app_permission;
    public String notes;
    public int entering_school_year;
    public int hometown_pid;
    public int hometown_cid;
    public String parents_call;
    public int intention_class;
    public int intention_time;
    public String college_name;
    public String province_name;
    public String city_name;
    public String hometown_pid_name;
    public String hometown_cid_name;
    public String source_name;
    public int guider_id;                   // 晨读讲师id
    public String guider_name;              // 晨读讲师名字
    public int campus_manager_id;
    public String campus_manager_name;
    public int school_chat_attache_id;      // 校聊专员id
    public String school_chat_attache_name;     // 校聊专员名字
    public int charge_id;
    public String charge_name;
    public int saler_id;
    public String saler_name;
    public int advisor_id;
    public String advisor_name;
    public String pay_state_name;
    public int class_hour_total;
    public int class_hour;
    public int payed;
    public String arrearage;
    public ArrayList<Integer> all_user_ids;
    public List<PayInfo> paylist_top3;
    public CommunicationInfo communication;
    public int old_class_state;
    public String free_time;


    protected StudentInfo(Parcel in) {
        id = in.readInt();
        campus_id = in.readInt();
        province_id = in.readInt();
        city_id = in.readInt();
        college_id = in.readInt();
        name = in.readString();
        password = in.readString();
        mobile = in.readString();
        wechat = in.readString();
        qq = in.readString();
        major = in.readString();
        idcard = in.readString();
        create_time = in.readInt();
        update_time = in.readInt();
        class_state = in.readInt();
        pay_state = in.readInt();
        upgrade_state = in.readInt();
        apply_state = in.readInt();
        course_type = in.readInt();
        is_sgin = in.readInt();
        avatar = in.readString();
        student_id = in.readInt();
        birthday = in.readInt();
        grade = in.readInt();
        english_name = in.readString();
        source = in.readInt();
        app_permission = in.readInt();
        notes = in.readString();
        entering_school_year = in.readInt();
        hometown_pid = in.readInt();
        hometown_cid = in.readInt();
        parents_call = in.readString();
        intention_class = in.readInt();
        intention_time = in.readInt();
        college_name = in.readString();
        province_name = in.readString();
        city_name = in.readString();
        hometown_pid_name = in.readString();
        hometown_cid_name = in.readString();
        source_name = in.readString();
        guider_id = in.readInt();
        guider_name = in.readString();
        campus_manager_id = in.readInt();
        campus_manager_name = in.readString();
        school_chat_attache_id = in.readInt();
        school_chat_attache_name = in.readString();
        charge_id = in.readInt();
        charge_name = in.readString();
        saler_id = in.readInt();
        saler_name = in.readString();
        advisor_id = in.readInt();
        advisor_name = in.readString();
        pay_state_name = in.readString();
        class_hour_total = in.readInt();
        class_hour = in.readInt();
        payed = in.readInt();
        arrearage = in.readString();
        paylist_top3 = in.createTypedArrayList(PayInfo.CREATOR);
        communication = in.readParcelable(CommunicationInfo.class.getClassLoader());
        old_class_state = in.readInt();
        free_time = in.readString();
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

    @Override
    public int describeContents() {
        return 0;
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
        dest.writeString(wechat);
        dest.writeString(qq);
        dest.writeString(major);
        dest.writeString(idcard);
        dest.writeInt(create_time);
        dest.writeInt(update_time);
        dest.writeInt(class_state);
        dest.writeInt(pay_state);
        dest.writeInt(upgrade_state);
        dest.writeInt(apply_state);
        dest.writeInt(course_type);
        dest.writeInt(is_sgin);
        dest.writeString(avatar);
        dest.writeInt(student_id);
        dest.writeInt(birthday);
        dest.writeInt(grade);
        dest.writeString(english_name);
        dest.writeInt(source);
        dest.writeInt(app_permission);
        dest.writeString(notes);
        dest.writeInt(entering_school_year);
        dest.writeInt(hometown_pid);
        dest.writeInt(hometown_cid);
        dest.writeString(parents_call);
        dest.writeInt(intention_class);
        dest.writeInt(intention_time);
        dest.writeString(college_name);
        dest.writeString(province_name);
        dest.writeString(city_name);
        dest.writeString(hometown_pid_name);
        dest.writeString(hometown_cid_name);
        dest.writeString(source_name);
        dest.writeInt(guider_id);
        dest.writeString(guider_name);
        dest.writeInt(campus_manager_id);
        dest.writeString(campus_manager_name);
        dest.writeInt(school_chat_attache_id);
        dest.writeString(school_chat_attache_name);
        dest.writeInt(charge_id);
        dest.writeString(charge_name);
        dest.writeInt(saler_id);
        dest.writeString(saler_name);
        dest.writeInt(advisor_id);
        dest.writeString(advisor_name);
        dest.writeString(pay_state_name);
        dest.writeInt(class_hour_total);
        dest.writeInt(class_hour);
        dest.writeInt(payed);
        dest.writeString(arrearage);
        dest.writeTypedList(paylist_top3);
        dest.writeParcelable(communication, flags);
        dest.writeInt(old_class_state);
        dest.writeString(free_time);
    }
}
