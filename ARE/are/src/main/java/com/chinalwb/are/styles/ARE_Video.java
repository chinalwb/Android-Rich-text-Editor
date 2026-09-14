package com.chinalwb.are.styles;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AlignmentSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;

import com.chinalwb.are.AREditText;
import com.chinalwb.are.AREActivityResultHost;
import com.chinalwb.are.Constants;
import com.chinalwb.are.R;
import com.chinalwb.are.Util;
import com.chinalwb.are.activities.Are_VideoPlayerActivity;
import com.chinalwb.are.spans.AreVideoSpan;
import com.chinalwb.are.strategies.VideoStrategy;
import com.chinalwb.are.styles.toolbar.ARE_Toolbar;

public class ARE_Video implements IARE_Style {

    private ImageView mInsertVideoImageView;

    private AREditText mEditText;

    private Context mContext;

    private static int sWidth = 0;

    /**
     * @param imageView the emoji image view
     */
    public ARE_Video(ImageView imageView) {
        this.mInsertVideoImageView = imageView;
        this.mContext = imageView.getContext();
        sWidth = Util.getScreenWidthAndHeight(mContext)[0];
        setListenerForImageView(this.mInsertVideoImageView);
    }

    public void setEditText(AREditText editText) {
        this.mEditText = editText;
    }

    @Override
    public void setListenerForImageView(ImageView imageView) {
        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                openVideoChooser();
            }
        });
    } // #End of setListenerForImageView(..)

    /**
     * Open system image chooser page.
     */
    private void openVideoChooser() {
        VideoStrategy videoStrategy = mEditText.getVideoStrategy();
        Are_VideoPlayerActivity.sVideoStrategy = videoStrategy;

        if (this.mContext instanceof AREActivityResultHost) {
            ((AREActivityResultHost) this.mContext).pickVideo(this::openVideoPlayer);
            return;
        }

        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        ((Activity) this.mContext).startActivityForResult(intent, ARE_Toolbar.REQ_VIDEO_CHOOSE);
    }

    private void openVideoPlayer(Uri uri) {
        VideoStrategy videoStrategy = mEditText.getVideoStrategy();
        Are_VideoPlayerActivity.sVideoStrategy = videoStrategy;

        Intent intent = new Intent(this.mContext, Are_VideoPlayerActivity.class);
        intent.setData(uri);
        if (this.mContext instanceof AREActivityResultHost) {
            ((AREActivityResultHost) this.mContext).launchVideoPlayer(intent, data -> {
                String videoUrl = data.getStringExtra(Are_VideoPlayerActivity.VIDEO_URL);
                Uri resultUri = data.getData();
                if (resultUri != null) {
                    insertVideo(resultUri, videoUrl);
                }
            });
            return;
        }
        ((Activity) this.mContext).startActivityForResult(intent, ARE_Toolbar.REQ_VIDEO);
    }


    /**
     *
     */
    public void insertVideo(final Uri uri, final String videoUrl) {
        String path = Util.GetPathFromUri4kitkat.getPath(mContext, uri);
        Bitmap thumb = Util.createVideoThumbnail(mContext, uri);
        if (thumb == null) {
            return;
        }

        Bitmap play = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.play);
        Bitmap video = Util.mergeBitmaps(thumb, play);
        String videoPath = path != null ? path : uri.toString();
        AreVideoSpan videoSpan = new AreVideoSpan(mContext, video, videoPath, videoUrl);
        insertSpan(videoSpan);
    }

    private void insertSpan(AreVideoSpan imageSpan) {
        Editable editable = this.mEditText.getEditableText();
        int start = this.mEditText.getSelectionStart();
        int end = this.mEditText.getSelectionEnd();

        AlignmentSpan centerSpan = new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER);
        SpannableStringBuilder ssb = new SpannableStringBuilder();
        ssb.append(Constants.CHAR_NEW_LINE);
        ssb.append(Constants.ZERO_WIDTH_SPACE_STR);
        ssb.append(Constants.CHAR_NEW_LINE);
        ssb.append(Constants.ZERO_WIDTH_SPACE_STR);
        ssb.setSpan(imageSpan, 1, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssb.setSpan(centerSpan, 1, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        AlignmentSpan leftSpan = new AlignmentSpan.Standard(Layout.Alignment.ALIGN_NORMAL);
        ssb.setSpan(leftSpan, 3, 4, Spanned.SPAN_INCLUSIVE_INCLUSIVE);

        editable.replace(start, end, ssb);
    }

    @Override
    public void applyStyle(Editable editable, int start, int end) {
        // Do nothing
    }

    @Override
    public ImageView getImageView() {
        return this.mInsertVideoImageView;
    }

    @Override
    public void setChecked(boolean isChecked) {
        // Do nothing
    }

    @Override
    public boolean getIsChecked() {
        return false;
    }

    @Override
    public EditText getEditText() {
        return this.mEditText;
    }
}
