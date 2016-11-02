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
import com.ishow.ischool.bean.saleprocess.Subordinate;
import com.ishow.ischool.bean.saleprocess.SubordinateObject;
import com.ishow.ischool.common.base.BaseListActivity4Crm;
import com.ishow.ischool.widget.custom.AvatarImageView;
import com.ishow.ischool.widget.custom.CircleTransform;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wqf on 16/8/18.
 */
public class UserPickActivity extends BaseListActivity4Crm<UserPickPresenter, UserPickModel, SubordinateObject> implements UserPickContract.View<Subordinate> {
    public static final int REQUEST_CODE_PICKUSER = 1002;
    public static final String PICK_USER = "user";

    private SearchView mSearchView;
    private String mSearchKey;
    private boolean mSearchMode = false;
    private int mCampusId, mPositionId;


    @Override
    protected void initEnv() {
        super.initEnv();
        mPositionId = getIntent().getIntExtra(PositionPickActivity.PICK_POSITION_ID, -1);
        mCampusId = getIntent().getIntExtra(PositionPickActivity.PICK_CAMPUS_ID, -1);
    }

    @Override
    protected void setUpContentView() {
        setContentView(R.layout.activity_pick_referrer, R.string.pick_user, R.menu.menu_search, MODE_BACK);
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
                if (mSearchView != null) KeyBoardUtil.closeKeybord(mSearchView, UserPickActivity.this);

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

        mPresenter.getUserByPosition(mCampusId, mPositionId, mSearchKey, mCurrentPage++);
    }


    @Override
    public void getListSuccess(Subordinate subordinate) {
        if (mSearchMode && mCurrentPage == 2) {
            mDataList.clear();
        }
        loadSuccess(subordinate.Subordinate);
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
            SubordinateObject data = mDataList.get(position);
            if (data.avatar != null && !TextUtils.isEmpty(data.avatar)) {
                avatarTv.setVisibility(View.GONE);
                avatarIv.setVisibility(View.VISIBLE);
                Glide.with(getApplicationContext()).load(data.avatar).fitCenter().placeholder(R.mipmap.img_header_default)
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
                avatarTv.setText(data.user_name, data.id, data.avatar);
            }

            referrerName.setText(data.user_name);
        }

        @Override
        public void onItemClick(View view, int position) {
            Intent intent = new Intent();
            SubordinateObject data = mDataList.get(position);
            intent.putExtra(PICK_USER, data);
            setResult(RESULT_OK, intent);
            finish();
        }
    }
}
