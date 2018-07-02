package com.chinalwb.are.spans;

import android.text.style.URLSpan;

/**
 * Created by wliu on 02/07/2018.
 */

public class AreUrlSpan extends URLSpan implements ARE_Clickable_Span {

    public AreUrlSpan(String url) {
        super(url);
    }

}
