package com.chinalwb.are.styles;

import android.content.Context;
import android.text.Editable;
import android.text.Spanned;

import com.chinalwb.are.Util;
import com.chinalwb.are.spans.AreDynamicSpan;

/**
 * Dynamic style abstract implementation.
 *
 * Dynamic means the Span has a configurable value which can provide different features.
 * Such as: Font color / Font size.
 *
 * @param <E>
 */
public abstract class ARE_ABS_Dynamic_Style<E extends AreDynamicSpan> extends ARE_ABS_Style<E> {

    public ARE_ABS_Dynamic_Style() {
        super();
    }

    public ARE_ABS_Dynamic_Style(Context context) {
        super(context);
    }

    protected void applyNewStyle(Editable editable, int start, int end, int currentStyle) {
        E startSpan = null;
        int startSpanStart = Integer.MAX_VALUE;
        E endSpan = null;
        int endSpanStart = -1;
        int endSpanEnd = -1;

        int detectStart = start;
        if (start > 0) {
            detectStart = start - 1;
        }
        int detectEnd = end;
        if (end < editable.length()) {
            detectEnd = end + 1;
        }

        E[] existingSpans = editable.getSpans(detectStart, detectEnd, clazzE);
        if (existingSpans != null && existingSpans.length > 0) {
            for (E span : existingSpans) {
                int spanStart = editable.getSpanStart(span);

                if (spanStart < startSpanStart) {
                    startSpanStart = spanStart;
                    startSpan = span;
                }

                if (spanStart >= endSpanStart) {
                    endSpanStart = spanStart;
                    endSpan = span;
                    int thisSpanEnd = editable.getSpanEnd(span);
                    if (thisSpanEnd > endSpanEnd) {
                        endSpanEnd = thisSpanEnd;
                    }
                }
            } // End for

            if (startSpan == null || endSpan == null) {
                Util.log("[ARE_ABS_Dynamic_Style#applyNewStyle] >>>>>>>>>>>>>>> ERROR!! startSpan or endSpan is null");
                return;
            }

            if (end > endSpanEnd) {
                Util.log("This should never happen! TAKE CARE!");
                endSpanEnd = end;
            }

            for (E span : existingSpans) {
                editable.removeSpan(span);
            }

            int startSpanFeature = startSpan.getDynamicFeature();
            int endSpanFeature = endSpan.getDynamicFeature();


            if (startSpanFeature == currentStyle && endSpanFeature == currentStyle) {
                editable.setSpan(newSpan(), startSpanStart, endSpanEnd, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            } else if (startSpanFeature == currentStyle) {
                editable.setSpan(newSpan(startSpanFeature), startSpanStart, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                editable.setSpan(newSpan(endSpanFeature), end, endSpanEnd, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            } else if (endSpanFeature == currentStyle) {
                editable.setSpan(newSpan(startSpanFeature), startSpanStart, start, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                editable.setSpan(newSpan(endSpanFeature), start, endSpanEnd, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            } else {
                editable.setSpan(newSpan(startSpanFeature), startSpanStart, start, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                if (endSpanEnd > end) {
                    editable.setSpan(newSpan(endSpanFeature), end, endSpanEnd, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                }
                editable.setSpan(newSpan(), start, end, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            }
        } else {
            editable.setSpan(newSpan(), start, end, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        }
    }

    private void logSpans(E[] es) {
        for (E e : es) {
            Editable editable = getEditText().getEditableText();
            int start = editable.getSpanStart(e);
            int end = editable.getSpanEnd(e);
            Util.log("start == " + start + ", end == " + end);
        }
    }

    @Override
    protected void extendPreviousSpan(Editable editable, int pos) {
        E[] pSpans = editable.getSpans(pos, pos, clazzE);
        if (pSpans != null && pSpans.length > 0) {
            E lastSpan = pSpans[0];
            int start = editable.getSpanStart(lastSpan);
            int end = editable.getSpanEnd(lastSpan);
            editable.removeSpan(lastSpan);
            int lastSpanFeature = lastSpan.getDynamicFeature();
            featureChangedHook(lastSpanFeature);
            editable.setSpan(newSpan(lastSpanFeature), start, end, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        }
    }

    protected abstract void featureChangedHook(int feature);
    protected abstract E newSpan(int feature);
}
