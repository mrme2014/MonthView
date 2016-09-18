package com.ishow.ischool.business.statistic.other;


import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.AxisValueFormatter;

/**
 * Created by abel on 16/9/14.
 */
public class CampusIAxisValueFormatter implements AxisValueFormatter {
    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return "杭州校区";
    }

    @Override
    public int getDecimalDigits() {
        return 0;
    }
}
