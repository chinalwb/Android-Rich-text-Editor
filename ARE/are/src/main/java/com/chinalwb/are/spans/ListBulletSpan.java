package com.chinalwb.are.spans;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.Layout;
import android.text.Spanned;

public class ListBulletSpan implements AreListSpan {

	public ListBulletSpan() {
		//
		// Default constructor
		// Do nothing
	}

	protected static final int LEADING_MARGIN = 30;

	// Gap should be about 1em
	public static final int STANDARD_GAP_WIDTH = 30;

	public int getLeadingMargin(boolean first) {
		return LEADING_MARGIN + 50;
	}

	@Override
	public void drawLeadingMargin(Canvas c, Paint p, int x, int dir, int top,
			int baseline, int bottom, CharSequence text, int start, int end,
			boolean first, Layout l) {

		if (((Spanned) text).getSpanStart(this) == start) {
			Paint.Style style = p.getStyle();
			p.setStyle(Paint.Style.FILL);

			c.drawText("\u2022", x + dir + LEADING_MARGIN, baseline, p);

			p.setStyle(style);
		}
	}

}
