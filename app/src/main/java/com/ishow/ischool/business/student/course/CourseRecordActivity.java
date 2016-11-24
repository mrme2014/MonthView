package com.ishow.ischool.business.student.course;

import com.commonlib.http.ApiFactory;
import com.commonlib.util.DateUtil;
import com.inqbarna.tablefixheaders.FixHeadersTableView;
import com.inqbarna.tablefixheaders.adapters.MatrixTableAdapter;
import com.ishow.ischool.R;
import com.ishow.ischool.bean.course.ClassHistory;
import com.ishow.ischool.bean.course.CourseRecord;
import com.ishow.ischool.common.api.ApiObserver;
import com.ishow.ischool.common.api.StudentApi;
import com.ishow.ischool.common.base.BaseActivity4Crm;

import java.util.HashMap;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.ishow.ischool.R.id.table;

public class CourseRecordActivity extends BaseActivity4Crm {

    public static final String P_STUDENT_ID = "studentId";
    @BindView(table)
    FixHeadersTableView tableview;
    private int mId;


    @Override
    protected void initEnv() {
        super.initEnv();
        mId = getIntent().getIntExtra(P_STUDENT_ID, 0);
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
                        showToast(msg);
                    }
                });
    }

    private void updateView(CourseRecord courseRecord) {

        String[][] tableData = new String[courseRecord.getLists().length + 1][];
        tableData[0] = getResources().getStringArray(R.array.course_record_table_head);

        for (int i = 0; i < courseRecord.getLists().length; i++) {
            ClassHistory classHistory = courseRecord.getLists()[i];
            tableData[i + 1] = new String[]{DateUtil.parseSecond2Str((long) classHistory.getClassInfo().getOpen_date()), classHistory.getClassHistoryInfo().getAction_name()
                    , classHistory.getClassInfo().getCourse_type(), classHistory.getClassInfo().getName()
                    , classHistory.getClassInfo().getTeacher_name(), classHistory.getClassInfo().getAdvisor_name()
                    , classHistory.getClassHistoryInfo().getStatus_name(), parsePay(classHistory)};
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
                    return "全款";
                } else {
                    return "欠款 " + classHistory.getPayListInfo().getCost() + "元";
                }
            case 6://退费
            case 10://升学中退款
                return "退费 " + (classHistory.getPayListInfo().getCost() * -1) + "元";
            default:
                return "-";
        }
    }
}
