package com.ishow.ischool.business.address;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.AdapterView;

import com.commonlib.http.ApiFactory;
import com.commonlib.widget.pullrecyclerview.PullRecyclerView;
import com.ishow.ischool.R;
import com.ishow.ischool.bean.university.Address;
import com.ishow.ischool.common.api.ApiObserver;
import com.ishow.ischool.common.api.StudentApi;
import com.ishow.ischool.common.api.UniversityApi;
import com.ishow.ischool.common.base.BaseActivity4Crm;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class AddressListActivity extends BaseActivity4Crm {

    public static final String P_STUDENT_ID = "student_id";
    @BindView(R.id.address_list)
    PullRecyclerView recyclerView;

    private AddrAdapter mAdapter;

    private ArrayList<Address> proviceList;
    private int deep;

    private Address curProvice;
    private Address curCity;
    private int studentId;

    @Override
    protected void initEnv() {
        super.initEnv();
        proviceList = new ArrayList<>();
        studentId = getIntent().getIntExtra(P_STUDENT_ID, 0);
    }

    @Override
    protected void setUpContentView() {
        setContentView(R.layout.activity_address_list, R.string.label_student_hometown);
    }

    @Override
    protected void setUpView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new AddrAdapter(this);
        recyclerView.setAdapter(mAdapter);

        recyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Address addr = mAdapter.getData(position);
                if (deep == 1) {
                    curCity = addr;
                    taskEditStudent(curProvice.id, curCity.id);
                } else {
                    curProvice = addr;
                    deep = 1;
                    recyclerView.setRefreshing(true);
                    taskGetCity(curProvice.id);
                }

            }
        });
        recyclerView.setMode(PullRecyclerView.MODE_NONE);
    }

    @Override
    protected void setUpData() {
        recyclerView.setRefreshing(true);
        taskGetProvice();
    }

    private void taskGetCity(int id) {
        ApiFactory.getInstance().getApi(UniversityApi.class).getCityByProvinceId(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiObserver<ArrayList<Address>>() {
                    @Override
                    public void onSuccess(ArrayList<Address> addresses) {
                        recyclerView.setPageData(addresses, 1);
                    }

                    @Override
                    public void onError(String msg) {
                        showToast(msg);
                    }

                    @Override
                    public void onCompleted() {
                        recyclerView.setRefreshing(false);
                    }
                });
    }

    private void taskGetProvice() {
        ApiFactory.getInstance().getApi(UniversityApi.class).getProvince()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiObserver<ArrayList<Address>>() {
                    @Override
                    public void onSuccess(ArrayList<Address> addresses) {
                        recyclerView.setPageData(addresses, 1);
                        proviceList = addresses;
                    }

                    @Override
                    public void onError(String msg) {
                        showToast(msg);
                    }

                    @Override
                    public void onCompleted() {
                        recyclerView.setRefreshing(false);
                    }
                });
    }

    private void taskEditStudent(int pid, int cid) {
        HashMap<String, String> params = new HashMap<>();
        params.put("hometown_pid", pid + "");
        params.put("hometown_cid", cid + "");
        params.put("id", studentId + "");
        ApiFactory.getInstance().getApi(StudentApi.class).editStudent(params).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiObserver<Object>() {
                    @Override
                    public void onSuccess(Object o) {
                        Intent intent = new Intent();
                        intent.putExtra("hometown_pid", curProvice.id);
                        intent.putExtra("hometown_cid", curCity.id);
                        intent.putExtra("hometown_pname", curProvice.name);
                        intent.putExtra("hometown_cname", curCity.name);
                        setResult(RESULT_OK, intent);
                        finish();
                    }

                    @Override
                    public void onError(String msg) {

                    }
                });
    }


    @Override
    public void onBackPressed() {
        if (deep > 0) {
            popProvice();
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onNavigationBtnClicked() {
        if (deep > 0) {
            popProvice();
            return;
        }
        super.onNavigationBtnClicked();
    }

    private void popProvice() {
        deep = 0;
        mAdapter.setData(proviceList);
    }
}
