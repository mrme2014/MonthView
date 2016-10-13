
package com.ishow.ischool.widget.table;

import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.ishow.ischool.R;
import com.ishow.ischool.bean.campusperformance.EducationMonth;

import java.util.ArrayList;

/**
 * Custom implementation of the MarkerView.
 */
public class MyMarkerView4 extends MarkerView {

    private TextView tvContent0, tvContent1, tvContent2, tvContent3;
    private ArrayList<EducationMonth> datas;
    private ArrayList<String> curCampus;

    public MyMarkerView4(Context context, boolean secondY, boolean thirdY, ArrayList<String> curCampus, ArrayList<EducationMonth> datas) {
        super(context, R.layout.custom_marker_view);

        tvContent0 = (TextView) findViewById(R.id.tv0);
        tvContent1 = (TextView) findViewById(R.id.tv1);
        tvContent2 = (TextView) findViewById(R.id.tv2);
        tvContent3 = (TextView) findViewById(R.id.tv3);
        if (!secondY) {
            tvContent2.setVisibility(GONE);
        }
        if (!thirdY) {
            tvContent3.setVisibility(GONE);
        }
        this.curCampus = curCampus;
        this.datas = datas;
    }

    // callbacks everytime the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        tvContent0.setText(curCampus.get((int) e.getX()));
        tvContent1.setText("当前业绩:" + datas.get((int) e.getX()).permonth_real + "%");
        tvContent2.setText("红线目标:" + datas.get((int) e.getX()).full_base + "%");
        tvContent3.setText("冲刺目标:" + datas.get((int) e.getX()).full_challenge + "%");

        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }
}
