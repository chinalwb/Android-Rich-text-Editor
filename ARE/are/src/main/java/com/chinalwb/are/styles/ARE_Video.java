package com.chinalwb.are.styles;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;
import android.text.Editable;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AlignmentSpan;
import android.text.style.ImageSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;

import com.chinalwb.are.AREditText;
import com.chinalwb.are.Constants;
import com.chinalwb.are.R;
import com.chinalwb.are.Util;
import com.chinalwb.are.activities.Are_VideoPlayerActivity;
import com.chinalwb.are.spans.AreImageSpan;
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

        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        ((Activity) this.mContext).startActivityForResult(intent, ARE_Toolbar.REQ_VIDEO_CHOOSE);
    }


    /**
     *
     */
    public void insertVideo(final Uri uri, final String videoUrl) {
//        this.mEditText.useSoftwareLayerOnAndroid8();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && ((Activity) mContext).checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //申请授权
            ((Activity) mContext).requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, ARE_Toolbar.REQ_VIDEO);
            return;
        }
        String path = Util.GetPathFromUri4kitkat.getPath(mContext, uri);
        Bitmap thumb = ThumbnailUtils.createVideoThumbnail(path, MediaStore.Images.Thumbnails.MINI_KIND);

        Bitmap play = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.play);
        Bitmap video = Util.mergeBitmaps(thumb, play);
        AreVideoSpan videoSpan = new AreVideoSpan(mContext, video, path, videoUrl);
        insertSpan(videoSpan);
    }

    /**
     * 根据图片的Uri获取图片的绝对路径(适配多种API)
     *
     * @return 如果Uri对应的图片存在, 那么返回该图片的绝对路径, 否则返回null
     */
    public static String getRealPathFromUri(Context context, Uri uri) {
        int sdkVersion = Build.VERSION.SDK_INT;
        if (sdkVersion < 11) return getRealPathFromUri_BelowApi11(context, uri);
        if (sdkVersion < 19) return getRealPathFromUri_Api11To18(context, uri);
        else return getRealPathFromUri_AboveApi19(context, uri);
    }

    /**
     * 适配api19以上,根据uri获取图片的绝对路径
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private static String getRealPathFromUri_AboveApi19(Context context, Uri uri) {
        String filePath = null;
        String wholeID = DocumentsContract.getDocumentId(uri);

        // 使用':'分割
        String id = wholeID.split(":")[1];

        String[] projection = {MediaStore.Images.Media.DATA};
        String selection = MediaStore.Images.Media._ID + "=?";
        String[] selectionArgs = {id};

        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,//
                projection, selection, selectionArgs, null);
        int columnIndex = cursor.getColumnIndex(projection[0]);
        if (cursor.moveToFirst()) filePath = cursor.getString(columnIndex);
        cursor.close();
        return filePath;
    }

    /**
     * 适配api11-api18,根据uri获取图片的绝对路径
     */
    private static String getRealPathFromUri_Api11To18(Context context, Uri uri) {
        String filePath = null;
        String[] projection = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(context, uri, projection, null, null, null);
        Cursor cursor = loader.loadInBackground();

        if (cursor != null) {
            cursor.moveToFirst();
            filePath = cursor.getString(cursor.getColumnIndex(projection[0]));
            cursor.close();
        }
        return filePath;
    }

    /**
     * 适配api11以下(不包括api11),根据uri获取图片的绝对路径
     */
    private static String getRealPathFromUri_BelowApi11(Context context, Uri uri) {
        String filePath = null;
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            filePath = cursor.getString(cursor.getColumnIndex(projection[0]));
            cursor.close();
        }
        return filePath;
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