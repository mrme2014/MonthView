package com.ishow.ischool.business.user.pick;

import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.commonlib.util.KeyBoardUtil;
import com.commonlib.util.LogUtil;
import com.commonlib.widget.pull.BaseItemDecor;
import com.commonlib.widget.pull.BaseViewHolder;
import com.commonlib.widget.pull.PullRecycler;
import com.ishow.ischool.R;
import com.ishow.ischool.bean.user.User;
import com.ishow.ischool.bean.user.UserListResult;
import com.ishow.ischool.common.base.BaseListActivity4Crm;
import com.ishow.ischool.widget.custom.AvatarImageView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wqf on 16/8/18.
 */
public class UserPickActivity extends BaseListActivity4Crm<UserPickPresenter, UserPickModel, User> implements UserPickContract.View {
    public static final int REQUEST_CODE_PICK_USER = 2002;
    public static final String PICK_USER = "user";
    public static final String P_TITLE = "title";

    private SearchView mSearchView;
    private String mSearchKey;
    private boolean mSearchMode = false;
    private String mTitle;
    private boolean enableSelect;

    @Override
    protected void initEnv() {
        super.initEnv();
        mTitle = getIntent().getStringExtra(P_TITLE);
    }

    @Override
    protected void setUpContentView() {
        setContentView(R.layout.activity_pick_referrer, TextUtils.isEmpty(mTitle) ? getString(R.string.pick_user) : mTitle, R.menu.menu_pickreferrer, MODE_BACK);
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
        mSearchView.setQueryHint(getString(R.string.search_user));
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

        mPresenter.listUser(mUser.positionInfo.campusId, mSearchKey, mCurrentPage++);
    }

    @Override
    public void getListSuccess(UserListResult userListResult) {
        if (mSearchView!=null)KeyBoardUtil.closeKeybord(mSearchView,this);
        if (mSearchMode && mCurrentPage == 2) {
            mDataList.clear();
        }
        loadSuccess(userListResult.lists);
        enableSelect= true;
    }

    @Override
    public void getListFail(String msg) {
        if (mSearchView!=null)KeyBoardUtil.closeKeybord(mSearchView,this);
        loadFailed();
        showToast(msg);
        enableSelect= false;
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
            referrerAvatar.setText(data.userInfo.user_name,data.userInfo.user_id,data.avatar.file_name);
            referrerName.setText(data.userInfo.user_name);
        }

        @Override
        public void onItemClick(View view, int position) {
            if (!enableSelect)
                return;
            Intent intent = new Intent();
            User data = mDataList.get(position);
            intent.putExtra(PICK_USER, data);
            setResult(RESULT_OK, intent);
            finish();
        }
    }
}
