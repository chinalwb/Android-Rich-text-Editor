package com.chinalwb.are.styles.toolitems;

import android.view.View;

import com.chinalwb.are.styles.IARE_Style;

import androidx.annotation.DrawableRes;

/**
 * The default tool item check status updater.
 */
public class ARE_ToolItem_UpdaterDefault implements IARE_ToolItem_Updater {

    private IARE_ToolItem mToolItem;

    @DrawableRes
    private final int mIconBackground;
    private final int mIconSize;

    public ARE_ToolItem_UpdaterDefault(IARE_ToolItem toolItem, @DrawableRes int iconBackground, int iconSize) {
        mToolItem = toolItem;
        mIconBackground = iconBackground;
        mIconSize = iconSize;
    }

    @Override
    public void onCheckStatusUpdate(boolean checked) {
        IARE_Style areStyle = mToolItem.getStyle();
        areStyle.setChecked(checked);
        View view = mToolItem.getView(null);
        view.setBackgroundResource(mIconBackground);
        view.setSelected(checked);
    }
}
