package com.ishow.ischool.business.home.market;

import android.content.res.Configuration;

import com.commonlib.http.ApiFactory;
import com.inqbarna.tablefixheaders.TableFixHeaders;
import com.inqbarna.tablefixheaders.adapters.MatrixTableAdapter;
import com.ishow.ischool.R;
import com.ishow.ischool.bean.statistics.Table;
import com.ishow.ischool.common.api.ApiObserver;
import com.ishow.ischool.common.api.DataApi;
import com.ishow.ischool.common.base.BaseActivity4Crm;
import com.ishow.ischool.util.AppUtil;

import java.util.HashMap;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MarketSummaryActivity extends BaseActivity4Crm {

    public static final String P_START_TIME = "start_time";
    public static final String P_END_TIME = "end_time";

    @BindView(R.id.table)
    TableFixHeaders tableFixHeaders;

    private HashMap<String, Integer> params = new HashMap<>();

    private int startTime;
    private int endTime;

    @Override
    protected void initEnv() {
        super.initEnv();
        startTime = getIntent().getIntExtra(P_START_TIME, 0);
        endTime = getIntent().getIntExtra(P_END_TIME, 0);
    }


    @Override
    protected void setUpContentView() {
        setContentView(R.layout.activity_summary, R.string.group_market_summary_table, 0, MODE_BACK);
    }

    @Override
    protected void setUpView() {
        tableFixHeaders.setLoading(true);
    }

    @Override
    protected void setUpData() {
        taskGetMarketSummary();
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private void taskGetMarketSummary() {
        params.put("begin_time", startTime == 0 ? (int) AppUtil.getLastWeekStart() : startTime);
        params.put("end_time", endTime == 0 ? (int) AppUtil.getLastWeekEnd() : endTime);

        ApiFactory.getInstance().getApi(DataApi.class).getHomeMarketSummary(params).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiObserver<Table>() {
                    @Override
                    public void onSuccess(Table table) {
                        updateView(table);
                    }


                    @Override
                    public void onError(String msg) {

                    }

                    @Override
                    protected boolean isAlive() {
                        return !isActivityFinished();
                    }
                });
    }

    private void updateView(Table table) {
        String[][] tableData = new String[table.tablebody.length + 1][];
        tableData[0] = table.tablehead;
        for (int i = 0; i < table.tablebody.length; i++) {
            tableData[i + 1] = table.tablebody[i];
        }
        MatrixTableAdapter<String> matrixTableAdapter = new MatrixTableAdapter<String>(this, tableData);
        tableFixHeaders.setAdapter(matrixTableAdapter);
        tableFixHeaders.invalidate();
        tableFixHeaders.setLoading(false);
    }
}
