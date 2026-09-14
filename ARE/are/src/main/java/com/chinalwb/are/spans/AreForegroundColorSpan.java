package com.chinalwb.are.spans;

import androidx.annotation.ColorInt;
import android.text.style.ForegroundColorSpan;

public class AreForegroundColorSpan extends ForegroundColorSpan implements AreDynamicSpan {
    public AreForegroundColorSpan(@ColorInt int color) {
        super(color);
    }

    @Override
    public int getDynamicFeature() {
        return this.getForegroundColor();
    }
}
