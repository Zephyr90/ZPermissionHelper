package com.zephyr.zpermissionhelperdemo;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.zephyr.zpermissionhelperdemo.permission.permission.IRequestPermissionCallback;
import com.zephyr.zpermissionhelperdemo.permission.permission.ZPermissionHelper;

public class MainActivity extends AppCompatActivity implements IRequestPermissionCallback {

    private static final int PERMISSION_REQUEST = 0x101;

    private String[] REQUEST_PERMISSIONS = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA
    };

    private ZPermissionHelper mPermissionHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPermissionHelper = new ZPermissionHelper(this, REQUEST_PERMISSIONS, PERMISSION_REQUEST, this);
        mPermissionHelper.checkPermissions();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mPermissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ZPermissionHelper.ACTION_APPLICATION_DETAILS_SETTINGS) {
            mPermissionHelper.checkPermissions();
        }
    }

    @Override
    public void onPermissionGranted() {
        showToast("onPermissionGranted");
    }

    @Override
    public void onAfterPermissionGranted(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        showToast("onAfterPermissionGranted");
    }

    @Override
    public void onShouldShowRequestPermissionRationale(@NonNull Activity activity, @NonNull String permission) {
        showToast("onShouldShowRequestPermissionRationale");
    }

    public void showToast(String content) {
        Toast.makeText(this, content, Toast.LENGTH_SHORT).show();
    }
}
