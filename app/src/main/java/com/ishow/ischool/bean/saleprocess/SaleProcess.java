package com.ishow.ischool.bean.saleprocess;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MrS on 2016/9/13.
 */
public class SaleProcess implements Parcelable {


    /**
     * chart : {"date":["2016-09-06","2016-09-07","2016-09-08","2016-09-09","2016-09-10","2016-09-11","2016-09-12"],"full_amount":["10","20","33","30","35","29","20"],"apply_number":["10","20","33","30","35","29","20"]}
     * tablehead : ["晨读讲师","晨读开班人数","邀约人数","邀约率","公开课现场人数","邀约到课率","报名人数","公开课报名人数","全款人数","全款率"]
     * tablebody : [["吴家俊","100","120","30","13","0.5","50","30","20","0.3"],["吴家俊","100","120","30","13","0.5","50","30","20","0.3"],["吴家俊","100","120","30","13","0.5","50","30","20","0.3"],["吴家俊","100","120","30","13","0.5","50","30","20","0.3"],["吴家俊","100","120","30","13","0.5","50","30","20","0.3"]]
     */

    public ChartBean chartBean;
    public SaleTable1 saleTable1;
    public SaleTabel2 saleTable2;

    public SaleProcess( int test_count) {
        chartBean = new ChartBean();
        chartBean.date = new ArrayList<>();
        chartBean.full_amount = new ArrayList<>();
        chartBean.apply_number = new ArrayList<>();

        saleTable1 = new SaleTable1();
        saleTable1.table = new Table();
        saleTable1.table.apply_numbers = 100;
        saleTable1.table.apply_rate = 0.4;
        saleTable1.table.full_amount = 1000;
        saleTable1.table.full_amount_rate = 0.7;

        //saleTabel2 = new SaleTabel2();

        saleTable1.tablehead = new ArrayList<>();
        saleTable1.tablebody = new ArrayList<>();
        List<String> list = new ArrayList<>();

        for (int i = 0; i < test_count; i++) {
            chartBean.date.add("09-06");
            chartBean.full_amount.add(i * 11 + 11 + "");
            chartBean.apply_number.add(i * 5 + 5 + "");
            saleTable1.tablehead.add("table_head" + i);
            list.add("table_body" + i);
            TableBody body = new TableBody();;
            for (int j = 0; j < test_count; j++) {
                body.add("row"+i+"--"+"col"+j);
            }
            saleTable1.tablebody.add(body);
        }

    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.chartBean, flags);
        dest.writeParcelable(this.saleTable1, flags);
        dest.writeParcelable(this.saleTable2, flags);
    }

    protected SaleProcess(Parcel in) {
        this.chartBean = in.readParcelable(ChartBean.class.getClassLoader());
        this.saleTable1 = in.readParcelable(SaleTable1.class.getClassLoader());
        this.saleTable2 = in.readParcelable(SaleTabel2.class.getClassLoader());
    }

    public static final Creator<SaleProcess> CREATOR = new Creator<SaleProcess>() {
        @Override
        public SaleProcess createFromParcel(Parcel source) {
            return new SaleProcess(source);
        }

        @Override
        public SaleProcess[] newArray(int size) {
            return new SaleProcess[size];
        }
    };
}
