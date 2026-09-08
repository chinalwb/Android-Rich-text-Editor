package com.chinalwb.are.styles.toolitems.styles;

import android.text.Editable;
import android.text.Spanned;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;

import com.chinalwb.are.AREditText;
import com.chinalwb.are.Util;
import com.chinalwb.are.spans.AreListSpan;
import com.chinalwb.are.styles.ARE_ABS_FreeStyle;
import com.chinalwb.are.styles.ARE_ListNumbering;

/**
 * Moves the list item the cursor is on one level in or out.
 *
 * <p>The level lives on the item's span: it is what indents the item, what makes
 * each level count on its own, and what is written back out as a nested
 * {@code <ol>} / {@code <ul>}.</p>
 *
 * All Rights Reserved.
 */
public class ARE_Style_ListIndent extends ARE_ABS_FreeStyle {

    private final AREditText mEditText;

    private final ImageView mImageView;

    /** +1 to indent, -1 to outdent. */
    private final int mStep;

    public ARE_Style_ListIndent(AREditText editText, ImageView imageView, int step) {
        super(editText.getContext());
        this.mEditText = editText;
        this.mImageView = imageView;
        this.mStep = step;
        setListenerForImageView(this.mImageView);
    }

    @Override
    public EditText getEditText() {
        return this.mEditText;
    }

    @Override
    public void setListenerForImageView(final ImageView imageView) {
        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                changeLevel();
            }
        });
    }

    private void changeLevel() {
        EditText editText = getEditText();
        if (null == editText || null == editText.getText()) {
            return;
        }

        Editable editable = editText.getText();
        int paragraph = Util.getCurrentCursorLine(editText);
        if (paragraph < 0) {
            return;
        }

        AreListSpan item = itemOnParagraph(editable, paragraph);
        if (null == item) {
            //
            // Nothing to indent: the cursor is not on a list item.
            return;
        }

        int level = item.getLevel() + mStep;
        if (level < 0) {
            return;
        }
        //
        // An item can only ever be one level deeper than the item above it,
        // otherwise the first item of a list could be pushed in with nothing to
        // hang off.
        int ceiling = levelAbove(editable, paragraph) + 1;
        if (level > ceiling) {
            return;
        }

        item.setLevel(level);

        //
        // The leading margin changed, so the span has to be set again for the
        // layout to be rebuilt with it.
        int start = editable.getSpanStart(item);
        int end = editable.getSpanEnd(item);
        editable.removeSpan(item);
        editable.setSpan(item, start, end, Spanned.SPAN_INCLUSIVE_INCLUSIVE);

        ARE_ListNumbering.renumber(editable);
    }

    /**
     * Returns the deepest list item on the given paragraph, or {@code null}.
     */
    private static AreListSpan itemOnParagraph(Editable editable, int paragraph) {
        int start = Util.getParagraphStart(editable, paragraph);
        int end = Util.getParagraphEnd(editable, paragraph);
        if (end > start && editable.charAt(end - 1) == '\n') {
            end--;
        }

        AreListSpan deepest = null;
        for (AreListSpan candidate : editable.getSpans(start, end, AreListSpan.class)) {
            if (deepest == null || candidate.getLevel() > deepest.getLevel()) {
                deepest = candidate;
            }
        }
        return deepest;
    }

    /**
     * Returns the level of the nearest list item above the given paragraph, or -1
     * when there is none.
     */
    private static int levelAbove(Editable editable, int paragraph) {
        for (int above = paragraph - 1; above >= 0; above--) {
            AreListSpan item = itemOnParagraph(editable, above);
            if (item != null) {
                return item.getLevel();
            }
            int start = Util.getParagraphStart(editable, above);
            int end = Util.getParagraphEnd(editable, above);
            if (end > start + 1) {
                //
                // A paragraph with content of its own: the list starts below it.
                return -1;
            }
        }
        return -1;
    }

    @Override
    public ImageView getImageView() {
        return mImageView;
    }

    @Override
    public void setChecked(boolean isChecked) {
        // An indent is an action, not a state
    }

    @Override
    public void applyStyle(Editable editable, int start, int end) {
        // The level is changed by the button, not by typing
    }
}
