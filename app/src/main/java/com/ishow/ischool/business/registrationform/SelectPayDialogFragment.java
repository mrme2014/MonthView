package com.ishow.ischool.business.registrationform;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.ishow.ischool.R;
import com.ishow.ischool.bean.ApiResult;
import com.ishow.ischool.util.ToastUtil;
import com.ishow.ischool.widget.pickerview.PickerDialogFragment;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func2;

/**
 * Created by MrS on 2016/11/22.
 */

public class SelectPayDialogFragment extends DialogFragment {

    @BindView(R.id.cancel)
    TextView cancel;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.ok)
    TextView ok;
    @BindView(R.id.pay_way)
    TextView payWay;
    @BindView(R.id.pay_money)
    EditText payMoney;


    private String pay_way;
    private String pay_way_acount;

    private int campus_id;

    private List<PayType> bankPayList;
    private List<PayType> apliPayList;
    private List<PayType> tempBankPayList;

    private PayType selectPayType = new PayType();
    private int colum1;
    public int colum2;
    private List<ApiResult<List<PayType>>> payTypeList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Dialog dialog = getDialog();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.activity_registration_pay_dialog, null);
        dialog.setContentView(contentView);
        ButterKnife.bind(this, contentView);

        Bundle bundle = getArguments();
        if (bundle != null) {
            campus_id = bundle.getInt("campuse_id");
        }
        selectPayType.type = "现金";
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void getPayWayList() {
        regisModel mModel = new regisModel();
        Observable observable = mModel.getPayWayList(campus_id);
        Observable observable1 = mModel.getPayWayListAlipay(campus_id);
        Observable.zip(observable, observable1, new Func2<ApiResult<List<PayType>>, ApiResult<List<PayType>>, List<ApiResult<List<PayType>>>>() {
            @Override
            public List<ApiResult<List<PayType>>> call(ApiResult<List<PayType>> o, ApiResult<List<PayType>> o2) {
                List<ApiResult<List<PayType>>> list = new ArrayList();
                list.add(o);
                list.add(o2);
                return list;
            }
        }).subscribe(new Subscriber<List<ApiResult<List<PayType>>>>() {
            @Override
            public void onCompleted() {
                onGetPayListSucess(payTypeList);
            }

            @Override
            public void onError(Throwable e) {
                ToastUtil.showToast(getContext(), e == null ? "data errror" : e.getMessage());
            }

            @Override
            public void onNext(List<ApiResult<List<PayType>>> payTypeList1) {
                payTypeList = payTypeList1;
                // payTypeList = payTypeList1;
            }
        });
    }

    private void onGetPayListSucess(List<ApiResult<List<PayType>>> payTypeList) {
        if (payTypeList != null && payTypeList.size() > 0) {
            Type type1 = new TypeToken<List<PayType>>() {
            }.getType();
            Gson gson = new Gson();
            ApiResult<List<PayType>> listApiResult = payTypeList.get(0);
            if (listApiResult != null) {
                JsonElement result = listApiResult.getResult();
                bankPayList = gson.fromJson(result, type1);
                if (bankPayList != null && bankPayList.size() > 0) {
                    tempBankPayList = new ArrayList<>();
                    for (int i = 0; i < bankPayList.size(); i++) {
                        PayType payType = bankPayList.get(i);
                       // LogUtil.e(bankPayList.size() + "---" + payType.type_id);
                        //检索出 银行卡中的收款卡
                        if (payType.type_id == 2)
                            tempBankPayList.add(payType);
                    }
                }
            }
            if (payTypeList.size() >= 1) {
                ApiResult<List<PayType>> listApiResultBank = payTypeList.get(1);
                if (listApiResult != null) {
                    JsonElement result = listApiResultBank.getResult();
                    apliPayList = gson.fromJson(result, type1);
                }
            }
        }
        PickerDialogFragment.Builder builder = new PickerDialogFragment.Builder();
        ArrayList<String> colums1Datas = new ArrayList<>();
        colums1Datas.add("收款卡");
        colums1Datas.add("支付宝");
        colums1Datas.add("转账");
        colums1Datas.add("现金");
        // .......
        builder.setBackgroundDark(true).setDialogTitle(R.string.registration_pick_acount).setDialogType(PickerDialogFragment.PICK_TYPE_OTHERS).setDatas(0, 2, colums1Datas, getListString(tempBankPayList));
        PickerDialogFragment fragment = builder.Build();
        fragment.show(getChildFragmentManager(), "dialog");
        fragment.addMultilinkPickCallback(new PickerDialogFragment.MultilinkPickCallback<int[]>() {
            @Override
            public ArrayList<String> endSelect(int colum, int selectPosition, String text) {
                //选中的是第一列 不需要更改数据源  直接返回
                if (colum == 1)
                    return null;

                if (colum == 0 && selectPosition == 0) {//银行卡
                    return getListString(tempBankPayList);
                } else if (colum == 0 && selectPosition == 1) {//支付宝
                    return getListString(apliPayList);
                } else if (colum == 0 && selectPosition == 2) {//转账
                    return getListString(tempBankPayList);
                } else if (colum == 0 && selectPosition == 3) {//现金
                    return getListString(null);

                }
                return null;
            }

            @Override
            public void onPickResult(int[] selectIds, String... result) {
             /*把已选择的 支付方式 存起来 因为待会可能要删除 还要传服务器   我的天*/
                colum1 = selectIds[0];
                colum2 = selectIds[1];
                if (colum1 == 0) {
                    if (tempBankPayList != null) selectPayType = tempBankPayList.get(colum2);
                    selectPayType.method_id = 1;
                    selectPayType.method = "刷卡";
                } else if (colum1 == 1) {
                    if (apliPayList != null) selectPayType = apliPayList.get(colum2);
                    selectPayType.method_id = 3;
                    selectPayType.method = "支付宝";
                } else if (colum1 == 2) {
                    if (tempBankPayList != null) selectPayType = tempBankPayList.get(colum2);
                    selectPayType.method_id = 4;
                    selectPayType.method = "转账";
                } else if (colum1 == 3) {
                    selectPayType.method_id = 2;
                    selectPayType.method = "现金";
                }
                // selectPayType.method_id = colum1;
                pay_way_acount = result[1];
                pay_way = result[0];
                if (TextUtils.equals(pay_way_acount, "")) {
                    payWay.setText(pay_way);
                } else {
                    payWay.setText(pay_way);
                    SpannableString string = new SpannableString("\n" + pay_way_acount);
                    if (string != null)
                        string.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getContext(), R.color.txt_9)), 0, string.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    payWay.append(string);
                }
            }
        });
    }

    private ArrayList<String> getListString(List<PayType> payTypeList) {
        ArrayList<String> needDatas = new ArrayList<>();
        if (payTypeList != null) {
            for (int i = 0; i < payTypeList.size(); i++) {
                needDatas.add(payTypeList.get(i).name);
            }
        } else needDatas.add("");
        return needDatas;
    }

    @OnClick({R.id.cancel, R.id.ok, R.id.pay_way})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancel:
                dismiss();
                break;
            case R.id.pay_way:
                if (payTypeList == null)
                    getPayWayList();
                else onGetPayListSucess(payTypeList);
                break;
            case R.id.ok:
                String string = payMoney.getText().toString();
                if (TextUtils.equals(string, "")) {
                    if (callback != null)
                        callback.onError(getContext().getString(R.string.registration_pick_money_not));
                    break;
                }
                if (Double.valueOf(string) < 0.01) {
                    if (callback != null)
                        callback.onError(getContext().getString(R.string.registration_pick_money_not_ok));
                    break;
                }
                if (TextUtils.equals(pay_way, "") || pay_way == null) {
                    if (callback != null)
                        callback.onError(getContext().getString(R.string.registration_pick_acount_no));
                    break;
                }
                if (callback != null) {
                    selectPayType.method_money = (Double.valueOf(payMoney.getText().toString()));
                    callback.onSelect(colum1, colum2, selectPayType, pay_way, pay_way_acount, payMoney.getText().toString());
                    dismiss();
                }
                break;
        }
    }

    public interface onPayTypeSelectCallback {
        void onSelect(int colum1, int colum2, PayType selectPayType, String payWay, String payWayAcount, String money);

        void onError(String msg);
    }

    private onPayTypeSelectCallback callback;

    public void setOnPayTypeSelectCallback(onPayTypeSelectCallback callback1) {
        callback = callback1;
    }
}
