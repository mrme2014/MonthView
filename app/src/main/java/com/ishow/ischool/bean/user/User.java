package com.ishow.ischool.bean.user;

import java.util.List;

/**
 * Created by MrS on 2016/7/19.
 */
public class User {

    /**
     * avatar : {"id":29370,"type":1,"file_name":"http://7xwcxj.com1.z0.glb.clouddn.com//1469341646348avator.jpg","user_id":10017}
     * campus : [{"id":1,"name":"杭州校区"}]
     * position : []
     * userInfo : {"mobile":"13512341234","user_name":"zhyon","nick_name":"jaya","avatar":29370,"campus_id":1,"position_id":1,"last_login_time":1469619991,"login_num":358,"status":1,"user_id":10017,"birthday":1469289600,"job":"PHP","qq":"12456789710"}
     * token : {"user_id":10017,"token":"136d0e6fc34b41c6b37562f2258f1cbb","over_time":1470224855,"refresh_token":"6fe2145f78bb42ef220579cfba92f872","refresh_time":1485172055,"loginname":"13512341234","ip":"58.100.185.5","status":1}
     */

    private Avatar avatar;
    private UserInfo userInfo;
    private Token token;
    private List<Campus> campus;
    private List<?> position;



    public Avatar getAvatar() {
        return avatar;
    }

    public void setAvatar(Avatar avatar) {
        this.avatar = avatar;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public List<Campus> getCampus() {
        return campus;
    }

    public void setCampus(List<Campus> campus) {
        this.campus = campus;
    }

    public List<?> getPosition() {
        return position;
    }

    public void setPosition(List<?> position) {
        this.position = position;
    }

}
