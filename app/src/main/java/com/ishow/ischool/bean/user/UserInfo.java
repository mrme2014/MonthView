package com.ishow.ischool.bean.user;

/**
 * Created by MrS on 2016/7/19.
 */
public class UserInfo {

    /**
     * mobile : 13512341234
     * user_name : zhyong299
     * nick_name : jaya
     * avatar : 0
     * campus_id : 1
     * role_id : 1
     * create_time : 0
     * last_login_time : 0
     * login_num : 0
     * status : 1
     * user_id : 10017
     * qrcode : 0
     * birthday : 0
     * job : PHP
     * qq : 32730100
     */

    private String mobile;
    private String user_name;
    private String nick_name;
    private int avatar;
    private int campus_id;
    private int role_id;
    private int create_time;
    private int last_login_time;
    private int login_num;
    private int status;
    private int user_id;
    private int qrcode;

    @Override
    public String toString() {
        return "userInfo{" +
                "mobile='" + mobile + '\'' +
                ", user_name='" + user_name + '\'' +
                ", nick_name='" + nick_name + '\'' +
                ", avatar=" + avatar +
                ", campus_id=" + campus_id +
                ", role_id=" + role_id +
                ", create_time=" + create_time +
                ", last_login_time=" + last_login_time +
                ", login_num=" + login_num +
                ", status=" + status +
                ", user_id=" + user_id +
                ", qrcode=" + qrcode +
                ", birthday=" + birthday +
                ", job='" + job + '\'' +
                ", qq='" + qq + '\'' +
                '}';
    }

    private int birthday;
    private String job;
    private String qq;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public int getAvatar() {
        return avatar;
    }

    public void setAvatar(int avatar) {
        this.avatar = avatar;
    }

    public int getCampus_id() {
        return campus_id;
    }

    public void setCampus_id(int campus_id) {
        this.campus_id = campus_id;
    }

    public int getRole_id() {
        return role_id;
    }

    public void setRole_id(int role_id) {
        this.role_id = role_id;
    }

    public int getCreate_time() {
        return create_time;
    }

    public void setCreate_time(int create_time) {
        this.create_time = create_time;
    }

    public int getLast_login_time() {
        return last_login_time;
    }

    public void setLast_login_time(int last_login_time) {
        this.last_login_time = last_login_time;
    }

    public int getLogin_num() {
        return login_num;
    }

    public void setLogin_num(int login_num) {
        this.login_num = login_num;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getQrcode() {
        return qrcode;
    }

    public void setQrcode(int qrcode) {
        this.qrcode = qrcode;
    }

    public int getBirthday() {
        return birthday;
    }

    public void setBirthday(int birthday) {
        this.birthday = birthday;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }
}
