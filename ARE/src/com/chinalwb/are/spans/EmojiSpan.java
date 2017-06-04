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
  
  public EmojiSpan(Context context, int resourceId) {
    this.mContext = context;
    this.mIconId = resourceId;
  }

  @Override
  public Drawable getDrawable() {
    if (null == this.mDrawable) {
      this.mDrawable = this.mContext.getResources().getDrawable(this.mIconId);
      this.mDrawable.setBounds(0, 0, this.mDrawable.getIntrinsicWidth() , this.mDrawable.getIntrinsicHeight() );
    }
    
    return this.mDrawable;
  }
  
  @Override
  public void draw(Canvas canvas, CharSequence text, int start, int end,
      float x, int top, int y, int bottom, Paint paint) {
    Drawable b = getCachedDrawable();
    canvas.save();
    
    int transY = bottom - b.getBounds().bottom - paint.getFontMetricsInt().descent - paint.getFontMetricsInt().bottom;
    
    canvas.translate(x, transY);
    b.draw(canvas);
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
