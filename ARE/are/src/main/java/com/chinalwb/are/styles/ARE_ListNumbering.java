package com.chinalwb.are.styles;

import android.text.Editable;

import com.chinalwb.are.Constants;
import com.chinalwb.are.Util;
import com.chinalwb.are.spans.AreListSpan;
import com.chinalwb.are.spans.ListNumberSpan;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Numbering for the ordered list items of a whole document.
 *
 * <p>The numbers of a {@link ListNumberSpan} are display only state, so instead of
 * trying to patch them up one by one after every edit, the numbers of the entire
 * text are recomputed. That keeps the numbering correct no matter how the edit
 * rearranged the spans, and it repairs documents whose numbering had already
 * drifted.</p>
 *
 * <p>Numbering restarts at 1 whenever a paragraph that is not an ordered list item
 * interrupts the run, which covers plain paragraphs and bullet items alike:</p>
 *
 * <pre>
 * 1. A
 * 2. B
 * plain paragraph
 * 1. C
 * 2. D
 * </pre>
 *
 * All Rights Reserved.
 */
public class ARE_ListNumbering {

    private ARE_ListNumbering() {
        // Static only
    }

    /**
     * Recomputes the number of every {@link ListNumberSpan} of the given text.
     *
     * <p>Spans are visited in document order, which the span array itself does not
     * guarantee, and a paragraph that ended up carrying more than one span - which
     * would draw two numbers on the same line - keeps only its first span.</p>
     *
     * <p>Nesting is read off the spans themselves: the span of an item that has a
     * sublist covers that sublist, so a span contained in another one is an item a
     * level deeper. Each level is numbered on its own, which keeps the outer list
     * counting across a sublist instead of restarting after it.</p>
     *
     * @param editable the text to renumber; {@code null} is ignored
     */
    public static void renumber(Editable editable) {
        if (null == editable) {
            return;
        }

        ListNumberSpan[] spans = editable.getSpans(0, editable.length(), ListNumberSpan.class);
        if (null == spans || spans.length == 0) {
            return;
        }

        List<ListNumberSpan> orderedSpans = sortByPosition(editable, spans);

        //
        // Group by depth, then number each depth as its own list.
        Map<Integer, List<ListNumberSpan>> byLevel = new LinkedHashMap<>();
        for (ListNumberSpan span : orderedSpans) {
            if (editable.getSpanStart(span) < 0) {
                continue;
            }
            int level = span.getLevel();
            List<ListNumberSpan> atLevel = byLevel.get(level);
            if (atLevel == null) {
                atLevel = new ArrayList<>();
                byLevel.put(level, atLevel);
            }
            atLevel.add(span);
        }

        for (List<ListNumberSpan> atLevel : byLevel.values()) {
            numberOneLevel(editable, atLevel);
        }
    }

    private static void numberOneLevel(Editable editable, List<ListNumberSpan> spans) {
        int number = 0;
        int previousParagraph = Integer.MIN_VALUE;
        int previousEnd = -1;

        for (ListNumberSpan span : spans) {
            int spanStart = editable.getSpanStart(span);
            int paragraph = Util.getParagraphIndex(editable, spanStart);

            if (paragraph == previousParagraph) {
                //
                // A second span on a paragraph that already has one would draw a
                // second number over the first. Drop it.
                editable.removeSpan(span);
                continue;
            }

            if (previousEnd >= 0
                    && continuesTheList(editable, previousEnd, spanStart, span.getLevel())) {
                number++;
            } else {
                number = 1;
            }

            span.setNumber(number);
            previousParagraph = paragraph;
            previousEnd = editable.getSpanEnd(span);
        }
    }

    /**
     * Returns whether an item at {@code level} starting at {@code spanStart}
     * carries on the list whose previous item at that level ended at
     * {@code previousEnd}.
     *
     * <p>What sits in between decides. A sublist does not break the list it hangs
     * off - nor does the blank paragraph it leaves behind - but a paragraph with
     * content of its own starts a new list.</p>
     */
    private static boolean continuesTheList(Editable editable, int previousEnd, int spanStart,
                                            int level) {
        int from = Util.getParagraphIndex(editable, previousEnd);
        int to = Util.getParagraphIndex(editable, spanStart);
        if (to <= from) {
            return true;
        }

        for (int paragraph = from + 1; paragraph < to; paragraph++) {
            int start = Util.getParagraphStart(editable, paragraph);
            int end = Util.getParagraphEnd(editable, paragraph);

            if (isDeeperListItem(editable, start, end, level)) {
                continue;
            }
            if (!isBlank(editable, start, end)) {
                return false;
            }
        }
        return true;
    }

    private static boolean isBlank(Editable editable, int start, int end) {
        for (int i = start; i < end; i++) {
            char c = editable.charAt(i);
            if (c != Constants.CHAR_NEW_LINE
                    && c != Constants.ZERO_WIDTH_SPACE_INT
                    && !Character.isWhitespace(c)) {
                return false;
            }
        }
        return true;
    }

    private static boolean isDeeperListItem(Editable editable, int start, int end, int level) {
        AreListSpan[] items = editable.getSpans(start, end, AreListSpan.class);
        for (AreListSpan item : items) {
            if (item.getLevel() > level) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the number the next item of the list containing {@code offset} should
     * get, or 1 when {@code offset} does not continue an existing list.
     *
     * @param editable the text to inspect
     * @param offset   an offset inside the paragraph that precedes the new item
     * @return the number to give to the new list item
     */
    public static int getNextNumber(Editable editable, int offset) {
        if (null == editable || offset <= 0) {
            return 1;
        }

        int paragraph = Util.getParagraphIndex(editable, offset);
        if (paragraph <= 0) {
            return 1;
        }

        int previousStart = Util.getParagraphStart(editable, paragraph - 1);
        int previousEnd = Util.getParagraphEnd(editable, paragraph - 1);
        if (previousEnd > previousStart) {
            //
            // Do not let the query run onto the trailing '\n', that offset already
            // belongs to the paragraph boundary.
            previousEnd--;
        }

        ListNumberSpan[] previousSpans =
                editable.getSpans(previousStart, previousEnd, ListNumberSpan.class);
        if (null == previousSpans || previousSpans.length == 0) {
            return 1;
        }

        int highest = 0;
        for (ListNumberSpan span : previousSpans) {
            if (span.getNumber() > highest) {
                highest = span.getNumber();
            }
        }
        return highest + 1;
    }

    private static List<ListNumberSpan> sortByPosition(final Editable editable,
                                                       ListNumberSpan[] spans) {
        List<ListNumberSpan> orderedSpans = new ArrayList<>(spans.length);
        Collections.addAll(orderedSpans, spans);

        //
        // Editable#getSpans orders by span priority and insertion order, never by
        // position, so the document order has to be established here.
        Collections.sort(orderedSpans, new Comparator<ListNumberSpan>() {
            @Override
            public int compare(ListNumberSpan left, ListNumberSpan right) {
                int leftStart = editable.getSpanStart(left);
                int rightStart = editable.getSpanStart(right);
                if (leftStart != rightStart) {
                    return leftStart < rightStart ? -1 : 1;
                }
                int leftEnd = editable.getSpanEnd(left);
                int rightEnd = editable.getSpanEnd(right);
                return Integer.compare(leftEnd, rightEnd);
            }
        });

        return orderedSpans;
    }
}
