package com.ishow.ischool.bean.user;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by MrS on 2016/7/19.
 */
public class UserInfo implements Parcelable {

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

    public String mobile;
    public String user_name;
    public String nick_name;
    public int avatar;
    public int campus_id;
    public int role_id;
    public int create_time;
    public int last_login_time;
    public int login_num;
    public int status;
    public int user_id;
    public int qrcode;
    public int birthday;
    public String job;
    public String qq;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mobile);
        dest.writeString(this.user_name);
        dest.writeString(this.nick_name);
        dest.writeInt(this.avatar);
        dest.writeInt(this.campus_id);
        dest.writeInt(this.role_id);
        dest.writeInt(this.create_time);
        dest.writeInt(this.last_login_time);
        dest.writeInt(this.login_num);
        dest.writeInt(this.status);
        dest.writeInt(this.user_id);
        dest.writeInt(this.qrcode);
        dest.writeInt(this.birthday);
        dest.writeString(this.job);
        dest.writeString(this.qq);
    }

    public UserInfo() {
    }

    protected UserInfo(Parcel in) {
        this.mobile = in.readString();
        this.user_name = in.readString();
        this.nick_name = in.readString();
        this.avatar = in.readInt();
        this.campus_id = in.readInt();
        this.role_id = in.readInt();
        this.create_time = in.readInt();
        this.last_login_time = in.readInt();
        this.login_num = in.readInt();
        this.status = in.readInt();
        this.user_id = in.readInt();
        this.qrcode = in.readInt();
        this.birthday = in.readInt();
        this.job = in.readString();
        this.qq = in.readString();
    }

    public static final Creator<UserInfo> CREATOR = new Creator<UserInfo>() {
        @Override
        public UserInfo createFromParcel(Parcel source) {
            return new UserInfo(source);
        }

        @Override
        public UserInfo[] newArray(int size) {
            return new UserInfo[size];
        }
    };


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
}
