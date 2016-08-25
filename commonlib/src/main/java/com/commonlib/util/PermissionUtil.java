package com.commonlib.util;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

import com.commonlib.R;

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
                boolean requestPermissionRationale = ActivityCompat.shouldShowRequestPermissionRationale(activity, permissions[i]);
                if (requestPermissionRationale) {
                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(activity);
                    final AlertDialog dialog = builder.create();
                    int permission = 0;
                    if (permissions[i] == Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        permission = R.string.permission_storage_denid;
                    else if (permissions[i] == Manifest.permission.CAMERA)
                        permission = R.string.permission_camera_denid;

                    builder.setMessage(permission).setPositiveButton(activity.getString(R.string.ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
                if (checker != null) checker.onDenied(permissions[i], i);
            }
        }
    }
}
