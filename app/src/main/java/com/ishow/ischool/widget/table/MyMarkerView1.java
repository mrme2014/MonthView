
package com.ishow.ischool.widget.table;

import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.ishow.ischool.R;
import com.ishow.ischool.bean.campusperformance.SignPerformance;

import java.util.ArrayList;

/**
 * Custom implementation of the MarkerView.
 */
public class MyMarkerView1 extends MarkerView {

    private TextView tvContent1, tvContent2, tvContent3;
    private ArrayList<SignPerformance> datas;

    public MyMarkerView1(Context context, boolean secondY, boolean thirdY, ArrayList<SignPerformance> datas) {
        super(context, R.layout.custom_marker_view);

        tvContent1 = (TextView) findViewById(R.id.tv1);
        tvContent2 = (TextView) findViewById(R.id.tv2);
        tvContent3 = (TextView) findViewById(R.id.tv3);
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

        tvContent1.setText("业绩:" + datas.get((int)e.getX()).perweek_full_base);
        tvContent2.setText("红线目标:" + datas.get((int)e.getX()).perweek_full_challenge);
        tvContent3.setText("冲刺目标:" + datas.get((int)e.getX()).perweek_real);

        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }
}
