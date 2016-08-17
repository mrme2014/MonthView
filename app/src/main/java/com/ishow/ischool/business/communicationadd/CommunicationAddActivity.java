package com.ishow.ischool.business.communicationadd;

import android.view.MenuItem;

import com.commonlib.widget.LabelTextView;
import com.ishow.ischool.R;
import com.ishow.ischool.common.base.BaseActivity4Crm;

import butterknife.BindView;

public class CommunicationAddActivity extends BaseActivity4Crm<CommunicationAddPresenter, CommunicationAddModel> implements CommunicationAddContract.View {

    @BindView(R.id.commun_student_name)
    LabelTextView studentNameTv;
    @BindView(R.id.commun_state)
    LabelTextView stateTv;
    @BindView(R.id.commun_faith)
    LabelTextView faithTv;
    @BindView(R.id.commun_oppose)
    LabelTextView opposeTv;
    @BindView(R.id.commun_back_date)
    LabelTextView backDateTv;
    @BindView(R.id.commun_date)
    LabelTextView dateTv;

    @BindView(R.id.commun_source)
    LabelTextView moneySourceTv;
    @BindView(R.id.commun_content)
    LabelTextView contentTv;


    @Override
    protected void setUpContentView() {
        setContentView(R.layout.activity_communication_add, R.string.add_communication, R.menu.menu_communication_add, MODE_BACK);
    }


    @Override
    protected void setUpView() {

    }

    @Override
    protected void setUpData() {

    }

    @Override
    public void onAddSuccess() {

    }

    @Override
    public void onAddFailed() {

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {

        return super.onMenuItemClick(item);
    }
}
