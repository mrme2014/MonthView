package com.ishow.ischool.business.student.pick4teach;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.commonlib.http.ApiFactory;
import com.commonlib.util.LogUtil;
import com.commonlib.widget.pull.BaseItemDecor;
import com.commonlib.widget.pull.BaseViewHolder;
import com.commonlib.widget.pull.PullRecycler;
import com.ishow.ischool.R;
import com.ishow.ischool.bean.attribute.AttrStudent;
import com.ishow.ischool.bean.student.StudentInfo;
import com.ishow.ischool.common.api.ApiObserver;
import com.ishow.ischool.common.api.Attribute;
import com.ishow.ischool.common.base.BaseListActivity4Crm;
import com.ishow.ischool.widget.custom.AvatarImageView;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTextChanged;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class PickStudentActivity extends BaseListActivity4Crm {

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
        params.put("page", mCurrentPage++ + "");
        taskStudentList4Teach(params);
    }

    private void taskStudentList4Teach(HashMap<String, String> params) {
        params.put("option", "campusAllStudent");
        ApiFactory.getInstance().getApi(Attribute.class).getStudentList(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiObserver<AttrStudent>() {
                    @Override
                    public void onSuccess(AttrStudent studentInfos) {
                        getStudentListSuccess(studentInfos);
                    }

                    @Override
                    public void onError(String msg) {
                        getStudentListFail(msg);
                    }
                });
    }

    @OnTextChanged(R.id.pick_search_text_et)
    void onSearchTextChanged(CharSequence text) {
        LogUtil.d("onSearchTextChanged text = " + text.toString());
        if (TextUtils.isEmpty(text)) {
            params.remove("keyword");
        } else {
            params.put("keyword", text.toString());
        }
        setRefreshing();
    }

    public void getStudentListSuccess(AttrStudent studentStatisticsList) {
        loadSuccess(studentStatisticsList.campusAllStudent.list);
    }

    public void getStudentListFail(String msg) {
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
            StudentInfo ss = (StudentInfo) mDataList.get(position);
            photoIv.setText(ss.name, ss.id, ss.avatar);
            usernameTv.setText(ss.name);
            moblieTv.setText(ss.mobile);
        }

        @Override
        public void onItemClick(View view, int position) {
            Intent intent = new Intent();
            StudentInfo student = (StudentInfo) mDataList.get(position);
            intent.putExtra(STUDENT, student);
            setResult(RESULT_OK, intent);
            finish();
        }
    }
}
