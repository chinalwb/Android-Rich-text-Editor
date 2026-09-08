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

	private int mLevel;

	@Override
	public int getLevel() {
		return mLevel;
	}

	@Override
	public void setLevel(int level) {
		this.mLevel = Math.max(0, level);
	}

	// Gap should be about 1em
	public static final int STANDARD_GAP_WIDTH = 30;

	public int getLeadingMargin(boolean first) {
		//
		// Every nesting level shifts the item, and its bullet with it, one indent
		// to the right.
		return (mLevel + 1) * LEVEL_INDENT;
	}

	@Override
	public void drawLeadingMargin(Canvas c, Paint p, int x, int dir, int top,
			int baseline, int bottom, CharSequence text, int start, int end,
			boolean first, Layout l) {

		if (((Spanned) text).getSpanStart(this) == start) {
			Paint.Style style = p.getStyle();
			p.setStyle(Paint.Style.FILL);

			//
			// dir is the paragraph direction: multiplying by it puts the bullet
			// inside the margin for RTL text too, where "x + dir" only shifted
			// it by a single pixel.
			c.drawText("\u2022",
					x + dir * (mLevel * LEVEL_INDENT + LEADING_MARGIN), baseline, p);

			p.setStyle(style);
		}
	}

}
