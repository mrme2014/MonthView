package com.ishow.ischool.business.tabindex;

import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.ishow.ischool.bean.attribute.PieChartEntry;
import com.ishow.ischool.bean.statistics.EducationHome;
import com.ishow.ischool.business.campusperformance.education.Performance4EduActivity;
import com.ishow.ischool.business.home.teach.TeachSummaryActivity;
import com.ishow.ischool.business.teachprocess.TeachProcessActivity4Home;
import com.ishow.ischool.common.api.ApiObserver;
import com.ishow.ischool.common.api.DataApi;
import com.ishow.ischool.common.base.BaseFragment4Crm;
import com.ishow.ischool.common.manager.JumpManager;
import com.ishow.ischool.util.AnimatorUtil;
import com.ishow.ischool.util.AppUtil;
import com.ishow.ischool.widget.custom.PieChartView;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class TabIndexTeachFragment extends BaseFragment4Crm implements TabIndexFragment.TabFragment {

    @BindView(R.id.home_advances_received)
    TextView advancesReceivedTv;
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
    @BindView(R.id.label_index_advances_received)
    TextView labelIndexAdvancesReceivedTv;

    @BindView(R.id.pie_chart)
    PieChartView pieChart;

    private int titlebarColor;

    HashMap<String, Integer> params = new HashMap<>();
    private TabIndexFragment parentFragment;


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
        labelIndexAdvancesReceivedTv.setText(R.string.income);
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
//                upProgressSubtitleTv.setText(getString(R.string.data, chooseTimeSpinner.getSelectedValue()));
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
        chooseTimeSpinner.setPosition(0);
    }

    @OnClick({R.id.process_group, R.id.performance_education_ll, R.id.pre_pay_group, R.id.title_radio_1})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.performance_education_ll:
                JumpManager.jumpActivity(getActivity(), Performance4EduActivity.class, Resource.NO_NEED_CHECK);
                break;
            case R.id.process_group:
                JumpManager.jumpActivity(getActivity(), TeachProcessActivity4Home.class, Resource.NO_NEED_CHECK);
                break;
            case R.id.pre_pay_group:
                Intent intent = new Intent(getActivity(), TeachSummaryActivity.class);
                intent.putExtra(TeachSummaryActivity.P_START_TIME, params.get("start_time"));
                intent.putExtra(TeachSummaryActivity.P_END_TIME, params.get("end_time"));
                JumpManager.jumpActivity(getActivity(), intent, Resource.NO_NEED_CHECK);
                break;
            case R.id.title_radio_1:
                setCurrentItem(0);
                break;
        }
    }


    private void taskGetHomeTeachData() {
        if (parentFragment.getCurrentItem() == 1) {
            handProgressbar(true);
        }
        ApiFactory.getInstance().getApi(DataApi.class).getEducationHomeData(params).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiObserver<EducationHome>() {
                    @Override
                    public void onSuccess(EducationHome marketHome) {
                        handProgressbar(false);
                        updateView(marketHome);
                    }

                    @Override
                    public void onError(String msg) {
                        handProgressbar(false);
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

        float max = Math.max(educationHome.education.full_challenge, Math.max(educationHome.education.real, educationHome.education.full_base));

        PropertyValuesHolder[] holders = new PropertyValuesHolder[15];
        holders[0] = AnimatorUtil.getPropertyValuesHolder("advancesReceivedTv", (int) Float.parseFloat(educationHome.data.body[7]));
        holders[1] = AnimatorUtil.getPropertyValuesHolder("refundNumTv", Integer.parseInt(educationHome.data.body[4]) + Integer.parseInt(educationHome.data.body[5]));
        holders[2] = AnimatorUtil.getPropertyValuesHolder("refundMoneyTv", (int) Float.parseFloat(educationHome.data.body[6]));

        holders[3] = AnimatorUtil.getPropertyValuesHolder("studentAllTv", Integer.parseInt(educationHome.data.body[0]));
        holders[4] = AnimatorUtil.getPropertyValuesHolder("studentFinalTv", Integer.parseInt(educationHome.data.body[1]));
        holders[5] = AnimatorUtil.getPropertyValuesHolder("studentShouldTv", Integer.parseInt(educationHome.data.body[2]));
        holders[14] = AnimatorUtil.getPropertyValuesHolder("studentStopTv", Integer.parseInt(educationHome.data.body[3]));

        holders[6] = AnimatorUtil.getPropertyValuesHolder("performancePb", educationHome.education.real * 100 / max);
        holders[7] = AnimatorUtil.getPropertyValuesHolder("redTargetPb", educationHome.education.full_base * 100 / max);
        holders[8] = AnimatorUtil.getPropertyValuesHolder("rushTargetPb", educationHome.education.full_challenge * 100 / max);

        holders[9] = AnimatorUtil.getPropertyValuesHolder("performanceTv", educationHome.education.real);
        holders[10] = AnimatorUtil.getPropertyValuesHolder("redTargetTv", educationHome.education.full_base);
        holders[11] = AnimatorUtil.getPropertyValuesHolder("rushTargetTv", educationHome.education.full_challenge);

        holders[12] = AnimatorUtil.getPropertyValuesHolder("upgradeRateTv", Float.parseFloat(educationHome.TeachingProcess.selfChartData.body[0].get(4).replace("%", "")));
        holders[13] = AnimatorUtil.getPropertyValuesHolder("fullPayRateTv", Float.parseFloat(educationHome.TeachingProcess.selfChartData.body[0].get(5).replace("%", "")));

        ValueAnimator valueAnimator = ValueAnimator.ofPropertyValuesHolder(holders);
        valueAnimator.setDuration(1000);
        final String ren = getString(R.string.people_unit);
        final String yuan = getString(R.string.money_unit);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                int received = (int) animation.getAnimatedValue("advancesReceivedTv");
                advancesReceivedTv.setText(AnimatorUtil.dfint.format(received));
                refundNumTv.setText(animation.getAnimatedValue("refundNumTv") + ren);
                refundMoneyTv.setText(AnimatorUtil.dfint.format(animation.getAnimatedValue("refundMoneyTv")) + yuan);

                studentAllTv.setText(animation.getAnimatedValue("studentAllTv").toString());
                studentFinalTv.setText(animation.getAnimatedValue("studentFinalTv").toString());
                studentShouldTv.setText(animation.getAnimatedValue("studentShouldTv").toString());
                studentStopTv.setText(animation.getAnimatedValue("studentStopTv").toString());

                performancePb.setProgress((int) (float) animation.getAnimatedValue("performancePb"));
                redTargetPb.setProgress((int) (float) animation.getAnimatedValue("redTargetPb"));
                rushTargetPb.setProgress((int) (float) animation.getAnimatedValue("rushTargetPb"));
                performanceTv.setText(animation.getAnimatedValue("performanceTv") + "%");
                redTargetTv.setText(animation.getAnimatedValue("redTargetTv") + "%");
                rushTargetTv.setText(animation.getAnimatedValue("rushTargetTv") + "%");

                upgradeRateTv.setText(AnimatorUtil.dffloat.format(animation.getAnimatedValue("upgradeRateTv")) + "%");
                fullPayRateTv.setText(AnimatorUtil.dffloat.format(animation.getAnimatedValue("fullPayRateTv")) + "%");
            }
        });
        valueAnimator.start();

        ArrayList<PieChartEntry> datas = new ArrayList<>();
        datas.add(new PieChartEntry(R.color.pie_color1, getString(R.string.campus_talk), educationHome.TeachingProcess.selfChartData.body[0].get(0)));
        datas.add(new PieChartEntry(R.color.pie_color2, getString(R.string.open_class), educationHome.TeachingProcess.selfChartData.body[0].get(1)));
        datas.add(new PieChartEntry(R.color.pie_color3, getString(R.string.apply_numbers), educationHome.TeachingProcess.selfChartData.body[0].get(2)));
        datas.add(new PieChartEntry(R.color.pie_color6, getString(R.string.full_amount_number), educationHome.TeachingProcess.selfChartData.body[0].get(3)));
        pieChart.setDatas(datas);


    }

    public Fragment setParentFragment(TabIndexFragment fragment) {
        this.parentFragment = fragment;
        return this;
    }

    @Override
    public void setCurrentItem(int index) {
        if (parentFragment != null) {
            parentFragment.setCurrentItem(index);
        }
    }
}
