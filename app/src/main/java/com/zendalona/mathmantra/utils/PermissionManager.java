package com.zendalona.mathmantra.utils;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

public class PermissionManager {
    public static final int REQUEST_CODE_MICROPHONE = 100;
    public static final int REQUEST_CODE_ACCELEROMETER = 101;

    private Activity activity;
    private PermissionCallback callback;

    private ActivityResultLauncher<String> permissionLauncher;

    public PermissionManager(Activity activity, PermissionCallback callback) {
        this.activity = activity;
        this.callback = callback;
//        initializePermissionLauncher();
    }

    /*private void initializePermissionLauncher() {
        permissionLauncher = activity.registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (callback != null) {
                        if (isGranted) {
                            callback.onPermissionGranted();
                        } else {
                            callback.onPermissionDenied();
                        }
                    }
                }
        );
    }*/

    public void requestMicrophonePermission() {
        requestPermission(android.Manifest.permission.RECORD_AUDIO);
    }

    public void requestAccelerometerPermission() {
        requestPermission(android.Manifest.permission.BODY_SENSORS);
    }

    private void requestPermission(String permission) {
        if (ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED) {
            if (callback != null) {
                callback.onPermissionGranted();
            }
        } else {
            permissionLauncher.launch(permission);
        }
    }

    public interface PermissionCallback {
        void onPermissionGranted();
        void onPermissionDenied();
    }
}
