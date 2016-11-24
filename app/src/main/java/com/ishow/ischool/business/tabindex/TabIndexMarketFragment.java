package com.ishow.ischool.business.tabindex;

import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.support.v4.widget.NestedScrollView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.commonlib.http.ApiFactory;
import com.commonlib.util.UIUtil;
import com.commonlib.widget.LabelTextView;
import com.commonlib.widget.TopBottomTextView;
import com.ishow.ischool.R;
import com.ishow.ischool.application.Resource;
import com.ishow.ischool.bean.attribute.PieChartEntry;
import com.ishow.ischool.bean.statistics.MarketHome;
import com.ishow.ischool.business.campusperformance.market.Performance4MarketActivity;
import com.ishow.ischool.business.companymarketsaleprocess.CompanyMarketSaleprocessActivity;
import com.ishow.ischool.business.home.market.MarketSummaryActivity;
import com.ishow.ischool.common.api.ApiObserver;
import com.ishow.ischool.common.api.StatisticsApi;
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

public class TabIndexMarketFragment extends BaseFragment4Crm {

    @BindView(R.id.home_advances_received)
    TextView advancesReceivedTv;
    @BindView(R.id.index_refund_num)
    TextView refundNumTv;
    @BindView(R.id.index_refund_money)
    TextView refundMoneyTv;

    @BindView(R.id.student_entrance)
    TextView studentEntranceTv;
    @BindView(R.id.student_apply)
    TextView studentApplyTv;
    @BindView(R.id.student_full_pay)
    TextView studentFullPayTv;

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

    @BindView(R.id.process_full_money_rate)
    TopBottomTextView fullMoneyRateTv;
    @BindView(R.id.process_apply_rate)
    TopBottomTextView applyRateTv;


    @BindView(R.id.market_title_bg)
    View titlebarGroup;
    @BindView(R.id.home_scroll)
    NestedScrollView homeScrollView;

    @BindView(R.id.performance_title)
    TextView performanceTitleTv;
    @BindView(R.id.performance_subtitle)
    TextView performanceSubtitleTv;

    @BindView(R.id.process_title)
    TextView processTv;

    @BindView(R.id.home_circle_bg)
    ImageView homeCircleIv;

    @BindView(R.id.pie_chart)
    PieChartView pieChart;
    @BindView(R.id.pre_pay_group)
    FrameLayout prePayGroup;

    private int titlebarColor;
    public int mSpPosition = 0;
    public String mSpValue = "本周";

    HashMap<String, Integer> params = new HashMap<>();
    private int mParamTimeType;
    private int mParamTimeValue;

    public TabIndexMarketFragment() {
        // Required empty public constructor
    }

   /* public static TabIndexMarketFragment newInstance() {
        TabIndexMarketFragment fragment = new TabIndexMarketFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }*/

    @Override
    public int getLayoutId() {
        return R.layout.fragment_tab_index_market;
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
    }

    private void updateToolbarBg(int alph) {
        titlebarColor &= ~(0xFF << 24);
        titlebarColor |= alph << 24;
        titlebarGroup.setBackgroundColor(titlebarColor);
    }

    private void setupData() {
//        taskGetHomeMarketData();
        update(mSpPosition, mSpValue, TYPE_WEEK);
    }

    @OnClick({R.id.performance_title, R.id.process_group, R.id.performance_market_ll, R.id.pre_pay_group})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.performance_title: {

                break;
            }
            case R.id.process_group: {
                Intent intent = new Intent(getActivity(), CompanyMarketSaleprocessActivity.class);
                intent.putExtra("select_position", mSpPosition);
                JumpManager.jumpActivity(getActivity(), intent, Resource.NO_NEED_CHECK);
                break;
            }
            case R.id.performance_market_ll:
                JumpManager.jumpActivity(getActivity(), Performance4MarketActivity.class, Resource.NO_NEED_CHECK);
                break;
            case R.id.pre_pay_group:
                Intent intent = new Intent(getActivity(), MarketSummaryActivity.class);
                intent.putExtra(MarketSummaryActivity.P_START_TIME, params.get("begin_time"));
                intent.putExtra(MarketSummaryActivity.P_END_TIME, params.get("end_time"));
                JumpManager.jumpActivity(getActivity(), intent, Resource.NO_NEED_CHECK);
                break;
        }
    }


    private void taskGetHomeMarketData() {
        if (getContext() != null && ((TabIndexFragment)getParentFragment()).getCurrentPage() == 0) {
            handProgressbar(true);
        }
        ApiFactory.getInstance().getApi(StatisticsApi.class).getMarketHomeData(params).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiObserver<MarketHome>() {
                    @Override
                    public void onSuccess(MarketHome marketHome) {
                        processTv.setText(getString(R.string.data, mSpValue));
                        performanceSubtitleTv.setText(getString(R.string.data, mSpValue));
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

    public void update(int spPosition, String spValue, int type) {
        this.mSpPosition = spPosition;
        this.mSpValue = spValue;
        //1:本月 2:上月 3:本周 4:上周
        switch (type) {
            case TYPE_WEEK:
                params.put("time_type", type);
                params.put("begin_time", (int) AppUtil.getWeekStart());
                params.put("end_time", (int) AppUtil.getWeekEnd());
                break;
            case TYPE_LAST_WEEK:
                params.put("time_type", type);
                params.put("begin_time", (int) AppUtil.getLastWeekStart());
                params.put("end_time", (int) AppUtil.getLastWeekEnd());
                break;
            case TYPE_MONTH:
                params.put("time_type", type);
                params.put("begin_time", (int) AppUtil.getMonthStart());
                params.put("end_time", (int) AppUtil.getMonthEnd());
                break;
            case TYPE_LAST_MONTH:
                params.put("time_type", type);
                params.put("begin_time", (int) AppUtil.getLastMonthStart());
                params.put("end_time", (int) AppUtil.getLastMonthEnd());
                break;
        }
        taskGetHomeMarketData();
    }

    private void updateView(final MarketHome marketHome) {

        int max = Math.max(marketHome.market.full_challenge, Math.max(marketHome.market.real, marketHome.market.full_base));

        int real = marketHome.market.real * 100 / max;

        PropertyValuesHolder[] holders = new PropertyValuesHolder[14];
        holders[0] = AnimatorUtil.getPropertyValuesHolder("advancesReceivedTv", (int) marketHome.summary.prepayments);
        holders[1] = AnimatorUtil.getPropertyValuesHolder("refundNumTv", Integer.parseInt(marketHome.summary.refund_number));
        holders[2] = AnimatorUtil.getPropertyValuesHolder("refundMoneyTv", (int) Float.parseFloat(marketHome.summary.refund_amount));
        holders[3] = AnimatorUtil.getPropertyValuesHolder("studentEntranceTv", Integer.parseInt(marketHome.summary.add_number));
        holders[4] = AnimatorUtil.getPropertyValuesHolder("studentApplyTv", Integer.parseInt(marketHome.summary.apply_number));
        holders[5] = AnimatorUtil.getPropertyValuesHolder("studentFullPayTv", Integer.parseInt(marketHome.summary.full_amount_number));
        holders[6] = AnimatorUtil.getPropertyValuesHolder("performancePb", real < 1 ? 1 : real);
        holders[7] = AnimatorUtil.getPropertyValuesHolder("redTargetPb", marketHome.market.full_base * 100 / max);
        holders[8] = AnimatorUtil.getPropertyValuesHolder("rushTargetPb", marketHome.market.full_challenge * 100 / max);
        holders[9] = AnimatorUtil.getPropertyValuesHolder("performanceTv", marketHome.market.real);
        holders[10] = AnimatorUtil.getPropertyValuesHolder("redTargetTv", marketHome.market.full_base);
        holders[11] = AnimatorUtil.getPropertyValuesHolder("rushTargetTv", marketHome.market.full_challenge);
        holders[12] = AnimatorUtil.getPropertyValuesHolder("fullMoneyRateTv", Float.parseFloat(marketHome.process.full_amount_rate.replace("%", "")));
        holders[13] = AnimatorUtil.getPropertyValuesHolder("applyRateTv", Float.parseFloat(marketHome.process.openclass_apply_rate.replace("%", "")));

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

                studentEntranceTv.setText(animation.getAnimatedValue("studentEntranceTv").toString());
                studentApplyTv.setText(animation.getAnimatedValue("studentApplyTv").toString());
                studentFullPayTv.setText(animation.getAnimatedValue("studentFullPayTv").toString());

                performancePb.setProgress((int) animation.getAnimatedValue("performancePb"));
                redTargetPb.setProgress((int) animation.getAnimatedValue("redTargetPb"));
                rushTargetPb.setProgress((int) animation.getAnimatedValue("rushTargetPb"));
                performanceTv.setText(animation.getAnimatedValue("performanceTv").toString());
                redTargetTv.setText(animation.getAnimatedValue("redTargetTv").toString());
                rushTargetTv.setText(animation.getAnimatedValue("rushTargetTv").toString());
                fullMoneyRateTv.setText(AnimatorUtil.dffloat.format(animation.getAnimatedValue("fullMoneyRateTv")) + "%");
                applyRateTv.setText(AnimatorUtil.dffloat.format(animation.getAnimatedValue("applyRateTv")) + "%");
            }
        });
        valueAnimator.start();

        ArrayList<PieChartEntry> datas = new ArrayList<>();
        datas.add(new PieChartEntry(R.color.pie_color1, getString(R.string.campus_talk), marketHome.process.add_number));
        datas.add(new PieChartEntry(R.color.pie_color2, getString(R.string.open_class), marketHome.process.openclass_sign_number));
        datas.add(new PieChartEntry(R.color.pie_color3, getString(R.string.apply_numbers), marketHome.process.openclass_apply_number));
        datas.add(new PieChartEntry(R.color.pie_color6, getString(R.string.full_amount_number), marketHome.process.openclass_full_amount_number));
        pieChart.setDatas(datas);


    }
}
