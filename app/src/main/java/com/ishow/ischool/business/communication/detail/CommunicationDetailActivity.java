package com.ishow.ischool.business.communication.detail;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.commonlib.util.DateUtil;
import com.commonlib.widget.LabelTextView;
import com.ishow.ischool.R;
import com.ishow.ischool.application.Cons;
import com.ishow.ischool.application.Resource;
import com.ishow.ischool.bean.market.Communication;
import com.ishow.ischool.business.communication.edit.CommunicationEditActivity;
import com.ishow.ischool.common.base.BaseActivity4Crm;
import com.ishow.ischool.common.manager.JumpManager;
import com.ishow.ischool.common.rxbus.RxBus;
import com.ishow.ischool.event.CommunicationEditRefreshEvent;
import com.ishow.ischool.util.AppUtil;
import com.ishow.ischool.util.PicUtils;
import com.ishow.ischool.widget.custom.CircleImageView;
import com.ishow.ischool.widget.custom.SelectDialogFragment;
import com.ishow.ischool.widget.pickerview.PickerDialogFragment;
import com.zaaach.citypicker.utils.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by wqf on 16/9/1.
 */
public class CommunicationDetailActivity extends BaseActivity4Crm<CommunicationDetailPresenter, CommunicationDetailModel>
        implements CommunicationDetailContract.View {

    public static final String COMMUNICATION_DATA = "communication_data";
    public static final int REQUEST_SOURCE = 200;

    @BindView(R.id.commun_state)
    LabelTextView communStateTv;
    @BindView(R.id.commun_faith)
    LabelTextView communFaithTv;
    @BindView(R.id.commun_oppose)
    LabelTextView communOpposeTv;
    @BindView(R.id.commun_source)
    LabelTextView communSourceTv;
    @BindView(R.id.commun_back_date)
    LabelTextView communBackDateTv;
    @BindView(R.id.commun_content)
    TextView communContentTv;
    @BindView(R.id.commun_user_avatar)
    CircleImageView avatarCiv;
    @BindView(R.id.oprater_name)
    TextView communNameTv;
    @BindView(R.id.oprater_date)
    TextView communDateTv;

    private Communication mData;

    @Override
    protected void setUpContentView() {
        setContentView(R.layout.activity_communication_detail, R.string.commun_detail, -1, MODE_BACK);
    }

    @Override
    protected void initEnv() {
        super.initEnv();
        mData = (Communication)getIntent().getExtras().get(COMMUNICATION_DATA);
    }

    @Override
    protected void setUpView() {
        communStateTv.setText(AppUtil.getStateById(mData.communicationInfo.status));
        communFaithTv.setText(AppUtil.getBeliefById(mData.communicationInfo.belief));
        communOpposeTv.setText(AppUtil.getRefuseById(mData.communicationInfo.refuse));
        communSourceTv.setEllipsizeText(mData.communicationInfo.tuition_source);
        communBackDateTv.setText(mData.communicationInfo.callback_date == 0 ?
                "" : DateUtil.parseSecond2Str(mData.communicationInfo.callback_date));
        communDateTv.setText(DateUtil.parseSecond2Str(mData.communicationInfo.communication_date));
        communContentTv.setText(mData.communicationInfo.content);
//        SpannableString ss = new SpannableString(getString(R.string.commun_label_content) + ": " + mData.communicationInfo.content);
//        ss.setSpan(new ForegroundColorSpan(Color.parseColor("#333333")), 0, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        communContentTv.setText(ss);
        communNameTv.setText(mData.userInfo.user_name);
        PicUtils.loadAvatarCircle(this, avatarCiv, mData.avatar.file_name);
    }

    @Override
    protected void setUpData() {
        if (!JumpManager.checkUserPermision(this, Resource.COMMUNICATION_EDIT, false) || mData.userInfo.user_id != mUser.userInfo.user_id) {
            communStateTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            communFaithTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            communOpposeTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            communSourceTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            communBackDateTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        }
    }

    @OnClick({R.id.commun_state, R.id.commun_faith, R.id.commun_oppose, R.id.commun_source, R.id.commun_back_date})
    void onClick(View view) {
        if (!JumpManager.checkUserPermision(this, Resource.COMMUNICATION_EDIT, false) || mData.userInfo.user_id != mUser.userInfo.user_id) {
            ToastUtils.showToast(this, R.string.no_permission);
            return;
        }

        final int communId = mData.communicationInfo.id;
        switch (view.getId()) {
            case R.id.commun_state:
                final ArrayList<String> datas = AppUtil.getStateList();
                AppUtil.showItemDialog(getSupportFragmentManager(), datas, new SelectDialogFragment.OnItemSelectedListner() {
                    @Override
                    public void onItemSelected(int position, String txt) {
                        communStateTv.setText(datas.get(position));
                        HashMap<String, String> params = AppUtil.getParamsHashMap(Resource.COMMUNICATION_EDIT);
                        params.put("status", (position + 1) + "");
                        params.put("status_str", datas.get(position));
                        params.put("id", communId + "");
                        mPresenter.editCommunication(params);
                    }
                });
                break;
            case R.id.commun_faith:
                final ArrayList<String> faiths = AppUtil.getBeliefList();
                AppUtil.showItemDialog(getSupportFragmentManager(), faiths, new SelectDialogFragment.OnItemSelectedListner() {
                    @Override
                    public void onItemSelected(int position, String txt) {
                        communFaithTv.setText(faiths.get(position));
                        HashMap<String, String> params = AppUtil.getParamsHashMap(Resource.COMMUNICATION_EDIT);
                        params.put("belief", (position + 1) + "");
                        params.put("belief_str", faiths.get(position));
                        params.put("id", communId + "");
                        mPresenter.editCommunication(params);
                    }
                });
                break;
            case R.id.commun_oppose:
                final ArrayList<String> opposes = AppUtil.getRefuseList();
                AppUtil.showItemDialog(getSupportFragmentManager(), opposes, new SelectDialogFragment.OnItemSelectedListner() {
                    @Override
                    public void onItemSelected(int position, String txt) {
                        communOpposeTv.setText(opposes.get(position));
                        HashMap<String, String> params = AppUtil.getParamsHashMap(Resource.COMMUNICATION_EDIT);
                        params.put("refuse", (position + 1) + "");
                        params.put("refuse_str", opposes.get(position));
                        params.put("id", communId + "");
                        mPresenter.editCommunication(params);
                    }
                });
                break;
            case R.id.commun_source:
                Intent intent = new Intent(CommunicationDetailActivity.this, CommunicationEditActivity.class);
                intent.putExtra(CommunicationEditActivity.P_ID, communId);
                intent.putExtra(CommunicationEditActivity.P_TITLE, getString(R.string.commun_label_source));
                intent.putExtra(CommunicationEditActivity.P_TYPE, Cons.Communication.source);
                intent.putExtra(CommunicationEditActivity.P_TEXT, mData.communicationInfo.tuition_source);
                intent.putExtra(CommunicationEditActivity.P_LEN, 20);
                startActivityForResult(intent, REQUEST_SOURCE);
                break;

            case R.id.commun_back_date:
                AppUtil.showTimePickerDialog(getSupportFragmentManager(), new PickerDialogFragment.Callback<Integer>() {
                    @Override
                    public void onPickResult(Integer object, String... result) {
                        communBackDateTv.setText(result[0]);
                        HashMap<String, String> params = AppUtil.getParamsHashMap(Resource.COMMUNICATION_EDIT);
                        params.put("callback_date", String.valueOf(object));
                        params.put("callback_date_str", result[0]);
                        params.put("id", communId + "");
                        mPresenter.editCommunication(params);
                    }
                });
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_SOURCE) {
                communSourceTv.setText(data.getExtras().get("data") + "");
                RxBus.getDefault().post(new CommunicationEditRefreshEvent());
            }
        }
    }

    @Override
    public void onEditCommunicationFailed(String msg) {

    }

    @Override
    public void onEditCommunicationSucceed(HashMap<String, String> params) {
        RxBus.getDefault().post(new CommunicationEditRefreshEvent());
    }
}
