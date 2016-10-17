package com.ishow.ischool.event;

import com.ishow.ischool.bean.student.Student;

/**
 * Created by abel on 16/10/17.
 */

public class UploadAvatarEvent {
    public Student student;

    public UploadAvatarEvent(Student student) {
        this.student = student;
    }
}
