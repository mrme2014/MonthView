package com.ishow.ischool.business.communicationlist;

import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.commonlib.util.DateUtil;
import com.commonlib.widget.LabelTextView;
import com.commonlib.widget.pull.BaseViewHolder;
import com.commonlib.widget.pull.PullRecycler;
import com.ishow.ischool.R;
import com.ishow.ischool.bean.market.Communication;
import com.ishow.ischool.bean.market.CommunicationList;
import com.ishow.ischool.business.communicationadd.CommunicationAddActivity;
import com.ishow.ischool.common.base.BaseListActivity4Crm;
import com.ishow.ischool.common.manager.JumpManager;
import com.ishow.ischool.util.AppUtil;
import com.ishow.ischool.widget.custom.CommuDialogFragment;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 沟通记录页面
 */
public class CommunicationListActivity extends BaseListActivity4Crm<CommunicationListPresenter, CommunicationListModel, Communication> implements CommunicationListContract.View, CommuDialogFragment.selectResultCallback {

    @Override
    protected void setUpContentView() {
        //super.setUpContentView();
        setContentView(R.layout.activity_communication_list, R.string.communication_list_title, R.menu.menu_communication_list, MODE_BACK);
    }

    @Override
    protected void setUpData() {
        super.setUpData();
    }

    @Override
    protected CommnunicationHolder getViewHolder(ViewGroup parent, int viewType) {
        View view = getLayoutInflater().inflate(R.layout.item_communication_list, parent, false);
        return new CommnunicationHolder(view);
    }

    @Override
    public void onRefresh(int action) {
        HashMap<String, String> map = AppUtil.getParamsHashMap(12);

        switch (action) {
            case PullRecycler.ACTION_PULL_TO_REFRESH:
                mCurrentPage = 1;
                map.put("page", mCurrentPage + "");
                mPresenter.listCommunication(map);

                break;
            case PullRecycler.ACTION_LOAD_MORE_LOADING:
                mCurrentPage++;
                map.put("page", mCurrentPage + "");
                mPresenter.listCommunication(map);

                break;
        }
    }

    @Override
    public void listCommunicationSuccess(CommunicationList data) {
        loadSuccess(data.lists);
    }

    @Override
    public void listCommunicationFailed(String msg) {
        loadFailed();
        showToast(msg);
    }

    @Override
    public boolean isAlive() {
        return !isActivityFinished();
    }



    class CommnunicationHolder extends BaseViewHolder {

        @BindView(R.id.user_photo_iv)
        TextView userPhotoIv;

        @BindView(R.id.user_name)
        TextView usernameTv;

        @BindView(R.id.communication_date)
        TextView dateTv;

        @BindView(R.id.communication_content)
        TextView contentTv;

        @BindView(R.id.user_state)
        LabelTextView stateTv;

        @BindView(R.id.user_oppose_point)
        LabelTextView opposePointTv;

        @BindView(R.id.user_faith)
        LabelTextView faithTv;

        public CommnunicationHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onBindViewHolder(int position) {
            Communication communication = mDataList.get(position);
            userPhotoIv.setText(AppUtil.getLast2Text(communication.studentInfo.name));
            usernameTv.setText(communication.studentInfo.name);
            dateTv.setText(DateUtil.parseDate2Str(communication.communicationInfo.update_time * 1000, "yyyy-MM-dd"));
            contentTv.setText(communication.communicationInfo.content);
            stateTv.setText(AppUtil.getStateById(communication.communicationInfo.status));
            opposePointTv.setText(AppUtil.getRefuseById(communication.communicationInfo.refuse));
            faithTv.setText(AppUtil.getBeliefById(communication.communicationInfo.belief));
        }
    }

    @OnClick(R.id.communication_add)
    public void onAddCommunication() {
        JumpManager.jumpActivity(this, CommunicationAddActivity.class);
    }

    CommuDialogFragment dialog=null;
    public boolean onMenuItemClick(MenuItem item) {
       switch (item.getItemId()){
           case R.id.action_filter:
              if (dialog==null){
                  dialog = new CommuDialogFragment();
                  dialog.show(getSupportFragmentManager(),"dialog");
                  dialog.addOnSelectResultCallback(this);

              }

               break;
           case R.id.action_search:

               break;
       }
        return false;
    }

    @Override
    public void cancelDilaog() {
        getSupportFragmentManager().beginTransaction().remove(dialog).commit();
        dialog.dismiss();
        dialog= null;
    }

    @Override
    public void onResult(int statePosition, int confidencePosition, int refusePosition, int orderPosition, int startUnix, int endUnix) {

    }
}
