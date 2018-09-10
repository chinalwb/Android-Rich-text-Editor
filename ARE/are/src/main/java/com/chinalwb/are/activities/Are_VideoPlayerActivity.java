package com.chinalwb.are.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.VideoView;

import com.chinalwb.are.R;
import com.chinalwb.are.Util;
import com.chinalwb.are.strategies.VideoStrategy;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class Are_VideoPlayerActivity extends AppCompatActivity {

    public static final String VIDEO_URL = "VIDEO_URL";

    public static VideoStrategy sVideoStrategy;

    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = false;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private VideoView mVideoView;
    private Button mAttachVideoButton;
    private Intent mIntent;
    private Uri mUri;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mVideoView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }

            if (sVideoStrategy != null) {
                callStrategy();
            } else {
                // insert video and finish
                Are_VideoPlayerActivity.this.setResult(RESULT_OK, mIntent);
                Are_VideoPlayerActivity.this.finish();
            }
            return false;
        }
    };

    private void callStrategy() {
        UploadCallback callBack = new UploadCallback() {
            @Override
            public void uploadFinish(Uri uri, String videoUrl) {
                mIntent.putExtra(VIDEO_URL, videoUrl);
                Are_VideoPlayerActivity.this.setResult(RESULT_OK, mIntent);
                Are_VideoPlayerActivity.this.finish();
            }
        };
        VideoUploadTask task = new VideoUploadTask(this, callBack, mUri, sVideoStrategy);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void onBackPressed() {
        this.setResult(RESULT_CANCELED);
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_are__video_player);

        mVisible = true;
        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mVideoView = findViewById(R.id.are_video_view);

        mIntent = getIntent();
        mUri = mIntent.getData();

        // Set up the user interaction to manually show or hide the system UI.
        mVideoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });
        mVideoView.setVideoURI(mUri);
        mVideoView.start();

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        mAttachVideoButton = findViewById(R.id.are_btn_attach_video);
        mAttachVideoButton.setOnTouchListener(mDelayHideTouchListener);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        // delayedHide(100);
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        mVideoView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in delay milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    private interface UploadCallback {
        void uploadFinish(Uri uri, String videoUrl);
    }

    private static class VideoUploadTask extends AsyncTask<String, Boolean, String> {

        UploadCallback mCallback;
        Uri mVideoUri;
        Activity mActivity;
        VideoStrategy mVideoStrategy;
        ProgressDialog mDialog;
        private VideoUploadTask(
                Activity activity,
                UploadCallback callback,
                Uri uri,
                VideoStrategy videoStrategy) {
            mActivity = activity;
            mCallback = callback;
            mVideoUri = uri;
            mVideoStrategy = videoStrategy;
        }

        @Override
        protected void onPreExecute() {
            if (mActivity == null || mActivity.isFinishing()) {
                return;
            }
            if (mDialog == null) {
                mDialog = ProgressDialog.show(
                        mActivity,
                        "",
                        "Uploading video. Please wait...",
                        true);
            } else {
                mDialog.show();
            }

            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            String url = mVideoStrategy.uploadVideo(mVideoUri);
            return url;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            mDialog.dismiss();
            mCallback.uploadFinish(mVideoUri, s);
        }
    }
}
