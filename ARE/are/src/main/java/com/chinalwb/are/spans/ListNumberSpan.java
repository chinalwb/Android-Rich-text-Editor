package com.chinalwb.are.spans;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.Layout;
import android.text.Spanned;

public class ListNumberSpan implements AreListSpan {
  protected static final int LEADING_MARGIN = 30;

  private int mNumber;

//  private static final int BULLET_RADIUS = 3;
//  private static final int NUMBER_RADIUS = 10;

  //Gap should be about 1em
  public static final int STANDARD_GAP_WIDTH = 30;

  public ListNumberSpan() {
      mNumber = -1;
  }

  public ListNumberSpan(int number) {
      mNumber = number;
  }
  
  public void setNumber(int number) {
    this.mNumber = number;
  }
  
  public int getNumber() {
    return this.mNumber;
  }

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

          // Util.log("mNumber == " + mNumber);
          if (mNumber != -1) {
              c.drawText(
                  mNumber + ".",
                  x + dir + LEADING_MARGIN, 
                  baseline,
                  p);
              
          } else {
              c.drawText("\u2022", x + dir, baseline, p);
          }

          p.setStyle(style);
      }
  }
}