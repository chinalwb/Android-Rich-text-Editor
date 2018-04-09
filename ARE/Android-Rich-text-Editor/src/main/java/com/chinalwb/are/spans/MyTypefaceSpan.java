package com.chinalwb.are.spans;

import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;

public class MyTypefaceSpan extends MetricAffectingSpan {
	
    private static Typeface sTypeface;
    
    /**
     * @param family The font family for this typeface.  Examples include:
     * ???
     */
    public MyTypefaceSpan(Typeface typeface) {
    	sTypeface = typeface;
    }

    public int getSpanTypeId() {
        return 13;
    }
    
    public int describeContents() {
        return 0;
    }


    private static void apply(Paint paint) {
    	if (null != sTypeface) {
    		paint.setTypeface(sTypeface);
    	}
    }

	@Override
	public void updateMeasureState(TextPaint p) {
		apply(p);
	}

	@Override
	public void updateDrawState(TextPaint tp) {
		apply(tp);
	}
}
