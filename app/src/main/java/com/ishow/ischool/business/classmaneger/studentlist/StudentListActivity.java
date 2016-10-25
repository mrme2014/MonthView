package com.ishow.ischool.business.classmaneger.studentlist;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.baoyz.actionsheet.ActionSheet;
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
            name.setText(data.studentInfo.name);
            final String nameStr = data.studentInfo.name;
            final String phoneNumber = data.studentInfo.mobile;
            if (data.studentInfo.class_state == 2) {        // 停课
                state.setVisibility(View.VISIBLE);
                state.setText("停课");
                state.setBackgroundResource(R.drawable.bg_round_corner_blue);
            }
//            chenduTv.setText(data.);
            keguTv.setText(data.studentInfo.advisor_name);

            phone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ActionSheet.createBuilder(StudentListActivity.this, StudentListActivity.this.getSupportFragmentManager())
                            .setCancelButtonTitle(R.string.str_cancel)
                            .setOtherButtonTitles(nameStr + "\n" + phoneNumber, getString(R.string.call), getString(R.string.phone_contacts))
                            .setCancelableOnTouchOutside(true)
                            .setListener(new ActionSheet.ActionSheetListener() {
                                @Override
                                public void onDismiss(ActionSheet actionSheet, boolean b) {

                                }

                                @Override
                                public void onOtherButtonClick(ActionSheet actionSheet, int i) {
                                    switch (i) {
                                        case 1:
                                            // TODO: 16/8/17  打电话
                                            Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
                                            callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(callIntent);
                                            break;
                                        case 2:
                                            // TODO: 16/8/17  保存至通讯录
                                            Intent intent = new Intent(Intent.ACTION_INSERT_OR_EDIT);
                                            intent.setType("vnd.android.cursor.item/person");
                                            intent.setType("vnd.android.cursor.item/contact");
                                            intent.setType("vnd.android.cursor.item/raw_contact");
                                            //    intent.putExtra(android.provider.ContactsContract.Intents.Insert.NAME, name);
                                            intent.putExtra(android.provider.ContactsContract.Intents.Insert.PHONE, phoneNumber);
                                            intent.putExtra(android.provider.ContactsContract.Intents.Insert.PHONE_TYPE, 3);
                                            startActivity(intent);
                                            break;
                                    }
                                }
                            }).show();
                }
            });
        }

        @Override
        public void onItemClick(View view, int position) {
            Student data = mDataList.get(position);
        }
    }
}
