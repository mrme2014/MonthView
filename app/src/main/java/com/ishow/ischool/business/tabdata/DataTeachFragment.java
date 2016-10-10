package com.ishow.ischool.business.tabdata;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.ishow.ischool.R;
import com.ishow.ischool.application.Resource;
import com.ishow.ischool.bean.saleprocess.SaleProcess;
import com.ishow.ischool.bean.user.CampusInfo;
import com.ishow.ischool.business.campusperformance.education.Performance4EduActivity;
import com.ishow.ischool.business.statistic.other.OtherStatisticActivity;
import com.ishow.ischool.business.statistic.other.SaleProcessIAxisValueFormatter;
import com.ishow.ischool.business.teachprocess.TeachProcessActivity;
import com.ishow.ischool.common.base.BaseFragment4Crm;
import com.ishow.ischool.common.manager.JumpManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 教务教学数据统计
 */
public class DataTeachFragment extends BaseFragment4Crm<DataTeachPreseneter, DataTeachModel> implements DataTeachContract.View {

    private OnFragmentInteractionListener mListener;
    @BindView(R.id.line_chart)
    LineChart mChart;
    private SaleProcess mSaleProcess;

    public DataTeachFragment() {
        // Required empty public constructor
    }

    public static DataTeachFragment newInstance() {
        DataTeachFragment fragment = new DataTeachFragment();
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
        return R.layout.fragment_data_teach;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void init() {
//        mChart.setOnChartGestureListener(this);
//        mChart.setOnChartValueSelectedListener(this);
        mChart.setDrawGridBackground(false);
        mChart.setBorderColor(Color.WHITE);

        // no description text
        mChart.setDescription("");
        mChart.setNoDataTextDescription("You need to provide data for the chart.");

        // enable touch gestures
        mChart.setTouchEnabled(true);

        // enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        // mChart.setScaleXEnabled(true);
        // mChart.setScaleYEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        mChart.setPinchZoom(true);

        // set an alternative background color
        mChart.setBackgroundColor(getResources().getColor(R.color.comm_blue));

        // create a custom MarkerView (extend MarkerView) and specify the layout
        // to use for it

        XAxis xAxis = mChart.getXAxis();
        //xAxis.enableGridDashedLine(10f, 10f, 0f);
        xAxis.setAxisLineColor(Color.WHITE);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelRotationAngle(-45);
        xAxis.setAxisMinimum(0);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
        //leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setDrawZeroLine(false);
        leftAxis.setDrawGridLines(false);
        leftAxis.setGridColor(Color.WHITE);
        leftAxis.setAxisLineColor(Color.WHITE);
        leftAxis.setTextColor(Color.WHITE);
        leftAxis.setGranularity(1);
        leftAxis.setAxisMinimum(0);

        // limit lines are drawn behind data (and not on top)
        leftAxis.setDrawLimitLinesBehindData(true);

        mChart.getAxisRight().setEnabled(false);

        //mChart.getViewPortHandler().setMaximumScaleY(2f);
        //mChart.getViewPortHandler().setMaximumScaleX(2f);

        // add data
        //setData(15, 100);

//        mChart.setVisibleXRange(20);
//        mChart.setVisibleYRange(20f, AxisDependency.LEFT);
//        mChart.centerViewTo(20, 50, AxisDependency.LEFT);

        mChart.animateX(2500);
        //mChart.invalidate();

        // get the legend (only possible after setting data)
        Legend l = mChart.getLegend();

        // modify the legend ...
        // l.setPosition(LegendPosition.LEFT_OF_CHART);
        l.setForm(Legend.LegendForm.LINE);
        l.setTextColor(Color.WHITE);

        // // dont forget to refresh the drawing
        // mChart.invalidate();
        if (mSaleProcess != null && mChart.getData() == null) {
            setData(mSaleProcess);
        }
    }

    @Override
    public void getTeachingProcessSuccess(ArrayList<CampusInfo> campusInfos) {

    }

    @Override
    public void getTeachingProcessFail(String msg) {

    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }


    @OnClick({R.id.data_market, R.id.data_campus, R.id.data_other})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.data_market:
                if (JumpManager.checkUserPermision(getActivity(), Resource.PERMISSION_DATA_SALE_PROCESS)) {
                    startActivity(new Intent(getActivity(), TeachProcessActivity.class));
                }
                break;
            case R.id.data_campus://PERMISSION_DATA_CAMPUS
                if (JumpManager.checkUserPermision(getActivity(), Resource.PERMISSION_DATA_CAMPUS)) {
                    startActivity(new Intent(getActivity(), Performance4EduActivity.class));
                }
                break;
            case R.id.data_other:
                if (JumpManager.checkUserPermision(getActivity(), Resource.PERMISSION_DATA_OTHER)) {
                    Intent intent = new Intent(getActivity(), OtherStatisticActivity.class);
                    intent.putExtra(OtherStatisticActivity.IS_TEACH_DATA, true);
                    startActivity(intent);
                }
                break;
        }
    }

    public void setData(SaleProcess saleProcess) {
        if (saleProcess != null && saleProcess.chart != null) {
            this.mSaleProcess = saleProcess;
        }
        if (mChart == null || saleProcess == null || saleProcess.chart == null) {
            return;
        }
        List<String> apply_number = saleProcess.chart.apply_number;
        List<String> full_amount = saleProcess.chart.full_amount;

        ArrayList<Entry> point1 = null;
        if (apply_number != null) {
            int aplply_size = apply_number.size();
            point1 = new ArrayList<Entry>();
            for (int i = 0; i < aplply_size; i++) {
                point1.add(new Entry(i, Float.valueOf(apply_number.get(i))));
            }
        }

        ArrayList<Entry> point2 = null;
        if (full_amount != null) {
            int full_size = full_amount.size();
            point2 = new ArrayList<Entry>();
            for (int i = 0; i < full_size; i++) {
                point2.add(new Entry(i, Float.valueOf(full_amount.get(i))));
            }
        }

        SaleProcessIAxisValueFormatter formatter = new SaleProcessIAxisValueFormatter(saleProcess.chart.date);
        mChart.getXAxis().setValueFormatter(formatter);


        LineDataSet set1;
        LineDataSet set2;


        if (mChart.getData() != null && mChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) mChart.getData().getDataSetByIndex(0);
            set1.setValues(point1);
            set2 = (LineDataSet) mChart.getData().getDataSetByIndex(1);
            set2.setValues(point2);

            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            if (point1 == null || point2 == null)
                return;

            set1 = new LineDataSet(point1, getString(R.string.apply_count));

            set1.setColor(Color.WHITE);
            set1.setCircleColor(Color.WHITE);
            set1.setLineWidth(0f);
            set1.setCircleRadius(3f);
            set1.setDrawCircleHole(false);
            set1.setValueTextSize(9f);
            set1.setDrawFilled(true);
            set1.setValueTextColor(Color.WHITE);

            set1.setFillColor(getResources().getColor(R.color.fill_color));

            set2 = new LineDataSet(point2, getString(R.string.full_amount));

            set2.setColor(Color.WHITE);
            set2.setCircleColor(Color.WHITE);
            set2.setLineWidth(0f);
            set2.setCircleRadius(3f);
            set2.setDrawCircleHole(false);
            set2.setValueTextSize(9f);
            set2.setDrawFilled(true);
            set2.setValueTextColor(Color.WHITE);

            set2.setFillColor(getResources().getColor(R.color.fill_color));

            set1.setValueFormatter(new IValueFormatter() {
                @Override
                public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                    return String.valueOf((int) value);
                }
            });

            set2.setValueFormatter(new IValueFormatter() {
                @Override
                public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                    return String.valueOf((int) value);
                }
            });

            ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
            dataSets.add(set1); // add the datasets
            dataSets.add(set2); // add the datasets

            // create a data object with the datasets
            LineData data = new LineData(dataSets);

            // set data
            mChart.setData(data);
            mChart.invalidate();
        }
    }

    public void refreshData(SaleProcess saleProcess) {
        setData(saleProcess);
//        mChart.notifyDataSetChanged();
    }
}
