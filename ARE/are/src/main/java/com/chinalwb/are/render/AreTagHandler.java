package com.chinalwb.are.render;

import android.text.Editable;

import com.chinalwb.are.android.inner.Html;

import org.xml.sax.XMLReader;

import java.util.Stack;

public class AreTagHandler implements Html.TagHandler {

    private static Stack OL_STACK = new Stack();

    private static class OL {
        public int level;
    }

    private static class UL {
        public int level;
    }

    @Override
    public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {
        if (opening) {
            if (tag.equalsIgnoreCase("ol")) {
                startOL();
            } else if (tag.equalsIgnoreCase("li")) {
//                startLI();
            } else if (tag.equalsIgnoreCase("ul")) {
//                startUL();
            }
        } else {
            if (tag.equalsIgnoreCase("ol")) {
//                endOL();
            } else if (tag.equalsIgnoreCase("li")) {
//                endLI();
            } else if (tag.equalsIgnoreCase("ul")) {
//                endUL();
            }
        }
    }

    private static void startOL() {
        OL ol = new OL();

    }

    private static void startLI() {

    }

    private static void startUL() {

    }
}
