package com.commonlib.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.commonlib.R;
import com.commonlib.widget.CircleChartView;

import java.util.ArrayList;

public class CircileChartViewActivity extends AppCompatActivity {

    private CircleChartView circleChartView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circile_chart_view);
        initViews();
    }

    private void initViews() {
        circleChartView = (CircleChartView) findViewById(R.id.chart);

        ArrayList<CircleChartView.Value> datas = new ArrayList<>();
        datas.add(new CircleChartView.Value("报名人数", "37"));
        datas.add(new CircleChartView.Value("报名人数", "27"));
        datas.add(new CircleChartView.Value("报名人数", "17"));
        datas.add(new CircleChartView.Value("报名人数", "7"));
        circleChartView.setData(datas);
    }

}
