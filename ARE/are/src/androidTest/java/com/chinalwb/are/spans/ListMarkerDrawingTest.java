package com.chinalwb.are.spans;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.SpannableStringBuilder;
import android.text.Spanned;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Where a list marker is painted inside its leading margin.
 */
@RunWith(AndroidJUnit4.class)
public class ListMarkerDrawingTest {

    /** Captures what a span asks the canvas to draw. */
    private static class RecordingCanvas extends Canvas {
        String text;
        float x;

        RecordingCanvas() {
            super(Bitmap.createBitmap(1000, 100, Bitmap.Config.ARGB_8888));
        }

        @Override
        public void drawText(String text, float x, float y, Paint paint) {
            this.text = text;
            this.x = x;
        }
    }

    @Test
    public void singleDigitMarkerStaysInsideItsMargin() {
        Result result = draw(new ListNumberSpan(1), 0, 1);

        assertEquals("1.", result.text);
        assertTrue("marker starts inside the line: " + result.x, result.x >= 0);
        assertTrue("marker ends before the content at " + result.margin + ": " + result.right(),
                result.right() <= result.margin);
    }

    @Test
    public void markerTooWideForItsMarginOverlapsAsLittleAsPossible() {
        //
        // The margin is a fixed number of pixels, so at a large text size a three
        // digit number simply does not fit in it. It then has to start at the very
        // left of the line - anything else would either clip it or push it further
        // into the text.
        Result result = draw(new ListNumberSpan(100), 0, 1);

        assertEquals("100.", result.text);
        assertTrue("marker starts inside the line: " + result.x, result.x >= 0);
        assertEquals("a marker that cannot fit starts at the line start",
                0f, result.x, 0.5f);

        //
        // The old code drew every marker at a fixed offset inside the margin, so
        // this number reached that much further into the content.
        float legacyRight = 1 + 30 + result.width;
        assertTrue("must overlap less than the fixed offset did",
                result.right() < legacyRight);
    }

    @Test
    public void markerFitsWheneverTheMarginIsWideEnough() {
        Paint paint = new Paint();
        paint.setTextSize(24f);
        Result result = draw(new ListNumberSpan(10), 0, 1, paint);

        assertTrue("marker must keep its gap to the content: " + result.right(),
                result.right() <= result.margin - ListNumberSpan.STANDARD_GAP_WIDTH);
    }

    @Test
    public void markerIsNotClippedWhenTheLineStartsAtZero() {
        for (int number : new int[] {1, 9, 10, 11, 99, 100}) {
            Result result = draw(new ListNumberSpan(number), 0, 1);
            assertTrue("number " + number + " is drawn off the left edge at " + result.x,
                    result.x >= 0);
        }
    }

    @Test
    public void bulletIsDrawnInsideTheMargin() {
        Result result = draw(new ListBulletSpan(), 0, 1);

        assertTrue("bullet starts inside the line: " + result.x, result.x >= 0);
        assertTrue("bullet ends before the content: " + result.right(),
                result.right() <= result.margin);
    }

    @Test
    public void rightToLeftMarkerIsDrawnOnTheOtherSide() {
        int x = 900;
        Result result = draw(new ListNumberSpan(1), x, -1);

        assertTrue("RTL marker belongs left of x: " + result.x, result.x < x);
        assertTrue("RTL marker stays within one margin of x: " + result.x,
                result.x >= x - result.margin);
    }

    // ---------------------------------------------------------------- helpers

    private static class Result {
        String text;
        float x;
        float width;
        int margin;

        float right() {
            return x + width;
        }
    }

    private static Result draw(Object span, int x, int dir) {
        Paint paint = new Paint();
        //
        // A realistic on-device text size: the markers have to fit at the size the
        // editor actually uses, not at the Paint default of 12px.
        paint.setTextSize(50f);
        return draw(span, x, dir, paint);
    }

    private static Result draw(Object span, int x, int dir, Paint paint) {
        SpannableStringBuilder text = new SpannableStringBuilder("item");
        text.setSpan(span, 0, text.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);

        RecordingCanvas canvas = new RecordingCanvas();
        android.text.style.LeadingMarginSpan marginSpan =
                (android.text.style.LeadingMarginSpan) span;
        marginSpan.drawLeadingMargin(canvas, paint, x, dir, 0, 40, 50, text, 0,
                text.length(), true, null);

        Result result = new Result();
        result.text = canvas.text;
        result.x = canvas.x;
        result.width = canvas.text == null ? 0 : paint.measureText(canvas.text);
        result.margin = marginSpan.getLeadingMargin(true);
        return result;
    }
}
