package com.ishow.ischool.bean.classattend;

import java.util.List;

/**
 * Created by MrS on 2016/10/20.
 */

public class ClazInfo {


    /**
     * id : 1
     * campus_id : 4
     * name : 160903
     * type : 10
     * course_type : ishow中级
     * numbers : 50
     * teacher : 123
     * advisor : 125
     * open_date : 1472832000
     * location :
     * status : 2
     * lesson_times : 28
     * current_numbers : 2
     * lessoned_times : 0
     * create_time : 1472890870
     * update_time : 1472890870
     * timeslot : [{"start_time":"09:00","end_time":"20:00","week":1}]
     * nextClassTime : 10月24日 09:00 - 20:00
     */

    public int id;
    public int campus_id;
    public String name;
    public int type;
    public String course_type;
    public String numbers;
    public String teacher;
    public String advisor;
    public int open_date;
    public String location;
    public String status;
    public int lesson_times;
    public int current_numbers;
    public int lessoned_times;
    public int create_time;
    public int update_time;
    public String nextClassTime;
    public String teacher_name;
    public String teacherAvatar;//teacherAvatar
    /**
     * start_time : 09:00
     * end_time : 20:00
     * week : 1
     */

    public List<TimeslotBean> timeslot;

    public static class TimeslotBean {
        public String start_time;
        public String end_time;
        public int week;
    }
}
