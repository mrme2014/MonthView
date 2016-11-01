package com.ishow.ischool.business.user.pick;

import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.commonlib.util.KeyBoardUtil;
import com.commonlib.util.LogUtil;
import com.commonlib.widget.pull.BaseItemDecor;
import com.commonlib.widget.pull.BaseViewHolder;
import com.commonlib.widget.pull.PullRecycler;
import com.ishow.ischool.R;
import com.ishow.ischool.bean.user.CampusInfo;
import com.ishow.ischool.bean.user.User;
import com.ishow.ischool.bean.user.UserListResult;
import com.ishow.ischool.common.base.BaseListActivity4Crm;
import com.ishow.ischool.widget.custom.AvatarImageView;
import com.ishow.ischool.widget.custom.CircleTransform;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wqf on 16/8/18.
 */
public class AgentPickActivity extends BaseListActivity4Crm<UserPickPresenter, UserPickModel, User> implements UserPickContract.View<UserListResult> {
    public static final int REQUEST_CODE_PICKAGENT = 1003;
    public static final String PICK_USER = "user";
    public static final String P_CAMPUS = "campus";

    private SearchView mSearchView;
    private String mSearchKey;
    private boolean mSearchMode = false;
    private boolean enableSelect;

    private CampusInfo campusInfo;


    @Override
    protected void setUpContentView() {
        setContentView(R.layout.activity_pick_referrer, R.string.pick_banliren, R.menu.menu_search, MODE_BACK);
    }

    @Override
    protected RecyclerView.ItemDecoration getItemDecoration() {
        return new BaseItemDecor(this, 67);
    }

    @Override
    protected void initEnv() {
        super.initEnv();
        campusInfo = getIntent().getParcelableExtra(P_CAMPUS);
    }

    @Override
    protected void setUpView() {
        super.setUpView();
        initSearchView();
        String campusName = mUser.campusInfo.name;
        if (campusInfo != null) {
            campusName = campusInfo.name;
        }
        setUpTitle(campusName + "-" + getString(R.string.pick_banliren));
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
                if (mSearchView != null)
                    KeyBoardUtil.closeKeybord(mSearchView, AgentPickActivity.this);
//                if (mSearchKey == null && newText.equals("")) {
//                    //第一次默认不加载
//                } else {
                mSearchMode = true;
                LogUtil.d("SearchView newText = " + newText);
                mSearchKey = newText;
                if (TextUtils.isEmpty(newText)) {
                    mSearchKey = null;
                } else {
                    mSearchKey = newText;
                }
                setRefreshing();
//                }
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

        int campusId = campusInfo == null ? mUser.positionInfo.campusId : campusInfo.id;
        mPresenter.listUser(campusId, mSearchKey, mCurrentPage++);
    }

    @Override
    public void getListSuccess(UserListResult userListResult) {
        if (mSearchMode && mCurrentPage == 2) {
            mDataList.clear();
        }
        loadSuccess(userListResult.lists);
        enableSelect = true;
    }

    @Override
    public void getListFail(String msg) {
        loadFailed();
        showToast(msg);
        enableSelect = false;
    }

    @Override
    protected BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
        View view = getLayoutInflater().inflate(R.layout.item_pick_referrer, parent, false);
        return new PickReferrerHolder(view);
    }

    class PickReferrerHolder extends BaseViewHolder {

        @BindView(R.id.avatar_text)
        AvatarImageView avatarTv;
        @BindView(R.id.avatar_iv)
        ImageView avatarIv;
        @BindView(R.id.referrer_name)
        TextView referrerName;

        public PickReferrerHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onBindViewHolder(final int position) {
            User data = mDataList.get(position);
            if (data.avatar != null && !TextUtils.isEmpty(data.avatar.file_name)) {
                avatarTv.setVisibility(View.GONE);
                avatarIv.setVisibility(View.VISIBLE);
                Glide.with(getApplicationContext()).load(data.avatar.file_name).fitCenter().placeholder(R.mipmap.img_header_default)
                        .transform(new CircleTransform(getApplicationContext())).into(new ImageViewTarget<GlideDrawable>(avatarIv) {
                    @Override
                    protected void setResource(GlideDrawable resource) {
                        avatarIv.setImageDrawable(resource);
                    }

                    @Override
                    public void setRequest(Request request) {
                        avatarIv.setTag(position);
                        avatarIv.setTag(R.id.glide_tag_id, request);
                    }

                    @Override
                    public Request getRequest() {
                        return (Request) avatarIv.getTag(R.id.glide_tag_id);
                    }
                });
            } else {
                avatarIv.setVisibility(View.GONE);
                avatarTv.setVisibility(View.VISIBLE);
                avatarTv.setText(data.userInfo.user_name, data.userInfo.user_id, data.avatar.file_name);
            }

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
