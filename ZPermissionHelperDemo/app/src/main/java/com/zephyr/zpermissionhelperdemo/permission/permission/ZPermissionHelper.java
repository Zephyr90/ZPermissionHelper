package com.zephyr.zpermissionhelperdemo.permission.permission;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

/**
 * Created by zephyr on 2017/7/11.
 */

public class ZPermissionHelper {

    private Activity mContext;

    final IRequestPermissionCallback mCallback;

    public static final int ACTION_APPLICATION_DETAILS_SETTINGS = 0X101;

    public static final String PACKAGE_URL_SCHEME = "package:";//权限方案

    private int PERMISSION_GRANTED = PackageManager.PERMISSION_GRANTED;

    private int PERMISSION_DENIED = PackageManager.PERMISSION_DENIED;

    private String[] REQUEST_PERMISSIONS;

    private int REQUEST_PERMISSIONS_TAG;

    public ZPermissionHelper(Activity mContext, String[] REQUEST_PERMISSIONS, int requestPermissionTag,
                             IRequestPermissionCallback callback) {
        this.mContext = mContext;
        this.REQUEST_PERMISSIONS = REQUEST_PERMISSIONS;
        REQUEST_PERMISSIONS_TAG = requestPermissionTag;
        mCallback = callback;
    }

    /**
     * 检查权限
     */
    public void checkPermissions() {
        if (checkSelfPermisson()) {
            // 权限被授予
            mCallback.onPermissionGranted();
        }
    }

    private boolean checkSelfPermisson() {
        for (int i = 0; i < REQUEST_PERMISSIONS.length; i++) {
            String permission = REQUEST_PERMISSIONS[i];
            if (isLeakPermission(permission)) {
                ActivityCompat.requestPermissions(mContext, REQUEST_PERMISSIONS, REQUEST_PERMISSIONS_TAG);
                return false;
            }
        }

        return true;
    }

    /**
     * 检查具体权限缺失
     * @param permission
     * @return
     */
    private boolean isLeakPermission(String permission) {
        int selfPermission = ActivityCompat.checkSelfPermission(mContext, permission);
        if (selfPermission == PERMISSION_DENIED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(mContext, permission)) {
                // 显示这个权限的重要性，给用户提示
                mCallback.onShouldShowRequestPermissionRationale( mContext, permission);
            }
            return true;
        }
        return false;
    }

    /**
     * 权限请求结果回调
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSIONS_TAG) {
            if (permissions.length > 0 && hasAllPermissionGranted(grantResults)) {
                // 授予权限成功
                mCallback.onAfterPermissionGranted(requestCode, permissions, grantResults);
            } else {
                showMissingPermissionDialog();
                // 授予权限失败
            }
        }
    }

    /**
     * 申请的权限是否全部授予
     * @param grantResults
     * @return
     */
    public boolean hasAllPermissionGranted(int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

    /**
     * 显示提示对话框
     */
    public void showMissingPermissionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("帮助");//提示帮助
        builder.setMessage("当前应用缺少必要权限。\n请点击\"设置\"-\"权限\"-打开所需权限。\n最后点击两次后退按钮，即可返回。");
        builder.setPositiveButton("设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startPermissionGrantActivity();
            }
        });
        builder.setNegativeButton("退出", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 退出应用
                mContext.finish();
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    /**
     * 调起系统权限控制界面
     */
    private void startPermissionGrantActivity() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse(PACKAGE_URL_SCHEME + mContext.getPackageName()));
        mContext.startActivityForResult(intent, ACTION_APPLICATION_DETAILS_SETTINGS);
    }

}
