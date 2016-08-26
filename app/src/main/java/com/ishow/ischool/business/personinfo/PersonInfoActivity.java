package com.ishow.ischool.business.personinfo;

import android.Manifest;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.commonlib.util.DateUtil;
import com.commonlib.util.LogUtil;
import com.commonlib.util.PermissionUtil;
import com.commonlib.util.StorageUtil;
import com.commonlib.widget.imageloader.ImageLoaderUtil;
import com.ishow.ischool.R;
import com.ishow.ischool.application.Resourse;
import com.ishow.ischool.bean.user.Avatar;
import com.ishow.ischool.bean.user.User;
import com.ishow.ischool.bean.user.UserInfo;
import com.ishow.ischool.business.Input.InputActivity;
import com.ishow.ischool.common.base.BaseActivity4Crm;
import com.ishow.ischool.common.manager.JumpManager;
import com.ishow.ischool.common.manager.UserManager;
import com.ishow.ischool.widget.custom.CircleImageView;
import com.ishow.ischool.widget.custom.FmItemTextView;
import com.ishow.ischool.widget.custom.SelectDialogFragment;
import com.ishow.ischool.widget.pickerview.PickerDialogFragment;

import java.io.File;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by MrS on 2016/8/15.
 */
public class PersonInfoActivity extends BaseActivity4Crm<PersonPresenter, PersonMode> implements SelectDialogFragment.OnItemSelectedListner, PersonView {
    @BindView(R.id.person_info_avart)
    CircleImageView personInfoAvart;
    @BindView(R.id.person_info_name)
    FmItemTextView personInfoName;
    @BindView(R.id.person_info_phone)
    FmItemTextView personInfoPhone;
    @BindView(R.id.person_info_birthday)
    FmItemTextView personInfoBirthday;
    // @BindView(R.id.person_info_QQ)
    FmItemTextView personInfoQQ;

    private String TAG = PersonInfoActivity.class.getSimpleName();
    private final int SELECT_SINGLE_IMAGE = 0;
    private final int CROP_IMAGE = 1;
    private final int CAPTURE = 2;
    private final int INPUT = 100;
    private String tempPath;//拍照或者 选择照片裁剪时的临时存储路径
    private User user;
    private UserInfo userInfo;

    private long time;

    @Override
    protected void setUpContentView() {
        setContentView(R.layout.activity_person_info, R.string.per_info_title, MODE_NONE);
        personInfoQQ = (FmItemTextView) findViewById(R.id.person_info_QQ);
        mToolbar = (Toolbar) findViewById(com.commonlib.R.id.toolbar);
        mToolbar.setTitle("");
        mToolbarTitle = (TextView) findViewById(com.commonlib.R.id.toolbar_title);
        mToolbarTitle.setText(getString(R.string.per_info_title));
        mToolbar.setNavigationIcon(com.commonlib.R.drawable.ic_return);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishActivity();
            }
        });

        time = new Date().getTime();

    }

    private void finishActivity() {
        Intent intent = new Intent();
        intent.putExtra("tempath", tempPath);
        setResult(100, intent);
        this.finish();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("tempath", tempPath);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            tempPath = savedInstanceState.getString("tempath");
            if (tempPath != null) {
                ImageLoaderUtil.getInstance().loadImage(this, personInfoAvart, tempPath);
                LogUtil.e("onRestoreInstanceState" + tempPath);
            }
        }
    }

    @Override
    protected void setUpView() {
        user = UserManager.getInstance().get();
    }

    @Override
    protected void setUpData() {
        if (user == null)
            return;

        userInfo = user.userInfo;
        if (userInfo == null)
            return;
        Avatar avatar = user.avatar;
        if (avatar != null)
            //PicUtils.loadUserHeader(this, personInfoAvart, avatar.file_name);
            ImageLoaderUtil.getInstance().loadImage(this, avatar.file_name, personInfoAvart);
        personInfoName.setTipTxt(userInfo.user_name);
        personInfoPhone.setTipTxt(userInfo.mobile);
        personInfoBirthday.setTipTxt(DateUtil.parseSecond2Str(Long.parseLong(userInfo.birthday + ""), "yyyy-MM-dd"));
        personInfoQQ.setTipTxt(userInfo.qq);
        //在赋值之后再设置监听
        //personInfoQQ.addTextChangedListener(this);
    }


    @OnClick({R.id.person_info_avart, R.id.person_info_birthday, R.id.person_info_QQ})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.person_info_avart:
                SelectDialogFragment.Builder builder = new SelectDialogFragment.Builder();
                SelectDialogFragment dialog = builder.setMessage(getString(R.string.capture), getString(R.string.amblue)).Build();
                dialog.show(getSupportFragmentManager());
                dialog.setOnItemSelectedListner(this);
                break;
            case R.id.person_info_birthday:
                PickerDialogFragment.Builder builder1 = new PickerDialogFragment.Builder();
                builder1.setBackgroundDark(true).setDialogType(PickerDialogFragment.PICK_TYPE_DATE).setDialogTitle(R.string.choose_birthday);
                PickerDialogFragment dialogFragment = builder1.Build();
                dialogFragment.show(getSupportFragmentManager(), "dialog");
                dialogFragment.addCallback(new PickerDialogFragment.Callback<Integer>() {
                    @Override
                    public void onPickResult(Integer unix, String... result) {
                        if (submitInfo(unix)) return;
                        if (result != null) personInfoBirthday.setTipTxt(result[0]);
                    }
                });
                break;
            case R.id.person_info_QQ:
                Intent intent = new Intent(this, InputActivity.class);
                intent.putExtra("title", getString(R.string.per_info_input_QQ));
                JumpManager.jumpActivityForResult(this, intent, INPUT,Resourse.NO_NEED_CHECK);
                break;

        }
    }

    private void cropImageUriAfterKikat(Uri uri) {
        tempPath = StorageUtil.getTempDir() + File.separator + time + ".jpg";
        File file = new File(tempPath);

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false); //true返回数据bitmap
        intent.putExtra("noFaceDetection", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        startActivityForResult(intent, CROP_IMAGE);
    }

    private void capture() {
        tempPath = StorageUtil.getTempDir() + File.separator + time + ".jpg";
        File file = new File(tempPath);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        cameraIntent.putExtra("return-data", true);
        startActivityForResult(cameraIntent, CAPTURE);
    }

    private void selectAlbums() {
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
        } else {
            intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        startActivityForResult(intent, SELECT_SINGLE_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //相册选择
        if (requestCode == SELECT_SINGLE_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            cropImageUriAfterKikat(uri);
        } else if (requestCode == CROP_IMAGE && resultCode == RESULT_OK && data != null) {

            //如果文件不存在
            if (tempPath == null || tempPath == "" || !new File(tempPath).exists())
                showToast("file not exists");
                //开始上传
            else if (userInfo != null) {
                handProgressbar(true);
                mPresenter.uploadImg(userInfo.user_id, tempPath);
            }
            //拍照
        } else if (requestCode == CAPTURE && resultCode == RESULT_OK) {
            if (tempPath != "" && tempPath != null) {
                File file = new File(tempPath);
                if (file.exists())
                    cropImageUriAfterKikat(Uri.fromFile(file));
                else showToast("file not exists");
            } else showToast("file not exists");
        } else if (requestCode == INPUT && resultCode == RESULT_OK && data != null) {
            String result = data.getStringExtra("result");
            if (TextUtils.equals(result, ""))
                return;
            personInfoQQ.setTipTxt(result);
            int unix = DateUtil.date2UnixTime(personInfoBirthday.getTipTxt().toString());
            if (TextUtils.equals(personInfoQQ.getTipTxt(), userInfo.qq))
                return;
            submitInfo(unix);
        }
    }


    @Override
    public void onItemSelected(final int position, String txt) {
        if (position == 0) {
            PermissionUtil.getInstance().checkPermission(this, new PermissionUtil.PermissionChecker() {
                @Override
                public void onGrant(String grantPermission, int index) {
                    capture();
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
                    selectAlbums();
                }

                @Override
                public void onDenied(String deniedPermission, int index) {
                    showToast(R.string.permission_storage_denid);
                }
            }, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtil.getInstance().notifyPermissionsChange(this, permissions, grantResults);
    }

    @Override
    public void onNetFailed(String strResId) {
        handProgressbar(false);
        showToast(strResId);
    }

    @Override
    public void updateInfo(int strResId, Avatar avatar, String qq, int birthday) {
        //裁剪完成后显示头像
        if (avatar != null)
            ImageLoaderUtil.getInstance().loadImage(this, personInfoAvart, tempPath);

        // LogUtil.e("updateInfo"+tempPath+"--"+birthday+"--"+qq);
        //更新本地用户头像数据
        UserManager.getInstance().upadteInfo(avatar, birthday, qq);
        handProgressbar(false);
        showToast(strResId);
    }

    @Override
    public boolean isAlive() {
        return !isActivityFinished();
    }


    private boolean submitInfo(int unix) {
        if (user == null)
            return true;
        mPresenter.submitInfo(user.userInfo.user_id, personInfoQQ.getTipTxt().toString(), unix);
        return false;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        finishActivity();
        super.onBackPressed();
    }
}
