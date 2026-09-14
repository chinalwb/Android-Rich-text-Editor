package com.chinalwb.are.styles;

import android.text.Editable;
import android.text.Selection;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.chinalwb.are.Constants;
import com.chinalwb.are.Util;
import com.chinalwb.are.spans.ListBulletSpan;
import com.chinalwb.are.spans.ListNumberSpan;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Editing an ordered / bullet list through the real toolbar buttons.
 *
 * <p>The list styles are driven the way the toolbar drives them: the tool item's
 * ImageView is clicked, and the text watcher of
 * {@link com.chinalwb.are.AREditText} feeds every edit back into the style.</p>
 */
@RunWith(AndroidJUnit4.class)
public class ListEditingTest extends ListEditingTestBase {

    private static final char ZWSP = (char) Constants.ZERO_WIDTH_SPACE_INT;

    @Test
    public void clickingTheNumberButtonTurnsTheLineIntoTheFirstItem() {
        setTextAndCursor("One", 3);
        click(numberButton);

        assertEquals(ZWSP + "One", text());
        assertEquals(1, numberOfParagraph(0));
    }

    @Test
    public void newLinesContinueTheNumbering() {
        setTextAndCursor("One", 3);
        click(numberButton);

        typeNewLineRaw();
        typeRaw("Two");
        typeNewLineRaw();
        typeRaw("Three");

        assertEquals(1, numberOfParagraph(0));
        assertEquals(2, numberOfParagraph(1));
        assertEquals(3, numberOfParagraph(2));
    }

    @Test
    public void numberingKeepsGoingPastNine() {
        setTextAndCursor("item 1", 6);
        click(numberButton);
        for (int i = 2; i <= 12; i++) {
            typeNewLineRaw();
            typeRaw("item " + i);
        }

        for (int paragraph = 0; paragraph < 12; paragraph++) {
            assertEquals("paragraph " + paragraph,
                    paragraph + 1, numberOfParagraph(paragraph));
        }
    }

    @Test
    public void turningPlainParagraphsIntoItemsOneByOneKeepsCounting() {
        //
        // The case the style is documented with:
        //   1. A
        //   B
        //   C
        // putting the cursor on B and then on C has to produce 2. and 3.
        setTextAndCursor("A\nB\nC", 1);
        click(numberButton);

        putCursorAtEndOfParagraph(1);
        click(numberButton);

        putCursorAtEndOfParagraph(2);
        click(numberButton);

        assertEquals(1, numberOfParagraph(0));
        assertEquals(2, numberOfParagraph(1));
        assertEquals(3, numberOfParagraph(2));
    }

    @Test
    public void aListStartedBelowAPlainParagraphStartsAtOne() {
        setTextAndCursor("plain\nfirst item", "plain\nfirst item".length());
        click(numberButton);

        assertEquals(1, numberOfParagraph(1));
    }

    @Test
    public void anItemBelowABulletStartsANewCount() {
        setTextAndCursor("A\nB", 1);
        click(bulletButton);

        putCursorAtEndOfParagraph(1);
        click(numberButton);

        assertEquals("a bullet interrupts the count", 1, numberOfParagraph(1));
    }

    @Test
    public void clickingTheNumberButtonAgainRemovesTheItemAndItsMarker() {
        setTextAndCursor("One", 3);
        click(numberButton);
        assertEquals(ZWSP + "One", text());

        putCursorAtEndOfParagraph(0);
        click(numberButton);

        assertEquals("the zero width marker must not be left behind", "One", text());
        assertEquals(0, spansOf(ListNumberSpan.class).length);
    }

    @Test
    public void clickingTheBulletButtonAgainRemovesTheItemAndItsMarker() {
        setTextAndCursor("One", 3);
        click(bulletButton);
        assertEquals(ZWSP + "One", text());

        putCursorAtEndOfParagraph(0);
        click(bulletButton);

        assertEquals("the zero width marker must not be left behind", "One", text());
        assertEquals(0, spansOf(ListBulletSpan.class).length);
    }

    @Test
    public void togglingAnItemOnAndOffRepeatedlyNeverGrowsTheText() {
        setTextAndCursor("One", 3);

        for (int i = 0; i < 5; i++) {
            putCursorAtEndOfParagraph(0);
            click(numberButton);
            assertEquals(ZWSP + "One", text());

            putCursorAtEndOfParagraph(0);
            click(numberButton);
            assertEquals("toggling must not add invisible characters", "One", text());
        }
    }

    @Test
    public void turningABulletIntoANumberDoesNotLeaveAMarkerBehind() {
        setTextAndCursor("One", 3);
        click(bulletButton);
        typeNewLineRaw();
        typeRaw("Two");

        String beforeConversion = text();
        putCursorAtEndOfParagraph(1);
        click(numberButton);

        //
        // Only the item the cursor is on converts - that is what the toolbar
        // promises. What must not happen is the text growing an extra zero width
        // space every time, which the no-op delete in the redraw hack used to do.
        assertEquals("converting must not change the text itself",
                beforeConversion, text());
        assertEquals(1, spansOf(ListNumberSpan.class).length);
        assertEquals(1, spansOf(ListBulletSpan.class).length);
    }

    @Test
    public void convertingBackAndForthNeverGrowsTheText() {
        setTextAndCursor("One", 3);
        click(bulletButton);
        typeNewLineRaw();
        typeRaw("Two");

        String beforeConversion = text();
        for (int i = 0; i < 5; i++) {
            putCursorAtEndOfParagraph(1);
            click(numberButton);
            putCursorAtEndOfParagraph(1);
            click(bulletButton);
        }

        assertEquals("repeated conversions must not add invisible characters",
                beforeConversion, text());
    }

    @Test
    public void deletingAnItemAwayDoesNotCrash() {
        setTextAndCursor("A", 1);
        click(numberButton);

        while (editText.getText().length() > 0) {
            deleteBackwardsFromEnd();
        }

        assertEquals("", text());
    }

    @Test
    public void deletingTheNewLineBetweenTwoItemsDoesNotCrash() {
        setTextAndCursor("One", 3);
        click(numberButton);
        typeNewLineRaw();
        typeRaw("Two");

        final int newLineIndex = text().indexOf('\n');
        onMain(new Runnable() {
            @Override
            public void run() {
                Selection.setSelection(editText.getText(), newLineIndex + 1);
                editText.getText().delete(newLineIndex, newLineIndex + 1);
            }
        });

        //
        // Whatever the merge decided, the list has to stay consistent - and the
        // editor has to still be alive, which used to not be the case: this path
        // indexed an empty span array.
        assertFalse(text().contains("\n"));
        assertTrue(spansOf(ListNumberSpan.class).length >= 1);
    }

    @Test
    public void emptyItemPlusEnterEndsTheList() {
        setTextAndCursor("One", 3);
        click(numberButton);
        typeNewLineRaw();

        //
        // Enter on the empty item that enter just created drops out of the list.
        int before = spansOf(ListNumberSpan.class).length;
        typeNewLineRaw();

        assertTrue("the empty item should not have survived",
                spansOf(ListNumberSpan.class).length <= before);
    }

    // ---------------------------------------------------------------- helpers

    private void putCursorAtEndOfParagraph(final int paragraph) {
        onMain(new Runnable() {
            @Override
            public void run() {
                Editable editable = editText.getText();
                int end = Util.getParagraphEnd(editable, paragraph);
                if (end > 0 && end <= editable.length()
                        && editable.charAt(end - 1) == '\n') {
                    end--;
                }
                Selection.setSelection(editable, end);
            }
        });
    }

    private void deleteBackwardsFromEnd() {
        onMain(new Runnable() {
            @Override
            public void run() {
                Editable editable = editText.getText();
                int at = editable.length();
                if (at > 0) {
                    Selection.setSelection(editable, at);
                    editable.delete(at - 1, at);
                }
            }
        });
    }

    private String text() {
        return editText.getText().toString();
    }

    private <T> T[] spansOf(Class<T> type) {
        Editable editable = editText.getText();
        return editable.getSpans(0, editable.length(), type);
    }

    private int numberOfParagraph(int paragraph) {
        Editable editable = editText.getText();
        int start = Util.getParagraphStart(editable, paragraph);
        int end = Util.getParagraphEnd(editable, paragraph);
        if (end > start && editable.charAt(end - 1) == '\n') {
            end--;
        }
        ListNumberSpan[] spans = editable.getSpans(start, end, ListNumberSpan.class);
        assertTrue("paragraph " + paragraph + " of \"" + printable(editable)
                + "\" carries no number", spans.length > 0);
        return spans[0].getNumber();
    }

    private static String printable(CharSequence text) {
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == ZWSP) {
                out.append("<z>");
            } else if (c == '\n') {
                out.append("\\n");
            } else {
                out.append(c);
            }
        }
        return out.toString();
    }
}
