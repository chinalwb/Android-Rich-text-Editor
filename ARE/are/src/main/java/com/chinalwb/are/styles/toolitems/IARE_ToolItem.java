package com.chinalwb.are.styles.toolitems;

import android.content.Context;
import android.view.View;

import com.chinalwb.are.AREditText;
import com.chinalwb.are.styles.IARE_Style;
import com.chinalwb.are.styles.toolbar.IARE_Toolbar;

/**
 * Created by wliu on 13/08/2018.
 */

public interface IARE_ToolItem {

    /**
     * Each tool item is a style, and a style combines with specific span.
     * @return
     */
    public IARE_Style getStyle();

    /**
     * Each tool item has a view.
     */
    public View getView(Context context);

    /**
     * Selection changed call back. Update tool item checked status
     *
     * @param selStart
     * @param selEnd
     */
    public void onSelectionChanged(int selStart, int selEnd);

    /**
     * Returns the toolbar of this tool item.
     * @return
     */
    public IARE_Toolbar getToolbar();

    /**
     * Sets the toolbar for this tool item.
     */
    public void setToolbar(IARE_Toolbar toolbar);

}
