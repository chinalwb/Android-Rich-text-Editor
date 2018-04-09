package com.chinalwb.are.spans;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.style.ImageSpan;

/**
 * Created by yixuanxuan on 16/8/9.
 */
public class MyImgSpan extends ImageSpan {
    private Uri mUri;
    public MyImgSpan(Context context, Bitmap b, Uri uri) {
        super(context, b);
        mUri = uri;
    }

    @Override
    public String getSource() {
        return mUri.toString();
    }
}
