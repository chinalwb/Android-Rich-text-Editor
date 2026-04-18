//package com.chinalwb.are.demo.helpers;
//
//import android.graphics.Color;
//import android.text.Editable;
//import android.text.Spannable;
//import android.text.Spanned;
//
//import com.chinalwb.are.android.inner.Html;
//import com.chinalwb.are.demo.MyApplication;
//import com.chinalwb.are.demo.toolitems.ARE_Span_Youtube;
//
//import org.xml.sax.Attributes;
//import org.xml.sax.XMLReader;
//
//import java.util.Stack;
//
//public class AreTagHandlerExt implements Html.TagHandler {
//
//    private static Stack OL_STACK = new Stack();
//
//    private static class OL {
//        public int level;
//    }
//
//    private static class UL {
//        public int level;
//    }
//
//    @Override
//    public void handleTag(boolean opening, String tag, Editable output, Attributes attributes) {
//        if (opening) {
//            if (tag.equalsIgnoreCase("iframe")) {
//                startIFrame(output, attributes);
//            }
//        } else {
//           //
//        }
//    }
//
//    private static void startIFrame(Editable text, Attributes attributes) {
//        String tag = attributes.getValue("", "tag");
//        if ("youtube".equals(tag)) {
//            String url = attributes.getValue("", "src");
//            String title = attributes.getValue("", "title");
//            int len = text.length();
//            text.append(title);
//            text.setSpan(new ARE_Span_Youtube(MyApplication.appContext, title, url, Color.RED), len, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        }
//    }
//}
