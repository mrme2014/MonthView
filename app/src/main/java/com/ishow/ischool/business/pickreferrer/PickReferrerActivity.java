package com.ishow.ischool.business.pickreferrer;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.commonlib.widget.pull.BaseViewHolder;
import com.commonlib.widget.pull.PullRecycler;
import com.ishow.ischool.R;
import com.ishow.ischool.bean.user.User;
import com.ishow.ischool.bean.user.UserListResult;
import com.ishow.ischool.common.base.BaseListActivity4Crm;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wqf on 16/8/18.
 */
public class PickReferrerActivity extends BaseListActivity4Crm<PickReferrerPresenter, PickReferrerModel, User> implements PickReferrerContract.View {

    public static final int REQUEST_CODE_PICK_REFERRER = 2002;
    public static final String PICKREFERRER = "pick_referrer";

    @Override
    protected void setUpContentView() {
        setContentView(R.layout.activity_pick_referrer, R.string.pick_referrer, R.menu.menu_pickreferrer, MODE_BACK);
    }

    @Override
    public void onRefresh(int action) {
        if (mDataList == null) {
            mDataList = new ArrayList<>();
        }

        if (action == PullRecycler.ACTION_PULL_TO_REFRESH) {
            mCurrentPage = 1;
        }
        mPresenter.getReferrers(mCurrentPage++);
    }

    @Override
    public void getListSuccess(UserListResult userListResult) {
        loadSuccess(userListResult.lists);
    }

    @Override
    public void getListFail(String msg) {
        loadFailed();
        showToast(msg);
    }

    @Override
    protected BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
        View view = getLayoutInflater().inflate(R.layout.item_pick_referrer, parent, false);
        return new PickReferrerHolder(view);
    }

    class PickReferrerHolder extends BaseViewHolder {

        @BindView(R.id.referrer_avatar)
        ImageView referrerAvatar;
        @BindView(R.id.referrer_name)
        TextView referrerName;

        public PickReferrerHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onBindViewHolder(int position) {
            User data = mDataList.get(position);
//            referrerAvatar.setText(AppUtil.getLast2Text(data.getUserInfo().user_name));
            referrerName.setText(data.userInfo.user_name);
        }

        @Override
        public void onItemClick(View view, int position) {
            Intent intent = new Intent();
            User data = mDataList.get(position);
            intent.putExtra(PICKREFERRER, data);
            setResult(RESULT_OK, intent);
            finish();
        }
    }
}
