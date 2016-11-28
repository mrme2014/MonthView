package com.commonlib.util;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by MrS on 2016/8/15.
 */
public class PermissionUtil {
    public static PermissionUtil permissionUtil;
    private static final int REQUEST_CODE = 1;

    public static PermissionUtil getInstance() {
        if (permissionUtil == null) {
            synchronized (PermissionUtil.class) {
                if (permissionUtil == null) {
                    permissionUtil = new PermissionUtil();
                }
            }
        }
        return permissionUtil;
    }

    public synchronized void checkPermission(Activity context, PermissionChecker checker1, String... permission) {
        checker = checker1;
        for (int i = 0; i < permission.length; i++) {
            if (ContextCompat.checkSelfPermission(context, permission[i]) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(context, new String[]{permission[i]}, REQUEST_CODE);
            } else if (checker1 != null) {
                checker.onGrant(permission[i], i);
            }
        }
    }


    PermissionChecker checker;


    public interface PermissionChecker {
        void onGrant(String grantPermission, int index);

        /*deniedPermission  被禁止的那个权限名字  该权限在 String[] permissions 属于第几个位置 因为有可能一次申请好几个*/
        void onDenied(String deniedPermission, int index);
    }

    public synchronized void notifyPermissionsChange(Activity activity, @NonNull String[] permissions, @NonNull int[] grantResults) {
        for (int i = 0; i < permissions.length; i++) {
            if (grantResults != null && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                if (checker != null)
                    checker.onGrant(permissions[i], i);
            } else {
               /* boolean requestPermissionRationale = ActivityCompat.shouldShowRequestPermissionRationale(activity, permissions[i]);
                if (requestPermissionRationale) {
                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(activity);
                    //   final AlertDialog dialog = builder.create();
                    String permission = "";
                    if (permissions[i] == Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        permission = "存储权限被禁止,部分功能可能无法正常工作";
                    else if (permissions[i] == Manifest.permission.CAMERA)
                        permission = "相机权限被禁止,部分功能可能无法正常工作.";

                    AlertDialog dialog = builder.setMessage(permission).setPositiveButton(activity.getString(R.string.str_ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create();
                    dialog.show();*/
                }
                if (checker != null) checker.onDenied(permissions[i], i);
            }
        }
    }

