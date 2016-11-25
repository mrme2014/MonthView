package com.ishow.ischool.business.tabme;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.commonlib.http.ApiFactory;
import com.commonlib.widget.pull.DividerItemDecoration;
import com.ishow.ischool.R;
import com.ishow.ischool.adpter.WeeklyAdapter;
import com.ishow.ischool.bean.market.SummaryWeekly;
import com.ishow.ischool.bean.user.User;
import com.ishow.ischool.common.api.ApiObserver;
import com.ishow.ischool.common.api.StatisticsApi;
import com.ishow.ischool.common.base.BaseFragment4Crm;
import com.ishow.ischool.common.manager.UserManager;
import com.ishow.ischool.common.rxbus.RxBus;
import com.ishow.ischool.event.WeeklyLoadEvent;

import java.util.List;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by mini on 16/11/24.
 */

public class WeeklySummaryFragment extends BaseFragment4Crm {

    @BindView(R.id.weekly_recyclerview)
    RecyclerView weeklyRecyclerView;
    WeeklyAdapter mAdapter;
    SummaryWeekly mSummaryWeekly;

    private long mBeginTime, mEndTime;
    private String mTitle;
    private User mUser;

    public WeeklySummaryFragment() {
    }

    public static WeeklySummaryFragment newInstance(long begin_time, long end_time, String title) {
        WeeklySummaryFragment fragment = new WeeklySummaryFragment();
        Bundle args = new Bundle();
        args.putLong("begin_time", begin_time);
        args.putLong("end_time", end_time);
        args.putString("title", title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        mUser = UserManager.getInstance().get();
        if (bundle != null) {
            mBeginTime = bundle.getLong("begin_time");
            mEndTime = bundle.getLong("end_time");
            mTitle = bundle.getString("title");
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_weekly_summary;
    }

    @Override
    public void init() {
        weeklyRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), R.drawable.weekly_list_divider));
        weeklyRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        ApiFactory.getInstance().getApi(StatisticsApi.class).getSummaryWeekly(mUser.userInfo.campus_id, mBeginTime, mEndTime)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiObserver<SummaryWeekly>() {
                    @Override
                    public void onSuccess(SummaryWeekly summaryWeekly) {
                        RxBus.getDefault().post(new WeeklyLoadEvent(true));
                        mSummaryWeekly = summaryWeekly;
                        mAdapter = new WeeklyAdapter(getActivity(), mSummaryWeekly);
//                        View headerView = LayoutInflater.from(getActivity()).inflate(R.layout.weekly_headview, weeklyRecyclerView, false);
//                        mWeeklyAdapter.setHeaderView(headerView);
                        weeklyRecyclerView.setAdapter(mAdapter);
                        mAdapter.setOnItemClickListener(new WeeklyAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(int position, RecyclerView.ViewHolder viewHolder) {
                                WeeklyAdapter.ViewHolder holder = (WeeklyAdapter.ViewHolder) viewHolder;
                                // 改变CheckBox的状态
                                holder.checkBox.toggle();
                                // 将CheckBox的选中状况记录下来
                                mAdapter.getmCheckedSparseArray().put(position, holder.checkBox.isChecked());
//                                mAdapter.notifyItemChanged(position);
                            }
                        });
                    }

                    @Override
                    public void onError(String msg) {
                        RxBus.getDefault().post(new WeeklyLoadEvent(false));
                    }
                });
    }


    public String getShareContent() {
        if (mAdapter.getCheckedCount() > 0) {
            StringBuilder content = new StringBuilder(mTitle + "\n\n");
            if (mSummaryWeekly != null && mSummaryWeekly.table != null) {
                for (int i = 0; i < mSummaryWeekly.table.body.size(); i++) {
                    if (mAdapter.getmCheckedSparseArray().get(i)) {
                        List<String> head = mSummaryWeekly.table.head;
                        List<List<String>> body = mSummaryWeekly.table.body;
                        content.append((i + 1) + "." + body.get(i).get(0) + "\n");
                        content.append(head.get(1) + "：" + body.get(i).get(1) + "\n");
                        content.append(head.get(2) + "：" + body.get(i).get(2) + "\n");
                        content.append(head.get(3) + "：" + body.get(i).get(3) + "\n");
                        content.append(head.get(4) + "：" + body.get(i).get(4) + "\n");
                        content.append(head.get(5) + "：" + body.get(i).get(5) + "\n\n");
                    }
                }
            }
            return content.toString();
        }
        return "";
    }

}