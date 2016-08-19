package com.ishow.ischool.business.tabbusiness;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ishow.ischool.R;
import com.ishow.ischool.adpter.BusinessAdapter;
import com.ishow.ischool.common.base.BaseFragment4Crm;

import butterknife.BindView;

/**
 * Created by wqf on 16/8/14.
 */
public class TabBusinessFragment extends BaseFragment4Crm<TabBusinessPresenter, TabBusinessModel> implements TabBusinessContract.View {

    @BindView(R.id.business_recyclerview)
    RecyclerView mRecyclerView;
    BusinessAdapter mAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_business;
    }

    @Override
    public void init() {
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        mRecyclerView.setLayoutManager(new GridLayoutManager(mActivity, 2));
        mAdapter = new BusinessAdapter(mActivity, mModel.getTabSpecs());
        mRecyclerView.setAdapter(mAdapter);
    }




}
