package com.ishow.ischool.business.classattention;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.commonlib.util.LogUtil;
import com.commonlib.widget.LabelTextView;
import com.commonlib.widget.pull.BaseItemDecor;
import com.ishow.ischool.R;
import com.ishow.ischool.bean.classattend.ClazInfo;
import com.ishow.ischool.bean.classattend.ClazStudentList;
import com.ishow.ischool.bean.classattend.ClazStudentObject;
import com.ishow.ischool.bean.classattend.TechAvart;
import com.ishow.ischool.common.base.BaseActivity4Crm;
import com.ishow.ischool.common.rxbus.RxBus;
import com.ishow.ischool.event.SignEvent;
import com.ishow.ischool.util.AppUtil;
import com.ishow.ischool.util.PicUtils;
import com.ishow.ischool.widget.custom.AvatarImageView;
import com.ishow.ischool.widget.custom.CircleImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;

/**
 * Created by MrS on 2016/10/20.
 */

public class ClassAttendActivity extends BaseActivity4Crm<ClazPresenter, ClazModle> implements ClazView<ClazStudentList> {
    @BindView(R.id.class_avart_img)
    CircleImageView classAvartImg;
    @BindView(R.id.class_avart_txt)
    AvatarImageView classAvartTxt;
    @BindView(R.id.class_list)
    RecyclerView classList;
    @BindView(R.id.class_name)
    TextView className;
    @BindView(R.id.claz_label)
    LabelTextView clazLabel;

    private int claz_id;
    private ClazListAdapter adapter;
    private List<ClazStudentObject> lists;
    private boolean checkInSucess;
    private ClazInfo classInfo;
    private AlertDialog dialog1;

    public static final String CLASSID = "claz_id";
    public int mItemPosition;

    @Override
    protected void initEnv() {
        super.initEnv();
        Intent intent = getIntent();
        claz_id = intent.getIntExtra(CLASSID, 1);
        mItemPosition = intent.getIntExtra("item_position", -1);
    }

    @Override
    protected void setUpContentView() {
        setContentView(R.layout.activity_class_attendance, "", R.menu.menu_class_attention, MODE_NONE);
    }

    @Override
    protected void setUpView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.inflateMenu(R.menu.menu_class_attention);
        mToolbar.setOnMenuItemClickListener(this);
        mToolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.mipmap.nav_back_normal));
        mToolbarTitle = (TextView) findViewById(R.id.toolbar_title);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                onReadyFinishActivity();
                finish();
            }
        });
        classList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        classList.setHasFixedSize(true);
        classList.addItemDecoration(new BaseItemDecor(this, 16));
        mPresenter.getStudentList(claz_id);
    }

    @Override
    protected void setUpData() {

    }

    @Override
    public void getResutSucess(ClazStudentList result) {
        lists = result.lists;
        adapter = new ClazListAdapter(this, result.lists);
//        adapter.setOnItemClickListner(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(ClassAttendActivity.this, ClazCheckinTableActivity.class);
//                intent.putExtra("claz_id", claz_id);
//                startActivity(intent);
//            }
//        });
        classList.setAdapter(adapter);

        classInfo = result.classInfo;
        TechAvart techAvart = result.teacherAvatar;
        if (classInfo == null)
            return;
        if (techAvart != null && techAvart.file_name != "" && techAvart.file_name != null) {
            PicUtils.loadpic(this, classAvartImg, techAvart.file_name);
        } else {
            classAvartImg.setVisibility(View.GONE);
            classAvartTxt.setVisibility(View.VISIBLE);
            classAvartTxt.setText(classInfo.teacher);
        }
        className.setText("教师:" + classInfo.teacher_name);
        //clazLabel.setLabelTextLeft("出勤  " + classInfo.current_numbers + "     " + "共  " + classInfo.numbers);
        clazLabel.setLabelTextRight(AppUtil.getTodayStr());
        mToolbarTitle.setText("第" + (classInfo.lessoned_times + 1) + "节");
        mToolbarTitle.append("\n");
        SpannableString spanText = new SpannableString(classInfo.course_type + " " + classInfo.name);
        spanText.setSpan(new AbsoluteSizeSpan(12, true), 0, spanText.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        mToolbarTitle.append(spanText);
    }

    @Override
    public void CheckInSucess(String msg) {
        RxBus.getDefault().post(new SignEvent(mItemPosition));
        handProgressbar(false);
        showToast(msg);
        this.finish();
    }

    @Override
    public void getResultEorre(String msg) {
        handProgressbar(false);
        showToast(msg);
    }

    private Button ok, cancel;
    private TextView info;

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (adapter == null || adapter.getItemCount() <= 0)
            return false;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.activity_class_attendance_checkin, null);
        ok = (Button) view.findViewById(R.id.check_in);
        cancel = (Button) view.findViewById(R.id.check_cancel);

        info = (TextView) view.findViewById(R.id.check_info);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                go2ChcekIn();
                dialog1.dismiss();
            }
        });
        int out = 0;
        for (int i = 0; i < adapter.checkInList.size(); i++) {
            if (!adapter.checkInList.get(i))
                out++;
        }
        info.setText(String.format(getString(R.string.check_info), classInfo.current_numbers, classInfo.current_numbers - out + ""));
        builder.setView(view);
        dialog1 = builder.create();
        dialog1.show();
        return super.onMenuItemClick(item);

    }

    private void go2ChcekIn() {
        if (adapter != null && lists != null && lists.size() > 0 && adapter.getItemCount() > 0) {
            JSONArray array = new JSONArray();
            for (int i = 0; i < lists.size(); i++) {
                JSONObject object = new JSONObject();
                try {
                    object.put("student_id", lists.get(i).studentInfo.student_id);
                    object.put("status", adapter.getCheckInState(i));
                    object.put("memo", adapter.getCheckBeiZhuContent(i));
                    //LogUtil.e("student_id="+lists.get(i).studentInfo.student_id+"--status="+adapter.getCheckInState(i));
                    array.put(object);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            handProgressbar(true);
            LogUtil.e(AppUtil.getTodayStart() + "----");
            mPresenter.checkIn(array.toString(), claz_id, AppUtil.getTodayStart());
        }
    }

//    @Override
//    public void onBackPressed() {
//        onReadyFinishActivity();
//    }
//
//    private void onReadyFinishActivity() {
//        if (checkInSucess) {
//            Intent intent = new Intent();
//            intent.putExtra("checkInSucess", checkInSucess);
//            this.setResult(RESULT_OK, intent);
//        }
//        this.finish();
//    }
}
