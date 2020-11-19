package com.chinalwb.are.styles;

import android.content.Context;
import android.text.Editable;
import android.text.Spanned;
import android.util.Log;

import com.chinalwb.are.Util;
import com.chinalwb.are.spans.AreDynamicSpan;

/**
 * Dynamic style abstract implementation.
 * <p>
 * Dynamic means the Span has a configurable value which can provide different features.
 * Such as: Font color / Font size.
 *
 * @param <E>
 */
public abstract class ARE_ABS_Dynamic_Style<E extends AreDynamicSpan> extends ARE_ABS_Style<E> {

    public ARE_ABS_Dynamic_Style(Context context) {
        super(context);
    }

    protected void applyNewStyle(Editable editable, int start, int end, int currentStyle) {
        try {
            E startSpan = null;
            int startSpanStart = Integer.MAX_VALUE;
            E endSpan = null;
            int endSpanStart = -1;
            int endSpanEnd = -1;
            //这里起始位置-1和结束位置+1我不是很懂为什么要这样做
            //detectStart与detectEnd的原意我理解为取出当前光标选中的文本内所含的spans,但是-1和+1后取的范围就会超过光标选中的范围。
//        int detectStart = start;
//        if (start > 0) {
//            detectStart = start - 1;
//        }
//        int detectEnd = end;
//        if (end < editable.length()) {
//            detectEnd = end + 1;
//        }

            E[] existingSpans = editable.getSpans(start, end, clazzE);
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
                    //起始span与结束span都一样，那么就将此范围内的所有文本置为同一span
                    //start可能在startSpanStart之前也有可能在之后，所以取两者最小值作为最终起点
                    //同理，end可能在endSpanEnd之前或之后，所以取两者最大值作为最终终点
                    int realStart = Math.min(start, startSpanStart);
                    int realEnd = Math.max(end, endSpanEnd);
                    editable.setSpan(newSpan(), realStart, realEnd, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                } else if (startSpanFeature == currentStyle) {
                    //起始span与当前一样，结束span与当前不一样，则将起始起点到选中终点设置为当前样式，
                    //start可能在startSpanStart之前也可能在之后，所以取两者最小值作为最终起点
                    int realStart = Math.min(start, startSpanStart);
                    editable.setSpan(newSpan(startSpanFeature), realStart, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                    //endSpanEnd(结束span的终点)如果>end，才将end到endSpanEnd之间的文本设置为结束span样式
                    if (endSpanEnd > end) {
                        editable.setSpan(newSpan(endSpanFeature), end, endSpanEnd, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                    }
                } else if (endSpanFeature == currentStyle) {
                    //此分支代表只有选中区域内的结束span与即将设置span相等,则设置start到末尾为结束span样式
                    //如果startSpanStart<start才将startSpanStart到start设置为起始span
                    if (startSpanStart < start) {
                        editable.setSpan(newSpan(startSpanFeature), startSpanStart, start, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                    }
                    //end可能在endSpanEnd之前或之后，所以取两者最大值作为最终终点
                    //set the realEnd index with the Max value between "end" and "endSpanEnd"
                    int realEnd = Math.max(end, endSpanEnd);
                    editable.setSpan(newSpan(endSpanFeature), start, realEnd, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                } else {
                    //设置起始span
                    //set the start area's span
                    if (startSpanStart < start) {
                        editable.setSpan(newSpan(startSpanFeature), startSpanStart, start, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                    }
                    //设置结束span
                    //set the end area's span
                    if (endSpanEnd > end) {
                        editable.setSpan(newSpan(endSpanFeature), end, endSpanEnd, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                    }
                    //设置选中区域的span
                    //set selected area's span
                    editable.setSpan(newSpan(), start, end, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                }
            } else {
                editable.setSpan(newSpan(), start, end, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            }
        } catch (Exception e) {
            //catch Exception,avoid crash
            //捕获异常，避免闪退
            //再多的逻辑判断也可能有疏漏，捕获异常能有效减少闪退
            Log.e(this.getClass().getSimpleName(), "ARE_ABS_Dynamic_Style setSpan failed ：" + e.getMessage());
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
