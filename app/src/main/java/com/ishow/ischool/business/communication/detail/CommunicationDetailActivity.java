package com.ishow.ischool.business.communication.detail;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.commonlib.util.DateUtil;
import com.commonlib.util.KeyBoardUtil;
import com.commonlib.widget.LabelTextView;
import com.ishow.ischool.R;
import com.ishow.ischool.application.Resource;
import com.ishow.ischool.bean.market.Communication;
import com.ishow.ischool.common.base.BaseActivity4Crm;
import com.ishow.ischool.common.manager.JumpManager;
import com.ishow.ischool.common.rxbus.RxBus;
import com.ishow.ischool.event.CommunicationEditRefreshEvent;
import com.ishow.ischool.fragment.SelectDialogFragment;
import com.ishow.ischool.util.AppUtil;
import com.ishow.ischool.util.PicUtils;
import com.ishow.ischool.util.ToastUtil;
import com.ishow.ischool.widget.custom.CircleImageView;
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
    EditText communContentEt;
    @BindView(R.id.commun_user_avatar)
    CircleImageView avatarCiv;
    @BindView(R.id.oprater_name)
    TextView communNameTv;
    @BindView(R.id.commun_content_count)
    TextView communCountTv;

    private Communication mData;
    // 修改后的数据
    private int mCurStatus;
    private int mCurBelief;
    private int mCurRefuse;
    private String mCurSource = "";
    private long mCurDate;
    private String mCurContent = "";
    private HashMap<String, String> updateParams;

    @Override
    protected void setUpContentView() {
        setContentView(R.layout.activity_communication_detail, R.string.commun_detail, R.menu.menu_save, MODE_BACK);
    }

    @Override
    protected void initEnv() {
        super.initEnv();
        mData = (Communication) getIntent().getExtras().get(COMMUNICATION_DATA);
        updateParams = AppUtil.getParamsHashMap(Resource.SHARE_COMMUNICATION_EDITM);
        mCurStatus = mData.communicationInfo.status;
        mCurBelief = mData.communicationInfo.belief;
        mCurRefuse = mData.communicationInfo.refuse;
        mCurSource = mData.communicationInfo.tuition_source;
        mCurDate = mData.communicationInfo.callback_date;
        mCurContent = mData.communicationInfo.content;
    }

    @Override
    protected void setUpView() {
        communStateTv.setText(AppUtil.getStateById(mData.communicationInfo.status));
        communFaithTv.setText(AppUtil.getBeliefById(mData.communicationInfo.belief));
        communOpposeTv.setText(AppUtil.getRefuseById(mData.communicationInfo.refuse));
        communSourceTv.setEllipsizeText(mData.communicationInfo.tuition_source);
        communBackDateTv.setText(mData.communicationInfo.callback_date == 0 ?
                "" : DateUtil.parseSecond2Str(mData.communicationInfo.callback_date));
//        communDateTv.setText(DateUtil.parseSecond2Str(mData.communicationInfo.communication_date));
        communCountTv.setText(600 - mData.communicationInfo.content.length() + "");
        communContentEt.setText(mData.communicationInfo.content);
        communContentEt.setSelection(mData.communicationInfo.content.length());
        if (!JumpManager.checkUserPermision(this, Resource.SHARE_COMMUNICATION_EDITM, false) || mData.userInfo.user_id != mUser.userInfo.user_id) {
            communContentEt.setEnabled(false);
            communCountTv.setVisibility(View.GONE);
        }
//        SpannableString ss = new SpannableString(getString(R.string.commun_label_content) + ": " + mData.communicationInfo.content);
//        ss.setSpan(new ForegroundColorSpan(Color.parseColor("#333333")), 0, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        communContentTv.setText(ss);
        communNameTv.setText(mData.userInfo.user_name);
        communContentEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                communCountTv.setText(600 - s.length() + "");
            }
        });
        PicUtils.loadAvatarCircle(this, avatarCiv, mData.avatar.file_name);
    }

    @Override
    protected void setUpData() {
        if (!checkStudentEditPermision() || mData.userInfo.user_id != mUser.userInfo.user_id) {
            communStateTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            communFaithTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            communOpposeTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            communSourceTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            communBackDateTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                KeyBoardUtil.closeKeybord(communContentEt, CommunicationDetailActivity.this);
                if (checkChanged() == true) {
                    updateParams.put("id", mData.communicationInfo.id + "");
                    updateParams.put("content", mCurContent);
                    mPresenter.editCommunication(updateParams);
                    handProgressbar(true);
                } else {
                    ToastUtil.showToast(CommunicationDetailActivity.this, "没有数据修改");
                }
                break;
        }
        return true;
    }

    boolean checkChanged() {
        mCurContent = communContentEt.getText().toString();
        if (mCurStatus == mData.communicationInfo.status && mCurBelief == mData.communicationInfo.belief
                && mCurRefuse == mData.communicationInfo.refuse && mCurSource.equals(mData.communicationInfo.tuition_source)
                && mCurDate == mData.communicationInfo.callback_date && mCurContent.equals(mData.communicationInfo.content)) {
            return false;
        } else {
            return true;
        }
    }

    @OnClick({R.id.commun_state, R.id.commun_faith, R.id.commun_oppose, R.id.commun_source, R.id.commun_back_date})
    void onClick(View view) {
        if (!checkStudentEditPermision() || mData.userInfo.user_id != mUser.userInfo.user_id) {
            ToastUtils.showToast(this, R.string.no_permission);
            return;
        }

        switch (view.getId()) {
            case R.id.commun_state:
                final ArrayList<String> datas = AppUtil.getStateList();
                AppUtil.showItemDialog(getSupportFragmentManager(), datas, new SelectDialogFragment.OnItemSelectedListner() {
                    @Override
                    public void onItemSelected(int position, String txt) {
                        if (mCurStatus != position + 1) {
                            mCurStatus = position + 1;
                            communStateTv.setText(datas.get(position));
                            updateParams.put("status", mCurStatus + "");
                            updateParams.put("status_str", datas.get(position));
                        }
                    }
                });
                break;
            case R.id.commun_faith:
                final ArrayList<String> faiths = AppUtil.getBeliefList();
                AppUtil.showItemDialog(getSupportFragmentManager(), faiths, new SelectDialogFragment.OnItemSelectedListner() {
                    @Override
                    public void onItemSelected(int position, String txt) {
                        if (mCurBelief != position + 1) {
                            mCurBelief = position + 1;
                            communFaithTv.setText(faiths.get(position));
                            updateParams.put("belief", mCurBelief + "");
                            updateParams.put("belief_str", faiths.get(position));
                        }
                    }
                });
                break;
            case R.id.commun_oppose:
                final ArrayList<String> opposes = AppUtil.getRefuseList();
                AppUtil.showItemDialog(getSupportFragmentManager(), opposes, new SelectDialogFragment.OnItemSelectedListner() {
                    @Override
                    public void onItemSelected(int position, String txt) {
                        if (mCurRefuse != position + 1) {
                            mCurRefuse = position + 1;
                            communOpposeTv.setText(opposes.get(position));
                            updateParams.put("refuse", mCurRefuse + "");
                            updateParams.put("refuse_str", opposes.get(position));
                        }
                    }
                });
                break;
            case R.id.commun_source:
                final ArrayList<String> sources = AppUtil.getSourceList();
                AppUtil.showItemDialog(getSupportFragmentManager(), sources, new SelectDialogFragment.OnItemSelectedListner() {
                    @Override
                    public void onItemSelected(int position, String txt) {
                        if (!mCurSource.equals(sources.get(position))) {
                            mCurSource = sources.get(position);
                            communSourceTv.setText(mCurSource);
                            updateParams.put("tuition_source", mCurSource);
                        }
                    }
                });
                break;
            case R.id.commun_back_date:
                AppUtil.showTimePickerDialog(getSupportFragmentManager(), new PickerDialogFragment.Callback<Integer>() {
                    @Override
                    public void onPickResult(Integer object, String... result) {
                        if (mCurDate != object) {
                            mCurDate = object;
                            communBackDateTv.setText(result[0]);
                            updateParams.put("callback_date", String.valueOf(object));
                            updateParams.put("callback_date_str", result[0]);
                        }
                    }
                });
              break;
        }
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == Activity.RESULT_OK) {
//            if (requestCode == REQUEST_SOURCE) {
//                communSourceTv.setText(data.getExtras().get("data") + "");
//                RxBus.getDefault().post(new CommunicationEditRefreshEvent());
//            }
//        }
//    }

    private boolean checkStudentEditPermision() {
        if (JumpManager.checkUserPermision(this, new int[]{Resource.EDUCATION_CLASSMANAGEMENT_EDITSTUDENT, Resource.MARKET_STUDENT_EDIT}, false)) {
            return true;
        }
        return false;
    }


    @Override
    public void onEditCommunicationFailed(String msg) {
        handProgressbar(false);
        showToast(msg);
    }

    @Override
    public void onEditCommunicationSucceed(HashMap<String, String> params) {
        handProgressbar(false);
        RxBus.getDefault().post(new CommunicationEditRefreshEvent());
        finish();
    }
}
