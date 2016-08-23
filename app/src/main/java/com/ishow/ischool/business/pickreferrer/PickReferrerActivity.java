package com.ishow.ischool.business.pickreferrer;

import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.commonlib.util.LogUtil;
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

    private ArrayList<User> originalDatas = new ArrayList<>();
    private SearchView mSearchView;
    private boolean isFirst = true;
    private String mSearchKey = "";

    @Override
    protected void setUpContentView() {
        setContentView(R.layout.activity_pick_referrer, R.string.pick_referrer, R.menu.menu_pickreferrer, MODE_BACK);
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
                LogUtil.d("SearchView newText = " + newText);
                if (TextUtils.isEmpty(newText)) {
                    loadFailed();
                } else {
                    mCurrentPage = 1;
                    mSearchKey = newText;
                    mPresenter.getReferrers(mSearchKey, mCurrentPage++);
                }
                return true;
            }
        });
        mSearchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                mDataList.clear();
                mSearchKey = "";
                loadSuccess(originalDatas);
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
        if (mSearchKey.equals("")) {
            mPresenter.getReferrers(null, mCurrentPage++);
        } else {
            mPresenter.getReferrers(mSearchKey, mCurrentPage++);
        }
    }

    @Override
    public void getListSuccess(UserListResult userListResult) {
        if (isFirst) {
            originalDatas.clear();
            originalDatas.addAll(userListResult.lists);
            isFirst = false;
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
