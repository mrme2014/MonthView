package com.ishow.ischool.business.communication.list;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.content.res.Resources;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import com.commonlib.util.DateUtil;
import com.commonlib.widget.LabelTextView;
import com.commonlib.widget.fabbehavior.HidingScrollListener;
import com.commonlib.widget.pull.BaseViewHolder;
import com.commonlib.widget.pull.PullRecycler;
import com.ishow.ischool.R;
import com.ishow.ischool.application.Resourse;
import com.ishow.ischool.bean.market.Communication;
import com.ishow.ischool.bean.market.CommunicationList;
import com.ishow.ischool.business.communication.add.CommunicationAddActivity;
import com.ishow.ischool.common.base.BaseListActivity4Crm;
import com.ishow.ischool.common.manager.JumpManager;
import com.ishow.ischool.util.AppUtil;
import com.ishow.ischool.widget.custom.CommuDialogFragment;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 沟通记录页面
 */
public class CommunicationListActivity extends BaseListActivity4Crm<CommunicationListPresenter, CommunicationListModel, Communication> implements CommunicationListContract.View, CommuDialogFragment.selectResultCallback {

    private static final int WATH_SEARCH = 10;
    private SearchView mSearchView;
    private HashMap<String, String> mParamsMap;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case WATH_SEARCH:
                    Bundle bundle = msg.getData();
                    String keyword = bundle.getString("keyword");
                    if (keyword.equals(mSearchText)) {
                        mCurrentPage = 1;
                        HashMap<String, String> params = AppUtil.getParamsHashMap(Resourse.COMMUNICATION_LIST);
                        params.put("keyword", keyword);
                        params.put("page", mCurrentPage + "");
                        //setRefreshing();
                        mPresenter.listCommunication(params);
                    }
                    break;
            }
        }
    };
    private String mSearchText;

    @Override
    protected void initEnv() {
        super.initEnv();
        mParamsMap = AppUtil.getParamsHashMap(Resourse.COMMUNICATION_LIST);
    }

    @BindView(R.id.communication_add)
    FloatingActionButton addFab;

    @Override
    protected void setUpContentView() {
        //super.setUpContentView();
        setContentView(R.layout.activity_communication_list, R.string.communication_list_title, R.menu.menu_communication_list, MODE_BACK);

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
                mSearchText = newText;
                Message msg = new Message();
                msg.what = WATH_SEARCH;
                Bundle bundle = new Bundle();
                bundle.putString("keyword", newText);
                msg.setData(bundle);
                mHandler.sendMessageDelayed(msg, 1000);
                return false;
            }
        });

        mSearchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                return false;
            }
        });
    }

    @Override
    protected void setUpView() {
        super.setUpView();

        final MenuItem searchItem = mToolbar.getMenu().findItem(R.id.action_search);
        mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        mSearchView.setQueryHint(getString(R.string.str_search_hint));

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
                mCurrentPage = 1;
                mParamsMap.put("page", mCurrentPage + "");
                mPresenter.listCommunication(mParamsMap);

                break;
            case PullRecycler.ACTION_LOAD_MORE_LOADING:
                mCurrentPage++;
                mParamsMap.put("page", mCurrentPage + "");
                mPresenter.listCommunication(mParamsMap);

                break;
        }
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


    class CommnunicationHolder extends BaseViewHolder {

        @BindView(R.id.user_photo_iv)
        TextView userPhotoIv;

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
            userPhotoIv.setText(AppUtil.getLast2Text(communication.studentInfo.name));
            usernameTv.setText(communication.studentInfo.name);
            dateTv.setText(DateUtil.parseDate2Str(communication.communicationInfo.update_time * 1000, "yyyy-MM-dd"));
            contentTv.setText(communication.communicationInfo.content);
            stateTv.setText(AppUtil.getStateById(communication.communicationInfo.status));
            opposePointTv.setText(AppUtil.getRefuseById(communication.communicationInfo.refuse));
            faithTv.setText(AppUtil.getBeliefById(communication.communicationInfo.belief));
        }
    }

    @OnClick(R.id.communication_add)
    public void onAddCommunication() {
        JumpManager.jumpActivity(this, CommunicationAddActivity.class);
    }

    CommuDialogFragment dialog = null;

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
    public void onResult(int statePosition, int confidencePosition, int refusePosition, int orderPosition, int startUnix, int endUnix) {
        mParamsMap = AppUtil.getParamsHashMap(Resourse.COMMUNICATION_LIST);
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
}
