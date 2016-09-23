package com.ishow.ischool.bean.saleprocess;

import com.google.gson.annotations.SerializedName;

/**
 * Created by MrS on 2016/9/22.
 */

public class MarketPositionObject {
    /**
     * name : 市场总监
     * id : 11
     * default : 0
     */
    public String name;
    public int id;
    @SerializedName("default")
    public int defaultX;
}
