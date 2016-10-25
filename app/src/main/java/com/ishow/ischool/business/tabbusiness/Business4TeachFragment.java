package com.ishow.ischool.business.tabbusiness;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.commonlib.widget.pull.DividerGridItemDecoration;
import com.ishow.ischool.R;
import com.ishow.ischool.adpter.BusinessAdapter;
import com.ishow.ischool.common.base.BaseFragment4Crm;

import butterknife.BindView;

/**
 * Created by wqf on 16/10/20.
 * 教务教学tab
 */
public class Business4TeachFragment extends BaseFragment4Crm<TabBusinessPresenter, TabBusinessModel> implements TabBusinessContract.View {

    @BindView(R.id.business_recyclerview)
    RecyclerView mRecyclerView;
//    @BindView(R.id.campus)
//    TextView campusTv;
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
        mAdapter = new BusinessAdapter(mActivity, mModel.getTabSpecs4Teach());
        mRecyclerView.setAdapter(mAdapter);
//        setCampus();
    }

//    void setCampus() {
//        User user = UserManager.getInstance().get();
//        if (user != null && user.positionInfo != null && !TextUtils.isEmpty(user.positionInfo.campus)) {
//            campusTv.setText(user.positionInfo.campus);
//        }
//        Subscription subscription = RxBus.getInstance().doSubscribe(String.classes, new Action1<String>() {
//
//            @Override
//            public void call(String s) {
//                campusTv.setText(s);
//            }
//        }, new Action1<Throwable>() {
//            @Override
//            public void call(Throwable throwable) {
//
//            }
//        });
//        com.commonlib.widget.event.RxBus.getInstance().addSubscription(this, subscription);
//    }
//
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        RxBus.getInstance().unSubscribe(this);
//    }
}
