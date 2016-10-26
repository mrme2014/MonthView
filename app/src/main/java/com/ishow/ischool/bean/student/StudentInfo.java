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
    public int guider_id;
    public String guider_name;
    public int campus_manager_id;
    public String campus_manager_name;
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
        dest.writeString(this.wechat);
        dest.writeString(this.qq);
        dest.writeString(this.major);
        dest.writeString(this.idcard);
        dest.writeInt(this.create_time);
        dest.writeInt(this.update_time);
        dest.writeInt(this.class_state);
        dest.writeInt(this.pay_state);
        dest.writeInt(this.upgrade_state);
        dest.writeInt(this.apply_state);
        dest.writeInt(this.course_type);
        dest.writeInt(this.is_sgin);
        dest.writeString(this.avatar);
        dest.writeInt(this.student_id);
        dest.writeInt(this.birthday);
        dest.writeInt(this.grade);
        dest.writeString(this.english_name);
        dest.writeInt(this.source);
        dest.writeInt(this.app_permission);
        dest.writeString(this.notes);
        dest.writeInt(this.entering_school_year);
        dest.writeInt(this.hometown_pid);
        dest.writeInt(this.hometown_cid);
        dest.writeString(this.parents_call);
        dest.writeInt(this.intention_class);
        dest.writeInt(this.intention_time);
        dest.writeString(this.college_name);
        dest.writeString(this.province_name);
        dest.writeString(this.city_name);
        dest.writeString(this.hometown_pid_name);
        dest.writeString(this.hometown_cid_name);
        dest.writeString(this.source_name);
        dest.writeInt(this.guider_id);
        dest.writeString(this.guider_name);
        dest.writeInt(this.campus_manager_id);
        dest.writeString(this.campus_manager_name);
        dest.writeInt(this.charge_id);
        dest.writeString(this.charge_name);
        dest.writeInt(this.saler_id);
        dest.writeString(this.saler_name);
        dest.writeInt(this.advisor_id);
        dest.writeString(this.advisor_name);
        dest.writeString(this.pay_state_name);
        dest.writeInt(this.class_hour_total);
        dest.writeInt(this.class_hour);
        dest.writeInt(this.payed);
        dest.writeString(this.arrearage);
        dest.writeList(this.all_user_ids);
        dest.writeTypedList(this.paylist_top3);
        dest.writeParcelable(this.communication, flags);
        dest.writeInt(this.old_class_state);
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
        this.wechat = in.readString();
        this.qq = in.readString();
        this.major = in.readString();
        this.idcard = in.readString();
        this.create_time = in.readInt();
        this.update_time = in.readInt();
        this.class_state = in.readInt();
        this.pay_state = in.readInt();
        this.upgrade_state = in.readInt();
        this.apply_state = in.readInt();
        this.course_type = in.readInt();
        this.is_sgin = in.readInt();
        this.avatar = in.readString();
        this.student_id = in.readInt();
        this.birthday = in.readInt();
        this.grade = in.readInt();
        this.english_name = in.readString();
        this.source = in.readInt();
        this.app_permission = in.readInt();
        this.notes = in.readString();
        this.entering_school_year = in.readInt();
        this.hometown_pid = in.readInt();
        this.hometown_cid = in.readInt();
        this.parents_call = in.readString();
        this.intention_class = in.readInt();
        this.intention_time = in.readInt();
        this.college_name = in.readString();
        this.province_name = in.readString();
        this.city_name = in.readString();
        this.hometown_pid_name = in.readString();
        this.hometown_cid_name = in.readString();
        this.source_name = in.readString();
        this.guider_id = in.readInt();
        this.guider_name = in.readString();
        this.campus_manager_id = in.readInt();
        this.campus_manager_name = in.readString();
        this.charge_id = in.readInt();
        this.charge_name = in.readString();
        this.saler_id = in.readInt();
        this.saler_name = in.readString();
        this.advisor_id = in.readInt();
        this.advisor_name = in.readString();
        this.pay_state_name = in.readString();
        this.class_hour_total = in.readInt();
        this.class_hour = in.readInt();
        this.payed = in.readInt();
        this.arrearage = in.readString();
        this.all_user_ids = new ArrayList<Integer>();
        in.readList(this.all_user_ids, Integer.class.getClassLoader());
        this.paylist_top3 = in.createTypedArrayList(PayInfo.CREATOR);
        this.communication = in.readParcelable(CommunicationInfo.class.getClassLoader());
        this.old_class_state = in.readInt();
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
}
