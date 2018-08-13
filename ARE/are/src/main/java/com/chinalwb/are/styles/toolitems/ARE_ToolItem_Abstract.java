package com.chinalwb.are.styles.toolitems;

import android.view.View;

import com.chinalwb.are.AREditText;
import com.chinalwb.are.AREditor;
import com.chinalwb.are.styles.IARE_Style;
import com.chinalwb.are.styles.toolbar.IARE_Toolbar;

/**
 * Created by wliu on 13/08/2018.
 */

public abstract class ARE_ToolItem_Abstract implements IARE_ToolItem {

    protected IARE_Style mStyle;

    protected View mToolItemView;

    protected IARE_ToolItem_Updater mToolItemUpdater;

    private IARE_Toolbar mToolbar;

    @Override
    public IARE_Toolbar getToolbar() {
        return mToolbar;
    }

    @Override
    public void setToolbar(IARE_Toolbar toolbar) {
        mToolbar = toolbar;
    }

    @Override
    public void setToolItemUpdater(IARE_ToolItem_Updater toolItemUpdater) {
        mToolItemUpdater = toolItemUpdater;
    }

    public AREditText getEditText() {
        return mToolbar.getEditText();
    }
}
