package com.ishow.ischool.business.classmaneger.classlist;

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

import com.commonlib.util.KeyBoardUtil;
import com.commonlib.util.LogUtil;
import com.commonlib.widget.pull.BaseItemDecor;
import com.commonlib.widget.pull.BaseViewHolder;
import com.commonlib.widget.pull.PullRecycler;
import com.ishow.ischool.R;
import com.ishow.ischool.bean.classes.TeacherInfo;
import com.ishow.ischool.bean.classes.TeacherList;
import com.ishow.ischool.common.base.BaseListActivity4Crm;
import com.ishow.ischool.widget.custom.AvatarImageView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mini on 16/10/25.
 */
public class TeacherPickActivity extends BaseListActivity4Crm<ClassListPresenter, ClassListModel, TeacherInfo> implements ClassListContract.View<TeacherList> {
    public static final int REQUEST_CODE_PICKTEACHER = 1003;
    public static final int REQUEST_CODE_PICKADVISOR = 1004;
    public static final String PICK_USER = "user";
    public static final String PICK_CAMPUS_ID = "pick_campus_id";
    public static final String PICK_TYPE = "pick_type";
    public static final String PICK_TITLE = "pick_title";
    public static final int PICK_TYPE_TEACHER = 1;
    public static final int PICK_TYPE_ADVISOR = 2;

    private SearchView mSearchView;
    private String mSearchKey;
    private boolean mSearchMode = false;
    private boolean enableSelect;
    private int mCampusId;
    private String mTitle;
    private int mPickType;

    @Override
    protected void initEnv() {
        super.initEnv();
        mCampusId = getIntent().getIntExtra(PICK_CAMPUS_ID, -1);
        mTitle = getIntent().getStringExtra(PICK_TITLE);
        mPickType = getIntent().getIntExtra(PICK_TYPE, -1);
    }

    @Override
    protected void setUpContentView() {
        setContentView(R.layout.activity_pick_referrer, mTitle, R.menu.menu_pickreferrer, MODE_BACK);
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

        String paramOption = null;
        if (mPickType == PICK_TYPE_TEACHER) {
            paramOption = "teacherInfo";
        } else if (mPickType == PICK_TYPE_ADVISOR) {
            paramOption = "advisorsInfo";
        }
        mPresenter.listTeacher(mCampusId == -1 ? null : mCampusId, paramOption, mSearchKey, mCurrentPage++);
    }


    @Override
    public void getListSuccess(TeacherList teacherInfos) {
        if (mSearchView != null) KeyBoardUtil.closeKeybord(mSearchView, this);
        if (mSearchMode && mCurrentPage == 2) {
            mDataList.clear();
        }

        if (mPickType == PICK_TYPE_TEACHER) {
            loadSuccess(teacherInfos.teacherInfo);
        } else if (mPickType == PICK_TYPE_ADVISOR) {
            loadSuccess(teacherInfos.advisorsInfo);
        }

        enableSelect = true;
    }

    @Override
    public void getListFail(String msg) {
        if (mSearchView != null) KeyBoardUtil.closeKeybord(mSearchView, this);
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
            TeacherInfo data = mDataList.get(position);
//            if (data.avatar != null && !TextUtils.isEmpty(data.avatar)) {
//                avatarTv.setVisibility(View.GONE);
//                avatarIv.setVisibility(View.VISIBLE);
//                Glide.with(getApplicationContext()).load(data.avatar).fitCenter().placeholder(R.mipmap.img_header_default)
//                        .transform(new CircleTransform(getApplicationContext())).into(new ImageViewTarget<GlideDrawable>(avatarIv) {
//                    @Override
//                    protected void setResource(GlideDrawable resource) {
//                        avatarIv.setImageDrawable(resource);
//                    }
//
//                    @Override
//                    public void setRequest(Request request) {
//                        avatarIv.setTag(position);
//                        avatarIv.setTag(R.id.glide_tag_id, request);
//                    }
//
//                    @Override
//                    public Request getRequest() {
//                        return (Request) avatarIv.getTag(R.id.glide_tag_id);
//                    }
//                });
//            } else {
//                avatarIv.setVisibility(View.GONE);
//                avatarTv.setVisibility(View.VISIBLE);
//                avatarTv.setText(data.user_name, data.id, data.avatar);
//            }

            referrerName.setText(data.user_name);
        }

        @Override
        public void onItemClick(View view, int position) {
            if (!enableSelect)
                return;
            Intent intent = new Intent();
            TeacherInfo data = mDataList.get(position);
            intent.putExtra(PICK_USER, data);
            setResult(RESULT_OK, intent);
            finish();
        }
    }
}
