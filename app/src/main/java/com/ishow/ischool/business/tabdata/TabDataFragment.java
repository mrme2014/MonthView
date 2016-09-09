package com.ishow.ischool.business.tabdata;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.ishow.ischool.R;
import com.ishow.ischool.business.campusperformance.CampusPerformanceActivity;
import com.ishow.ischool.business.salesprocess.SalesProcessActivity;
import com.ishow.ischool.common.base.BaseFragment4Crm;
import com.ishow.ischool.widget.custom.FmItemTextView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by wqf on 16/9/6.
 */
public class TabDataFragment extends BaseFragment4Crm<TabDataPresenter, TabDataModel> implements TabDataContract.View {

    @BindView(R.id.toolbar_title)
    TextView mToolbarTitle;
    @BindView(R.id.sale_process)
    FmItemTextView saleProcessTv;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_data;
    }

    @Override
    public void init() {
        mToolbarTitle.setText(getString(R.string.tab_data));
    }


    @OnClick({R.id.sale_process, R.id.campus_performance})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.sale_process:
                startActivity(new Intent(getActivity(), SalesProcessActivity.class));
                break;
            case R.id.campus_performance:
                startActivity(new Intent(getActivity(), CampusPerformanceActivity.class));
                break;
        }
    }

}
