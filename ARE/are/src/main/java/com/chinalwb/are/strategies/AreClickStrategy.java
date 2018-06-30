package com.chinalwb.are.strategies;

import android.content.Context;

import com.chinalwb.are.spans.ARE_Clickable_Span;

/**
 * Created by wliu on 30/06/2018.
 */

public interface AreClickStrategy {

    /**
     * Do your actions upon span clicking
     *
     * @param context
     * @param areClickableSpan
     * @return handled return true; or else return false
     */
    boolean onClick(Context context, ARE_Clickable_Span areClickableSpan);
}
