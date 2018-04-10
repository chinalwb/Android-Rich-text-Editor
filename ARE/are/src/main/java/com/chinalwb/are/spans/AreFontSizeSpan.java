package com.chinalwb.are.spans;

import android.text.style.AbsoluteSizeSpan;

public class AreFontSizeSpan extends AbsoluteSizeSpan implements AreDynamicSpan {

    public AreFontSizeSpan(int size) {
        super(size, true);
    }

    @Override
    public int getDynamicFeature() {
        return this.getSize();
    }
}
