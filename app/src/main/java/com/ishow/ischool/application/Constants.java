package com.ishow.ischool.application;

/**
 * Created by abel on 16/9/28.
 */

public class Constants {
    public static final int CAMPUS_HEADQUARTERS = 1;

    /*校聊专员*/
    public static final int CHAT_COMMISSIONER = 20;
    /*晨读讲师*/
    public static final int MORNING_READ_TEACHER = 17;
    /*课程顾问*/
    public static final int COURSE_CONSULTANT = 19;
    /*课程顾问主管*/
    public static final int COURSE_CONSULTANT_LEADER = 14;

    /*pin lecturer销讲师*/
    public static final int PIN_LECTURER = 18;

    public static final String IS_TEACH = "is_teach";

    public static final String FROM_M_E = "from";
    public static final int FROM_TEACH = 1;
    public static final int FROM_MARKET = 0;

    public interface PaySate {
        int unapply = 1;
        int debt = 2;
        int fullPay = 3;
        int refund = 4;
    }

}
