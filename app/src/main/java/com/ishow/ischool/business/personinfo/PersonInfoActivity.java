package com.ishow.ischool.business.personinfo;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.view.View;

import com.commonlib.util.PermissionUtil;
import com.commonlib.util.StorageUtil;
import com.ishow.ischool.R;
import com.ishow.ischool.common.base.BaseActivity4Crm;
import com.ishow.ischool.widget.custom.CircleImageView;
import com.ishow.ischool.widget.custom.FmItemTextView;
import com.ishow.ischool.widget.custom.SelectDialogFragment;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by MrS on 2016/8/15.
 */
public class PersonInfoActivity extends BaseActivity4Crm<PersonPresenter, PersonMode> implements SelectDialogFragment.onItemselectListner, PersonView {
    @BindView(R.id.person_info_avart)
    CircleImageView personInfoAvart;
    @BindView(R.id.person_info_name)
    FmItemTextView personInfoName;
    @BindView(R.id.person_info_phone)
    FmItemTextView personInfoPhone;
    @BindView(R.id.person_info_birthday)
    FmItemTextView personInfoBirthday;
    @BindView(R.id.person_info_QQ)
    FmItemTextView personInfoQQ;

    private final int SELECT_SINGLE_IMAGE = 0;
    private final int CROP_IMAGE = 1;
    private final int CAPTURE = 2;
    private String tempPath;//拍照或者 选择照片裁剪时的临时存储路径
    private Bitmap bitmap;

    @Override
    protected void setUpContentView() {
        setContentView(R.layout.activity_person_info, R.string.per_info_title, 0);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            tempPath = savedInstanceState.getString("tempPath");
            bitmap = savedInstanceState.getParcelable("bitmap");
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("tempPath", tempPath);
        outState.putParcelable("bitmap", bitmap);
    }

    @Override
    protected void setUpView() {

    }

    @Override
    protected void setUpData() {

    }


    @OnClick({R.id.person_info_avart, R.id.person_info_birthday, R.id.person_info_QQ})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.person_info_avart:
                SelectDialogFragment.Builder builder = new SelectDialogFragment.Builder();
                SelectDialogFragment dialog = builder.setMessage(getString(R.string.capture), getString(R.string.amblue))
                        .Build();
                dialog.show(getSupportFragmentManager());
                dialog.addOnItemselectListner(this);
                break;
            case R.id.person_info_birthday:
                break;
            case R.id.person_info_QQ:
                break;
        }
    }

    private void cropImageUriAfterKikat(Uri uri) {
        tempPath = StorageUtil.getTempDir() + "/avart.jpg";
        File file = new File(tempPath);
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", true); //返回数据bitmap
        //intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        startActivityForResult(intent, CROP_IMAGE);
    }

    private void capture() {
        tempPath = StorageUtil.getTempDir() + "/avart.jpg";
        File file = new File(tempPath);
        if (file.exists())
            file.delete();
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
            //LogUtil.e(uri + "onActivityResult");
        } else if (requestCode == CROP_IMAGE && resultCode == RESULT_OK && data != null) {
            //裁剪完成后显示头像
            bitmap = data.getExtras().getParcelable("data");
            personInfoAvart.setImageBitmap(bitmap);
            //如果文件不存在
            if (tempPath == null || !new File(tempPath).exists())
                showToast("file not exists");
            //开始上传
            else {
                handProgressbar(true);
                mPresenter.uploadImg(tempPath);
            }
            //拍照
        } else if (requestCode == CAPTURE && resultCode == RESULT_OK) {
            File file = new File(tempPath);
            if (file.exists())
                cropImageUriAfterKikat(Uri.fromFile(file));
            else showToast("file not exists");
        }
    }


    @Override
    public void onItemselect(int position) {
        if (position == 0) {
            PermissionUtil.getInstance().checkPermission(this, new PermissionUtil.PermissionChecker() {
                @Override
                public void onGrant(String grantPermission, int index) {
                    //因为 我申请了  相机 和存储的权限 只有当两个都允许的时候 才应该 拍照 否者会有问题
                    capture();
                }

                @Override
                public void onDenied(String deniedPermission, int index) {
                    if (index == 0) showToast(R.string.permission_camera_denid);
                    else if (index == 1) showToast(R.string.permission_storage_denid);
                }
            }, Manifest.permission.CAMERA);
        } else if (position == 1) {
            selectAlbums();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtil.getInstance().notifyPermissionsChange(this, permissions, grantResults);
    }

    @Override
    public void onNetSucess(int strResId) {
        handProgressbar(false);
        showToast(strResId);
    }

    @Override
    public void onNetFailed(int strResId) {
        handProgressbar(false);
        showToast(strResId);
    }
}
