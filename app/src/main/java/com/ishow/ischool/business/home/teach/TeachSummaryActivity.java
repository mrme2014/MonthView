package com.ishow.ischool.business.home.teach;

import android.content.res.Configuration;

import com.commonlib.http.ApiFactory;
import com.inqbarna.tablefixheaders.TableFixHeaders;
import com.inqbarna.tablefixheaders.adapters.MatrixTableAdapter;
import com.ishow.ischool.R;
import com.ishow.ischool.bean.statistics.EducationSummary;
import com.ishow.ischool.bean.statistics.Table1;
import com.ishow.ischool.common.api.ApiObserver;
import com.ishow.ischool.common.api.DataApi;
import com.ishow.ischool.common.base.BaseActivity4Crm;
import com.ishow.ischool.util.AppUtil;

import java.util.HashMap;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class TeachSummaryActivity extends BaseActivity4Crm {

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
        setContentView(R.layout.activity_summary, R.string.group_teach_summary_table, 0, MODE_BACK);
    }

    @Override
    protected void setUpView() {

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
        params.put("start_time", startTime == 0 ? (int) AppUtil.getLastWeekStart() : startTime);
        params.put("end_time", endTime == 0 ? (int) AppUtil.getLastWeekEnd() : endTime);

        ApiFactory.getInstance().getApi(DataApi.class).getHomeEducationSummary(params).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiObserver<EducationSummary>() {
                    @Override
                    public void onSuccess(EducationSummary table) {
                        updateView(table.data);
                    }


                    @Override
                    public void onError(String msg) {
                        showToast(msg);
                    }
                });
    }

    private void updateView(Table1 table) {
        String[][] tableData = new String[table.body.length + 1][];
        tableData[0] = table.head;
        for (int i = 0; i < table.body.length; i++) {
            tableData[i + 1] = table.body[i];
        }
        MatrixTableAdapter<String> matrixTableAdapter = new MatrixTableAdapter<String>(this, tableData);
        tableFixHeaders.setAdapter(matrixTableAdapter);
        tableFixHeaders.invalidate();
    }
}
