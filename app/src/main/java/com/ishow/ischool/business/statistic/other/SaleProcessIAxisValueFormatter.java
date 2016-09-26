package com.ishow.ischool.business.statistic.other;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.List;

/**
 * Created by abel on 16/9/23.
 */
public class SaleProcessIAxisValueFormatter implements IAxisValueFormatter {
    private List<String> mdate;

    public SaleProcessIAxisValueFormatter(List<String> mdate) {
        this.mdate = mdate;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        String str = mdate.get((int) value);
        return str.substring(5, str.length());
    }

    @Override
    public int getDecimalDigits() {
        return 0;
    }
}
