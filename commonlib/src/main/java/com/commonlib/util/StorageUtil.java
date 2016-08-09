package com.commonlib.util;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * 存储 相关 utils
 */
public class StorageUtil {

    public static File getCacheDir(Context ctx) {
        File file = ctx.getExternalCacheDir();
        if (file == null) {
            file = ctx.getCacheDir();
        }
        return file;
    }

    public static File getImgFileDir(Context ctx) {
        File file = ctx.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (file == null) {
            file = ctx.getFilesDir();
        }
        return file;
    }

    public static File getTempDir() {
        File file = Environment.getExternalStorageDirectory();
        File tempDir = new File(file.getAbsoluteFile() + "/ishow/temp/");
        if (!tempDir.exists()) {
            tempDir.mkdirs();
        }

        return tempDir;
    }

    public static File getDownloadDir() {
        String path = Environment.getExternalStorageDirectory().getAbsoluteFile() + "/ishow/downloads/";
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }
}
