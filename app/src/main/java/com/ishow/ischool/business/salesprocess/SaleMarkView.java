package com.ishow.ischool.business.salesprocess;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.ishow.ischool.R;

/**
 * Created by MrS on 2016/9/29.
 */

public class SaleMarkView extends MarkerView {
    /**
     * Constructor. Sets up the MarkerView with a custom layout resource.
     *
     * @param context
     * @param layoutResource the layout resource to use for the MarkerView
     */
    private TextView tvContent1, tvContent3;

    public SaleMarkView(Context context) {
        super(context, R.layout.custom_marker_view);
        tvContent1 = (TextView) findViewById(R.id.tv1);
        tvContent3 = (TextView) findViewById(R.id.tv3);
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {

        tvContent1.setText("报名人数:" + getSelectValueByIndex(1, (int) e.getX()));
        tvContent3.setText("全款人数:" + getSelectValueByIndex(0, (int) e.getX()));

        tvContent1.setVisibility(isDataSetIndexVisible(1) ? View.VISIBLE : View.GONE);
        tvContent3.setVisibility(isDataSetIndexVisible(0) ? View.VISIBLE : View.GONE);

        super.refreshContent(e, highlight);
    }

    public void hideMarkView() {
        layout(0, 0, 0, 0);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }
}
