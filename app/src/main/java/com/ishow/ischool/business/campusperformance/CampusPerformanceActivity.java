package com.ishow.ischool.business.campusperformance;

import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.AxisValueFormatter;
import com.ishow.ischool.R;
import com.ishow.ischool.adpter.CampusSelectAdapter;
import com.ishow.ischool.common.base.BaseActivity4Crm;
import com.ishow.ischool.common.manager.CampusManager;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Created by wqf on 16/9/8.
 */
public class CampusPerformanceActivity extends BaseActivity4Crm<CampusPerformancePresenter, CampusPerformanceModel> implements CampusPerformanceContract.View {

    @BindView(R.id.combinedChart)
    CombinedChart mCombinedChart;
    @BindView(R.id.filter_layout)
    LinearLayout filertLayout;
    @BindView(R.id.filter_type)
    TextView filertType;
    @BindView(R.id.filter_campus)
    TextView filertCampus;
    @BindView(R.id.filter_date)
    TextView filertDate;

    private final int mStart = 0;
    private final int mItemCount = 12;

    @Override
    protected void setUpContentView() {
        setContentView(R.layout.activity_campusperformance, R.string.campus_performance, -1, MODE_BACK);
    }

    @Override
    protected void setUpView() {
        initChart();
    }

    @Override
    protected void setUpData() {

    }

    void initChart() {
        mCombinedChart.setDescription("");
        mCombinedChart.setBackgroundColor(Color.WHITE);
//        mCombinedChart.setDrawGridBackground(false);     //设置图表内格子背景是否显示，默认是false
//        mCombinedChart.setDrawBarShadow(false);         //是否显示阴影。启动它会降低约40%的性能，默认false
        mCombinedChart.setHighlightFullBarEnabled(false);

        // draw bars behind lines       调整实体显示顺序
        mCombinedChart.setDrawOrder(new CombinedChart.DrawOrder[]{
                CombinedChart.DrawOrder.BAR, CombinedChart.DrawOrder.BUBBLE, CombinedChart.DrawOrder.CANDLE, CombinedChart.DrawOrder.LINE, CombinedChart.DrawOrder.SCATTER
        });

        Legend legend = mCombinedChart.getLegend();      // 设置坐标线描述的样式
        legend.setEnabled(true);
        legend.setFormSize(10f);
        legend.setForm(Legend.LegendForm.SQUARE);
        legend.setWordWrapEnabled(true);
        legend.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);


        YAxis leftAxis = mCombinedChart.getAxisLeft();
        leftAxis.setDrawAxisLine(false);       //是否绘制坐标轴的线，即含有坐标的那条线，默认是true
//        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);     默认
//        leftAxis.setDrawGridLines(true);     //是否显示Y坐标轴上的刻度横线，默认是true
        leftAxis.setSpaceTop(20);           //Y轴坐标距顶有多少距离，即留白
        mCombinedChart.getAxisRight().setEnabled(false);    // 隐藏右边的坐标轴

        XAxis xAxis = mCombinedChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);      //是否显示X坐标轴上的刻度竖线，默认是true
       /* xAxis.setAxisMinValue(itemcount);
        xAxis.setAxisMaxValue(itemcount + 2);*/

        xAxis.setGranularity(0f);           // 设置轴最小间隔

        xAxis.setLabelCount(13);
        xAxis.setLabelRotationAngle(-80);       //设置x轴字体显示角度
        mCombinedChart.getXAxis().setAxisMinValue(mStart);
        mCombinedChart.getXAxis().setAxisMaxValue(mStart + mItemCount + 1);

        xAxis.setValueFormatter(new AxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return mMonths[(int) value % mMonths.length];
            }

            @Override
            public int getDecimalDigits() {
                return 0;
            }
        });

        CombinedData data = new CombinedData();

        data.setData(generateLineData());
        data.setData(generateBarData());

//        xAxis.setAxisMaximum(data.getXMax() + 0.25f);

        mCombinedChart.setData(data);
        mCombinedChart.invalidate();
    }

    private LineData generateLineData() {
        LineData lineData = new LineData();

        for (int z = 0; z < 2; z++) {
            ArrayList<Entry> entries = new ArrayList<Entry>();

            for (int index = 0; index < mStart + mItemCount; index++) {
                entries.add(new Entry(index + 1f, getRandom(15, 20)));
            }

            int color;
            if (z == 0) {
                color = Color.rgb(240, 128, 128);
            } else {
                color = Color.rgb(60, 220, 78);
            }

            LineDataSet set = new LineDataSet(entries, "Line DataSet" + (z + 1));
            set.setColor(color);
            set.setLineWidth(1f);
            set.setCircleColor(color);
            set.setCircleRadius(3f);
            set.setFillColor(color);
            set.setMode(LineDataSet.Mode.LINEAR);
            set.setDrawValues(true);
            set.setValueTextSize(10f);
            set.setValueTextColor(color);

            set.setAxisDependency(YAxis.AxisDependency.LEFT);     //以左边坐标轴为准 还是以右边坐标轴
            lineData.addDataSet(set);
        }

        return lineData;
    }

    private BarData generateBarData() {
        ArrayList<BarEntry> entries1 = new ArrayList<BarEntry>();

        for (int index = 0; index < mStart + mItemCount; index++) {
            entries1.add(new BarEntry(index + 1f, getRandom(25, 25)));
        }

        BarDataSet set1 = new BarDataSet(entries1, "Bar 1");
        set1.setColor(Color.rgb(135,206,250));
        set1.setValueTextColor(Color.rgb(135,206,250));
        set1.setValueTextSize(10f);
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);      //以左边坐标轴为准 还是以右边坐标轴为基准

        float groupSpace = 0.06f;
        float barSpace = 0.02f; // x2 dataset

        //        float groupSpace = 0.06f;
//        float barSpace = 0.02f; // x2 dataset
        float barWidth = 0.45f; // x2 dataset
        // (0.45 + 0.02) * 2 + 0.06 = 1.00 -> interval per "group"

        BarData d = new BarData(set1);
        d.setBarWidth(barWidth);

        // make this BarData object grouped
//        d.groupBars(0, groupSpace, barSpace); // start at x = 0

        return d;
    }

    protected float getRandom(float range, float startsfrom) {
        return (float) (Math.random() * range) + startsfrom;
    }

    protected String[] mMonths = new String[] {
            "", "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Okt", "Nov", "Dec"
    };


    @OnClick({R.id.filter_type, R.id.filter_campus, R.id.filter_date, R.id.detail_layout})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.filter_type:
                if (mTypePopup != null && isTypeShow) {
                    dismissTypePopup();
                } else {
                    showTypePopup();
                }
                break;
            case R.id.filter_campus:
                if (mCampusPopup != null && isCampusShow) {
                    dismissCampusPopup();
                } else {
                    showCampusPopup();
                }
                break;
            case R.id.filter_date:
                if (mDatePopup != null && isDateShow) {
                    dismissDatePopup();
                } else {
                    showDatePopup();
                }
                break;
            case R.id.detail_layout:

                break;
        }
    }


    private PopupWindow mTypePopup, mCampusPopup, mDatePopup;
    private boolean isTypeShow,  isCampusShow, isDateShow;
    private ArrayList<String> mList = new ArrayList<>();
    LinearLayoutManager layoutManager;
    CampusSelectAdapter mAdapter;
    void showCampusPopup() {
        if (mCampusPopup == null) {
            View contentView = LayoutInflater.from(CampusPerformanceActivity.this).inflate(R.layout.filter_campus_layout, null);
            mCampusPopup = new PopupWindow(contentView);
            mCampusPopup.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            mCampusPopup.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
            //外部是否可以点击
            mCampusPopup.setBackgroundDrawable(new BitmapDrawable());
            mCampusPopup.setOutsideTouchable(true);

            RecyclerView recyclerView = (RecyclerView) contentView.findViewById(R.id.recyclerview);
            TextView resetTv = (TextView) contentView.findViewById(R.id.filter_reset);
            TextView okTv = (TextView) contentView.findViewById(R.id.filter_ok);
            View blankView = contentView.findViewById(R.id.blank_view_campus);
            resetTv.setOnClickListener(onClickListener);
            okTv.setOnClickListener(onClickListener);
            blankView.setOnClickListener(onClickListener);

            mList.addAll(CampusManager.getInstance().getCampusNames());
            layoutManager = new LinearLayoutManager(CampusPerformanceActivity.this);
            recyclerView.setLayoutManager(layoutManager);
            mAdapter = new CampusSelectAdapter(CampusPerformanceActivity.this, mList);
            recyclerView.setAdapter(mAdapter);
        }
        mCampusPopup.showAsDropDown(filertLayout);
        isCampusShow = true;
        isTypeShow = false;
        isDateShow = false;
    }

    void dismissCampusPopup() {
        mCampusPopup.dismiss();
        isCampusShow = false;
    }

    void showTypePopup() {
        if (mTypePopup == null) {
            View contentView = LayoutInflater.from(CampusPerformanceActivity.this).inflate(R.layout.filter_type_layout, null);
            mTypePopup = new PopupWindow(contentView);
            mTypePopup.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            mTypePopup.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
            //外部是否可以点击
            mTypePopup.setBackgroundDrawable(new BitmapDrawable());
            mTypePopup.setOutsideTouchable(true);

            TextView performanceTv = (TextView) contentView.findViewById(R.id.performance_tv);
            TextView numberTv = (TextView) contentView.findViewById(R.id.number_tv);
            View blankView = contentView.findViewById(R.id.blank_view_type);
            performanceTv.setOnClickListener(onClickListener);
            numberTv.setOnClickListener(onClickListener);
            blankView.setOnClickListener(onClickListener);
        }
        mTypePopup.showAsDropDown(filertLayout);
        isTypeShow = true;
        isCampusShow = false;
        isDateShow = false;
    }

    void dismissTypePopup() {
        mTypePopup.dismiss();
        isTypeShow = false;
    }

    void showDatePopup() {
        if (mDatePopup == null) {
            View contentView = LayoutInflater.from(CampusPerformanceActivity.this).inflate(R.layout.filter_date_layout, null);
            mDatePopup = new PopupWindow(contentView);
            mDatePopup.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            mDatePopup.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
            //外部是否可以点击
            mDatePopup.setBackgroundDrawable(new BitmapDrawable());
            mDatePopup.setOutsideTouchable(true);

            TextView startDateTv = (TextView) contentView.findViewById(R.id.start_date);
            TextView endDateTv = (TextView) contentView.findViewById(R.id.end_date);
            View blankView = contentView.findViewById(R.id.blank_view_date);
            startDateTv.setOnClickListener(onClickListener);
            endDateTv.setOnClickListener(onClickListener);
            blankView.setOnClickListener(onClickListener);
        }
        mDatePopup.showAsDropDown(filertLayout);
        isDateShow = true;
        isCampusShow = false;
        isTypeShow = false;
    }

    void dismissDatePopup() {
        mDatePopup.dismiss();
        isDateShow = false;
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.filter_reset:
                    dismissCampusPopup();
                    break;
                case R.id.filter_ok:
//                    mAdapter.updateDataSet(mAdapter.getSelectedItem());
//                    mAdapter.notifyDataSetChanged();
                    dismissCampusPopup();
                    break;
                case R.id.blank_view_campus:
                    dismissCampusPopup();
                    break;
                case R.id.performance_tv:
                    filertType.setText("业绩对比");
                    dismissTypePopup();
                    break;
                case R.id.number_tv:
                    filertType.setText("人数对比");
                    dismissTypePopup();
                    break;
                case R.id.blank_view_type:
                    dismissTypePopup();
                    break;
                case R.id.start_date:
                    dismissDatePopup();
                    break;
                case R.id.end_date:
                    dismissDatePopup();
                    break;
                case R.id.blank_view_date:
                    dismissDatePopup();
                    break;
            }
        }
    };
}
