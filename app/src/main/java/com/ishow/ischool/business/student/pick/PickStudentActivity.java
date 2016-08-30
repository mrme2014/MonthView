package com.ishow.ischool.business.student.pick;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.commonlib.util.LogUtil;
import com.commonlib.widget.pull.BaseItemDecor;
import com.commonlib.widget.pull.BaseViewHolder;
import com.commonlib.widget.pull.PullRecycler;
import com.ishow.ischool.R;
import com.ishow.ischool.bean.student.Student;
import com.ishow.ischool.bean.student.StudentList;
import com.ishow.ischool.common.base.BaseListActivity4Crm;
import com.ishow.ischool.util.ColorUtil;
import com.ishow.ischool.widget.custom.AvatarImageView;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTextChanged;

public class PickStudentActivity extends BaseListActivity4Crm<PickStudentPresenter, PickStudentModel, Student> implements PickStudentContract.View {

    public static final String STUDENT_ID = "sid";
    public static final String STUDENT_NAME = "sname";
    public static final String STUDENT = "student";
    HashMap<String, String> params;
    @BindView(R.id.pick_search_text_et)
    EditText mSearchEt;

    @Override
    protected void setUpContentView() {
        setContentView(R.layout.activity_pick_student, R.string.pick_student, -1, MODE_BACK);
    }

    @Override
    protected RecyclerView.ItemDecoration getItemDecoration() {
        return new BaseItemDecor(this, 67);
    }

    @Override
    protected void setUpData() {
        super.setUpData();
        params = new HashMap<>();
        params.put("source", "-1");
    }

    @Override
    public void onRefresh(int action) {
        if (action == PullRecycler.ACTION_PULL_TO_REFRESH) {
            mCurrentPage = 1;
        }
        params.put("campus_id", mUser.positionInfo.campusId + "");
        mPresenter.getStudentStatisticsList(params, mCurrentPage++);
    }

    @OnTextChanged(R.id.pick_search_text_et)
    void onSearchTextChanged(CharSequence text) {
        LogUtil.d("onSearchTextChanged text = " + text.toString());
        if (TextUtils.isEmpty(text)) {
            params.remove("mobile_or_name");
        } else {
            params.put("mobile_or_name", text.toString());
        }
        setRefreshing();
    }


    @Override
    public void getListSuccess(StudentList studentStatisticsList) {
        loadSuccess(studentStatisticsList.lists);
    }

    @Override
    public void getListFail(String msg) {
        loadFailed();
        showToast(msg);
    }

    @Override
    protected BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
        View view = getLayoutInflater().inflate(R.layout.item_pick_student, parent, false);
        return new PickStudentHolder(view);
    }

    class PickStudentHolder extends BaseViewHolder {

        @BindView(R.id.user_photo_iv)
        AvatarImageView photoIv;
        @BindView(R.id.user_name_tv)
        TextView usernameTv;
        @BindView(R.id.user_mobile_tv)
        TextView moblieTv;

        public PickStudentHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onBindViewHolder(int position) {
            Student ss = mDataList.get(position);
            photoIv.setText(ss.studentInfo.name);
            photoIv.setBackgroundColor(ColorUtil.getColorById(ss.studentInfo.id));
            usernameTv.setText(ss.studentInfo.name);
            moblieTv.setText(ss.studentInfo.mobile);
        }

        @Override
        public void onItemClick(View view, int position) {
            Intent intent = new Intent();
            Student student = mDataList.get(position);
            intent.putExtra(STUDENT, student.studentInfo);
            setResult(RESULT_OK, intent);
            finish();
        }
    }
}
