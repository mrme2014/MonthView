package com.ishow.ischool.business.classes.memberlist;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.commonlib.widget.pull.BaseItemDecor;
import com.commonlib.widget.pull.BaseViewHolder;
import com.commonlib.widget.pull.PullRecycler;
import com.ishow.ischool.R;
import com.ishow.ischool.bean.student.Student;
import com.ishow.ischool.bean.student.StudentList;
import com.ishow.ischool.common.base.BaseListActivity4Crm;
import com.ishow.ischool.widget.custom.AvatarImageView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by wqf on 16/10/20.
 */
public class StudentListActivity extends BaseListActivity4Crm<StudentListPresenter, StudentListModel, Student> implements StudentListContract.View {

    public static final String CLASSID = "class_id";
    public static final String CLASSNAME = "class_name";

    private int mClassId;
    private String mClassName;

    @Override
    protected void initEnv() {
        super.initEnv();
        Intent intent = getIntent();
        mClassId = intent.getIntExtra(CLASSID, -1);
        mClassName = intent.getStringExtra(CLASSNAME);
    }

    @Override
    protected void setUpToolbar(int titleResId, int menuId, int mode) {
        setUpToolbar(mClassName + "班", -1, MODE_BACK);
    }

    @Override
    protected void setUpView() {
        super.setUpView();

    }

    @Override
    protected RecyclerView.ItemDecoration getItemDecoration() {
        return new BaseItemDecor(this, 67);
    }

    @Override
    public void onRefresh(int action) {
        if (mDataList == null) {
            mDataList = new ArrayList<>();
        }

        if (action == PullRecycler.ACTION_PULL_TO_REFRESH) {
            mCurrentPage = 1;
        }

        mPresenter.getListStudent(mClassId);
    }

    @Override
    public void getListSuccess(StudentList studentList) {
        loadSuccess(studentList.lists);
        setUpTitle(mClassName + "班(" + Math.max(studentList.total, getDataCounts()) + ")");
    }

    @Override
    public void getListFail(String msg) {
        loadFailed();
        showToast(msg);
    }


    @Override
    protected BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_class_member, parent, false);
        return new StudentListViewHolder(view);
    }

    class StudentListViewHolder extends BaseViewHolder {
        @BindView(R.id.avatar)
        AvatarImageView avatar;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.state)
        TextView state;
        @BindView(R.id.chengdu_tv)
        TextView chenduTv;
        @BindView(R.id.kegu_tv)
        TextView keguTv;
        @BindView(R.id.phone)
        ImageView phone;

        public StudentListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onBindViewHolder(int position) {
            Student data = mDataList.get(position);
        }

        @Override
        public void onItemClick(View view, int position) {
            Student data = mDataList.get(position);
        }
    }
}
