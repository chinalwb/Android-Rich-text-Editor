package com.chinalwb.are.spans;

import android.text.style.LeadingMarginSpan;

/**
 * A list item.
 *
 * <p>An item knows how deep it is nested. The level decides how far the item is
 * indented, how it is numbered - each level counts on its own - and how deep the
 * {@code <ol>} / {@code <ul>} it belongs to sits when the document is saved.</p>
 */
public interface AreListSpan extends LeadingMarginSpan {

    /** Indent of one nesting level, in pixels. */
    int LEVEL_INDENT = 80;

    /**
     * Returns how deep this item is nested, 0 for a top level item.
     */
    int getLevel();

    /**
     * Sets how deep this item is nested.
     *
     * @param level 0 for a top level item; negative values are clamped
     */
    void setLevel(int level);
}
