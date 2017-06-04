package com.chinalwb.are.spans;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetricsInt;
import android.text.style.ReplacementSpan;

public class AtSpan extends ReplacementSpan {

	/**
	 * Will be used when generating HTML code for @
	 */
	private String mUserKey;
	
	private int mColor;
	
	public AtSpan(String userKey, int textColor) {
		this.mUserKey = userKey;
		this.mColor = textColor;
	}

	public String getmUserKey() {
		return mUserKey;
	}

	@Override
	public int getSize(Paint paint, CharSequence text, int start, int end,
			FontMetricsInt fm) {
		int width = (int) paint.measureText(text, start, end);
		return width;
	}

	@Override
	public void draw(Canvas canvas, CharSequence text, int start, int end,
			float x, int top, int y, int bottom, Paint paint) {
		paint.setColor(Color.BLUE);
		float width = paint.measureText(text.toString(), start, end);
		canvas.drawRect(x, top,  x + width, bottom, paint);
		paint.setColor(mColor);
		canvas.drawText(text, start, end, x, (float) y, paint);
	}
	
}
