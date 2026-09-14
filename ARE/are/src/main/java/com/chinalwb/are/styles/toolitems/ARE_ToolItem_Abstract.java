package com.chinalwb.are.styles.toolitems;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;

import com.chinalwb.are.AREditText;
import com.chinalwb.are.AREditor;
import com.chinalwb.are.R;
import com.chinalwb.are.Util;
import com.chinalwb.are.spans.AreQuoteSpan;
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Default do nothing
        // Children classes can override if necessary
        return;
    }


    /**
     * Size of a toolbar button, in dp. Large enough to be a comfortable touch
     * target; the icon inside it stays on the 24dp grid the icon set is drawn on.
     */
    protected static final int TOOL_ITEM_SIZE_DP = 48;

    /** Padding around the icon, in dp. */
    protected static final int TOOL_ITEM_PADDING_DP = 12;

    /**
     * Builds the button of a tool item.
     *
     * <p>Every tool item is laid out the same way - same box, same icon size,
     * same tint and the same "this style is on" treatment - so the toolbar reads
     * as one set rather than as a row of unrelated pictures.</p>
     *
     * @param context        used to inflate the icon
     * @param iconResId      the icon, from the ARE icon set
     * @param descriptionRes the label accessibility services read out
     * @return the button
     */
    protected ImageView createToolItemView(Context context, @DrawableRes int iconResId,
                                           @StringRes int descriptionRes) {
        ImageView imageView = new ImageView(context);

        int size = Util.getPixelByDp(context, TOOL_ITEM_SIZE_DP);
        imageView.setLayoutParams(new LinearLayout.LayoutParams(size, size));

        int padding = Util.getPixelByDp(context, TOOL_ITEM_PADDING_DP);
        imageView.setPadding(padding, padding, padding, padding);

        imageView.setImageResource(iconResId);
        imageView.setImageTintList(
                ContextCompat.getColorStateList(context, R.color.are_tool_item_tint));
        imageView.setBackgroundResource(R.drawable.are_tool_item_background);
        imageView.setContentDescription(context.getString(descriptionRes));
        return imageView;
    }

    public AREditText getEditText() {
        return mToolbar.getEditText();
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
