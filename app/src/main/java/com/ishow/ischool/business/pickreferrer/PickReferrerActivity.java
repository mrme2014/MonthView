package com.ishow.ischool.business.pickreferrer;

import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.commonlib.util.LogUtil;
import com.commonlib.widget.pull.BaseItemDecor;
import com.commonlib.widget.pull.BaseViewHolder;
import com.commonlib.widget.pull.PullRecycler;
import com.ishow.ischool.R;
import com.ishow.ischool.bean.user.User;
import com.ishow.ischool.bean.user.UserListResult;
import com.ishow.ischool.common.base.BaseListActivity4Crm;
import com.ishow.ischool.util.ColorUtil;
import com.ishow.ischool.widget.custom.AvatarImageView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wqf on 16/8/18.
 */
public class PickReferrerActivity extends BaseListActivity4Crm<PickReferrerPresenter, PickReferrerModel, User> implements PickReferrerContract.View {

    public static final int REQUEST_CODE_PICK_REFERRER = 2002;
    public static final String PICKREFERRER = "pick_referrer";

    private SearchView mSearchView;
    private String mSearchKey;
    private boolean mSearchMode = false;

    @Override
    protected void setUpContentView() {
        setContentView(R.layout.activity_pick_referrer, R.string.pick_referrer, R.menu.menu_pickreferrer, MODE_BACK);
    }

    @Override
    protected RecyclerView.ItemDecoration getItemDecoration() {
        return new BaseItemDecor(this, 67);
    }

    @Override
    protected void setUpView() {
        super.setUpView();
        initSearchView();
    }

    void initSearchView() {
        final MenuItem item = mToolbar.getMenu().findItem(R.id.action_search);
        mSearchView = (SearchView) MenuItemCompat.getActionView(item);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mSearchMode = true;
                LogUtil.d("SearchView newText = " + newText);
                mSearchKey = newText;
                if (TextUtils.isEmpty(newText)) {
                    mSearchKey = null;
                } else {
                    mSearchKey = newText;
                }
                setRefreshing();
                return true;
            }
        });
        mSearchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                mSearchMode = false;
                mSearchKey = null;
                return false;
            }
        });
    }

    @Override
    public void onRefresh(int action) {
        if (mDataList == null) {
            mDataList = new ArrayList<>();
        }

        if (action == PullRecycler.ACTION_PULL_TO_REFRESH) {
            mCurrentPage = 1;
        }

        mPresenter.getReferrers(mSearchKey, mCurrentPage++,mUser.positionInfo.campusId);
    }

    @Override
    public void getListSuccess(UserListResult userListResult) {
        if (mSearchMode && mCurrentPage == 2) {
            mDataList.clear();
        }
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
        AvatarImageView referrerAvatar;
        @BindView(R.id.referrer_name)
        TextView referrerName;

        public PickReferrerHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onBindViewHolder(int position) {
            User data = mDataList.get(position);
            if (data.avatar != null && TextUtils.isEmpty(data.avatar.file_name)) {
                referrerAvatar.setImageUrl(data.avatar.file_name);
            } else {
                referrerAvatar.setText(data.userInfo.user_name);
                referrerAvatar.setBackgroundColor(ColorUtil.getColorById(data.userInfo.user_id));
            }
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
