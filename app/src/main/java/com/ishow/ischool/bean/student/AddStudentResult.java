package com.ishow.ischool.bean.student;

/**
 * Created by MrS on 2016/7/19.
 */
public class AddStudentResult {


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getReferrer() {
        return referrer;
    }

    public void setReferrer(int referrer) {
        this.referrer = referrer;
    }

    public int getStudent_id() {
        return student_id;
    }

    public void setStudent_id(int student_id) {
        this.student_id = student_id;
    }

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

    private String notes;
    private int id;
    private int source;

    @Override
    public String toString() {
        return "addStudentResult{" +
                "notes='" + notes + '\'' +
                ", id=" + id +
                ", source=" + source +
                ", student_id=" + student_id +
                ", referrer=" + referrer +
                '}';
    }

    private int student_id;
    private int referrer;
}
