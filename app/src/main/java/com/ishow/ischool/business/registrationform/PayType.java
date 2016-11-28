package com.ishow.ischool.business.registrationform;

/**
 * Created by MrS on 2016/11/22.
 */

public class PayType {

    /*'2为收款卡，3为退款卡，4为收款+退款卡，5为企业支付宝，6为个人支付宝'*/
    /**
     * id : 151
     * campus_id : 20
     * name : 中国农业银行8900
     * method_type : 1
     * type : 收款卡
     * banker : 1
     * account : sk62284816987298900
     * samefee : 0.0020
     * differentfee : 0.0030
     * status : 1
     * create_time : 1476870040
     * update_time : 1476870040
     * bankName : 中国农业银行
     */


    public int id;
    public int campus_id;
    public String name;
    public int method_type;
    public String type;
    public String banker;
    public String account;
    public String samefee;
    public String differentfee;
    public int status;
    public int create_time;
    public int update_time;
    public String bankName;
    public int type_id;

    public double method_money;

    public int method_id;
    public String method;
    public String account_id;
    public String balance;

}
