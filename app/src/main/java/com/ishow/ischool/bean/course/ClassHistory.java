package com.ishow.ischool.bean.course;

public class ClassHistory {
    private PayListInfo payListInfo;
    private ClassInfo classInfo;
    private ClassHistoryInfo classHistoryInfo;

    public PayListInfo getPayListInfo() {
        return this.payListInfo;
    }

    public void setPayListInfo(PayListInfo payListInfo) {
        this.payListInfo = payListInfo;
    }

    public ClassInfo getClassInfo() {
        return this.classInfo;
    }

    public void setClassInfo(ClassInfo classInfo) {
        this.classInfo = classInfo;
    }

    public ClassHistoryInfo getClassHistoryInfo() {
        return this.classHistoryInfo;
    }

    public void setClassHistoryInfo(ClassHistoryInfo classHistoryInfo) {
        this.classHistoryInfo = classHistoryInfo;
    }
}