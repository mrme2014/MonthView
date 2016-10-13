package com.ishow.ischool.business.student.detail;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import com.commonlib.util.PermissionUtil;
import com.commonlib.util.StorageUtil;
import com.commonlib.widget.LabelTextView;
import com.ishow.ischool.R;
import com.ishow.ischool.adpter.FragmentAdapter;
import com.ishow.ischool.application.Resource;
import com.ishow.ischool.bean.student.Student;
import com.ishow.ischool.bean.student.StudentInfo;
import com.ishow.ischool.bean.user.Avatar;
import com.ishow.ischool.business.communication.add.CommunicationAddActivity;
import com.ishow.ischool.common.base.BaseActivity4Crm;
import com.ishow.ischool.common.manager.JumpManager;
import com.ishow.ischool.common.rxbus.RxBus;
import com.ishow.ischool.fragment.SelectDialogFragment;
import com.ishow.ischool.util.AppUtil;
import com.ishow.ischool.util.PhotoUtil;
import com.ishow.ischool.widget.custom.AvatarImageView;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

public class StudentDetailActivity extends BaseActivity4Crm<StudentDetailPresenter, StudentDetailModel> implements
        CommunicationListFragment.OnFragmentInteractionListener, StudentInfoFragment.OnFragmentInteractionListener,
        StudentDetailContract.View {

    public static final String P_STUDENT = "student";
    public static final String P_STUDENT_ID = "student_id";
    public static final String P_COMMUNICATION = "communication";
    @BindView(R.id.tabs)
    TabLayout mTabs;

    @BindView(R.id.student_viewpager)
    ViewPager mViewPaper;
    @BindView(R.id.student_avatar_iv)
    AvatarImageView avatarTv;
    @BindView(R.id.student_user_name)
    TextView usernameTv;
    @BindView(R.id.student_apply_state)
    LabelTextView applyStateLtv;
    @BindView(R.id.student_class_hour)
    LabelTextView classHourLtv;
    @BindView(R.id.student_tuition)
    LabelTextView tuitionLtv;
    @BindView(R.id.titlebar_title)
    TextView titlebarTitleTv;
    @BindView(R.id.appbar)
    AppBarLayout mAppBar;
    @BindView(R.id.fab)
    FloatingActionButton addFab;

    public Student student;
    public int studentId;

    private StudentInfoFragment studentInfoFragment;
    private CommunicationListFragment communicationListFragment;
    private FragmentAdapter mFragmentAdapter;
    private boolean isCommun;
    private boolean needRefresh;
    private String tempPath = StorageUtil.getTempDir().getAbsolutePath() + "/capture.avatar";
    private String tempCropPath = StorageUtil.getTempDir().getAbsolutePath() + "/capture_crop.avatar";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            tempPath = savedInstanceState.getString("tempPath");
            tempCropPath = savedInstanceState.getString("tempCropPath");
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initEnv() {
        super.initEnv();
        studentId = getIntent().getIntExtra(P_STUDENT_ID, 0);
        if (studentId == 0) {
            StudentInfo student = getIntent().getParcelableExtra(P_STUDENT);
            if (student != null) {
                studentId = student.student_id;
            }
        }
        isCommun = getIntent().getBooleanExtra(P_COMMUNICATION, false);

    }

    @Override
    protected void setUpContentView() {
        setContentView(R.layout.activity_student_detail, -1, -1, MODE_BACK);
    }

    @Override
    protected void setUpView() {

        initViewPager();

        mTabs.setTabGravity(TabLayout.GRAVITY_FILL);
        mTabs.setupWithViewPager(mViewPaper);

        mAppBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int maxScroll = appBarLayout.getTotalScrollRange();
                float percentage = (float) Math.abs(verticalOffset) / (float) maxScroll;
                handleAlphaOnTitle(percentage);
            }
        });
    }

    private void handleAlphaOnTitle(float percentage) {
        titlebarTitleTv.setAlpha(percentage);
        //mToolbar.getBackground().setAlpha((int) (255 * percentage));
    }

    private void initViewPager() {
        ArrayList<Fragment> fragments = new ArrayList<>();
        studentInfoFragment = StudentInfoFragment.newInstance();
        communicationListFragment = CommunicationListFragment.newInstance();
        fragments.add(studentInfoFragment);
        fragments.add(communicationListFragment);

        ArrayList<String> titleList = new ArrayList<>();
        titleList.add(getString(R.string.student_detail));
        titleList.add(getString(R.string.commun_list));
        mFragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), fragments, titleList);
        mViewPaper.setAdapter(mFragmentAdapter);
        mViewPaper.setCurrentItem(isCommun ? 1 : 0);
        final int screenHeight = getResources().getDisplayMetrics().heightPixels;
        mViewPaper.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position == 0) {
                    addFab.animate()
                            .translationY(screenHeight - addFab.getHeight())
                            .setInterpolator(new AccelerateInterpolator(2))
                            .start();
                } else {
                    addFab.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void setUpData() {
        if (studentId != 0) {
            HashMap<String, String> params = new HashMap<>();
            params.put("id", studentId + "");
            params.put("resources_id", "7");
            params.put("fields", "studentInfo,avatarInfo");
            mPresenter.getStudent(params);
        }
    }

    @Override
    public void onFragmentInteraction(Bundle data) {
        needRefresh = data.getBoolean("refresh", false);
    }

    @Override
    public void onGetStudentSuccess(Student student) {
        this.student = student;
        updateView(student);
    }

    private void updateView(Student student) {
//        avatarTv.setText(student.studentInfo.name);
//        avatarTv.setBackgroundColor(ColorUtil.getColorById(student.studentInfo.id));
        avatarTv.setText(student.studentInfo.name, student.studentInfo.id, student.avatarInfo.file_name);

        usernameTv.setText(student.studentInfo.name);
        applyStateLtv.setText(student.studentInfo.pay_state_name);
        classHourLtv.setText(student.studentInfo.class_hour + "/" + student.studentInfo.class_hour_total);
        tuitionLtv.setText(student.studentInfo.payed + "");
        titlebarTitleTv.setText(student.studentInfo.name);

        studentInfoFragment.refresh();
        communicationListFragment.initData();
    }

    @Override
    public void onGetStudentFailed(String msg) {
        showToast(msg);
    }

    @Override
    public void onEditStudentSuccess(Object student) {

    }

    @Override
    public void onEditStudentFailed(String msg) {
        showToast(msg);
    }


    @Override
    public void onUploadAvatarFailed(String msg) {
        showToast(msg);
    }

    @Override
    public void updateAvatar(int per_net_token_sucess, Avatar avatar) {
        avatarTv.setText("", 0, avatar.file_name);
    }

    public StudentInfo getStudentInfo() {
        if (student != null) {
            return student.studentInfo;
        }
        return null;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (needRefresh) {
            RxBus.getDefault().post(new StudentInfo());
        }
    }

    @Override
    protected void onNavigationBtnClicked() {
        super.onNavigationBtnClicked();
        if (needRefresh) {
            RxBus.getDefault().post(new StudentInfo());
        }
    }

    public Student getStudent() {
        if (student != null) {
            return student;
        }
        return null;
    }

    @OnClick({R.id.fab, R.id.student_avatar_iv})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                Intent intent = new Intent(this, CommunicationAddActivity.class);
                intent.putExtra(CommunicationAddActivity.P_STUDENT_INFO, getStudentInfo());
                JumpManager.jumpActivity(this, intent, Resource.COMMUNICATION_ADD);
                break;

            case R.id.student_avatar_iv:
                if (JumpManager.checkUserPermision(this, Resource.STUDENT_EDIT)) {
                    ArrayList<String> avatars = new ArrayList<>();
                    avatars.add(getString(R.string.capture));
                    avatars.add(getString(R.string.amblue));
                    AppUtil.showItemDialog(getSupportFragmentManager(), avatars, new SelectDialogFragment.OnItemSelectedListner() {
                        @Override
                        public void onItemSelected(int position, String txt) {
                            StudentDetailActivity.this.onItemSelected(position);
                        }
                    });
                }
                break;
        }

    }

    private void onItemSelected(int position) {
        if (position == 0) {
            PermissionUtil.getInstance().checkPermission(this, new PermissionUtil.PermissionChecker() {
                @Override
                public void onGrant(String grantPermission, int index) {
                    PhotoUtil.capture(StudentDetailActivity.this, tempPath);
                }

                @Override
                public void onDenied(String deniedPermission, int index) {
                    showToast(R.string.permission_camera_denid);
                }
            }, Manifest.permission.CAMERA);
        } else if (position == 1) {
            PermissionUtil.getInstance().checkPermission(this, new PermissionUtil.PermissionChecker() {
                @Override
                public void onGrant(String grantPermission, int index) {
                    PhotoUtil.selectAlbums(StudentDetailActivity.this);
                }

                @Override
                public void onDenied(String deniedPermission, int index) {
                    showToast(R.string.permission_storage_denid);
                }
            }, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        PhotoUtil.onActivityResult(this, tempPath, tempCropPath, requestCode, resultCode, data, new PhotoUtil.UploadListener() {
            @Override
            public void upload() {
                mPresenter.uploadImg(student.studentInfo.id, tempCropPath);
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("tempPath", tempPath);
        outState.putString("tempCropPath", tempCropPath);
        super.onSaveInstanceState(outState);
    }

}
