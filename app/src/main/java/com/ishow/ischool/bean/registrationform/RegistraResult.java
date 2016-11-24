package com.ishow.ischool.bean.registrationform;

import com.ishow.ischool.bean.student.StudentInfo;

import java.util.List;

/**
 * Created by MrS on 2016/11/22.
 */

public class RegistraResult {
    public List<RegistraInfo> payListInfo;
    public List<CheapType> preferentialCourse;
    public BasePriceInfo basePriceInfo;
    public StudentInfo studentInfo;
    public List<List<Integer>> free_time_arr;

}
