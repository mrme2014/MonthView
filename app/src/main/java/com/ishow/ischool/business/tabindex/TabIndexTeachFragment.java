package com.ishow.ischool.business.tabindex;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.commonlib.http.ApiFactory;
import com.commonlib.util.UIUtil;
import com.commonlib.widget.LabelTextView;
import com.commonlib.widget.TopBottomTextView;
import com.commonlib.widget.base.MySpinner;
import com.ishow.ischool.R;
import com.ishow.ischool.application.Resource;
import com.ishow.ischool.bean.statistics.EducationHome;
import com.ishow.ischool.business.campusperformance.education.Performance4EduActivity;
import com.ishow.ischool.business.home.market.MarketSummaryActivity;
import com.ishow.ischool.business.home.teach.TeachSummaryActivity;
import com.ishow.ischool.common.api.ApiObserver;
import com.ishow.ischool.common.api.DataApi;
import com.ishow.ischool.common.base.BaseFragment4Crm;
import com.ishow.ischool.common.manager.JumpManager;
import com.ishow.ischool.util.AppUtil;
import com.ishow.ischool.widget.custom.PieChartView;
import com.ishow.ischool.widget.custom.RiseNumTextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class TabIndexTeachFragment extends BaseFragment4Crm {

    @BindView(R.id.home_advances_received)
    RiseNumTextView advancesReceivedTv;
    @BindView(R.id.index_refund_num)
    TextView refundNumTv;
    @BindView(R.id.index_refund_money)
    TextView refundMoneyTv;

    @BindView(R.id.student_all)
    TextView studentAllTv;
    @BindView(R.id.student_final)
    TextView studentFinalTv;
    @BindView(R.id.student_should)
    TextView studentShouldTv;
    @BindView(R.id.student_stop)
    TextView studentStopTv;

    @BindView(R.id.up_progress_bar_performance)
    ProgressBar performancePb;
    @BindView(R.id.up_progress_bar_red_target)
    ProgressBar redTargetPb;
    @BindView(R.id.up_progress_bar_rush_target)
    ProgressBar rushTargetPb;

    @BindView(R.id.up_progress_performance)
    LabelTextView performanceTv;
    @BindView(R.id.up_progress_red_target)
    LabelTextView redTargetTv;
    @BindView(R.id.up_progress_rush_target)
    LabelTextView rushTargetTv;

    @BindView(R.id.full_pay_rate)
    TopBottomTextView fullPayRateTv;
    @BindView(R.id.upgrade_rate)
    TopBottomTextView upgradeRateTv;


    @BindView(R.id.home_titlebar_group)
    RelativeLayout titlebarGroup;
    @BindView(R.id.home_scroll)
    NestedScrollView homeScrollView;

    @BindView(R.id.up_progress_title)
    TextView upProgressTitleTv;
    @BindView(R.id.up_progress_subtitle)
    TextView upProgressSubtitleTv;
    @BindView(R.id.process_title)
    TextView processTv;

    @BindView(R.id.home_circle_bg)
    ImageView homeCircleIv;

    @BindView(R.id.home_choose_time_sp)
    MySpinner chooseTimeSpinner;

    @BindView(R.id.pie_chart)
    PieChartView pieChart;

    private int titlebarColor;

    HashMap<String, Integer> params = new HashMap<>();


    public static TabIndexTeachFragment newInstance() {
        TabIndexTeachFragment fragment = new TabIndexTeachFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_tab_index_teach;
    }

    @Override
    public void init() {
        initView();
        setupData();
    }

    private void initView() {
        titlebarColor = getResources().getColor(R.color.colorPrimary);
        final int max = UIUtil.dip2px(getActivity(), 200);
        homeScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY > max) {
                    return;
                }
                updateToolbarBg(scrollY * 255 / max);
            }
        });
        updateToolbarBg(0);

        AnimationSet animationSet = new AnimationSet(true);
        //参数1：从哪个旋转角度开始
        //参数2：转到什么角度
        //后4个参数用于设置围绕着旋转的圆的圆心在哪里
        //参数3：确定x轴坐标的类型，有ABSOLUT绝对坐标、RELATIVE_TO_SELF相对于自身坐标、RELATIVE_TO_PARENT相对于父控件的坐标
        //参数4：x轴的值，0.5f表明是以自身这个控件的一半长度为x轴
        //参数5：确定y轴坐标的类型
        //参数6：y轴的值，0.5f表明是以自身这个控件的一半长度为x轴
        RotateAnimation rotateAnimation = new RotateAnimation(0, 360,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(2000);
        animationSet.addAnimation(rotateAnimation);
        homeCircleIv.startAnimation(animationSet);

        String[] chooseTimes = getResources().getStringArray(R.array.home_choose_times);
        ArrayList<String> chooseTimesArray = new ArrayList<>();
        for (int i = 0; i < chooseTimes.length; i++) {
            chooseTimesArray.add(chooseTimes[i]);
        }
        chooseTimeSpinner.setDatas(chooseTimesArray);

        chooseTimeSpinner.setOnItemSelectedListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        computerParams(TYPE_WEEK);
                        break;
                    case 1:
                        computerParams(TYPE_LAST_WEEK);
                        break;
                    case 2:
                        computerParams(TYPE_MONTH);
                        break;
                    case 3:
                        computerParams(TYPE_LAST_MONTH);
                        break;
                }

                processTv.setText(getString(R.string.data, chooseTimeSpinner.getSelectedValue()));
                upProgressSubtitleTv.setText(getString(R.string.data, chooseTimeSpinner.getSelectedValue()));
                taskGetHomeTeachData();

            }
        });
    }

    private void updateToolbarBg(int alph) {
        titlebarColor &= ~(0xFF << 24);
        titlebarColor |= alph << 24;
        titlebarGroup.setBackgroundColor(titlebarColor);
    }

    private void setupData() {
        chooseTimeSpinner.setPosition(1);
    }

    @OnClick({R.id.process_group, R.id.performance_education_ll, R.id.pre_pay_group})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.performance_education_ll:
                JumpManager.jumpActivity(getActivity(), Performance4EduActivity.class, Resource.NO_NEED_CHECK);
                break;
            case R.id.process_group:
                JumpManager.jumpActivity(getActivity(), Performance4EduActivity.class, Resource.NO_NEED_CHECK);
                break;
            case R.id.pre_pay_group:
                Intent intent = new Intent(getActivity(), TeachSummaryActivity.class);
                intent.putExtra(TeachSummaryActivity.P_START_TIME, params.get("start_time"));
                intent.putExtra(TeachSummaryActivity.P_END_TIME, params.get("end_time"));
                JumpManager.jumpActivity(getActivity(), intent, Resource.NO_NEED_CHECK);
                break;
        }
    }


    private void taskGetHomeTeachData() {
        ApiFactory.getInstance().getApi(DataApi.class).getEducationHomeData(params).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiObserver<EducationHome>() {
                    @Override
                    public void onSuccess(EducationHome marketHome) {
                        updateView(marketHome);
                    }

                    @Override
                    public void onError(String msg) {
                        showToast(msg);
                    }

                    @Override
                    protected boolean isAlive() {
                        return !isActivityFinished();
                    }
                });
    }

    private static final int TYPE_WEEK = 3;
    private static final int TYPE_LAST_WEEK = 4;
    private static final int TYPE_MONTH = 1;
    private static final int TYPE_LAST_MONTH = 2;

    private void computerParams(int type) {
        //1:本月 2:上月 3:本周 4:上周
        switch (type) {
            case TYPE_WEEK:
                params.put("time_type", type);
                params.put("start_time", (int) AppUtil.getWeekStart());
                params.put("end_time", (int) AppUtil.getWeekEnd());
                break;
            case TYPE_LAST_WEEK:
                params.put("time_type", type);
                params.put("start_time", (int) AppUtil.getLastWeekStart());
                params.put("end_time", (int) AppUtil.getLastWeekEnd());
                break;
            case TYPE_MONTH:
                params.put("time_type", type);
                params.put("start_time", (int) AppUtil.getMonthStart());
                params.put("end_time", (int) AppUtil.getMonthEnd());
                break;
            case TYPE_LAST_MONTH:
                params.put("time_type", type);
                params.put("start_time", (int) AppUtil.getLastMonthStart());
                params.put("end_time", (int) AppUtil.getLastMonthEnd());
                break;
        }
    }


    private void updateView(final EducationHome educationHome) {

        advancesReceivedTv.withNumber((int) Float.parseFloat(educationHome.data.body[7]));
        advancesReceivedTv.setOnEndListener(new RiseNumTextView.OnEndListener() {
            @Override
            public void onEndFinish() {
                advancesReceivedTv.setText(educationHome.data.body[7]);
            }
        });
        advancesReceivedTv.start();

        refundNumTv.setText(Integer.parseInt(educationHome.data.body[4]) + Integer.parseInt(educationHome.data.body[5]) + getString(R.string.people_unit));
        refundMoneyTv.setText(educationHome.data.body[6] + getString(R.string.money_unit));

        studentAllTv.setText(educationHome.data.body[0]);
        studentFinalTv.setText(educationHome.data.body[1]);
        studentShouldTv.setText(educationHome.data.body[2]);
        studentStopTv.setText(educationHome.data.body[3]);


        int max = (int) Math.max(educationHome.education.full_challenge, Math.max(educationHome.education.real, educationHome.education.full_base));

        performancePb.setProgress((int) educationHome.education.real * 100 / max);
        redTargetPb.setProgress((int) educationHome.education.full_base * 100 / max);
        rushTargetPb.setProgress((int) educationHome.education.full_challenge * 100 / max);

        performanceTv.setText(educationHome.education.real + "%");
        redTargetTv.setText(educationHome.education.full_base + "%");
        rushTargetTv.setText(educationHome.education.full_challenge + "%");

        upgradeRateTv.setText(educationHome.TeachingProcess.selfChartData.body[0].get(4));
        fullPayRateTv.setText(educationHome.TeachingProcess.selfChartData.body[0].get(5));


        List<String> list = new ArrayList<>();
        list.add(educationHome.TeachingProcess.selfChartData.body[0].get(0));
        list.add(educationHome.TeachingProcess.selfChartData.body[0].get(1));
        list.add(educationHome.TeachingProcess.selfChartData.body[0].get(2));
        list.add(educationHome.TeachingProcess.selfChartData.body[0].get(3));
        pieChart.setFloorProperty(list, educationHome.TeachingProcess.selfChartData.body[0].get(4), educationHome.TeachingProcess.selfChartData.body[0].get(5));
    }

}
