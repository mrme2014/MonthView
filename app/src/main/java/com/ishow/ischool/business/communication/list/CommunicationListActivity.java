package com.ishow.ischool.business.communication.list;


import android.content.Intent;
import android.content.res.Resources;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.commonlib.util.DateUtil;
import com.commonlib.util.LogUtil;
import com.commonlib.widget.LabelTextView;
import com.commonlib.widget.fabbehavior.HidingScrollListener;
import com.commonlib.widget.pull.BaseViewHolder;
import com.commonlib.widget.pull.PullRecycler;
import com.ishow.ischool.R;
import com.ishow.ischool.activity.CommunicationSearchFragment;
import com.ishow.ischool.application.Resource;
import com.ishow.ischool.bean.market.Communication;
import com.ishow.ischool.bean.market.CommunicationList;
import com.ishow.ischool.business.communication.add.CommunicationAddActivity;
import com.ishow.ischool.business.student.detail.StudentDetailActivity;
import com.ishow.ischool.common.base.BaseListActivity4Crm;
import com.ishow.ischool.common.manager.JumpManager;
import com.ishow.ischool.common.rxbus.RxBus;
import com.ishow.ischool.event.CommunicationAddRefreshEvent;
import com.ishow.ischool.event.CommunicationEditRefreshEvent;
import com.ishow.ischool.fragment.CommuDialogFragment;
import com.ishow.ischool.util.AppUtil;
import com.ishow.ischool.widget.custom.AvatarImageView;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.functions.Action1;

/**
 * 沟通记录页面
 */
public class CommunicationListActivity extends BaseListActivity4Crm<CommunicationListPresenter, CommunicationListModel, Communication> implements CommunicationListContract.View, CommuDialogFragment.selectResultCallback {

    @BindView(R.id.communication_add)
    FloatingActionButton addFab;
    @BindView(R.id.search_content)
    FrameLayout frameLayout;

    //  搜索
    private SearchView mSearchView;
    private String mSearchKey;
    CommunicationSearchFragment searchFragment;

    private HashMap<String, String> mParamsMap;
    private boolean needRefresh;

    CommuDialogFragment dialog = null;

    @Override
    protected void initEnv() {
        super.initEnv();
        initParamsMap();

        RxBus.getDefault().register(CommunicationAddRefreshEvent.class, new Action1<CommunicationAddRefreshEvent>() {
            @Override
            public void call(CommunicationAddRefreshEvent o) {
                needRefresh = true;
            }
        });

        RxBus.getDefault().register(CommunicationEditRefreshEvent.class, new Action1<CommunicationEditRefreshEvent>() {
            @Override
            public void call(CommunicationEditRefreshEvent o) {
                needRefresh = true;
            }
        });
    }

    private void initParamsMap() {
        mParamsMap = AppUtil.getParamsHashMap(Resource.MARKET_STUDENT_STATISTICS);
        mParamsMap.put("list_type", "2");
    }

    @Override
    protected void setUpContentView() {
        setContentView(R.layout.activity_communication_list, R.string.communication_list_title, R.menu.menu_communication_list, MODE_BACK);
    }

    @Override
    protected void setUpView() {
        super.setUpView();

        MenuItem searchItem = mToolbar.getMenu().findItem(R.id.action_search);
        mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        mSearchView.setQueryHint(getString(R.string.hint_search_phone_username));
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                LogUtil.d("SearchView newText = " + newText);
                mSearchKey = newText;
                if (searchFragment == null) {
                    searchFragment = CommunicationSearchFragment.newInstance(Resource.MARKET_STUDENT_STATISTICS + "");
                }
                searchFragment.startSearch(mSearchKey);
                return true;
            }
        });
        mSearchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSearchFragment();
            }
        });
        mSearchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                hideSearchFragment();
                return false;
            }
        });

        // recycleview上滑隐藏fab,下滑显示
        recycler.getRecyclerView().addOnScrollListener(new HidingScrollListener() {
            @Override
            public void onHide() {
                Resources resources = CommunicationListActivity.this.getResources();
                DisplayMetrics dm = resources.getDisplayMetrics();
                float density = dm.density;
                int width = dm.widthPixels;
                int height = dm.heightPixels;
                addFab.animate()
                        .translationY(height - addFab.getHeight())
                        .setInterpolator(new AccelerateInterpolator(2))
                        .start();
            }

            @Override
            public void onShow() {
                addFab.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
            }
        });

        searchFragment = CommunicationSearchFragment.newInstance(Resource.MARKET_STUDENT_STATISTICS + "");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (needRefresh) {
            initParamsMap();
            setRefreshing();
            needRefresh = false;
        }
    }

    @Override
    public void onRefresh(int action) {
        if (mDataList == null) {
            mDataList = new ArrayList<>();
        }

        if (action == PullRecycler.ACTION_PULL_TO_REFRESH) {
            mCurrentPage = 1;
        }

        mPresenter.listCommunication(mParamsMap, mCurrentPage++);
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


    @Override
    protected CommnunicationHolder getViewHolder(ViewGroup parent, int viewType) {
        View view = getLayoutInflater().inflate(R.layout.item_communication_list, parent, false);
        return new CommnunicationHolder(view);
    }

    class CommnunicationHolder extends BaseViewHolder {
        @BindView(R.id.user_photo_iv)
        AvatarImageView userPhotoIv;
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
            userPhotoIv.setText(communication.studentInfo.name, communication.studentInfo.id, communication.studentAvatar != null ? communication.studentAvatar.file_name : "");
            usernameTv.setText(communication.studentInfo.name);
            dateTv.setText(DateUtil.parseDate2Str(communication.communicationInfo.communication_date * 1000, "yyyy-MM-dd"));
            contentTv.setText(communication.communicationInfo.content);
            stateTv.setText(AppUtil.getStateById(communication.communicationInfo.status));
            opposePointTv.setText(AppUtil.getRefuseById(communication.communicationInfo.refuse));
            faithTv.setText(AppUtil.getBeliefById(communication.communicationInfo.belief));
        }

        @Override
        public void onItemClick(View view, int position) {
            Communication communication = mDataList.get(position);
            Intent intent = new Intent(CommunicationListActivity.this, StudentDetailActivity.class);
            intent.putExtra(StudentDetailActivity.P_COMMUNICATION, true);
            intent.putExtra(StudentDetailActivity.P_STUDENT_ID, communication.studentInfo.student_id);
            //Resource.MARKET_STUDENT_STUDENTINFO
            JumpManager.jumpActivity(CommunicationListActivity.this, intent, Resource.NO_NEED_CHECK);
        }
    }

    @OnClick(R.id.communication_add)
    public void onAddCommunication() {
        JumpManager.jumpActivity(this, CommunicationAddActivity.class, Resource.SHARE_COMMUNICATION_ADDM);
    }


    void showSearchFragment() {
        if (searchFragment == null)
            searchFragment = CommunicationSearchFragment.newInstance(Resource.MARKET_STUDENT_STATISTICS + "");
        frameLayout.setVisibility(View.VISIBLE);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.search_content, searchFragment);
        ft.commit();
    }

    void hideSearchFragment() {
        frameLayout.setVisibility(View.GONE);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.remove(searchFragment);
        ft.commit();
        searchFragment = null;
    }


    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_filter:
                if (dialog == null) {
                    dialog = new CommuDialogFragment();
                    dialog.addOnSelectResultCallback(this);
                }

                dialog.show(getSupportFragmentManager(), "dialog");

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
        dialog = null;
    }

    /**
     * @param statePosition
     * @param confidencePosition
     * @param refusePosition
     * @param orderPosition
     * @param startUnix
     * @param endUnix
     */
    @Override
    public void onResult(int statePosition, int confidencePosition, int refusePosition, int orderPosition, long startUnix, long endUnix) {
        //mParamsMap = AppUtil.getParamsHashMap(Resource.MARKET_STUDENT_STATISTICS);
        initParamsMap();
        mCurrentPage = 1;
        mParamsMap.put("page", mCurrentPage + "");
        if (statePosition != 0) {
            mParamsMap.put("student_status", statePosition + "");
        }
        if (refusePosition != 0) {
            mParamsMap.put("refuse_point", refusePosition + "");
        }
        if (confidencePosition != 0) {
            mParamsMap.put("study_belief", confidencePosition + "");
        }
        if (startUnix != 0) {
            mParamsMap.put("begin_time", startUnix + "");
        }
        if (endUnix != 0) {
            mParamsMap.put("end_time", endUnix + "");
        }
        if (orderPosition != 0) {
            mParamsMap.put("incharger", orderPosition + "");
        }

//        mPresenter.listCommunication(mParamsMap);
        setRefreshing();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus.getDefault().unregister(CommunicationAddRefreshEvent.class);
        RxBus.getDefault().unregister(CommunicationEditRefreshEvent.class);
    }
}
