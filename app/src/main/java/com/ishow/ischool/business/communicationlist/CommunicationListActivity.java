package com.ishow.ischool.business.communicationlist;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.commonlib.util.DateUtil;
import com.commonlib.widget.LabelTextView;
import com.commonlib.widget.pull.BaseViewHolder;
import com.commonlib.widget.pull.PullRecycler;
import com.ishow.ischool.R;
import com.ishow.ischool.bean.market.Communication;
import com.ishow.ischool.common.base.BaseListActivity4Crm;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 沟通记录页面
 */
public class CommunicationListActivity extends BaseListActivity4Crm<CommunicationListPresenter, CommunicationListModel, Communication> {

    @Override
    protected void setUpContentView() {
        super.setUpContentView();
        setUpToolbar(R.string.communication_list_title, R.menu.menu_communication_list, MODE_BACK);
//        setContentView(com.commonlib.R.layout.activity_base_list, R.string.communication_list_title, R.menu.menu_communication_list, MODE_BACK);
    }

    @Override
    protected CommnunicationHolder getViewHolder(ViewGroup parent, int viewType) {
        View view = getLayoutInflater().inflate(R.layout.item_communication_list, parent, false);
        return new CommnunicationHolder(view);
    }

    @Override
    public void onRefresh(int action) {
        switch (action) {
            case PullRecycler.ACTION_PULL_TO_REFRESH:
                break;
            case PullRecycler.ACTION_LOAD_MORE_LOADING:
                break;
        }
    }

    class CommnunicationHolder extends BaseViewHolder {

        @BindView(R.id.user_photo_iv)
        ImageView userPhotoIv;
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
            ButterKnife.bind(itemView);
        }

        @Override
        public void onBindViewHolder(int position) {
            Communication communication = mDataList.get(position);
//            PicUtils.loadpic(this,userPhotoIv,communication.userInfo.avatar);
            dateTv.setText(DateUtil.parseDate2Str(communication.communicationInfo.update_time, "yyyy-MM-dd"));
            contentTv.setText(communication.communicationInfo.content);
            stateTv.setText(communication.communicationInfo.status);
            opposePointTv.setText(communication.communicationInfo.refuse);
            faithTv.setText(communication.communicationInfo.belief);
        }
    }
}
