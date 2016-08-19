package com.ishow.ischool.bean.user;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by MrS on 2016/7/19.
 */
public class User implements Parcelable {

    /**
     * avatar : {"id":29370,"type":1,"file_name":"http://7xwcxj.com1.z0.glb.clouddn.com//1469341646348avator.jpg","user_id":10017}
     * campus : [{"id":1,"name":"杭州校区"}]
     * position : []
     * userInfo : {"mobile":"13512341234","user_name":"zhyon","nick_name":"jaya","avatar":29370,"campus_id":1,"position_id":1,"last_login_time":1469619991,"login_num":358,"status":1,"user_id":10017,"birthday":1469289600,"job":"PHP","qq":"12456789710"}
     * token : {"user_id":10017,"token":"136d0e6fc34b41c6b37562f2258f1cbb","over_time":1470224855,"refresh_token":"6fe2145f78bb42ef220579cfba92f872","refresh_time":1485172055,"loginname":"13512341234","ip":"58.100.185.5","status":1}
     */

    public Avatar avatar;
    public UserInfo userInfo;
    public Token token;
    public List<Campus> campus;
    public List<Position> position;
    public PositionInfo positionInfo;
    public Qrcode qrcode;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.avatar, flags);
        dest.writeParcelable(this.userInfo, flags);
        dest.writeParcelable(this.token, flags);
        dest.writeTypedList(this.campus);
        dest.writeTypedList(this.position);
        dest.writeParcelable(this.positionInfo, flags);
        dest.writeParcelable(this.qrcode, flags);
    }

    public User() {
    }

    protected User(Parcel in) {
        this.avatar = in.readParcelable(Avatar.class.getClassLoader());
        this.userInfo = in.readParcelable(UserInfo.class.getClassLoader());
        this.token = in.readParcelable(Token.class.getClassLoader());
        this.campus = in.createTypedArrayList(Campus.CREATOR);
        this.position = in.createTypedArrayList(Position.CREATOR);
        this.positionInfo = in.readParcelable(PositionInfo.class.getClassLoader());
        this.qrcode = in.readParcelable(Qrcode.class.getClassLoader());
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
