package com.ishow.ischool.business.classmaneger.classlist;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.commonlib.util.DateUtil;
import com.commonlib.util.LogUtil;
import com.commonlib.widget.pull.BaseViewHolder;
import com.commonlib.widget.pull.PullRecycler;
import com.ishow.ischool.R;
import com.ishow.ischool.adpter.TimeSlotAdapter;
import com.ishow.ischool.bean.classes.ClassList;
import com.ishow.ischool.bean.classes.ClassPojo;
import com.ishow.ischool.bean.classes.ClassTimeSlot;
import com.ishow.ischool.bean.user.Campus;
import com.ishow.ischool.business.classattention.ClassAttendActivity;
import com.ishow.ischool.business.classattention.ClazCheckinTableActivity;
import com.ishow.ischool.business.classmaneger.studentlist.StudentListActivity;
import com.ishow.ischool.common.base.BaseListActivity4Crm;
import com.ishow.ischool.fragment.ClassListFilterFragment;
import com.ishow.ischool.util.AppUtil;
import com.ishow.ischool.widget.custom.CustomGridLayoutManager;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by wqf on 16/10/21.
 */
public class ClassListActivity extends BaseListActivity4Crm<ClassListPresenter, ClassListModel, ClassPojo> implements ClassListContract.View<ClassList>,
        ClassListFilterFragment.FilterCallback {

    private PopupWindow popupWindow;
    private View popupView;
    private int mParamStatus = 2;
    private String mTitle = "上课中";

    // 筛选
    private HashMap<String, String> filterParams;
    private String mFilterCourseTypeName;
    private String mFilterTeacherName;
    private String mFilterAdvisorName;
    private String mCampusId;
    ClassListFilterFragment dialog = null;

    @Override
    protected void setUpView() {
        super.setUpView();
        mToolbarTitle.setText(R.string.in_class);
        mToolbarTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.icon_xiala, 0);
        mToolbarTitle.setCompoundDrawablePadding(20);
        mToolbarTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        setUpMenu(R.menu.menu_class_list);
        mToolbarTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupWindow();
            }
        });
        initFilter();
    }

    void showPopupWindow() {
        TextView statu1Tv, statu2Tv, statu3Tv;
        if (popupWindow == null) {
            popupView = getLayoutInflater().inflate(R.layout.popup_class_status, null);
            statu1Tv = (TextView) popupView.findViewById(R.id.status1);
            statu2Tv = (TextView) popupView.findViewById(R.id.status2);
            statu3Tv = (TextView) popupView.findViewById(R.id.status3);
            statu1Tv.setOnClickListener(handler);
            statu2Tv.setOnClickListener(handler);
            statu3Tv.setOnClickListener(handler);

            popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
            popupView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            popupWindow.setOutsideTouchable(true);
            popupWindow.setBackgroundDrawable(new BitmapDrawable());
            popupWindow.setAnimationStyle(R.style.popup_anim);
        }
        int popupWidth = popupView.getMeasuredWidth();
        int popupHeight = popupView.getMeasuredHeight();
        int[] location = new int[2];
        mToolbarTitle.getLocationOnScreen(location);
        popupWindow.showAtLocation(mToolbarTitle, Gravity.NO_GRAVITY, (location[0] + mToolbarTitle.getWidth() / 2) - popupWidth / 2,
                location[1] + mToolbarTitle.getHeight());
        LogUtil.d("mToolbarTitle popupWidth", popupWidth + "");
        LogUtil.d("mToolbarTitle popupHeight", popupHeight + "");
        LogUtil.d("mToolbarTitle x", location[0] + "");
        LogUtil.d("mToolbarTitle getWidth", mToolbarTitle.getWidth() + "");
        LogUtil.d("mToolbarTitle getHeight", mToolbarTitle.getHeight() + "");
        LogUtil.d("mToolbarTitle y", location[1] + "");
    }

    View.OnClickListener handler = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int selectedItem = -1;
            popupWindow.dismiss();
            switch (view.getId()) {
                case R.id.status1:
                    mTitle = getString(R.string.not_in_class);
                    selectedItem = 1;
                    break;
                case R.id.status2:
                    mTitle = getString(R.string.in_class);
                    selectedItem = 2;
                    break;
                case R.id.status3:
                    mTitle = getString(R.string.class_has_been);
                    selectedItem = 3;
                    break;
            }
            if (selectedItem != mParamStatus) {
                mParamStatus = selectedItem;
                setRefreshing();
            }
        }
    };

    @Override
    protected RecyclerView.ItemDecoration getItemDecoration() {
        return null;
    }


    void initFilter() {
        filterParams = new HashMap<>();
        if (mUser.userInfo.campus_id == Campus.HEADQUARTERS) {
            mCampusId = Campus.HEADQUARTERS + "";
            filterParams.put("campus_id", Campus.HEADQUARTERS + "");                  // 总部获取学院统计列表campus_id传1
        } else {
            mCampusId = mUser.userInfo.campus_id + "";
            filterParams.put("campus_id", mUser.userInfo.campus_id + "");
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_filter:
                if (dialog == null) {
                    dialog = ClassListFilterFragment.newInstance(filterParams, mFilterCourseTypeName, mFilterTeacherName, mFilterAdvisorName);
                    dialog.setOnFilterCallback(ClassListActivity.this);
                }
                dialog.show(getSupportFragmentManager(), "dialog");
                break;
        }
        return true;
    }

    /**
     * @param map              筛选参数
     * @param course_type_name 用于缓存筛选后，再次进入时填充内容（课程类型）
     * @param teacher_name     用于缓存筛选后，再次进入时填充内容（教师名字）
     * @param advisor_name     用于缓存筛选后，再次进入时填充内容 (学习顾问名字)
     */
    @Override
    public void onFinishFilter(HashMap<String, String> map, String course_type_name, String teacher_name, String advisor_name) {
        dialog = null;
        if (!filterParams.equals(map)) {
            mFilterCourseTypeName = course_type_name;
            mFilterTeacherName = teacher_name;
            mFilterAdvisorName = advisor_name;
            filterParams.clear();
            filterParams.putAll(map);
            setRefreshing();
        }
    }

    @Override
    public void onCancelDilaog() {
        getSupportFragmentManager().beginTransaction().remove(dialog).commit();
        dialog.dismiss();
        dialog = null;
    }


    @Override
    public void onRefresh(int action) {
        if (mDataList == null) {
            mDataList = new ArrayList<>();
        }

        if (action == PullRecycler.ACTION_PULL_TO_REFRESH) {
            mCurrentPage = 1;
        }

        filterParams.put("status", mParamStatus + "");
        filterParams.put("fields", "classDynamic,classInfo");
        mPresenter.getListClasses(filterParams, mCurrentPage++);
    }

    @Override
    public void getListSuccess(ClassList classList) {
        loadSuccess(classList.lists);
        setUpTitle(mTitle + "(" + Math.max(classList.total, getDataCounts()) + ")");
    }

    @Override
    public void getListFail(String msg) {
        loadFailed();
        showToast(msg);
    }


    @Override
    protected BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_class_info, parent, false);
        return new StatisticsListViewHolder(view);
    }

    class StatisticsListViewHolder extends BaseViewHolder {
        @BindView(R.id.start_date)
        TextView startDateTv;
        @BindView(R.id.class_bg)
        RelativeLayout classBg;
        @BindView(R.id.class_signed)
        TextView signedTv;
        @BindView(R.id.class_name)
        TextView classNameTv;
        @BindView(R.id.lesson_schedule_layout)
        LinearLayout lessonScheduleLayout;
        @BindView(R.id.lesson_schedule)
        TextView lessonScheduleTv;
        @BindView(R.id.class_history)
        TextView classHistoryTv;
        @BindView(R.id.teacher_avatar)
        ImageView teacherAvatar;
        @BindView(R.id.teacher_name)
        TextView teacherName;
        @BindView(R.id.advisor_avatar)
        ImageView advisorAvatar;
        @BindView(R.id.advisor_name)
        TextView advisorName;
        @BindView(R.id.class_time_slot)
        RecyclerView recyclerView;
        @BindView(R.id.student_number_layout)
        RelativeLayout studentNumberLayout;
        @BindView(R.id.student_number)
        TextView studentNumberTv;

        public StatisticsListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onBindViewHolder(final int position) {
            final ClassPojo data = mDataList.get(position);
            startDateTv.setText(getString(R.string.class_open_date, DateUtil.parseSecond2Str(Long.parseLong(data.classInfo.open_date))));
            switch (data.classInfo.type) {
                case 1:
                    classBg.setBackgroundResource(R.drawable.bg_corner_top_primary);
                    break;
                case 10:
                    classBg.setBackgroundResource(R.drawable.bg_corner_top_intermediate);
                    break;
                case 20:
                    classBg.setBackgroundResource(R.drawable.bg_corner_top_senior);
                    break;
                case 30:
                    classBg.setBackgroundResource(R.drawable.bg_corner_top_movie);
                    break;
                default:
                    classBg.setBackgroundColor(getResources().getColor(R.color.comm_blue));
                    break;
            }
            classNameTv.setText(data.classInfo.course_type + "-" + data.classInfo.name);
            switch (data.classInfo.status) {
                case 2:
                    int day = AppUtil.getDayOfWeek();
                    for (ClassTimeSlot classTimeSlot : data.classInfo.timeslot) {
                        if (classTimeSlot.week == day) {
                            if (data.classDynamic.checkin_status == 0) {
                                signedTv.setVisibility(View.VISIBLE);
                            }
                            break;
                        }
                    }
                    break;
                case 1:
                case 3:
                    signedTv.setVisibility(View.GONE);
                    break;
            }
            lessonScheduleTv.setText(getString(R.string.lesson_schedule, data.classInfo.current_numbers, data.classInfo.lesson_times));
//            Glide.with(getApplicationContext()).load(data.avatar).fitCenter().placeholder(R.mipmap.img_header_default)
//                    .transform(new CircleTransform(getApplicationContext())).into(new ImageViewTarget<GlideDrawable>(teacherAvatar) {
//                @Override
//                protected void setResource(GlideDrawable resource) {
//                    teacherAvatar.setImageDrawable(resource);
//                }
//
//                @Override
//                public void setRequest(Request request) {
//                    teacherAvatar.setTag(position);
//                    teacherAvatar.setTag(R.id.glide_tag_id, request);
//                }
//
//                @Override
//                public Request getRequest() {
//                    return (Request) teacherAvatar.getTag(R.id.glide_tag_id);
//                }
//            });
            teacherName.setText(data.classInfo.teacher_name);
            advisorName.setText(data.classInfo.advisor_name);
//            classTimeTv.setText(data.classInfo.);
            CustomGridLayoutManager gridLayoutManager = new CustomGridLayoutManager(ClassListActivity.this, 2);
            gridLayoutManager.setScrollEnabled(false);
            recyclerView.setLayoutManager(gridLayoutManager);
            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(new TimeSlotAdapter(ClassListActivity.this, data.classInfo.timeslot));
            studentNumberTv.setText(getString(R.string.lesson_student_number, data.classInfo.current_numbers, data.classInfo.numbers));

            View.OnClickListener handler = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (view.getId()) {
                        case R.id.class_signed:
                            Intent signedIntent = new Intent(ClassListActivity.this, ClassAttendActivity.class);
                            signedIntent.putExtra(ClassAttendActivity.CLASSID, data.classInfo.id);
                            startActivity(signedIntent);
                            break;
                        case R.id.student_number_layout:
                            Intent classIntent = new Intent(ClassListActivity.this, StudentListActivity.class);
                            classIntent.putExtra(StudentListActivity.CLASSID, data.classInfo.id);
                            classIntent.putExtra(StudentListActivity.CLASSNAME, data.classInfo.name);
                            startActivity(classIntent);
                            break;
                        case R.id.class_history:
                            Intent historyIntent = new Intent(ClassListActivity.this, ClazCheckinTableActivity.class);
                            historyIntent.putExtra("claz_id", data.classInfo.id);
                            startActivity(historyIntent);
                            break;
                    }
                }
            };
            signedTv.setOnClickListener(handler);
            studentNumberLayout.setOnClickListener(handler);
            classHistoryTv.setOnClickListener(handler);
        }

        @Override
        public void onItemClick(View view, int position) {

        }
    }
}
