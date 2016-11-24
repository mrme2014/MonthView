package com.ishow.ischool.bean.course;

public class CourseRecord {
    private CurrentStatus currentStatus;
    private ClassHistory[] lists;

    public CurrentStatus getCurrentStatus() {
        return this.currentStatus;
    }

    public void setCurrentStatus(CurrentStatus currentStatus) {
        this.currentStatus = currentStatus;
    }

    public ClassHistory[] getLists() {
        return this.lists;
    }

    public void setLists(ClassHistory[] lists) {
        this.lists = lists;
    }
}
