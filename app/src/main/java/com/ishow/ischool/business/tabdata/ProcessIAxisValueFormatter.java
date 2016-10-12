package com.ishow.ischool.business.tabdata;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.List;

/**
 * Created by abel on 16/10/12.
 */

public class ProcessIAxisValueFormatter implements IAxisValueFormatter {

    private List<String> datas;

    public ProcessIAxisValueFormatter(List<String> datas) {
        this.datas = datas;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return datas.get((int) value - 1);
    }

    @Override
    public int getDecimalDigits() {
        return 0;
    }
}
