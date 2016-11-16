package com.ishow.ischool.bean.attribute;

/**
 * Created by MrS on 2016/11/16.
 */

public class PieChartEntry {
    public int floorColor;//楼层颜色值 ---R.color.white

    public int floorHeight;// 楼层高度 不写就默认

    public String floorDes; //楼层文本描述

    public String floorNum; //数字描述

    public PieChartEntry(int floorColor1, String floorDes1, String floorNum1) {
        this.floorColor = floorColor1;
        this.floorDes = floorDes1;
        this.floorNum = floorNum1;
    }

    public PieChartEntry(int floorColor1, int floorHeight1, String floorDes1, String floorNum1) {
        this.floorColor = floorColor1;
        this.floorHeight = floorHeight1;
        this.floorDes = floorDes1;
        this.floorNum = floorNum1;
    }
}
