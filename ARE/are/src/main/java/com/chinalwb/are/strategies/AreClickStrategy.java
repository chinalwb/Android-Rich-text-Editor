package com.chinalwb.are.strategies;

import android.content.Context;
import android.text.style.URLSpan;

import com.chinalwb.are.spans.ARE_Clickable_Span;
import com.chinalwb.are.spans.AreAtSpan;
import com.chinalwb.are.spans.AreImageSpan;
import com.chinalwb.are.spans.AreVideoSpan;

/**
 * Created by wliu on 30/06/2018.
 */

public interface AreClickStrategy {


    // boolean onClick(Context context, ARE_Clickable_Span areClickableSpan);

    /**
     * Do your actions upon span clicking {@link com.chinalwb.are.spans.AreAtSpan}
     *
     * @param context
     * @param atSpan
     * @return handled return true; or else return false
     */
    boolean onClickAt(Context context, AreAtSpan atSpan);

    /**
     * Do your actions upon span clicking {@link com.chinalwb.are.spans.AreImageSpan}
     *
     * @param context
     * @param imageSpan
     * @return handled return true; or else return false
     */
    boolean onClickImage(Context context, AreImageSpan imageSpan);

    /**
     * Do your actions upon span clicking {@link android.text.style.URLSpan}
     *
     * @param context
     * @param urlSpan
     * @return handled return true; or else return false
     */
    boolean onClickUrl(Context context, URLSpan urlSpan);

    /**
     * Do your actions upon span clicking {@link com.chinalwb.are.spans.AreVideoSpan}
     *
     * @param context
     * @param videoSpan
     * @return handled return true; or else return false
     */
    boolean onClickVideo(Context context, AreVideoSpan videoSpan);
}
