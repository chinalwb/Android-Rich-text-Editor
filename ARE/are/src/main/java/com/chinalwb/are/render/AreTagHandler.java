package com.chinalwb.are.render;

import android.text.Editable;

import com.chinalwb.are.android.inner.Html;

import org.xml.sax.XMLReader;

public class AreTagHandler implements Html.TagHandler {

    @Override
    public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {
        if (opening) {
            if (tag.equalsIgnoreCase("hr")) {

            }
        }
    }
}
