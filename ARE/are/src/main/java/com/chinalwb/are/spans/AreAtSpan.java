package com.chinalwb.are.spans;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetricsInt;
import android.text.style.ReplacementSpan;

import com.chinalwb.are.models.AtItem;

public class AreAtSpan extends ReplacementSpan implements ARE_Span, ARE_Clickable_Span {

	/**
	 * Will be used when generating HTML code for @
	 */
	private String mUserKey;

	private String mUserName;

	private int mColor;

	private static final String KEY_ATTR = "key";

	public AreAtSpan(AtItem atItem) {
        this.mUserKey = atItem.mKey;
        this.mUserName = atItem.mName;
        this.mColor = atItem.mColor;
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
		paint.setColor(Color.TRANSPARENT);
		float width = paint.measureText(text.toString(), start, end);
		canvas.drawRect(x, top,  x + width, bottom, paint);
		paint.setColor(0xFF000000 | mColor);
		canvas.drawText(text, start, end, x, (float) y, paint);
	}

	@Override
	public String getHtml() {
		StringBuffer html = new StringBuffer();
		html.append("<a");
		html.append(" href=\"#\"");
		html.append(" uKey=\"" + mUserKey + "\"");
		html.append(" uName=\"" + mUserName + "\"");
		html.append(String.format(" style=\"color:#%06X;\"", 0xFFFFFF & mColor));
		html.append(">");
		html.append("@" + this.mUserName);
		html.append("</a>");
		return html.toString();
	}

	public String getUserName() {
		return this.mUserName;
	}

	public String getUserKey() {
		return this.mUserKey;
	}
}
