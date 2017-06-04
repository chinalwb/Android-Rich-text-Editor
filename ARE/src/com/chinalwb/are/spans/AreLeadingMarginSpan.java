package com.chinalwb.are.spans;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.Layout;
import android.text.style.LeadingMarginSpan;

import com.chinalwb.are.Constants;
import com.chinalwb.are.Util;

public class AreLeadingMarginSpan implements LeadingMarginSpan {

	private static final int LEADING_MARGIN = 30;
	
	private int mStandardLeading = LEADING_MARGIN;
	
	private int mLevel = 0;
	
	private int mLeadingMargin = mStandardLeading;
	
	public AreLeadingMarginSpan(Context context) {
		mStandardLeading = Util.getPixelByDp(context, LEADING_MARGIN);
	}
	
	@Override
	public int getLeadingMargin(boolean first) {
		return mLeadingMargin;
	}
	
	@Override
	public void drawLeadingMargin(Canvas c, Paint p, int x, int dir, int top,
			int baseline, int bottom, CharSequence text, int start, int end,
			boolean first, Layout layout) {
		c.drawText(Constants.ZERO_WIDTH_SPACE_STR, x + dir + mLeadingMargin, baseline, p);
	}

	/**
	 * Set leading level
	 * 
	 * @param level
	 */
	public void setLevel(int level) {
		mLevel = level;
		mLeadingMargin = mStandardLeading * mLevel;
	}
	
	public int getLevel() {
		return mLevel;
	}
	
	/**
	 * Increase leading level.
	 * 
	 * @return
	 */
	public int increaseLevel() {
		++mLevel;
		mLeadingMargin = mStandardLeading * mLevel;
		return mLevel;
	}
	
	/**
	 * Decrease leading level.
	 * 
	 * @return
	 */
	public int decreaseLevel() {
		--mLevel;
		if (mLevel < 0) {
			mLevel = 0;
		}
		mLeadingMargin = mStandardLeading * mLevel;
		return mLevel;
	}

}
