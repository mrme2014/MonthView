package com.ishow.ischool.business.tabbusiness;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.commonlib.core.BaseView;
import com.commonlib.widget.pull.DividerGridItemDecoration;
import com.ishow.ischool.R;
import com.ishow.ischool.adpter.BusinessAdapter;
import com.ishow.ischool.common.base.BaseFragment4Crm;

import butterknife.BindView;

/**
 * Created by wqf on 16/10/20.
 * 市场业务tab
 */
public class Business4MarketFragment extends BaseFragment4Crm<TabBusinessPresenter, TabBusinessModel> implements BaseView {

    @BindView(R.id.business_recyclerview)
    RecyclerView mRecyclerView;
    BusinessAdapter mAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_business_tabs;
    }

    @Override
    public void init() {
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        mRecyclerView.setLayoutManager(new GridLayoutManager(mActivity, 2));
        mRecyclerView.addItemDecoration(new DividerGridItemDecoration(getActivity()));
        mAdapter = new BusinessAdapter(mActivity, mModel.getTabSpecs4Market());
        mRecyclerView.setAdapter(mAdapter);
    }
}
