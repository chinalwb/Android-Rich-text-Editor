package com.chinalwb.are.colorpicker;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by wliu on 2018/3/11.
 */

public class ColorCheckedView extends View {

    private Context mContext;

    private int mSize;

    public ColorCheckedView(Context context, int size) {
        super(context);
        this.mContext = context;
        this.mSize = size;
        initView();
    }

    private void initView() {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(mSize, mSize);
        layoutParams.gravity = Gravity.CENTER;
        this.setLayoutParams(layoutParams);
        this.setBackgroundColor(Color.WHITE);
    }
}
