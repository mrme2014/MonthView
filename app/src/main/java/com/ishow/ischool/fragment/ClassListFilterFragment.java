package com.ishow.ischool.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.commonlib.util.DateUtil;
import com.ishow.ischool.R;
import com.ishow.ischool.bean.classes.TeacherInfo;
import com.ishow.ischool.bean.user.Campus;
import com.ishow.ischool.bean.user.User;
import com.ishow.ischool.business.classmaneger.classlist.TeacherPickActivity;
import com.ishow.ischool.common.manager.CampusManager;
import com.ishow.ischool.common.manager.UserManager;
import com.ishow.ischool.util.AppUtil;
import com.ishow.ischool.util.ToastUtil;
import com.ishow.ischool.widget.custom.InputLinearLayout;
import com.ishow.ischool.widget.pickerview.PickerDialogFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

/**
 * Created by mini on 16/10/25.
 */
public class ClassListFilterFragment extends DialogFragment implements InputLinearLayout.EidttextClick, View.OnTouchListener {
    @BindView(R.id.item_campus)
    InputLinearLayout campusIL;
    @BindView(R.id.start_time)
    EditText startTimeEt;
    @BindView(R.id.start_time_clear)
    ImageView startTimeIv;
    @BindView(R.id.end_time)
    EditText endTimeEt;
    @BindView(R.id.end_time_clear)
    ImageView endTimeIv;
    @BindView(R.id.item_course_type)
    InputLinearLayout courseTypeIL;
    @BindView(R.id.item_teacher)
    InputLinearLayout teacherIL;
    @BindView(R.id.item_advisor)
    InputLinearLayout advisorIL;

    private Dialog dialog;

    private final int TYPE_START_TIME = 1;
    private final int TYPE_END_TIME = 2;

    private Boolean startTimeFlag = true;
    private GestureDetector mGestureDetector;
    private SimpleDateFormat sdf;
    private ArrayList<String> courseType;

    private String mFilterCampusId;
    private String mFilterStartTime;
    private String mFilterEndTime;
    private String mFilterCourseTypeId;
    private String mFilterCourseTypeName;
    private String mFilterTeacherId;
    private String mFilterTeacherName;
    private String mFilterAdvisorId;
    private String mFilterAdvisorName;

    private boolean isUserCampus;       // 是否是校区员工（非总部员工）
    private User mUser;
    private FilterCallback callback;


    public static ClassListFilterFragment newInstance(Map<String, String> params, String source_type_name, String teacher_name, String advisor_name) {
        ClassListFilterFragment fragment = new ClassListFilterFragment();
        Bundle args = new Bundle();
        Iterator iter = params.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            args.putString(entry.getKey().toString(), entry.getValue().toString());
        }
        args.putString("course_type_name", source_type_name);
        args.putString("teacher_name", teacher_name);
        args.putString("advisor_name", advisor_name);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mFilterCampusId = bundle.getString("campus_id", "");
            mFilterStartTime = bundle.getString("begin_time", "");
            mFilterEndTime = bundle.getString("end_time", "");
            mFilterCourseTypeId = bundle.getString("course_type", "");
            mFilterCourseTypeName = bundle.getString("course_type_name", "");
            mFilterTeacherId = bundle.getString("teacher", "");
            mFilterTeacherName = bundle.getString("teacher_name", "");
            mFilterAdvisorId = bundle.getString("advisor", "");
            mFilterAdvisorName = bundle.getString("advisor_name", "");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        dialog = new Dialog(getContext(), R.style.Comm_dialogfragment_windowAnimationStyle);
        Window win = dialog.getWindow();
        // 一定要设置Background，如果不设置，window属性设置无效
        win.setBackgroundDrawable(new ColorDrawable(getResources().getColor(android.R.color.transparent)));

        WindowManager.LayoutParams params = win.getAttributes();
        params.gravity = Gravity.TOP;
        // 使用ViewGroup.LayoutParams，以便Dialog 宽度充满整个屏幕
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        win.setAttributes(params);

        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.filter_classlist_layout, null);
        //  View viewById = contentView.findViewById(R.id.root);
        // viewById.setTop(UIUtil.getToolbarSize(getContext()));
        ButterKnife.bind(this, contentView);

        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(contentView);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Dialog dialog = getDialog();
        Window window = dialog.getWindow();
        window.setLayout(-1, -1);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        sdf = new SimpleDateFormat("yyyy-MM-dd");
        courseType = new ArrayList<String>() {{
            add("ishow初级");
            add("ishow中级");
            add("ishow高级");
            add("ishow影视");
        }};
        initFilter();
    }

    void initFilter() {
        mUser = UserManager.getInstance().get();
        isUserCampus = (mUser.userInfo.campus_id == Campus.HEADQUARTERS) ? false : true;
        if (!isUserCampus) {        // 总部才显示“所属校区”筛选条件（默认杭州校区）
            campusIL.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(mFilterCampusId) && !mFilterCampusId.equals(Campus.HEADQUARTERS + "")) {
//                campusIL.setContent("杭州校区");
                campusIL.setContent(CampusManager.getInstance().getCampusNameById(Integer.parseInt(mFilterCampusId)));
            }
        }
        if (!TextUtils.isEmpty(mFilterStartTime)) {
            startTimeEt.setText(sdf.format(new Date(Long.parseLong(mFilterStartTime) * 1000)));
            startTimeIv.setVisibility(View.VISIBLE);
        }
        if (!TextUtils.isEmpty(mFilterEndTime)) {
            endTimeEt.setText(sdf.format(new Date(Long.parseLong(mFilterEndTime) * 1000)));
            endTimeIv.setVisibility(View.VISIBLE);
        }
        if (!TextUtils.isEmpty(mFilterCourseTypeId) && !TextUtils.isEmpty(mFilterCourseTypeName)) {
            courseTypeIL.setContent(mFilterCourseTypeName);
        }
        if (!TextUtils.isEmpty(mFilterTeacherId) && !TextUtils.isEmpty(mFilterTeacherName)) {
            teacherIL.setContent(mFilterTeacherName);
        }
        if (!TextUtils.isEmpty(mFilterAdvisorId) && !TextUtils.isEmpty(mFilterAdvisorName)) {
            advisorIL.setContent(mFilterAdvisorName);
        }
        campusIL.setOnEidttextClick(this);
        courseTypeIL.setOnEidttextClick(this);
        teacherIL.setOnEidttextClick(this);
        advisorIL.setOnEidttextClick(this);
        mGestureDetector = new GestureDetector(new Gesturelistener());
        startTimeEt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                startTimeFlag = true;
                return mGestureDetector.onTouchEvent(motionEvent);
            }
        });
        endTimeEt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                startTimeFlag = false;
                return mGestureDetector.onTouchEvent(motionEvent);
            }
        });
    }

    @OnClick({R.id.commun_block_top, R.id.commun_block_bottom, R.id.time_type, R.id.start_time_clear, R.id.end_time_clear, R.id.filter_reset, R.id.filter_ok})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start_time_clear:
                startTimeEt.setText("");
                startTimeIv.setVisibility(View.GONE);
                break;
            case R.id.end_time_clear:
                endTimeEt.setText("");
                endTimeIv.setVisibility(View.GONE);
                break;
            case R.id.filter_reset:
                resetFilter();
                break;
            case R.id.filter_ok:
                this.dismiss();
                if (callback != null) {
                    HashMap<String, String> params = new HashMap<>();
                    if (!TextUtils.isEmpty(mFilterCampusId)) {
                        params.put("campus_id", mFilterCampusId);
                    }
                    if (!TextUtils.isEmpty(mFilterStartTime)) {
                        params.put("begin_time", mFilterStartTime);
                    }
                    if (!TextUtils.isEmpty(mFilterEndTime)) {
                        params.put("end_time", mFilterEndTime);
                    }
                    if (!TextUtils.isEmpty(mFilterCourseTypeId)) {
                        params.put("course_type", mFilterCourseTypeId);
                    }
                    if (!TextUtils.isEmpty(mFilterTeacherId)) {
                        params.put("teacher", mFilterTeacherId);
                    }
                    if (!TextUtils.isEmpty(mFilterAdvisorId)) {
                        params.put("advisor", mFilterAdvisorId);
                    }
                    callback.onFinishFilter(params, mFilterCourseTypeName, mFilterTeacherName, mFilterAdvisorName);
                }
                break;
            case R.id.commun_block_top:
            case R.id.commun_block_bottom:
                this.dismiss();
                if (callback != null)
                    callback.onCancelDilaog();
                break;
        }
    }

    void resetFilter() {
        campusIL.setContent("");
        startTimeEt.setText("");
        startTimeIv.setVisibility(View.GONE);
        endTimeEt.setText("");
        endTimeIv.setVisibility(View.GONE);
        courseTypeIL.setContent("");
        teacherIL.setContent("");
        advisorIL.setContent("");
        mFilterCampusId = "";
        mFilterStartTime = "";
        mFilterEndTime = "";
        mFilterCourseTypeId = "";
        mFilterCourseTypeName = "";
        mFilterTeacherId = "";
        mFilterTeacherName = "";
        mFilterAdvisorId = "";
        mFilterAdvisorName = "";
    }

    @Override
    public void onEdittextClick(View view) {
        switch (view.getId()) {
            case R.id.item_campus:
                final ArrayList<String> campusList = CampusManager.getInstance().getCampusNames();
                PickerDialogFragment.Builder campusBuilder = new PickerDialogFragment.Builder();
                campusBuilder.setBackgroundDark(true)
                        .setDialogTitle(R.string.item_campus)
                        .setDialogType(PickerDialogFragment.PICK_TYPE_OTHERS)
                        .setDatas(0, 1, campusList);
                PickerDialogFragment campusFragment = campusBuilder.Build();
                campusFragment.show(getChildFragmentManager(), "dialog");
                campusFragment.addMultilinkPickCallback(new PickerDialogFragment.MultilinkPickCallback() {
                    @Override
                    public ArrayList<String> endSelect(int colum, int selectPosition, String text) {
                        return null;
                    }

                    @Override
                    public void onPickResult(Object object, String... result) {
                        campusIL.setContent(result[0]);
                        int index = campusList.indexOf(result[0]);
                        String selectedCampusId = (CampusManager.getInstance().get().get(index).id + "");
                        if (!mFilterCampusId.equals(selectedCampusId)) {            // 校区改变后，清空教师和学习顾问
                            if (!TextUtils.isEmpty(mFilterTeacherId)) {
                                mFilterTeacherId = "";
                                mFilterTeacherName = "";
                                teacherIL.setContent("");
                            }
                            if (!TextUtils.isEmpty(mFilterAdvisorId)) {
                                mFilterAdvisorId = "";
                                mFilterAdvisorName = "";
                                advisorIL.setContent("");
                            }
                            mFilterCampusId = selectedCampusId;
                        }
                    }
                });
                break;
            case R.id.item_course_type:
                PickerDialogFragment.Builder fromBuilder = new PickerDialogFragment.Builder();
                fromBuilder.setBackgroundDark(true)
                        .setDialogTitle(R.string.course_type)
                        .setDialogType(PickerDialogFragment.PICK_TYPE_OTHERS)
                        .setDatas(0, 1, courseType);

                PickerDialogFragment fromFragment = fromBuilder.Build();
                fromFragment.show(getChildFragmentManager(), "dialog");
                fromFragment.addMultilinkPickCallback(new PickerDialogFragment.MultilinkPickCallback() {
                    @Override
                    public ArrayList<String> endSelect(int colum, int selectPosition, String text) {
                        return null;
                    }

                    @Override
                    public void onPickResult(Object object, String... result) {
                        mFilterCourseTypeName = result[0];
                        courseTypeIL.setContent(mFilterCourseTypeName);
                        switch (mFilterCourseTypeName) {
                            case "ishow初级":
                                mFilterCourseTypeId = 1 + "";
                                break;
                            case "ishow中级":
                                mFilterCourseTypeId = 10 + "";
                                break;
                            case "ishow高级":
                                mFilterCourseTypeId = 20 + "";
                                break;
                            case "ishow影视":
                                mFilterCourseTypeId = 30 + "";
                                break;
                        }

                    }
                });
                break;
            case R.id.item_teacher:
                if (!TextUtils.isEmpty(mFilterCampusId) && !mFilterCampusId.equals(Campus.HEADQUARTERS + "")) {
                    Intent teacherIntent = new Intent(getActivity(), TeacherPickActivity.class);
                    teacherIntent.putExtra(TeacherPickActivity.PICK_CAMPUS_ID, TextUtils.isEmpty(mFilterCampusId) ? -1 : Integer.parseInt(mFilterCampusId));
                    teacherIntent.putExtra(TeacherPickActivity.PICK_TITLE, "选择教师");
                    teacherIntent.putExtra(TeacherPickActivity.PICK_TYPE, TeacherPickActivity.PICK_TYPE_TEACHER);
                    startActivityForResult(teacherIntent, TeacherPickActivity.REQUEST_CODE_PICKTEACHER);
                } else {
                    ToastUtil.showToast(getActivity(), "请先选择校区");
                }
                break;
            case R.id.item_advisor:
                if (!TextUtils.isEmpty(mFilterCampusId) && !mFilterCampusId.equals(Campus.HEADQUARTERS + "")) {
                    Intent advisorIntent = new Intent(getActivity(), TeacherPickActivity.class);
                    advisorIntent.putExtra(TeacherPickActivity.PICK_CAMPUS_ID, TextUtils.isEmpty(mFilterCampusId) ? -1 : Integer.parseInt(mFilterCampusId));
                    advisorIntent.putExtra(TeacherPickActivity.PICK_TITLE, "选择学习顾问");
                    advisorIntent.putExtra(TeacherPickActivity.PICK_TYPE, TeacherPickActivity.PICK_TYPE_ADVISOR);
                    startActivityForResult(advisorIntent, TeacherPickActivity.REQUEST_CODE_PICKADVISOR);
                } else {
                    ToastUtil.showToast(getActivity(), "请先选择校区");
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case TeacherPickActivity.REQUEST_CODE_PICKTEACHER:
                    if (data != null) {
                        TeacherInfo teacherInfo = data.getExtras().getParcelable(TeacherPickActivity.PICK_USER);
                        mFilterTeacherName = teacherInfo.user_name;
                        mFilterTeacherId = teacherInfo.id + "";
                        teacherIL.setContent(mFilterTeacherName);
                    }
                    break;
                case TeacherPickActivity.REQUEST_CODE_PICKADVISOR:
                    if (data != null) {
                        TeacherInfo teacherInfo = data.getExtras().getParcelable(TeacherPickActivity.PICK_USER);
                        mFilterTeacherName = teacherInfo.user_name;
                        mFilterTeacherId = teacherInfo.id + "";
                        advisorIL.setContent(mFilterTeacherName);
                    }
                    break;
            }
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
    }

    private class Gesturelistener implements GestureDetector.OnGestureListener {

        public boolean onDown(MotionEvent e) {
            // TODO Auto-generated method stub
            return false;
        }

        public void onShowPress(MotionEvent e) {
            // TODO Auto-generated method stub
        }

        public boolean onSingleTapUp(MotionEvent e) {
            // TODO Auto-generated method stub
            if (startTimeFlag) {
                showTimePickPop(TYPE_START_TIME);
            } else {
                showTimePickPop(TYPE_END_TIME);
            }
            return false;
        }

        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                float distanceX, float distanceY) {
            // TODO Auto-generated method stub
            return false;
        }

        public void onLongPress(MotionEvent e) {
            // TODO Auto-generated method stub
        }

        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {
            // TODO Auto-generated method stub
            return false;
        }
    }

    private void showTimePickPop(final int type) {
        AppUtil.showTimePickerDialog(getChildFragmentManager(), (type == TYPE_START_TIME ? R.string.item_start_time : R.string.item_end_time),
                new PickerDialogFragment.Callback<Integer>() {
                    @Override
                    public void onPickResult(Integer unix, String... result) {
                        if (type == TYPE_START_TIME) {
                            if (!TextUtils.isEmpty(mFilterEndTime) && unix > Integer.parseInt(mFilterEndTime)) {
                                showTimeError();
                                mFilterStartTime = "";
                                startTimeEt.setText("");
                                startTimeIv.setVisibility(View.GONE);
                            } else {
                                startTimeEt.setText(result[0]);
                                mFilterStartTime = String.valueOf(unix);
                                startTimeIv.setVisibility(View.VISIBLE);
                            }
                        } else if (type == TYPE_END_TIME) {
                            long end4Today = DateUtil.getEndTime(new Date((long) unix * 1000)) / 1000;      // 获取当日23:59:59的timestamp
                            mFilterEndTime = String.valueOf(end4Today);
                            if (!TextUtils.isEmpty(mFilterStartTime) && unix < Integer.parseInt(mFilterStartTime)) {
                                showTimeError();
                                mFilterEndTime = "";
                                endTimeEt.setText("");
                                endTimeIv.setVisibility(View.GONE);
                            } else {
                                endTimeEt.setText(result[0]);
                                endTimeIv.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                });
    }

    void showTimeError() {
        final Snackbar snackbar = Snackbar.make(startTimeEt, getString(R.string.time_error), Snackbar.LENGTH_LONG);
        snackbar.setAction("朕知道了", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }

    public interface FilterCallback {
        void onFinishFilter(HashMap<String, String> map, String source_name, String university_name, String referrer_name);

        void onCancelDilaog();
    }

    public void setOnFilterCallback(FilterCallback callback) {
        this.callback = callback;
    }
}
