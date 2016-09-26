package com.ishow.ischool.business.statistic.other;


import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.ishow.ischool.bean.statistics.OtherStatistics;

import java.util.ArrayList;

/**
 * Created by abel on 16/9/14.
 */
public class CampusIAxisValueFormatter implements IAxisValueFormatter {

    private ArrayList<OtherStatistics> others;

    public CampusIAxisValueFormatter(ArrayList<OtherStatistics> otherStatistics) {
        this.others = otherStatistics;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {

        try {
            return others.get((int) value - 1).name;
        } catch (Exception e) {
            return "";
        }
    }

    @Override
    public int getDecimalDigits() {
        return 0;
    }
}
