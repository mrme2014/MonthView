package com.ishow.ischool.business.tabindex;

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
import com.ishow.ischool.bean.statistics.MarketHome;
import com.ishow.ischool.business.campusperformance.market.Performance4MarketActivity;
import com.ishow.ischool.common.api.ApiObserver;
import com.ishow.ischool.common.api.DataApi;
import com.ishow.ischool.common.base.BaseFragment4Crm;
import com.ishow.ischool.common.manager.JumpManager;
import com.ishow.ischool.util.AppUtil;
import com.ishow.ischool.widget.custom.RiseNumTextView;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class TabIndexMarketFragment extends BaseFragment4Crm {

    @BindView(R.id.home_advances_received)
    RiseNumTextView advancesReceivedTv;
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


    @BindView(R.id.home_titlebar_group)
    RelativeLayout titlebarGroup;
    @BindView(R.id.home_scroll)
    NestedScrollView homeScrollView;

    @BindView(R.id.up_progress_title)
    TextView upProgressTitleTv;
    @BindView(R.id.process_title)
    TextView processTv;

    @BindView(R.id.home_circle_bg)
    ImageView homeCircleIv;

    @BindView(R.id.home_choose_time_sp)
    MySpinner chooseTimeSpinner;

    private int titlebarColor;

    HashMap<String, Integer> params = new HashMap<>();


    public TabIndexMarketFragment() {
        // Required empty public constructor
    }

    public static TabIndexMarketFragment newInstance() {
        TabIndexMarketFragment fragment = new TabIndexMarketFragment();
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
                taskGetHomeMarketData();

            }
        });


    }

    private void updateToolbarBg(int alph) {
        titlebarColor &= ~(0xFF << 24);
        titlebarColor |= alph << 24;
        titlebarGroup.setBackgroundColor(titlebarColor);
    }

    private void setupData() {
//        taskGetHomeMarketData();
        chooseTimeSpinner.setPosition(1);
    }

    @OnClick({R.id.up_progress_title, R.id.process_title, R.id.performance_market_ll})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.up_progress_title: {

                break;
            }
            case R.id.process_title: {
                break;
            }
            case R.id.performance_market_ll:
                JumpManager.jumpActivity(getActivity(), Performance4MarketActivity.class, Resource.NO_NEED_CHECK);
                break;
        }
    }


    private void taskGetHomeMarketData() {
        computerParams(TYPE_LAST_WEEK);

        ApiFactory.getInstance().getApi(DataApi.class).getMarketHomeData(params).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiObserver<MarketHome>() {
                    @Override
                    public void onSuccess(MarketHome marketHome) {
                        updateView(marketHome);
                    }

                    @Override
                    public void onError(String msg) {
                        showToast(msg);
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
    }

    private void updateView(final MarketHome marketHome) {

        advancesReceivedTv.withNumber(marketHome.summary.prepayments);
//        advancesReceivedTv.withNumber(324000);
        advancesReceivedTv.setOnEndListener(new RiseNumTextView.OnEndListener() {
            @Override
            public void onEndFinish() {
                advancesReceivedTv.setText(marketHome.summary.prepayments + "");
            }
        });
        advancesReceivedTv.start();

        refundNumTv.setText(marketHome.summary.refund_number);
        refundMoneyTv.setText(marketHome.summary.refund_amount);

        studentEntranceTv.setText(marketHome.summary.add_number);
        studentApplyTv.setText(marketHome.summary.apply_number);
        studentFullPayTv.setText(marketHome.summary.full_amount_number);


        int max = Math.max(marketHome.market.full_challenge, Math.max(marketHome.market.real, marketHome.market.full_base));

        performancePb.setProgress(marketHome.market.real * 100 / max);
        redTargetPb.setProgress(marketHome.market.full_base * 100 / max);
        rushTargetPb.setProgress(marketHome.market.full_challenge * 100 / max);
        performanceTv.setText(marketHome.market.real + "");
        redTargetTv.setText(marketHome.market.full_base + "");
        rushTargetTv.setText(marketHome.market.full_challenge + "");

        fullMoneyRateTv.setText(marketHome.process.full_amount_rate);
        applyRateTv.setText(marketHome.process.openclass_apply_rate);
    }

}
