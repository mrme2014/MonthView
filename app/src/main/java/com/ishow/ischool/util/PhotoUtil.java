package com.ishow.ischool.util;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

import com.commonlib.util.StorageUtil;
import com.commonlib.util.StringUtils;

import java.io.File;

import static android.app.Activity.RESULT_OK;


/**
 * Created by abel on 16/10/9.
 */

public class PhotoUtil {

    private static final int SELECT_SINGLE_IMAGE = 10;
    private static final int CROP_IMAGE = 11;
    private static final int CAPTURE = 12;


    public static void capture(Activity activity, String tempCapturePath) {
        File file = new File(tempCapturePath);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        //cameraIntent.putExtra("return-data", false);
        activity.startActivityForResult(cameraIntent, CAPTURE);
    }

    public static void cropImage(Activity activity, Uri inUri, String tempOutPath) {
        File file = new File(tempOutPath);

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(inUri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false); //true返回数据bitmap
        intent.putExtra("noFaceDetection", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        activity.startActivityForResult(intent, CROP_IMAGE);
    }


    public static void selectAlbums(Activity activity) {
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
        } else {
            intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        activity.startActivityForResult(intent, SELECT_SINGLE_IMAGE);
    }

    public static boolean onActivityResult(Activity activity, String tempCapturePath, String tempCropPath, int requestCode, int resultCode, Intent data, UploadListener listenter) {

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case SELECT_SINGLE_IMAGE:
                    Uri inUri = data.getData();
                    cropImage(activity, inUri, tempCropPath);
                    return true;
                case CROP_IMAGE:
                    //如果文件不存在
                    if (tempCropPath == null || "".equals(tempCropPath) || !new File(tempCropPath).exists()) {
                        ToastUtil.showToast(activity, "file not exists 1");
                        //开始上传
                    } else if (listenter != null) {
                        listenter.upload();
                    }
                    return true;
                case CAPTURE:
                    if (!StringUtils.isEmpty(tempCapturePath)) {
                        File file = new File(tempCapturePath);
                        if (file.exists())
                            cropImage(activity, Uri.fromFile(file), tempCropPath);
                        else {
                            ToastUtil.showToast(activity, "file not exists 2");
                        }
                    } else {
                        ToastUtil.showToast(activity, "file not exists 3");
                    }
                    return true;
            }
        }

        return false;

    }

    public interface UploadListener {
        void upload();
    }
}
