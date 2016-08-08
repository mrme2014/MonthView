package com.ishow.ischool.bean.student;

/**
 * Created by MrS on 2016/7/24.
 */
public class StudentInfo {

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

    private int id;
    private int campus_id;
    private int province_id;
    private int city_id;
    private int college_id;
    private String name;
    private String password;
    private String mobile;
    private String qq;
    private String major;
    private String idcard;
    private int create_time;
    private int update_time;
    private int class_sate;
    private int pay_sate;
    private int upgrade_stae;
    private int apply_stae;

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
                ", class_sate=" + class_sate +
                ", pay_sate=" + pay_sate +
                ", upgrade_stae=" + upgrade_stae +
                ", apply_stae=" + apply_stae +
                ", student_id=" + student_id +
                ", birthday=" + birthday +
                ", grade=" + grade +
                ", english_name='" + english_name + '\'' +
                ", source=" + source +
                ", notes='" + notes + '\'' +
                '}';
    }

    private int student_id;
    private int birthday;
    private String grade;
    private String english_name;
    private int source;
    private String notes;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCampus_id() {
        return campus_id;
    }

    public void setCampus_id(int campus_id) {
        this.campus_id = campus_id;
    }

    public int getProvince_id() {
        return province_id;
    }

    public void setProvince_id(int province_id) {
        this.province_id = province_id;
    }

    public int getCity_id() {
        return city_id;
    }

    public void setCity_id(int city_id) {
        this.city_id = city_id;
    }

    public int getCollege_id() {
        return college_id;
    }

    public void setCollege_id(int college_id) {
        this.college_id = college_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public int getCreate_time() {
        return create_time;
    }

    public void setCreate_time(int create_time) {
        this.create_time = create_time;
    }

    public int getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(int update_time) {
        this.update_time = update_time;
    }

    public int getClass_sate() {
        return class_sate;
    }

    public void setClass_sate(int class_sate) {
        this.class_sate = class_sate;
    }

    public int getPay_sate() {
        return pay_sate;
    }

    public void setPay_sate(int pay_sate) {
        this.pay_sate = pay_sate;
    }

    public int getUpgrade_stae() {
        return upgrade_stae;
    }

    public void setUpgrade_stae(int upgrade_stae) {
        this.upgrade_stae = upgrade_stae;
    }

    public int getApply_stae() {
        return apply_stae;
    }

    public void setApply_stae(int apply_stae) {
        this.apply_stae = apply_stae;
    }

    public int getStudent_id() {
        return student_id;
    }

    public void setStudent_id(int student_id) {
        this.student_id = student_id;
    }

    public int getBirthday() {
        return birthday;
    }

    public void setBirthday(int birthday) {
        this.birthday = birthday;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getEnglish_name() {
        return english_name;
    }

    public void setEnglish_name(String english_name) {
        this.english_name = english_name;
    }

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
