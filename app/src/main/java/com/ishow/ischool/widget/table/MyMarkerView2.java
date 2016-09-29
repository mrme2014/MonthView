
package com.ishow.ischool.widget.table;

import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.ishow.ischool.R;
import com.ishow.ischool.bean.campusperformance.SignAmount;

import java.util.ArrayList;

/**
 * Custom implementation of the MarkerView.
 */
public class MyMarkerView2 extends MarkerView {

    private TextView tvContent1, tvContent2, tvContent3;
    private ArrayList<SignAmount> datas;

    public MyMarkerView2(Context context, boolean firstY, boolean secondY, boolean thirdY, ArrayList<SignAmount> datas) {
        super(context, R.layout.custom_marker_view);

        tvContent1 = (TextView) findViewById(R.id.tv1);
        tvContent2 = (TextView) findViewById(R.id.tv2);
        tvContent3 = (TextView) findViewById(R.id.tv3);
        if (!firstY) {
            tvContent1.setVisibility(GONE);
        }
        if (!secondY) {
            tvContent2.setVisibility(GONE);
        }
        if (!thirdY) {
            tvContent3.setVisibility(GONE);
        }
        this.datas = datas;
    }

    // callbacks everytime the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(Entry e, Highlight highlight) {

        tvContent1.setText("总现场:" + datas.get((int) e.getX()).scene);
        tvContent2.setText("总报名:" + datas.get((int) e.getX()).sign);
        tvContent3.setText("总全款:" + datas.get((int) e.getX()).fullPay);

        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }
}
