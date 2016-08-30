package com.ishow.ischool.business.morningqrcode;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.MenuItem;
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
    private Bitmap bitmap;

   // private int IMAGE_HALFWIDTH = 10;

    @Override
    protected void setUpContentView() {
        setContentView(R.layout.activity_mornig_read_qrcode, R.string.morning_read_qrcode_title, R.menu.menu_morning_qrcode, 0);
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
        if (qrcode != null && qrcode.file_name != "" && qrcode.file_name != null)
            ImageLoaderUtil.getInstance().loadImageWithCallback(this, qrcode.file_name, qrcodeCode, this);
        UserInfo userInfo = user.userInfo;
        if (userInfo != null) qrcodeName.setText(userInfo.user_name);



        /*Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        try {
            Bitmap bm=createCode("http://baidu.com",bitmap,BarcodeFormat.QR_CODE);
            qrcodeCode.setImageBitmap(bm);
        } catch (WriterException e) {
            e.printStackTrace();
        }*/
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.action_more) {
            showSlectDialog();
        }
        return super.onMenuItemClick(item);
    }

    @Override
    public void Complete(Bitmap resource) {
        bitmap = resource;
    }

    private void showSlectDialog() {
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
                        File file = new File(dir.getPath() + "/qrcode.png");

                        FileOutputStream fos = null;
                        try {
                            fos = new FileOutputStream(file);
                            if (bitmap != null)
                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                            fos.flush();

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            if (fos != null) try {
                                fos.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        if (file.exists()) {
                            showToast(R.string.save_pic_complete);
                            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                            Uri uri = Uri.fromFile(file);
                            intent.setData(uri);
                            sendBroadcast(intent);//这个广播的目的就是更新图库，发了这个广播进入相册就可以找到你保存的图片
                        } else {
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
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtil.getInstance().notifyPermissionsChange(this, permissions, grantResults);
    }

    /*public Bitmap createCode(String string, Bitmap mBitmap, BarcodeFormat format)
            throws WriterException {
        Matrix m = new Matrix();
        float sx = (float) 2 * IMAGE_HALFWIDTH / mBitmap.getWidth();
        float sy = (float) 2 * IMAGE_HALFWIDTH / mBitmap.getHeight();
        m.setScale(sx, sy);//设置缩放信息
        //将logo图片按martix设置的信息缩放
        mBitmap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(), mBitmap.getHeight(), m, false);
        MultiFormatWriter writer = new MultiFormatWriter();
        Hashtable<EncodeHintType, String> hst = new Hashtable<EncodeHintType, String>();
        hst.put(EncodeHintType.CHARACTER_SET, "UTF-8");//设置字符编码
        BitMatrix matrix = writer.encode(string, format, 400, 400, hst);//生成二维码矩阵信息
        int width = matrix.getWidth();//矩阵高度
        int height = matrix.getHeight();//矩阵宽度
        int halfW = width / 2;
        int halfH = height / 2;
        int[] pixels = new int[width * height];//定义数组长度为矩阵高度*矩阵宽度，用于记录矩阵中像素信息
        for (int y = 0; y < height; y++) {//从行开始迭代矩阵
            for (int x = 0; x < width; x++) {//迭代列
                if (x > halfW - IMAGE_HALFWIDTH && x < halfW + IMAGE_HALFWIDTH
                        && y > halfH - IMAGE_HALFWIDTH
                        && y < halfH + IMAGE_HALFWIDTH) {//该位置用于存放图片信息
//记录图片每个像素信息
                    pixels[y * width + x] = mBitmap.getPixel(x - halfW
                            + IMAGE_HALFWIDTH, y - halfH + IMAGE_HALFWIDTH);
                } else {
                    if (matrix.get(x, y)) {//如果有黑块点，记录信息
                        pixels[y * width + x] = 0xff000000;//记录黑块信息
                    }
                }

            }
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        // 通过像素数组生成bitmap
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }*/
}
