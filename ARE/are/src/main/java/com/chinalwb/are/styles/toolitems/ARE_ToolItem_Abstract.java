package com.chinalwb.are.styles.toolitems;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chinalwb.are.AREditText;
import com.chinalwb.are.Util;
import com.chinalwb.are.styles.IARE_Style;
import com.chinalwb.are.styles.toolbar.IARE_Toolbar;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;

/**
 * Created by wliu on 13/08/2018.
 */

public abstract class ARE_ToolItem_Abstract implements IARE_ToolItem {

    protected IARE_Style mStyle;

    protected View mToolItemView;

    protected IARE_ToolItem_Updater mToolItemUpdater;

    private IARE_Toolbar mToolbar;
    @DrawableRes
    protected int mIconBackground;
    protected int mIconSize;
    protected int mIconMargin;

    @Override
    public IARE_Toolbar getToolbar() {
        return mToolbar;
    }

    @Override
    public void setToolbar(IARE_Toolbar toolbar, @DrawableRes int iconBackground, int iconSize, int iconMargin) {
        mToolbar = toolbar;
        mIconBackground = iconBackground;
        mIconSize = iconSize;
    }

    @Override
    public void setToolItemUpdater(IARE_ToolItem_Updater toolItemUpdater) {
        mToolItemUpdater = toolItemUpdater;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Default do nothing
        // Children classes can override if necessary
        return;
    }

    public AREditText getEditText() {
        return mToolbar.getEditText();
    }

    protected ImageView createIcon(@NonNull Context context, @DrawableRes int resIcon) {
        ImageView imageView = new ImageView(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mIconSize, mIconSize);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            params.setMarginEnd(mIconMargin);
            params.setMarginStart(mIconMargin);
        }
        imageView.setLayoutParams(params);
        imageView.setBackgroundResource(mIconBackground);
        imageView.setImageResource(resIcon);
        imageView.bringToFront();
        return imageView;
    }


    protected <T> void printSpans(Class<T> clazz) {
        EditText editText = getEditText();
        Editable editable = editText.getEditableText();
        T[] spans = editable.getSpans(0, editable.length(), clazz);
        for (T span : spans) {
            int start = editable.getSpanStart(span);
            int end = editable.getSpanEnd(span);
            Util.log("Span -- " + clazz + ", start = " + start + ", end == " + end);
        }
    }
}
