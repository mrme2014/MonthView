package com.commonlib.widget.pullrecyclerview;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.commonlib.R;
import com.commonlib.util.LogUtil;

import java.util.ArrayList;

public class TestRecyclerViewActivity extends AppCompatActivity {

    PullRecyclerView mPullRecyclerView;
    MyPullRecyclerViewAdapter mAdapter;
    private int mPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_recycler_view);

        initView();
        initData();
    }

    private void initView() {

        mPullRecyclerView = (PullRecyclerView) findViewById(R.id.pull_recyclerview);
        mPullRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mAdapter = new MyPullRecyclerViewAdapter(this);
        mPullRecyclerView.setAdapter(mAdapter);

        mPullRecyclerView.setOnRefreshListener1(new PullRecyclerView.OnRefreshListener1() {

            @Override
            public void onRefresh() {
                LogUtil.d("onRefresh");
                taskGetData();
            }

            @Override
            public void onRefreshMore() {
                LogUtil.d("onRefreshMore");
                taskGetDataMore();

            }
        });

    }

    private void taskGetDataMore() {
        mPage++;
        if (mPage < 3) {
            ArrayList<String> datas = new ArrayList<>();
            for (int i = 0; i < 20; i++) {
                datas.add(mPage + "_" + i);
            }
            mAdapter.addData(datas);
            mPullRecyclerView.setRefreshCompleted();
        } else {
            mPullRecyclerView.setNoMoreData();
        }
    }

    private void taskGetData() {
        mPage = 1;
        ArrayList<String> datas = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            datas.add("1_" + i);
        }
        mAdapter.setData(datas);
        mPullRecyclerView.setRefreshCompleted();
    }


    private void initData() {
        taskGetData();
    }

    class MyPullRecyclerViewAdapter extends PullRecyclerViewAdapter<String> {

        public static final int type_1 = 1;
        public static final int type_2 = 2;

        private Context context;

        public MyPullRecyclerViewAdapter(Context context) {
            this.context = context;
        }

        @Override
        public PullViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
            switch (viewType) {
                case type_1:
                    View view = LayoutInflater.from(context).inflate(R.layout.item_test_recycler_view, parent, false);
                    return new MyViewHolder(view);
            }
            return null;
        }

        @Override
        public void onBindViewHolder(PullViewHolder holder, int position) {
            if (holder instanceof MyViewHolder) {
                String str = mDatas.get(position);
                MyViewHolder myViewHolder = (MyViewHolder) holder;
                myViewHolder.textTv.setText(str);
            }
        }

        @Override
        public int getDataViewType(int position) {
            return type_1;
        }
    }

    class MyViewHolder extends PullViewHolder {

        TextView textTv;

        public MyViewHolder(View itemView) {
            super(itemView);
            textTv = (TextView) itemView.findViewById(R.id.item_test_text);
        }
    }
}
