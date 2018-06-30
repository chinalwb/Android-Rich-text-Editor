package com.chinalwb.are.strategies.defaults;

import android.content.Context;
import android.content.Intent;

import com.chinalwb.are.Util;
import com.chinalwb.are.spans.ARE_Clickable_Span;
import com.chinalwb.are.spans.AreAtSpan;
import com.chinalwb.are.spans.AreImageSpan;
import com.chinalwb.are.spans.AreVideoSpan;
import com.chinalwb.are.strategies.AreClickStrategy;

import static com.chinalwb.are.spans.AreImageSpan.ImageType;

/**
 * Created by wliu on 30/06/2018.
 */

public class DefaultClickStrategy implements AreClickStrategy {

    @Override
    public boolean onClick(Context context, ARE_Clickable_Span areClickableSpan) {
        Intent intent = new Intent();
        if (areClickableSpan instanceof AreAtSpan) {
            AreAtSpan atSpan = ((AreAtSpan) areClickableSpan);
            intent.setClass(context, DefaultProfileActivity.class);
            intent.putExtra("userKey", atSpan.getUserKey());
            intent.putExtra("userName", atSpan.getUserName());
            context.startActivity(intent);
            return true;
        } else if (areClickableSpan instanceof AreImageSpan) {
            AreImageSpan imageSpan = (AreImageSpan) areClickableSpan;
            ImageType imageType = imageSpan.getImageType();
            intent.putExtra("imageType", imageType);
            if (imageType == ImageType.URI) {
                intent.putExtra("uri", imageSpan.getUri());
            } else if (imageType == ImageType.URL) {
                intent.putExtra("url", imageSpan.getURL());
            } else {
                intent.putExtra("resId", imageSpan.getResId());
            }
            intent.setClass(context, DefaultImagePreviewActivity.class);
            context.startActivity(intent);
            return true;
        } else if (areClickableSpan instanceof AreVideoSpan) {
            Util.toast(context, "Video span");
            return true;
        }
        return false;
    }
}
