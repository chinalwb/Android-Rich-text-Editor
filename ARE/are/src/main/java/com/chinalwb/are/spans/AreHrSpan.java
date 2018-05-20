package com.chinalwb.are.spans;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.style.ReplacementSpan;

import com.chinalwb.are.Constants;
import com.chinalwb.are.Util;

public class AreHrSpan extends ReplacementSpan implements ARE_Span {

    private int mScreenWidth;

    private static final float p = 1;

    public AreHrSpan() {
        mScreenWidth = Constants.SCREEN_WIDTH;
    }

    @Override
    public int getSize(@NonNull Paint paint, CharSequence text, int start, int end, @Nullable Paint.FontMetricsInt fm) {
        return (int) (mScreenWidth * p);
    }

    @Override
    public void draw(@NonNull Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, @NonNull Paint paint) {
        paint.setStrokeWidth(1);
        int lineY = top + (bottom - top) / 2;
        canvas.drawLine(x + (int) (mScreenWidth * (1 - p) / 2), lineY, x + (int) (mScreenWidth * p), lineY, paint);
    }

    @Override
    public String getHtml() {
        return "<hr />";
    }
}
