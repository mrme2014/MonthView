package com.ishow.ischool.bean.user;

import java.util.List;

/**
 * Created by MrS on 2016/7/19.
 */
public class User {

    /**
     * id : 29370
     * type : 1
     * file_name : http://7xwcxj.com1.z0.glb.clouddn.com//1469341646348avator.jpg
     * user_id : 10017
     */

    private AvatarBean avatar;
    /**
     * mobile : 13512341234
     * user_name : zhyon
     * nick_name : jaya
     * avatar : 29370
     * campus_id : 1
     * position_id : 1
     * last_login_time : 1469619991
     * login_num : 358
     * status : 1
     * user_id : 10017
     * birthday : 1469289600
     * job : PHP
     * qq : 12456789710
     */

    private UserInfoBean userInfo;
    /**
     * user_id : 10017
     * token : 136d0e6fc34b41c6b37562f2258f1cbb
     * over_time : 1470224855
     * refresh_token : 6fe2145f78bb42ef220579cfba92f872
     * refresh_time : 1485172055
     * loginname : 13512341234
     * ip : 58.100.185.5
     * status : 1
     */

    private TokenBean token;
    /**
     * id : 1
     * name : 杭州校区
     */

    private List<CampusBean> campus;
    /**
     * avatar : {"id":29370,"type":1,"file_name":"http://7xwcxj.com1.z0.glb.clouddn.com//1469341646348avator.jpg","user_id":10017}
     * campus : [{"id":1,"name":"杭州校区"}]
     * position : []
     * userInfo : {"mobile":"13512341234","user_name":"zhyon","nick_name":"jaya","avatar":29370,"campus_id":1,"position_id":1,"last_login_time":1469619991,"login_num":358,"status":1,"user_id":10017,"birthday":1469289600,"job":"PHP","qq":"12456789710"}
     * token : {"user_id":10017,"token":"136d0e6fc34b41c6b37562f2258f1cbb","over_time":1470224855,"refresh_token":"6fe2145f78bb42ef220579cfba92f872","refresh_time":1485172055,"loginname":"13512341234","ip":"58.100.185.5","status":1}
     */

    private List<?> position;

    public AvatarBean getAvatar() {
        return avatar;
    }

    public void setAvatar(AvatarBean avatar) {
        this.avatar = avatar;
    }

    public UserInfoBean getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfoBean userInfo) {
        this.userInfo = userInfo;
    }

    public TokenBean getToken() {
        return token;
    }

    public void setToken(TokenBean token) {
        this.token = token;
    }

    public List<CampusBean> getCampus() {
        return campus;
    }

    public void setCampus(List<CampusBean> campus) {
        this.campus = campus;
    }

    public List<?> getPosition() {
        return position;
    }

    public void setPosition(List<?> position) {
        this.position = position;
    }

    public static class AvatarBean {
        private int id;
        private int type;
        private String file_name;
        private int user_id;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getFile_name() {
            return file_name;
        }

        public void setFile_name(String file_name) {
            this.file_name = file_name;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }
    }

    public static class UserInfoBean {
        private String mobile;
        private String user_name;
        private String nick_name;
        private int avatar;
        private int campus_id;
        private int position_id;
        private int last_login_time;
        private int login_num;
        private int status;
        private int user_id;
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

        public int getPosition_id() {
            return position_id;
        }

        public void setPosition_id(int position_id) {
            this.position_id = position_id;
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

    public static class TokenBean {
        private int user_id;
        private String token;
        private int over_time;
        private String refresh_token;
        private int refresh_time;
        private String loginname;
        private String ip;
        private int status;

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public int getOver_time() {
            return over_time;
        }

        public void setOver_time(int over_time) {
            this.over_time = over_time;
        }

        public String getRefresh_token() {
            return refresh_token;
        }

        public void setRefresh_token(String refresh_token) {
            this.refresh_token = refresh_token;
        }

        public int getRefresh_time() {
            return refresh_time;
        }

        public void setRefresh_time(int refresh_time) {
            this.refresh_time = refresh_time;
        }

        public String getLoginname() {
            return loginname;
        }

        public void setLoginname(String loginname) {
            this.loginname = loginname;
        }

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }

    public static class CampusBean {
        private int id;
        private String name;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
