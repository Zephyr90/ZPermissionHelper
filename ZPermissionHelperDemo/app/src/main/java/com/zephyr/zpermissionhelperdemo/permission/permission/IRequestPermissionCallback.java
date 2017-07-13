package com.zephyr.zpermissionhelperdemo.permission.permission;

import android.app.Activity;
import android.support.annotation.NonNull;

/**
 * Created by zephyr on 2017/7/11.
 */

public interface IRequestPermissionCallback { // 权限授予失败的回调默认为调起系统权限设置activity

    /**
     * 检查权限时，已经授予了所有权限
     */
    void onPermissionGranted();

    /**
     * 权限授予成功回调
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    void onAfterPermissionGranted(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults);


    /**
     * 权限被拒绝后调起(如果选择不再显示，则不会被调用)
     * @param activity
     * @param permission
     */
    void onShouldShowRequestPermissionRationale(@NonNull Activity activity, @NonNull String permission);
}
