package com.ishow.ischool.business.morningqrcode;

import android.Manifest;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.commonlib.util.PermissionUtil;
import com.commonlib.util.StorageUtil;
import com.commonlib.widget.imageloader.ImageLoaderUtil;
import com.ishow.ischool.R;
import com.ishow.ischool.bean.user.Avatar;
import com.ishow.ischool.bean.user.Qrcode;
import com.ishow.ischool.bean.user.User;
import com.ishow.ischool.bean.user.UserInfo;
import com.ishow.ischool.common.base.BaseActivity4Crm;
import com.ishow.ischool.common.manager.UserManager;
import com.ishow.ischool.widget.custom.CircleImageView;
import com.ishow.ischool.widget.custom.SelectDialogFragment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;

/**
 * Created by MrS on 2016/8/12.
 */
public class MorningReadActivity extends BaseActivity4Crm implements ImageLoaderUtil.loadComplete {
    @BindView(R.id.qrcode_name)
    TextView qrcodeName;
    @BindView(R.id.qrcode_code)
    ImageView qrcodeCode;
    @BindView(R.id.qrcode_avart)
    CircleImageView qrcodeAvart;
    private User user;

    @Override
    protected void setUpContentView() {
        setContentView(R.layout.activity_mornig_read_qrcode, R.string.morning_read_qrcode_title, -1, 0);
    }

    @Override
    protected void setUpView() {
        user = UserManager.getInstance().get();
    }

    @Override
    protected void setUpData() {
        if (user == null)
            return;
        Avatar avatar = user.avatar;
        if (avatar != null)
            ImageLoaderUtil.getInstance().loadImage(this, avatar.file_name, qrcodeAvart);
        Qrcode qrcode = user.qrcode;
        if (qrcode != null&&qrcode.file_name !="")
            ImageLoaderUtil.getInstance().loadImageWithCallback(this, qrcode.file_name, qrcodeCode,this);
        UserInfo userInfo = user.userInfo;
        if (userInfo != null) qrcodeName.setText(userInfo.user_name);

        //qrcodeCode.setOnLongClickListener(this);
    }

    @Override
    public void Complete(final Bitmap resource) {
        qrcodeCode.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                SelectDialogFragment.Builder builder = new SelectDialogFragment.Builder();
                SelectDialogFragment dialog = builder.setMessage(getString(R.string.save_pic)).Build();
                dialog.show(getSupportFragmentManager());
                dialog.setOnItemSelectedListner(new SelectDialogFragment.OnItemSelectedListner() {
                    @Override
                    public void onItemSelected(int position, String txt) {
                        PermissionUtil.getInstance().checkPermission(MorningReadActivity.this, new PermissionUtil.PermissionChecker() {
                            @Override
                            public void onGrant(String grantPermission, int index) {
                                File dir = StorageUtil.getTempDir();
                                File file = new File(dir.getPath()+"/qrcode.png");

                                FileOutputStream fos = null;
                                try {
                                    fos = new FileOutputStream(file);
                                    resource.compress(Bitmap.CompressFormat.PNG,100,fos);
                                    fos.flush();

                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }finally {
                                    if (fos!=null) try {
                                        fos.close();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }

                                if (file.exists()){
                                    showToast(R.string.save_pic_complete);
                                }else{
                                    showToast(R.string.save_pic_faild);
                                }
                            }

                            @Override
                            public void onDenied(String deniedPermission, int index) {
                               // showToast();
                            }
                        }, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    }
                });
                return false;
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtil.getInstance().notifyPermissionsChange(this,permissions,grantResults);
    }
}
