package com.ishow.ischool.util;

import com.ishow.ischool.bean.student.ApplyInfo;

/**
 * Created by wqf on 16/8/17.
 */
public class UserUtil {

    public static String getUserPayState(int stateCode) {
        String stateStr = "";
        switch (stateCode) {
            case ApplyInfo.PAYSTATE_UNENROL:
                stateStr = "未报名";
                break;
            case ApplyInfo.PAYSTATE_DEBT:
                stateStr = "欠款";
                break;
            case ApplyInfo.PAYSTATE_ENROL:
                stateStr = "已报名";
                break;
            case ApplyInfo.PAYSTATE_REFUND:
                stateStr = "退款";
                break;
            default:
                break;
        }
        return stateStr;
    }
}
