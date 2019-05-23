package com.chinalwb.are.styles.toolitems;

import android.content.Context;
import android.text.Editable;
import android.text.style.CharacterStyle;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chinalwb.are.AREditText;
import com.chinalwb.are.R;
import com.chinalwb.are.Util;
import com.chinalwb.are.spans.AreForegroundColorSpan;
import com.chinalwb.are.styles.IARE_Style;
import com.chinalwb.are.styles.toolitems.styles.ARE_Style_FontColor;

public class ARE_ToolItem_FontColor extends ARE_ToolItem_Abstract {
    @Override
    public IARE_Style getStyle() {
        if (mStyle == null) {
            AREditText editText = this.getEditText();
            mStyle = new ARE_Style_FontColor(editText, (ImageView) mToolItemView);
        }
        return mStyle;
    }

    @Override
    public View getView(Context context) {
        if (null == context) {
            return mToolItemView;
        }
        if (mToolItemView == null) {
            ImageView imageView = new ImageView(context);
            int size = Util.getPixelByDp(context, 35);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(size, size);
            imageView.setLayoutParams(params);
            imageView.setImageResource(R.drawable.foregroundcolor);
            imageView.bringToFront();
            mToolItemView = imageView;
        }

        return mToolItemView;
    }

    @Override
    public void onSelectionChanged(int selStart, int selEnd) {
        int spanColor = -1;

        AREditText editText = this.getEditText();
        Editable editable = editText.getEditableText();
        if (selStart > 0 && selStart == selEnd) {
            AreForegroundColorSpan[] styleSpans = editable.getSpans(selStart - 1, selStart, AreForegroundColorSpan.class);

            if (styleSpans.length > 0) {
                spanColor = ((AreForegroundColorSpan) styleSpans[styleSpans.length - 1]).getForegroundColor();
            }
            ((ARE_Style_FontColor) mStyle).setColorChecked(spanColor);
        } else {
            //≤
            // Selection is a range
            AreForegroundColorSpan[] styleSpans = editable.getSpans(selStart, selEnd, AreForegroundColorSpan.class);

            boolean multipleColor = false;
            for (int i = 0; i < styleSpans.length; i++) {

                int thisSpanColor = styleSpans[i].getForegroundColor();
                if (spanColor == -1) {
                    spanColor = thisSpanColor;
                } else {
                    if (spanColor != thisSpanColor) {
                        multipleColor = true;
                        break;
                    }
                }
            }
            if (!multipleColor) {
                ((ARE_Style_FontColor) mStyle).setColorChecked(spanColor);
                return;
            }
        }
    }

    @Override
    public IARE_ToolItem_Updater getToolItemUpdater() {
        return null;
    }
}
