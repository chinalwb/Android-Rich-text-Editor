package com.chinalwb.are.styles.toolitems;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chinalwb.are.AREditText;
import com.chinalwb.are.R;
import com.chinalwb.are.Util;
import com.chinalwb.are.activities.Are_VideoPlayerActivity;
import com.chinalwb.are.strategies.VideoStrategy;
import com.chinalwb.are.styles.IARE_Style;
import com.chinalwb.are.styles.toolbar.ARE_Toolbar;
import com.chinalwb.are.styles.toolitems.styles.ARE_Style_Video;

/**
 * Created by wliu on 13/08/2018.
 */

public class ARE_ToolItem_Video extends ARE_ToolItem_Abstract {

    @Override
    public IARE_ToolItem_Updater getToolItemUpdater() {
        return null;
    }

    @Override
    public IARE_Style getStyle() {
        if (mStyle == null) {
            AREditText editText = this.getEditText();
            mStyle = new ARE_Style_Video(editText, (ImageView) mToolItemView);
        }
        return mStyle;
    }

    @Override
    public View getView(Context context) {
        if (null == context) {
            return mToolItemView;
        }
        if (mToolItemView == null) {
            ImageView imageView = new ImageView(context);
            int size = Util.getPixelByDp(context, 30);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(size, size);
            imageView.setLayoutParams(params);
            imageView.setImageResource(R.drawable.video);
            imageView.bringToFront();
            mToolItemView = imageView;
        }

        return mToolItemView;
    }

    @Override
    public void onSelectionChanged(int selStart, int selEnd) {
        return;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (ARE_Style_Video.REQUEST_CODE_CHOOSER == requestCode) {
                Uri uri = data.getData();
                openVideoPlayer(uri);
            } else if (ARE_Style_Video.REQUEST_CODE_CHOOSE_RESULT == requestCode) {
                String videoUrl = data.getStringExtra(Are_VideoPlayerActivity.VIDEO_URL);
                Uri uri = data.getData();
                ((ARE_Style_Video) getStyle()).insertVideo(uri, videoUrl);
            }
        }
    }


    /**
     * Open Video player page
     */
    public void openVideoPlayer(Uri uri) {
        Activity context = (Activity) getEditText().getContext();
        VideoStrategy videoStrategy = getEditText().getVideoStrategy();
        Are_VideoPlayerActivity.sVideoStrategy = videoStrategy;

        Intent intent = new Intent();
        intent.setClass(context, Are_VideoPlayerActivity.class);
        intent.setData(uri);
        context.startActivityForResult(intent, ARE_Style_Video.REQUEST_CODE_CHOOSE_RESULT);
    }
}
