package com.ishow.ischool.business.student.course;

import android.content.Intent;
import android.view.MenuItem;

import com.commonlib.http.ApiFactory;
import com.inqbarna.tablefixheaders.FixHeadersTableView;
import com.inqbarna.tablefixheaders.adapters.MatrixTableAdapter;
import com.ishow.ischool.R;
import com.ishow.ischool.bean.course.ClassHistory;
import com.ishow.ischool.bean.course.CourseRecord;
import com.ishow.ischool.business.registrationform.registrationFormActivity;
import com.ishow.ischool.business.registrationform.registrationInfoConfirmActivity;
import com.ishow.ischool.common.api.ApiObserver;
import com.ishow.ischool.common.api.StudentApi;
import com.ishow.ischool.common.base.BaseActivity4Crm;
import com.ishow.ischool.util.TextUtil;

import java.util.HashMap;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.ishow.ischool.R.id.table;

public class CourseRecordActivity extends BaseActivity4Crm {

    public static final String P_STUDENT_ID = "studentId";
    public static final String STUDENT_STATUS = "student_status";
    @BindView(table)
    FixHeadersTableView tableview;
    private int mId;
    private int student_status;


    @Override
    protected void initEnv() {
        super.initEnv();
        mId = getIntent().getIntExtra(P_STUDENT_ID, 0);
        student_status = getIntent().getIntExtra(STUDENT_STATUS, 0);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        Intent intent = new Intent(this, registrationInfoConfirmActivity.class);
        intent.putExtra(registrationFormActivity.STUDENT_ID, mId);
        intent.putExtra(registrationFormActivity.STUDENT_STATUS, student_status);
        startActivity(intent);
        return super.onMenuItemClick(item);
    }

    @Override
    protected void setUpContentView() {
        setContentView(R.layout.activity_course_record, R.string.str_course_record, R.menu.menu_course_record, MODE_BACK);
    }

    @Override
    protected void setUpView() {

    }

    @Override
    protected void setUpData() {

        taskGetCourseRecord();

    }

    private void taskGetCourseRecord() {
        tableview.setLoading(true);
        HashMap<String, Integer> mParams = new HashMap<>();
        mParams.put("student_id", mId);
        ApiFactory.getInstance().getApi(StudentApi.class).getCourseRecord(mParams).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiObserver<CourseRecord>() {
                    @Override
                    public void onSuccess(CourseRecord courseRecord) {
                        tableview.setLoading(false);
                        updateView(courseRecord);
                    }

                    @Override
                    public void onError(String msg) {
                        tableview.setLoading(false);
                        tableview.setEnabled(false);
                        showToast(msg);
                    }
                });
    }

    private void updateView(CourseRecord courseRecord) {

        String[][] tableData = new String[courseRecord.getLists().length + 1][];
        tableData[0] = getResources().getStringArray(R.array.course_record_table_head);

        for (int i = 0; i < courseRecord.getLists().length; i++) {
            ClassHistory classHistory = courseRecord.getLists()[i];
            tableData[i + 1] = new String[]{
                    TextUtil.format4Table(classHistory.getClassInfo().getOpen_date()),
                    TextUtil.format4Table(classHistory.getClassHistoryInfo().getAction_name()),
                    TextUtil.format4Table(classHistory.getClassInfo().getCourse_type()),
                    TextUtil.format4Table(classHistory.getClassInfo().getName()),
                    TextUtil.format4Table(classHistory.getClassInfo().getTeacher_name()),
                    TextUtil.format4Table(classHistory.getClassInfo().getAdvisor_name()),
                    TextUtil.format4Table(classHistory.getClassHistoryInfo().getStatus_name()),
                    parsePay(classHistory)};
        }

        MatrixTableAdapter<String> matrixTableAdapter = new MatrixTableAdapter<String>(this, tableData);
        tableview.setAdapter(matrixTableAdapter);
        tableview.invalidate();
    }

    private String parsePay(ClassHistory classHistory) {
        switch (classHistory.getClassHistoryInfo().getAction()) {
            case 4://升学
            case 8://报名
                if (classHistory.getPayListInfo().getCost() == 0) {
                    return getString(R.string.full_pay);
                } else {
                    return getString(R.string.debt_pay, classHistory.getPayListInfo().getCost() + "");
                }
            case 6://退费
            case 10://升学中退款
                return getString(R.string.refund, (classHistory.getPayListInfo().getCost() * -1) + "");//"退费 " + (classHistory.getPayListInfo().getCost() * -1) + "元";
            default:
                return "-";
        }
    }
}
