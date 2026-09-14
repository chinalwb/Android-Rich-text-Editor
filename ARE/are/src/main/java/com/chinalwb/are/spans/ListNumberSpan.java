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
          String label = mNumber != -1 ? (mNumber + ".") : "\u2022";
          c.drawText(label, getLabelX(p, x, dir, label), baseline, p);

          p.setStyle(style);
      }
  }

  /**
   * Returns the x the marker should be drawn at.
   *
   * <p>The marker is aligned against the text it belongs to rather than against
   * the left edge of the margin, so a wide number such as "10." keeps its gap to
   * the content instead of growing into it. {@code dir} is the paragraph
   * direction, so the marker also lands on the correct side for RTL text.</p>
   */
  private float getLabelX(Paint p, int x, int dir, String label) {
      int margin = getLeadingMargin(true);
      if (dir < 0) {
          //
          // RTL: x is the right edge, the margin runs to the left of it.
          return x - margin + STANDARD_GAP_WIDTH;
      }

      float labelWidth = p.measureText(label);
      float labelX = x + margin - STANDARD_GAP_WIDTH - labelWidth;
      //
      // A marker too wide for its margin grows away from the content instead of
      // over it.
      return Math.max(labelX, (float) x);
  }
}