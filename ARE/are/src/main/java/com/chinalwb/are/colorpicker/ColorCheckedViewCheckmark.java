package com.chinalwb.are.colorpicker;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.view.Gravity;
import android.widget.LinearLayout;

import com.chinalwb.are.R;

/**
 * Created by wliu on 2018/3/19.
 */

public class ColorCheckedViewCheckmark extends AppCompatImageView {

    private Context mContext;
    private int mSize;

    public ColorCheckedViewCheckmark(Context context, int size) {
        super(context);
        this.mContext = context;
        this.mSize = size;
        initView();
    }

    private void initView() {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(mSize, mSize);
        layoutParams.gravity = Gravity.CENTER;
        this.setLayoutParams(layoutParams);
        this.setImageResource(R.drawable.check_mark);
    }
}
