package com.ishow.ischool.event;

/**
 * Created by wqf on 16/11/24.
 */
public class WeeklyLoadEvent {

    boolean isLoadSuccess;

    public WeeklyLoadEvent(boolean isLoadSuccess) {
        this.isLoadSuccess = isLoadSuccess;
    }

    public boolean isLoadSuccess() {
        return isLoadSuccess;
    }
}
