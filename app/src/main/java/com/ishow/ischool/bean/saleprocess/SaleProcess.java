package com.ishow.ischool.bean.saleprocess;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by MrS on 2016/9/13.
 */
public class SaleProcess implements Serializable {


    /**
     * chart : {"date":["2016-09-06","2016-09-07","2016-09-08","2016-09-09","2016-09-10","2016-09-11","2016-09-12"],"full_amount":["10","20","33","30","35","29","20"],"apply_number":["10","20","33","30","35","29","20"]}
     * tablehead : ["晨读讲师","晨读开班人数","邀约人数","邀约率","公开课现场人数","邀约到课率","报名人数","公开课报名人数","全款人数","全款率"]
     * tablebody : [["吴家俊","100","120","30","13","0.5","50","30","20","0.3"],["吴家俊","100","120","30","13","0.5","50","30","20","0.3"],["吴家俊","100","120","30","13","0.5","50","30","20","0.3"],["吴家俊","100","120","30","13","0.5","50","30","20","0.3"],["吴家俊","100","120","30","13","0.5","50","30","20","0.3"]]
     */

    public List<String> tablehead;
    public List<List<String>> tablebody;
    public ChartBean chartBean;

    public SaleProcess() {
        int test_count = 7;
        chartBean = new ChartBean();
        chartBean.date = new ArrayList<>();
        chartBean.full_amount = new ArrayList<>();
        chartBean.apply_number = new ArrayList<>();

        tablehead = new ArrayList<>();
        tablebody = new ArrayList<>();
        List<String> list = new ArrayList<>();
        for (int i = 0; i <test_count; i++) {
            chartBean.date.add("2016-09-06" + i);
            Random random =new Random();
            chartBean.full_amount.add(i * 11+11+"");
            chartBean.apply_number.add(i * 5+5+ "");
            tablehead.add("table_head" + i);
            list.add("table_body" + i);
        }
        tablebody.add(list);
    }
}
