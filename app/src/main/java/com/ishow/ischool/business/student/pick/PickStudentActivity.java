package com.ishow.ischool.business.student.pick;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.commonlib.widget.pull.BaseViewHolder;
import com.commonlib.widget.pull.PullRecycler;
import com.ishow.ischool.R;
import com.ishow.ischool.bean.student.Student;
import com.ishow.ischool.bean.student.StudentList;
import com.ishow.ischool.common.base.BaseListActivity4Crm;
import com.ishow.ischool.util.AppUtil;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTextChanged;

public class PickStudentActivity extends BaseListActivity4Crm<PickStudentPresenter, PickStudentModel, Student> implements PickStudentContract.View {

    public static final String STUDENT_ID = "sid";
    public static final String STUDENT_NAME = "sname";
    public static final String STUDENT = "student";
    @BindView(R.id.pick_search_text_et)
    EditText mSearchEt;

    @Override
    protected void setUpContentView() {
        setContentView(R.layout.activity_pick_student, R.string.pick_student, -1, MODE_BACK);
    }

    @Override
    protected BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
        View view = getLayoutInflater().inflate(R.layout.item_pick_student, parent, false);
        return new PickStudentHolder(view);
    }

    @Override
    public void onRefresh(int action) {
        HashMap<String, String> params = AppUtil.getParamsHashMap(1);
        if (action == PullRecycler.ACTION_PULL_TO_REFRESH) {
            mCurrentPage = 1;
        }
        mPresenter.getStudentStatisticsList(mUser.positionInfo.campusId, params, mCurrentPage++);
    }

    @OnTextChanged(R.id.pick_search_text_et)
    void onSearchTextChanged() {
        HashMap<String, String> params = new HashMap<>();
        mCurrentPage = 1;
        params.put("page", mCurrentPage + "");
        params.put("campus_id", mUser.positionInfo.campusId + "");
        params.put("mobile_or_name", mSearchEt.getText().toString());
        mPresenter.getStudentStatisticsList(mUser.positionInfo.campusId, params, mCurrentPage++);
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

    class PickStudentHolder extends BaseViewHolder {

        @BindView(R.id.user_photo_iv)
        TextView photoIv;
        @BindView(R.id.user_name_tv)
        TextView usernameTv;

        public PickStudentHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        @Override
        public void onBindViewHolder(int position) {
            Student ss = mDataList.get(position);
            photoIv.setText(AppUtil.getLast2Text(ss.studentInfo.name));
            usernameTv.setText(ss.studentInfo.name);
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
