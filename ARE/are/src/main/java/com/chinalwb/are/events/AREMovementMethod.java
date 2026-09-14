package com.chinalwb.are.events;

import android.content.Context;
import android.text.Layout;
import android.text.Spannable;
import android.text.method.ArrowKeyMovementMethod;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.view.MotionEvent;
import android.widget.TextView;

import com.chinalwb.are.spans.ARE_Clickable_Span;
import com.chinalwb.are.spans.AreAtSpan;
import com.chinalwb.are.spans.AreImageSpan;
import com.chinalwb.are.spans.AreUrlSpan;
import com.chinalwb.are.spans.AreVideoSpan;
import com.chinalwb.are.strategies.AreClickStrategy;
import com.chinalwb.are.styles.ARE_Underline;
import com.chinalwb.are.styles.ARE_Video;

/**
 * <p>
 * This base MovementMethod is a compound product from {@link android.text.method.ArrowKeyMovementMethod} and {@link android.text.method.LinkMovementMethod}.
 * It supports all behaviours of theirs.
 * </p>
 * <p>
 * Before sub-class extends this, you must clearly know 2 points of them:
 * <ul>
 * <li>{@link ArrowKeyMovementMethod}: ArrowKeyMovementMethod does <b>support selection of text</b> but <b>not the clicking of links</b>.</li>
 * <li>{@link LinkMovementMethod}: LinkMovementMethod does <b>support clicking of links</b> but <b>not the selection of text</b>.</li>
 * </ul>
 * In order to fit general and variable requirements, it should support the above 2 behaviors. So this base-class is just to solve it,
 * which compounding <b>"support selection of text"</b> and "<b>support clicking of links"</b> to one class. So in later you just
 * extends this only!
 * </p>
 * <p>
 * <b>In addition, you'd better know this:</b>
 * <pre>
 *     In some Samsung devices(e.g. Samsung GT-N7108, Android 4.3 version), one EditText contains links(can be clicked) and selection(can be selected):
 *     If you just extends {@link LinkMovementMethod}, it'll report some exception and result in a NullPointerException!
 *     So, this solution is pretty perfect to solve it!
 *     e.g. For solving Bug #16456 App crash if click TextView in process ADP page
 * </pre>
 * </p>
 *
 * @author Created by SongHui (@BroadVision) on 2017/7/14.
 */
public class AREMovementMethod extends ArrowKeyMovementMethod {

    private AreClickStrategy mAreClickStrategy;

    public AREMovementMethod() {
        this(null);
    }

    public AREMovementMethod(AreClickStrategy areClickStrategy) {
        this.mAreClickStrategy = areClickStrategy;
    }

    public static int getTextOffset(TextView widget, Spannable buffer, MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();

        x -= widget.getTotalPaddingLeft();
        y -= widget.getTotalPaddingTop();

        x += widget.getScrollX();
        y += widget.getScrollY();

        Layout layout = widget.getLayout();
        int line = layout.getLineForVertical(y);
        int off = layout.getOffsetForHorizontal(line, x);
        return off;
    }

    @Override
    public boolean onTouchEvent(TextView widget, Spannable buffer, MotionEvent event) {
        // Supports android.text.method.LinkMovementMethod.onTouchEvent(TextView, Spannable, MotionEvent)'s
        // clickable event. So post all these codes to here and comment out "Selection.removeSelection(buffer);"
        // because this has extended ArrowKeyMovementMethod which has supported Selection text.
        //
        // So, it is forbidden modifying the bellow codes!
        // ----------- Last modified by Songhui on 2017-7-14
        int action = event.getAction();

        if (action == MotionEvent.ACTION_UP) {
            int off = getTextOffset(widget, buffer, event);
            ARE_Clickable_Span[] clickableSpans = buffer.getSpans(off, off, ARE_Clickable_Span.class);
            Context context = widget.getContext();
            boolean handled = false;
            if (mAreClickStrategy != null && clickableSpans != null && clickableSpans.length > 0) {
                if (clickableSpans[0] instanceof AreAtSpan) {
                    handled = mAreClickStrategy.onClickAt(context, (AreAtSpan) clickableSpans[0]);
                } else if (clickableSpans[0] instanceof AreImageSpan) {
                    handled = mAreClickStrategy.onClickImage(context, (AreImageSpan) clickableSpans[0]);
                } else if (clickableSpans[0] instanceof AreVideoSpan) {
                    handled = mAreClickStrategy.onClickVideo(context, (AreVideoSpan) clickableSpans[0]);
                } else if (clickableSpans[0] instanceof AreUrlSpan) {
                    handled = mAreClickStrategy.onClickUrl(context, (AreUrlSpan) clickableSpans[0]);
                }
            }
            if (handled) {
                return true;
            }

            ClickableSpan[] link = buffer.getSpans(off, off, ClickableSpan.class);

            if (link.length != 0) {
                if (action == MotionEvent.ACTION_UP) {
                    link[0].onClick(widget);
                } else if (action == MotionEvent.ACTION_DOWN) {
                    android.text.Selection.setSelection(buffer, buffer.getSpanStart(link[0]), buffer.getSpanEnd(link[0]));
                }

                return true;
            }
            /*else {
                Selection.removeSelection(buffer);
            }*/
        }
        return super.onTouchEvent(widget, buffer, event);
    }

    public void setClickStrategy(AreClickStrategy areClickStrategy) {
        this.mAreClickStrategy = areClickStrategy;
    }
}
