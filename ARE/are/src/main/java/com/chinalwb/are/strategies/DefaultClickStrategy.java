package com.chinalwb.are.strategies;

import android.content.Context;

import com.chinalwb.are.Util;
import com.chinalwb.are.spans.ARE_Clickable_Span;
import com.chinalwb.are.spans.AreAtSpan;
import com.chinalwb.are.spans.AreImageSpan;
import com.chinalwb.are.spans.AreVideoSpan;

/**
 * Created by wliu on 30/06/2018.
 */

public class DefaultClickStrategy implements AreClickStrategy {

    @Override
    public boolean onClick(Context context, ARE_Clickable_Span areClickableSpan) {
        if (areClickableSpan instanceof AreAtSpan) {
            Util.toast(context, "At span");
            return true;
        } else if (areClickableSpan instanceof AreImageSpan) {
            Util.toast(context, "Image span");
            return true;
        } else if (areClickableSpan instanceof AreVideoSpan) {
            Util.toast(context, "Video span");
            return true;
        }
        return false;
    }
}
