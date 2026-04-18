package com.chinalwb.are.demo;

import android.content.Intent;
import android.net.Uri;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.chinalwb.are.AREActivityResultCallback;
import com.chinalwb.are.AREActivityResultHost;

public abstract class AREDemoBaseActivity extends AppCompatActivity implements AREActivityResultHost {

    private AREActivityResultCallback<Uri> pendingImageCallback;
    private AREActivityResultCallback<Uri> pendingVideoCallback;
    private AREActivityResultCallback<Intent> pendingAtPickerCallback;
    private AREActivityResultCallback<Intent> pendingVideoPlayerCallback;

    private final ActivityResultLauncher<String> imagePickerLauncher =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if (pendingImageCallback != null && uri != null) {
                    pendingImageCallback.onResult(uri);
                }
                pendingImageCallback = null;
            });

    private final ActivityResultLauncher<String> videoPickerLauncher =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if (pendingVideoCallback != null && uri != null) {
                    pendingVideoCallback.onResult(uri);
                }
                pendingVideoCallback = null;
            });

    private final ActivityResultLauncher<Intent> atPickerLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (pendingAtPickerCallback != null && result.getResultCode() == RESULT_OK && result.getData() != null) {
                    pendingAtPickerCallback.onResult(result.getData());
                }
                pendingAtPickerCallback = null;
            });

    private final ActivityResultLauncher<Intent> videoPlayerLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (pendingVideoPlayerCallback != null && result.getResultCode() == RESULT_OK && result.getData() != null) {
                    pendingVideoPlayerCallback.onResult(result.getData());
                }
                pendingVideoPlayerCallback = null;
            });

    @Override
    public void pickImage(AREActivityResultCallback<Uri> callback) {
        pendingImageCallback = callback;
        imagePickerLauncher.launch("image/*");
    }

    @Override
    public void pickVideo(AREActivityResultCallback<Uri> callback) {
        pendingVideoCallback = callback;
        videoPickerLauncher.launch("video/*");
    }

    @Override
    public void launchAtPicker(Intent intent, AREActivityResultCallback<Intent> callback) {
        pendingAtPickerCallback = callback;
        atPickerLauncher.launch(intent);
    }

    @Override
    public void launchVideoPlayer(Intent intent, AREActivityResultCallback<Intent> callback) {
        pendingVideoPlayerCallback = callback;
        videoPlayerLauncher.launch(intent);
    }
}
