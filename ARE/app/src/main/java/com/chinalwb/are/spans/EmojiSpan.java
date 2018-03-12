package com.chinalwb.are.spans;

import java.lang.ref.WeakReference;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.style.DynamicDrawableSpan;

public class EmojiSpan extends DynamicDrawableSpan {

  private Context mContext;
  
  private int mIconId;
  
  private Drawable mDrawable;

  private final int size = 50; // Should not be hard-coded

  public EmojiSpan(Context context, int resourceId) {
    this.mContext = context;
    this.mIconId = resourceId;
  }

  @Override
  public Drawable getDrawable() {
    if (null == this.mDrawable) {
      this.mDrawable = this.mContext.getResources().getDrawable(this.mIconId);
      this.mDrawable.setBounds(0, 0, size , size );
    }

    return this.mDrawable;
  }

  @Override public int getSize(final Paint paint, final CharSequence text, final int start,
                               final int end, final Paint.FontMetricsInt fontMetrics) {
    if (fontMetrics != null) {
      final Paint.FontMetrics paintFontMetrics = paint.getFontMetrics();
      fontMetrics.top = (int) paintFontMetrics.top;
      fontMetrics.bottom = (int) paintFontMetrics.bottom;
    }

    return (int) size;
  }

  @Override public void draw(final Canvas canvas, final CharSequence text, final int start,
                             final int end, final float x, final int top, final int y,
                             final int bottom, final Paint paint) {
    final Drawable drawable = getDrawable();
    final Paint.FontMetrics paintFontMetrics = paint.getFontMetrics();
    final float fontHeight = paintFontMetrics.descent - paintFontMetrics.ascent;
    final float centerY = y + paintFontMetrics.descent - fontHeight / 2;
    final float transitionY = centerY - size / 2;

    canvas.save();
    canvas.translate(x, transitionY);
    drawable.draw(canvas);
    canvas.restore();
  }
  
  private Drawable getCachedDrawable() {
    WeakReference<Drawable> wr = mDrawableRef;
    Drawable d = null;

    if (wr != null)
        d = wr.get();

    if (d == null) {
        d = getDrawable();
        mDrawableRef = new WeakReference<Drawable>(d);
    }

    return d;
}

private WeakReference<Drawable> mDrawableRef;
}
