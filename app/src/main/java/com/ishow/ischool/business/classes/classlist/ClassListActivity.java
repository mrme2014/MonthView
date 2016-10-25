package com.ishow.ischool.business.classes.classlist;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.commonlib.util.DateUtil;
import com.commonlib.widget.pull.BaseViewHolder;
import com.commonlib.widget.pull.PullRecycler;
import com.ishow.ischool.R;
import com.ishow.ischool.adpter.TimeSlotAdapter;
import com.ishow.ischool.bean.classes.ClassList;
import com.ishow.ischool.bean.classes.ClassPojo;
import com.ishow.ischool.business.classattention.ClassAttendActivity;
import com.ishow.ischool.business.classes.memberlist.StudentListActivity;
import com.ishow.ischool.common.base.BaseListActivity4Crm;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by wqf on 16/10/21.
 */
public class ClassListActivity extends BaseListActivity4Crm<ClassListPresenter, ClassListModel, ClassPojo> implements ClassListContract.View {

    private ArrayList<ClassPojo> mNotInClass, mInClass, mHasBeenClass;

    @Override
    protected void initEnv() {
        super.initEnv();
        mNotInClass = new ArrayList<>();
        mInClass = new ArrayList<>();
    }

    @Override
    protected void setUpView() {
        super.setUpView();
        mToolbarTitle.setText(R.string.in_class);
        mToolbarTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.icon_adress_down, 0);
        mToolbarTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        setUpMenu(R.menu.menu_class_list);
    }

    @Override
    protected RecyclerView.ItemDecoration getItemDecoration() {
        return null;
    }

    //    @Override
//    public boolean onMenuItemClick(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.action_filter:
//                if (dialog == null) {
//                    dialog = StatisticsFilterFragment.newInstance(filterParams, mFilterSourceName, mFilterCollegeName, mFilterReferrerName);
//                    dialog.setOnFilterCallback(ClassListActivity.this);
//                }
//                dialog.show(getSupportFragmentManager(), "dialog");
//                break;
//        }
//        return true;
//    }



    @Override
    public void onRefresh(int action) {
        if (mDataList == null) {
            mDataList = new ArrayList<>();
        }

        if (action == PullRecycler.ACTION_PULL_TO_REFRESH) {
            mCurrentPage = 1;
        }

        HashMap<String, String> params = new HashMap<>();
        params.put("status", "2");
        mPresenter.getListClasses(params, mCurrentPage++);
    }

    @Override
    public void getListSuccess(ClassList classList) {

        loadSuccess(classList.lists);
//        setUpTitle(getString(R.string.in_class) + getString(R.string.student_statistics_total, Math.max(classList.total, getDataCounts())));
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
            recyclerView.setLayoutManager(new GridLayoutManager(ClassListActivity.this, 2));
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
                    }
                }
            };
            signedTv.setOnClickListener(handler);

        }

        @Override
        public void onItemClick(View view, int position) {
            ClassPojo data = mDataList.get(position);
            Intent classIntent = new Intent(ClassListActivity.this, StudentListActivity.class);
            classIntent.putExtra(StudentListActivity.CLASSID, data.classInfo.id);
            classIntent.putExtra(StudentListActivity.CLASSNAME, data.classInfo.name);
            startActivity(classIntent);
        }
    }
}
