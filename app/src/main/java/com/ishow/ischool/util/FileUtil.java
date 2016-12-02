package com.ishow.ischool.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.text.TextUtils;
import android.view.ViewGroup;

import com.commonlib.util.StorageUtil;
import com.ishow.ischool.R;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by MrS on 2016/12/2.
 */

public class FileUtil {
    /**
     * @param context
     * @param needCaptureView 需要截图的viewgroup
     * @param fileName        需要保存的文件名  如果 传null  将以时间戳命名  路径为本app的 sd卡 ishow/temp下
     * @return
     */
    public static void captureAndSave2Gallery(Context context, ViewGroup needCaptureView, String fileName) {
        int h = 0;
        Bitmap bitmap = null;
        // 获取scrollview实际高度
        for (int i = 0; i < needCaptureView.getChildCount(); i++) {
            h += needCaptureView.getChildAt(i).getHeight();
            needCaptureView.getChildAt(i).setBackgroundColor(
                    Color.parseColor("#ffffff"));
        }
        // 创建对应大小的bitmap
        bitmap = Bitmap.createBitmap(needCaptureView.getWidth(), h,
                Bitmap.Config.RGB_565);
        final Canvas canvas = new Canvas(bitmap);
        needCaptureView.draw(canvas);
        save2Gallery(context, bitmap, fileName);
    }

    /**
     * 压缩图片
     *
     * @param image
     * @return
     */
    public static void compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        int options = 100;
        // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
        while (baos.toByteArray().length / 1024 > 100) {
            // 重置baos
            baos.reset();
            // 这里压缩options%，把压缩后的数据存放到baos中
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);
            // 每次都减少10
            options -= 10;
        }
        // 把压缩后的数据baos存放到ByteArrayInputStream中
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        // 把ByteArrayInputStream数据生成图片
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);

    }

    /**
     * 保存到sdcard
     *
     * @param b
     * @param fileName
     * @return
     */
    public static void save2Gallery(Context context, Bitmap b, String fileName) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.US);
        File dir = StorageUtil.getTempDir();
        File outfile = new File(dir.getPath(), TextUtils.isEmpty(fileName) ? sdf.format(new Date()) + ".png" : fileName);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(outfile);
            if (null != fos) {
                b.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.flush();
                fos.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (outfile.exists()) {
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri uri = Uri.fromFile(outfile);
            intent.setData(uri);
            context.sendBroadcast(intent);//这个广播的目的就是更新图库，发了这个广播进入相册就可以找到你保存的图片
            ToastUtil.showToast(context, context.getString(R.string.save_pic_complete));
        } else ToastUtil.showToast(context, context.getString(R.string.save_pic_faild));
    }
}
