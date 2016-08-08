package com.ishow.ischool.bean.user;

/**
 * Created by MrS on 2016/7/19.
 */
public class Token {

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

    private int user_id;
    private String token;
    private int over_time;

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
