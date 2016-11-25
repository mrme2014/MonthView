package com.ishow.ischool.business.registrationform;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import com.commonlib.util.DateUtil;
import com.commonlib.util.LogUtil;
import com.ishow.ischool.R;
import com.ishow.ischool.adpter.BasicAdapter;
import com.ishow.ischool.adpter.ViewHolder;
import com.ishow.ischool.bean.registrationform.RegistraInfo;
import com.ishow.ischool.bean.registrationform.RegistraResult;
import com.ishow.ischool.common.base.BaseActivity4Crm;

import java.util.List;

import butterknife.BindView;

/**
 * Created by MrS on 2016/11/24.
 */

public class registraDetailActivity extends BaseActivity4Crm<regisPresenter, regisModel> implements regisView, SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.detail_hasPay)
    TextView detailHasPay;
    @BindView(R.id.detail_notPay)
    TextView detailNotPay;
    @BindView(R.id.detail_list)
    ListView detailList;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    private String action;
    private String feilds = "payListInfo";

    private int student_id;
    private int student_status;
    private cheapListAdapter adapter;

    @Override
    protected void initEnv() {
        super.initEnv();
        student_id = getIntent().getIntExtra(registrationFormActivity.STUDENT_ID, student_id);
        student_status = getIntent().getIntExtra(registrationFormActivity.STUDENT_STATUS, student_status);
    }

    @Override
    protected void setUpContentView() {
        setContentView(R.layout.activity_registration_detail, R.string.registration_detail_list_title, MODE_BACK);
    }

    @Override
    protected void setUpView() {
        if (student_status == 1) {
            action = "apply";
        } else if (student_status == 2) {
            action = "pay";
        }
        mPresenter.getPayInfo(student_id, student_status, action, feilds);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.color_orange),
                getResources().getColor(R.color.chart_red),
                getResources().getColor(R.color.text_tuichu),
                getResources().getColor(R.color.colorAccent));
        swipeRefreshLayout.setOnRefreshListener(this);
        detailList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                View childAt0 = detailList.getChildAt(0);
                if (childAt0 != null) {
                    LogUtil.e(childAt0.getTop() + "--" + detailList.getTop() + "----" + detailList.getPaddingTop());
                }
                if (childAt0 != null && childAt0.getTop() >= detailList.getPaddingTop())
                    swipeRefreshLayout.setEnabled(true);
                else swipeRefreshLayout.setEnabled(false);
            }
        });
    }

    @Override
    protected void setUpData() {

    }

    @Override
    public void getRegistraInfo(RegistraResult registraResult) {
        swipeRefreshLayout.setRefreshing(false);
        if (registraResult != null && registraResult.payListInfo != null) {
            List<RegistraInfo> payListInfo = registraResult.payListInfo;
            if (adapter == null) {
                adapter = new cheapListAdapter(this, payListInfo);
                detailList.setAdapter(adapter);
            } else adapter.notifyDataSetChanged();
            double hasPayed = 0;
            double notPayed = payListInfo.get(0).arrearage;
            detailNotPay.setText(getString(R.string.registration_apply_sure_notpay) + "  " + notPayed);
            for (int i = 0; i < payListInfo.size(); i++) {
                hasPayed += payListInfo.get(i).payed;
            }
            detailHasPay.setText(getString(R.string.registration_apply_sure_haspay) + "  " + hasPayed);
        }
    }

    @Override
    public void getRegistraError(String error) {
        swipeRefreshLayout.setRefreshing(false);
        showToast(error);
    }

    @Override
    public void payActionSucess(String info) {

    }

    @Override
    public void onRefresh() {
        mPresenter.getPayInfo(student_id, student_status, action, feilds);
    }

    class cheapListAdapter extends BasicAdapter<RegistraInfo> {

        public cheapListAdapter(Context context, List<RegistraInfo> datas) {
            super(context, datas);
        }

        @Override
        public View getContentView(int position, View convertView, ViewGroup parent) {
            RegistraInfo registraInfo = datas.get(position);
            ViewHolder holder = ViewHolder.get(context, convertView, parent, R.layout.activity_registration_detail_item, position);
            ((TextView) holder.getView(R.id.detai_price)).setText("+" + registraInfo.payed);
            ((TextView) holder.getView(R.id.detail_tradNum)).setText(registraInfo.receipt_no);
            ((TextView) holder.getView(R.id.detail_date)).setText(DateUtil.parseSecond2Str(Long.valueOf(registraInfo.pay_time)));
            return holder.getConvertView();
        }
    }
}
