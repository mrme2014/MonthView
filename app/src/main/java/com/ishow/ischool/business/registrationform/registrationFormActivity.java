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
import com.commonlib.util.LogUtil;
import com.commonlib.util.SpUtil;
import com.commonlib.widget.LabelEditText;
import com.commonlib.widget.LabelTextView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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

import java.lang.reflect.Type;
import java.text.DecimalFormat;
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
    @BindView(R.id.click_add)
    TextView clickAdd;


    private int fisrt_pay_time_unix;
    private int sec_end_time_unix;

    private List<PayType> selectPayList;
    private ArrayList<CheapType> cheapTypeList;

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
    private double cheap_price;


    @Override
    protected void initEnv() {
        super.initEnv();
        student_id = getIntent().getIntExtra(STUDENT_ID, student_id);
        student_status = getIntent().getIntExtra(STUDENT_STATUS, student_status);
        request_code = getIntent().getIntExtra(REQUEST_CODE, request_code);

    }

    @Override
    protected void setUpContentView() {
        /*付款状态。1.未报名；2.欠款；3.已报名；4.退款'*/
        if (student_status == 1) {
            action = "apply";
            setContentView(R.layout.activcity_registration_form, R.string.registration_title, MODE_BACK);
        } else if (student_status == 2) {
            action = "pay";
            setContentView(R.layout.activcity_registration_form, R.string.registration_title1, R.menu.menu_registration, MODE_BACK);
        }
    }

    @Override
    protected void onNavigationBtnClicked() {
        checkIfNeedCache();
    }

    private void finishActivity() {
        if (cheapType == 1 && cheapTypePrice != 0) {
            campus_price = (realCampusPrice * cheapTypePrice * 1.0 / 10);
        }
        int state = 0;//定义 三种状态  0是未报名没做任状态 1是定金 2是全款
        if (totalRealMoney == 0) {
            state = 0;
        } else if (campus_price - totalRealMoney > 0) {
            state = 1;
        } else if (campus_price - totalRealMoney >= 0) {
            state = 2;
        }
        Intent intent = new Intent();
        intent.putExtra("apply_result", state);
        this.setResult(RESULT_OK, intent);
        this.finish();
    }

    @Override
    protected void setUpView() {
        readCache();
        mPresenter.getPayInfo(student_id, student_status, action, feilds);
    }

    @Override
    protected void setUpData() {

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        //studentInfo
        Intent intent = new Intent(this, registraDetailActivity.class);
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
                    realCampusPrice = campus_price = registraInfo.get(0).arrearage;
                    //moneyJust.setText(getString(R.string.registration_money_just) + " ￥" + registraInfo.get(0).arrearage);
                    resetAdjustMoney();
                    if (registraInfo.get(0).pay_time != 0) {
                        fisrt_pay_time_unix = registraInfo.get(0).pay_time;
                    }
                }
            } else {
                realCampusPrice = campus_price = basePriceInfo.campus_price;
                resetAdjustMoney();
                //moneyJust.setText(getString(R.string.registration_money_just) + " ￥" + campus_price);
            }
            fisrt_pay_time_unix = AppUtil.getTodayStart();
            payDate.setText(DateUtil.parseSecond2Str(Long.valueOf(fisrt_pay_time_unix)));
            if (sec_end_time_unix != 0)
                secPayDate.setText(DateUtil.parseSecond2Str(Long.valueOf(sec_end_time_unix)));
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
        finishActivity();
    }


    @OnClick({R.id.pay_way, R.id.click_add, R.id.pay_way_hide_layout, R.id.pay_date, R.id.sec_pay_date, R.id.registration_save, R.id.regis_cheap})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.regis_cheap: {
                if (cheapTypeList == null || cheapTypeList.size() == 0) {
                    showToast(R.string.registration_cheap_type_null);
                    break;
                }
                CheapListDialogFragment dialog = new CheapListDialogFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("cheap_list", cheapTypeList);
                dialog.setArguments(bundle);
                dialog.show(getSupportFragmentManager(), "dialog");
                dialog.setOnItemClickListener(new CheapListDialogFragment.OnItemClickListener() {
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
                        if (selectPayList == null) selectPayList = new ArrayList<PayType>();
                        selectPayList.add(selectPayType);
                        resetRealMoney();

                        addPayTypeItemView(selectPayType, payWay1, payWayAcount, money);
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
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                AlertDialog alertDialog = builder.setMessage(moneyReal.getText().toString() + "  " + moneyJust.getText().toString()).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        realPayAction();

                    }
                }).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
                alertDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.setCancelable(false);
                alertDialog.show();
                //realPayAction();
                break;
        }
    }

    private void realPayAction() {
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
                try {
                    object.put("method", payType.method);
                    object.put("account_id", payType.id);
                    object.put("balance", payType.method_money);
                    object.put("method_id", payType.method_id);
                    object.put("account", payType.name);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                array.put(object);
            }
        }

        mPresenter.payAction(student_id,
                array.toString(),
                action,
                (float) campus_price,
                (float) totalRealMoney,
                Integer.parseInt(tradNum.getText().toString()),
                beizhu.getText().toString(),
                cheapTypeId,
                cheap_price,
                integerHashMap);
    }

    private void addPayTypeItemView(PayType selectPayType, String payWay1, String payWayAcount, String money) {
        payWayHideLayout.setVisibility(View.VISIBLE);
        payWay.setVisibility(View.GONE);
        LinearLayout inflate = (LinearLayout) LayoutInflater.from(registrationFormActivity.this).inflate(R.layout.activity_registration_form_pay_way_select, null);
        TextView payAcountType = (TextView) inflate.findViewById(R.id.select_pay_way);
        TextView payAcount = (TextView) inflate.findViewById(R.id.select_pay_money);
        final ImageView close = (ImageView) inflate.findViewById(R.id.select_pay_close);

        payAcountType.setText(payWay1);
        if (payWayAcount != null && payWayAcount != "") {
            SpannableString string = new SpannableString("\n" + payWayAcount);
            string.setSpan(new ForegroundColorSpan(ContextCompat.getColor(registrationFormActivity.this, R.color.txt_9)), 0, string.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            payAcountType.append(string);
        }
        DecimalFormat df = new DecimalFormat("###.###");
        SpannableString moneyRealStr = new SpannableString("  ¥" + df.format(selectPayType.method_money));
        payAcount.setText(moneyRealStr);
        int childCount = payWayListLayout.getChildCount();

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

    private void resetAdjustMoney() {
        if (cheapType == 1 && cheapTypePrice != 0) {
            campus_price = (campus_price * cheapTypePrice * 1.0 / 10);
        } else if (cheapType == 2) {
            campus_price -= cheapTypePrice;
        }
        moneyJust.setText(getString(R.string.registration_money_just));
        DecimalFormat df = new DecimalFormat("##.00");
        SpannableString moneyRealStr = new SpannableString("  ¥" + df.format(campus_price));
        moneyRealStr.setSpan(new ForegroundColorSpan(ContextCompat.getColor(registrationFormActivity.this, R.color.color_orange)), 0, moneyRealStr.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        moneyJust.append(moneyRealStr);
        cheap_price = realCampusPrice - campus_price;
        campus_price = realCampusPrice;

    }

    private void resetRealMoney() {
        if (totalRealMoney == 0) {
            return;
        }
        moneyReal.setText(getString(R.string.registration_real_money));
        DecimalFormat df = new DecimalFormat("###.###");
        SpannableString moneyRealStr = new SpannableString("  ¥" + df.format(totalRealMoney));
        moneyRealStr.setSpan(new ForegroundColorSpan(ContextCompat.getColor(registrationFormActivity.this, R.color.color_orange)), 0, moneyRealStr.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        moneyReal.append(moneyRealStr);

    }

    @Override
    public void onBackPressed() {
        checkIfNeedCache();
    }

    private void checkIfNeedCache() {
        //!isEmpty(regisCheap) || !isEmpty(payDate) || !isEmpty(secPayDate) || !isEmpty(beizhu) || !isEmpty(tradNum) ||
        if (cheapTypeId != 0 || (student_status == 1 && fisrt_pay_time_unix != 0) || sec_end_time_unix != 0 || !isEmpty(beizhu) || !isEmpty(tradNum) || selectPayList != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            AlertDialog alertDialog = builder.setMessage(getString(R.string.registration_exit_tip)).setPositiveButton(getString(R.string.str_ok), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    doOrClearCache(true);
                    dialog.dismiss();
                    registrationFormActivity.this.finish();
                }
            }).setNegativeButton(getString(R.string.str_cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    registrationFormActivity.this.finish();
                }
            }).create();
            alertDialog.show();
        } else registrationFormActivity.this.finish();
    }

    private boolean isEmpty(TextView textView) {
        return TextUtils.isEmpty(getString(textView));
    }

    private String getString(TextView textView) {
        LogUtil.e(textView.getId() + "---" + textView.getText().toString());
        return textView.getText().toString();
    }

    private void doOrClearCache(boolean doCache) {
        //缓存的时候嘉善student_id  加以区分 不同的学生
        SpUtil.getInstance(this).setValue("student_id" + student_id, doCache ? student_id : 0);
        SpUtil.getInstance(this).setValue("cheap_name" + student_id, doCache ? getString(regisCheap) : null);
        SpUtil.getInstance(this).setValue("cheap_price" + student_id, doCache ? (float) cheapTypePrice : 0);
        SpUtil.getInstance(this).setValue("cheap_type" + student_id, doCache ? cheapType : 0);

        SpUtil.getInstance(this).setValue("fisrt_pay_time_unix" + student_id, doCache ? fisrt_pay_time_unix : 0);
        SpUtil.getInstance(this).setValue("sec_end_time_unix" + student_id, doCache ? sec_end_time_unix : 0);
        SpUtil.getInstance(this).setValue("trae_num" + student_id, doCache ? getString(tradNum) : "");
        SpUtil.getInstance(this).setValue("pay_date" + student_id, doCache ? getString(payDate) : "");
        SpUtil.getInstance(this).setValue("sec_pay_date" + student_id, doCache ? getString(secPayDate) : "");
        SpUtil.getInstance(this).setValue("memo" + student_id, doCache ? getString(beizhu) : null);
        SpUtil.getInstance(this).setValue("totalRealMoney" + student_id, doCache ? (float) totalRealMoney : 0);
        SpUtil.getInstance(this).setValue("cheapTypeId" + student_id, cheapTypeId);
        if (selectPayList != null) {
            if (doCache) {
                Type type = new TypeToken<List<PayType>>() {
                }.getType();
                Gson gson = new Gson();
                String toJson = gson.toJson(selectPayList, type);
                LogUtil.e("doCache" + toJson);
                SpUtil.getInstance(this).setValue("pay_type_lisy" + student_id, toJson);
            } else SpUtil.getInstance(this).setValue("pay_type_lisy" + student_id, null);

        }
    }

    private void readCache() {
        int student_id_read = SpUtil.getInstance(this).getIntegerValue("student_id" + student_id);
        if (student_id_read == 0)
            return;
        regisCheap.setText(SpUtil.getInstance(this).getStringValue("cheap_name" + student_id));
        totalRealMoney = SpUtil.getInstance(this).getFloatValue("totalRealMoney" + student_id);
        cheapTypePrice = SpUtil.getInstance(this).getFloatValue("cheap_price" + student_id);
        cheapTypeId = SpUtil.getInstance(this).getIntegerValue("cheapTypeId" + student_id_read);
        cheapType = SpUtil.getInstance(this).getIntegerValue("cheap_type" + student_id);
        fisrt_pay_time_unix = SpUtil.getInstance(this).getIntegerValue("fisrt_pay_time_unix" + student_id);
        sec_end_time_unix = SpUtil.getInstance(this).getIntegerValue("sec_end_time_unix" + student_id);
        tradNum.setText(SpUtil.getInstance(this).getStringValue("trae_num" + student_id));
        payDate.setText(SpUtil.getInstance(this).getStringValue("pay_date" + student_id));
        secPayDate.setText(SpUtil.getInstance(this).getStringValue("sec_pay_date" + student_id));
        beizhu.setText(SpUtil.getInstance(this).getStringValue("memo" + student_id));

        resetRealMoney();
        String stringValue = SpUtil.getInstance(this).getStringValue("pay_type_lisy" + student_id);
        if (!TextUtils.equals(stringValue, "") && stringValue != null) {
            Type type = new TypeToken<List<PayType>>() {
            }.getType();
            Gson gson = new Gson();
            selectPayList = gson.fromJson(stringValue, type);
            if (selectPayList != null) {
                for (int i = 0; i < selectPayList.size(); i++) {
                    PayType payType = selectPayList.get(i);
                    addPayTypeItemView(payType, payType.method, payType.name, payType.method_money + "");
                }
            }
        }
    }
}
