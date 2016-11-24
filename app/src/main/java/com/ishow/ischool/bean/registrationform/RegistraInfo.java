package com.ishow.ischool.bean.registrationform;

/**
 * Created by MrS on 2016/11/22.
 */

public class RegistraInfo {

    /**
     * id : 2
     * apply_id : 2
     * student_id : 1
     * class_cost : 4971.2
     * payed : 4382.4
     * arrearage : 588.8
     * teacher_id : 0
     * advisor_id : 32
     * pay_time : 1472888175
     * is_del : 1
     * pay_info : [{"method":"现金","method_id":2,"account_id":0,"account":"","balance":4382.4},{"method":"支付宝","method_id":2,"account_id":0,"account":"","balance":4382.4}]
     * receipt_no : 123456789
     * memo : 全部交齐
     * cheap : 0
     * preferential_course_id : 0
     * advisor_name : 张志慧07课程顾问1
     */

    public int id;
    public int apply_id;
    public int student_id;
    public double class_cost;
    public double payed;
    public double arrearage;
    public int teacher_id;
    public int advisor_id;
    public int pay_time;
    public int is_del;
    public String pay_info;
    public String receipt_no;
    public String memo;
    public int cheap;
    public int preferential_course_id;
    public String advisor_name;
    public String preferential_course_name;
    public int arrearage_time;
}
