package com.ishow.ischool.business.registrationform;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.commonlib.util.DateUtil;
import com.commonlib.widget.LabelEditText;
import com.commonlib.widget.LabelTextView;
import com.ishow.ischool.R;
import com.ishow.ischool.bean.registrationform.BasePriceInfo;
import com.ishow.ischool.bean.registrationform.CheapType;
import com.ishow.ischool.bean.registrationform.RegistraInfo;
import com.ishow.ischool.bean.registrationform.RegistraResult;
import com.ishow.ischool.common.base.BaseActivity4Crm;
import com.ishow.ischool.util.AppUtil;
import com.ishow.ischool.widget.pickerview.PickerDialogFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by MrS on 2016/11/21.
 */

public class registrationFormActivity extends BaseActivity4Crm<regisPresenter, regisModel> implements regisView {

    @BindView(R.id.regis_cheap)
    LabelTextView regisCheap;
    @BindView(R.id.pay_way)
    LabelTextView payWay;
    @BindView(R.id.pay_way_hide_layout)
    LinearLayout payWayHideLayout;
    @BindView(R.id.payWayListLayout)
    LinearLayout payWayListLayout;

    @BindView(R.id.trad_num)
    LabelEditText tradNum;
    @BindView(R.id.pay_date)
    LabelTextView payDate;
    @BindView(R.id.sec_pay_date)
    LabelTextView secPayDate;
    @BindView(R.id.beizhu)
    LabelEditText beizhu;
    @BindView(R.id.guwen)
    LabelTextView guwen;
    @BindView(R.id.money_real)
    TextView moneyReal;
    @BindView(R.id.money_just)
    TextView moneyJust;
    @BindView(R.id.registration_save)
    TextView registrationSave;


    private int fisrt_pay_time_unix;
    private int sec_end_time_unix;

    private List<PayType> selectPayList;
    private ArrayList<CheapType> cheapTypeList = new ArrayList<>();
    //private ArrayList<Integer> selectColums = new ArrayList<>();
    //  private ArrayList<Double> selectMoneys = new ArrayList<>();

    private int cheapTypeId;//优惠类型id
    private double totalRealMoney;//本次 实际累计收款
    public int cheapType;
    private double cheapTypePrice = 1;
    private double campus_price;
    private double realCampusPrice;

    private int student_id = 858;
    private int student_status = 2;
    private int request_code;
    private String action;
    private String feilds = "basePriceInfo,payListInfo,preferentialCourse";

    public static final String STUDENT_ID = "student_id";
    public static final String STUDENT_STATUS = "student_status";
    public static final String REQUEST_CODE = "request_code";

    @Override
    protected void initEnv() {
        super.initEnv();
        getIntent().getIntExtra(STUDENT_ID, student_id);
        getIntent().getIntExtra(STUDENT_STATUS, student_status);
        getIntent().getIntExtra(REQUEST_CODE, request_code);
    }

    @Override
    protected void setUpContentView() {
        if (student_status == 1) {
            action = "apply";
            setContentView(R.layout.activcity_registration_form, R.string.registration_title, MODE_BACK);
        } else if (student_status == 2) {
            action = "pay";
            setContentView(R.layout.activcity_registration_form, R.string.registration_title1, R.menu.menu_registration, MODE_BACK);
        }
    }

    @Override
    protected void setUpView() {
        mPresenter.getPayInfo(student_id, student_status, action, feilds);
    }

    @Override
    protected void setUpData() {

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        //studentInfo
        Intent intent = new Intent(this, registrationInfoConfirm.class);
        intent.putExtra(STUDENT_ID, student_id);
        intent.putExtra(STUDENT_STATUS, student_status);
        startActivity(intent);
        return super.onMenuItemClick(item);

    }

    @Override
    public void getRegistraInfo(RegistraResult registraResult) {

        if (registraResult != null && registraResult.basePriceInfo != null) {
            BasePriceInfo basePriceInfo = registraResult.basePriceInfo;
            List<RegistraInfo> registraInfo = registraResult.payListInfo;
            cheapTypeList = (ArrayList<CheapType>) registraResult.preferentialCourse;
            /*欠款状态下进来        优惠方式不显示*/
            if (student_status == 2) {
                regisCheap.setVisibility(View.GONE);
                if (registraInfo != null) {
                    realCampusPrice = campus_price = (int) registraInfo.get(0).arrearage;
                    moneyJust.setText(getString(R.string.registration_money_just) + " ￥" + registraInfo.get(0).arrearage);
                    if (registraInfo.get(0).pay_time != 0) {
                        fisrt_pay_time_unix = registraInfo.get(0).pay_time;
                        payDate.setText(DateUtil.parseSecond2Str(Long.valueOf(fisrt_pay_time_unix)));
                    }
                }
            } else {
                realCampusPrice = campus_price = basePriceInfo.campus_price;
                moneyJust.setText(getString(R.string.registration_money_just) + " ￥" + campus_price);
            }

            guwen.setText(mUser.userInfo.user_name);

        }
    }

    @Override
    public void getRegistraError(String error) {
        handProgressbar(false);
        showToast(error);
    }

    @Override
    public void payActionSucess(String info) {
        handProgressbar(false);
        //showToast(info);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog alertDialog = builder.setMessage(moneyReal.getText().toString() + "  " + moneyJust.getText().toString()).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                registrationFormActivity.this.finish();
            }
        }).create();
        alertDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);
        alertDialog.show();
    }


    @OnClick({R.id.regis_cheap, R.id.pay_way, R.id.click_add, R.id.pay_way_hide_layout, R.id.pay_date, R.id.sec_pay_date, R.id.registration_save})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.regis_cheap: {
                if (cheapTypeList == null) {
                    showToast(R.string.registration_cheap_type_null);
                    break;
                }
                cheapListDialogFragment dialog = new cheapListDialogFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("cheap_list", cheapTypeList);
                dialog.setArguments(bundle);
                dialog.show(getSupportFragmentManager(), "dialog");
                dialog.setOnItemClickListener(new cheapListDialogFragment.OnItemClickListener() {
                    @Override
                    public void OnItemClickListener(String cheapTypeTitle, int cheapType1, double cheapTypePrice1, int cheapTypeId1) {
                        cheapType = cheapType1;                            //  折扣 减免        折扣比例 减免金额     折扣类型id
                        cheapTypeId = cheapTypeId1;
                        cheapTypePrice = cheapTypePrice1;
                        regisCheap.setText(cheapTypeTitle);
                        resetAdjustMoney();
                    }
                });
            }
            break;
            case R.id.pay_way:
            case R.id.click_add:
                Bundle bundle = new Bundle();
                bundle.putInt("campuse_id", mUser.campusInfo.id);
                SelectPayDialogFragment dialog = new SelectPayDialogFragment();
                dialog.setArguments(bundle);
                dialog.show(getSupportFragmentManager(), "dialog");
                dialog.setOnPayTypeSelectCallback(new SelectPayDialogFragment.onPayTypeSelectCallback() {
                    @Override
                    public void onSelect(int colum1, int colum2, PayType selectPayType, String payWay1, String payWayAcount, String money) {
                        payWayHideLayout.setVisibility(View.VISIBLE);
                        payWay.setVisibility(View.GONE);
                        totalRealMoney += Float.valueOf(money);
                        resetRealMoney();

                        LinearLayout inflate = (LinearLayout) LayoutInflater.from(registrationFormActivity.this).inflate(R.layout.activity_registration_form_pay_way_select, null);
                        TextView payAcountType = (TextView) inflate.findViewById(R.id.select_pay_way);
                        TextView payAcount = (TextView) inflate.findViewById(R.id.select_pay_money);
                        final ImageView close = (ImageView) inflate.findViewById(R.id.select_pay_close);

                        payAcountType.setText(payWay1);
                        SpannableString string = new SpannableString("\n" + payWayAcount);
                        string.setSpan(new ForegroundColorSpan(ContextCompat.getColor(registrationFormActivity.this, R.color.txt_9)), 0, string.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        payAcountType.append(string);
                        payAcount.setText(money);

                        int childCount = payWayListLayout.getChildCount();
                        if (selectPayList == null) selectPayList = new ArrayList<PayType>();
                        selectPayType.method_id = colum1;
                        selectPayType.method_money = (Double.valueOf(money));

                        selectPayList.add(selectPayType);
                        //  selectColums.add(colum1);
                        //  selectMoneys.add(Double.valueOf(money));

                        close.setTag(R.id.registration_close_tag_paytype, selectPayType);
                        close.setTag(R.id.registration_close_tag_contenview, inflate);
                        // close.setTag(R.id.registration_close_tag_money, Double.valueOf(money));
                        //  close.setTag(R.id.registration_close_tag_select_index, selectColums.size() - 1);
                        payWayListLayout.addView(inflate, childCount - 1);
                        close.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                PayType selectPayType = (PayType) close.getTag(R.id.registration_close_tag_paytype);
                                View view1 = (View) close.getTag(R.id.registration_close_tag_contenview);
                                //    double money = (Double) close.getTag(R.id.registration_close_tag_money);
                                //    int index = (int) close.getTag(R.id.registration_close_tag_select_index);
                                //   selectColums.remove(index);
                                //   selectMoneys.remove(index);

                                totalRealMoney -= selectPayType.method_money;
                                resetRealMoney();
                                selectPayList.remove(selectPayType);
                                payWayListLayout.removeView(view1);

                                if (payWayListLayout.getChildCount() == 1) {
                                    payWayHideLayout.setVisibility(View.GONE);
                                    payWay.setVisibility(View.VISIBLE);
                                }
                            }
                        });
                    }

                    @Override
                    public void onError(String error) {
                        showToast(error);
                    }
                });
                break;
            case R.id.pay_date:
                AppUtil.showTimePickerDialog(getSupportFragmentManager(), new PickerDialogFragment.Callback<Integer>() {
                    @Override
                    public void onPickResult(Integer selectIds, String... result) {
                        fisrt_pay_time_unix = selectIds;
                        payDate.setText(result[0]);
                    }
                });
                break;
            case R.id.sec_pay_date:
                AppUtil.showTimePickerDialog(getSupportFragmentManager(), new PickerDialogFragment.Callback<Integer>() {
                    @Override
                    public void onPickResult(Integer selectIds, String... result) {
                        sec_end_time_unix = selectIds;
                        secPayDate.setText(result[0]);
                    }
                });
                break;
            case R.id.registration_save:
                if (TextUtils.equals(tradNum.getText().toString(), "")) {
                    showToast(R.string.registration_trad_num);
                    return;
                }
                if (cheapType == 1 && cheapTypePrice != 0) {
                    campus_price = (realCampusPrice * cheapTypePrice * 1.0 / 10);
                }
                if (totalRealMoney > campus_price) {
                    showToast(R.string.registration_price_not);
                    return;
                }
                handProgressbar(true);
                HashMap<String, Integer> integerHashMap = new HashMap<>();
                integerHashMap.put("pay_time", fisrt_pay_time_unix);
                integerHashMap.put("advisor_id", mUser.userInfo.user_id);
                if (sec_end_time_unix != 0) {
                    integerHashMap.put("arrearage_time", sec_end_time_unix);
                }
                /*[{"method":"现金","method_id":2,"account_id":0,"account":"","balance":4500}]*/
                /*// 付款方式'PaymentMethod' => array( 1 => '刷卡', 2 => '现金', 3 => '支付宝', 4 => '转账' ), */
                JSONArray array = new JSONArray();
                if (selectPayList != null) {
                    for (int i = 0; i < selectPayList.size(); i++) {
                        JSONObject object = new JSONObject();
                        PayType payType = selectPayList.get(i);
                        //Integer position = selectColums.get(i);
                        // double money = selectMoneys.get(i);
                        try {//payType.id == 0 ? "现金" : payType.type
                            object.put("method", payType.type);
                            object.put("account_id", payType.id);
                            object.put("balance", payType.method_money);
                            object.put("method_id", payType.method_id);
                            /*switch (position) {
                                case 0:
                                    object.put("method_id", 1);
                                    break;
                                case 1:
                                    object.put("method_id", 3);
                                    break;
                                case 2:
                                    object.put("method_id", 2);
                                    break;
                                case 3:
                                    object.put("method_id", 4);
                                    break;
                            }*/
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        array.put(object);
                    }
                }

                mPresenter.payAction(student_id,
                        array,
                        action,
                        (float) campus_price,
                        (float) totalRealMoney,
                        Integer.parseInt(tradNum.getText().toString()),
                        beizhu.getText().toString(),
                        integerHashMap);
                break;
        }
    }

    private void resetAdjustMoney() {
        if (cheapType == 1 && cheapTypePrice != 0) {
            campus_price = (campus_price * cheapTypePrice * 1.0 / 10);
        } else if (cheapType == 2) {
            campus_price -= cheapTypePrice;
        }
        moneyJust.setText(getString(R.string.registration_money_just));
        SpannableString moneyRealStr = new SpannableString("  ¥" + campus_price);
        moneyRealStr.setSpan(new ForegroundColorSpan(ContextCompat.getColor(registrationFormActivity.this, R.color.color_orange)), 0, moneyRealStr.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        moneyJust.append(moneyRealStr);
        campus_price = realCampusPrice;
    }

    private void resetRealMoney() {

        moneyReal.setText(getString(R.string.registration_real_money));
        SpannableString moneyRealStr = new SpannableString("  ¥" + totalRealMoney);
        moneyRealStr.setSpan(new ForegroundColorSpan(ContextCompat.getColor(registrationFormActivity.this, R.color.color_orange)), 0, moneyRealStr.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        moneyReal.append(moneyRealStr);

    }
}
