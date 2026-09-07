package com.chinalwb.are.styles.toolitems;

import android.view.View;

import com.chinalwb.are.styles.IARE_Style;

/**
 * The default tool item check status updater.
 *
 * <p>The button is put into the selected state rather than being painted here, so
 * the "this style is on" treatment lives in {@code are_tool_item_background} and
 * {@code are_tool_item_tint} and is the same for every tool item.</p>
 */
public class ARE_ToolItem_UpdaterDefault implements IARE_ToolItem_Updater {

    private IARE_ToolItem mToolItem;

    public ARE_ToolItem_UpdaterDefault(IARE_ToolItem toolItem) {
        mToolItem = toolItem;
    }

    /**
     * @param toolItem       the tool item to update
     * @param checkedColor   ignored
     * @param uncheckedColor ignored
     * @deprecated the active state is a view state now, not a colour. Use
     *             {@link #ARE_ToolItem_UpdaterDefault(IARE_ToolItem)} and restyle
     *             through the {@code are_tool_item_*} resources instead.
     */
    @Deprecated
    public ARE_ToolItem_UpdaterDefault(IARE_ToolItem toolItem, int checkedColor,
                                       int uncheckedColor) {
        this(toolItem);
    }

    @Override
    public void onCheckStatusUpdate(boolean checked) {
        IARE_Style areStyle = mToolItem.getStyle();
        areStyle.setChecked(checked);

        View view = mToolItem.getView(null);
        if (null == view) {
            //
            // Some tool items - the "@" one for instance - have no button.
            return;
        }
        view.setSelected(checked);
    }
}
