package com.rainliu.colorpicker;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;

/**
 * Created by wliu on 2018/3/6.
 */

public class ColorPickerView extends HorizontalScrollView {

    private ColorPickerListener mColorPickerListener;

    private int[] mColors;

    public ColorPickerView(Context context) {
        this(context, null);
    }

    public ColorPickerView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public ColorPickerView(Context context, AttributeSet attributeSet, int defStyleAttr) {
        super(context, attributeSet, defStyleAttr);

        init();
    }

    private void init() {
//        this.addView(colorsContainer);
    }

    public void setColorPickerListener(ColorPickerListener listener) {
        this.mColorPickerListener = listener;
    }
}
