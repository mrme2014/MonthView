package com.ishow.ischool.bean.market;

/**
 * Created by abel on 16/8/20.
 */
public class CommunicationItem {

    public static final int TYPE_COMMUNICSTION_LATEST = 1;
    public static final int TYPE_COMMUNICSTION_ADD = 2;
    public static final int TYPE_COMMUNICSTION_CONTENT = 3;

    public int type;
    public Communication communication;

    public CommunicationItem(int type, Communication communication) {
        this.type = type;
        this.communication = communication;
    }
}
