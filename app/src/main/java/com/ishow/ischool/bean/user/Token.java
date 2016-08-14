package com.ishow.ischool.bean.user;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by MrS on 2016/7/19.
 */
public class Token implements Parcelable {

    /**
     * user_id : 10017
     * token : bee9b1dac0bc7900ae3044fd537986c4
     * over_time : 1469494195
     * refresh_token : d0e0bf35f2b77798aeb898e9f30eb4b3
     * refresh_time : 1484441395
     * loginname : 13512341234
     * ip : 58.100.185.5
     * status : 1
     */

    public int user_id;
    public String token;
    public int over_time;
    public String refresh_token;
    public int refresh_time;
    public String loginname;
    public String ip;
    public int status;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.user_id);
        dest.writeString(this.token);
        dest.writeInt(this.over_time);
        dest.writeString(this.refresh_token);
        dest.writeInt(this.refresh_time);
        dest.writeString(this.loginname);
        dest.writeString(this.ip);
        dest.writeInt(this.status);
    }

    public Token() {
    }

    protected Token(Parcel in) {
        this.user_id = in.readInt();
        this.token = in.readString();
        this.over_time = in.readInt();
        this.refresh_token = in.readString();
        this.refresh_time = in.readInt();
        this.loginname = in.readString();
        this.ip = in.readString();
        this.status = in.readInt();
    }

    public static final Creator<Token> CREATOR = new Creator<Token>() {
        @Override
        public Token createFromParcel(Parcel source) {
            return new Token(source);
        }

        @Override
        public Token[] newArray(int size) {
            return new Token[size];
        }
    };


    @Override
    public String toString() {
        return "token{" +
                "user_id=" + user_id +
                ", token='" + token + '\'' +
                ", over_time=" + over_time +
                ", refresh_token='" + refresh_token + '\'' +
                ", refresh_time=" + refresh_time +
                ", loginname='" + loginname + '\'' +
                ", ip='" + ip + '\'' +
                ", status=" + status +
                '}';
    }
}
