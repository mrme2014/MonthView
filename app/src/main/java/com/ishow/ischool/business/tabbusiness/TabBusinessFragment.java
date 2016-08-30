package com.ishow.ischool.business.tabbusiness;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.TextView;

import com.commonlib.widget.event.RxBus;
import com.commonlib.widget.pull.DividerGridItemDecoration;
import com.ishow.ischool.R;
import com.ishow.ischool.adpter.BusinessAdapter;
import com.ishow.ischool.bean.user.CampusInfo;
import com.ishow.ischool.bean.user.User;
import com.ishow.ischool.common.base.BaseFragment4Crm;
import com.ishow.ischool.common.manager.CampusManager;
import com.ishow.ischool.common.manager.UserManager;

import java.util.ArrayList;

import butterknife.BindView;
import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by wqf on 16/8/14.
 */
public class TabBusinessFragment extends BaseFragment4Crm<TabBusinessPresenter, TabBusinessModel> implements TabBusinessContract.View {

    @BindView(R.id.business_recyclerview)
    RecyclerView mRecyclerView;
    @BindView(R.id.campus)
    TextView campusTv;
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
        mRecyclerView.addItemDecoration(new DividerGridItemDecoration(getActivity()));
        mAdapter = new BusinessAdapter(mActivity, mModel.getTabSpecs());
        mRecyclerView.setAdapter(mAdapter);
        mPresenter.getCampusList();     //进入app获取所有校区信息
        setCampus();
    }

    void setCampus() {
        User user = UserManager.getInstance().get();
        if (user != null && user.positionInfo != null && !TextUtils.isEmpty(user.positionInfo.campus)) {
            campusTv.setText(user.positionInfo.campus);
        }
        Subscription subscription = RxBus.getInstance().doSubscribe(String.class, new Action1<String>() {

            @Override
            public void call(String s) {
                campusTv.setText(s);
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {

            }
        });
        com.commonlib.widget.event.RxBus.getInstance().addSubscription(this, subscription);
    }

    @Override
    public void getListSuccess(ArrayList<CampusInfo> campusInfos) {
        CampusManager.getInstance().init(getActivity().getApplicationContext());
        CampusManager.getInstance().save(campusInfos);
    }

    @Override
    public void getListFail(String msg) {

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        RxBus.getInstance().unSubscribe(this);
    }
}
