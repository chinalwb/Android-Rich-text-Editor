package com.chinalwb.are.styles;

import android.text.Editable;
import android.text.Selection;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.chinalwb.are.Util;
import com.chinalwb.are.spans.AreListSpan;
import com.chinalwb.are.spans.ListNumberSpan;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Indenting a list item into a sublist, and back out of it.
 */
@RunWith(AndroidJUnit4.class)
public class ListNestingTest extends ListEditingTestBase {

    @Test
    public void indentingAnItemMakesItASublistItem() {
        buildList("one", "two");

        putCursorOnParagraph(1);
        click(indentButton);

        assertEquals("the first item stays where it is", 0, levelOfParagraph(0));
        assertEquals(1, levelOfParagraph(1));
    }

    @Test
    public void aSublistCountsOnItsOwn() {
        buildList("one", "two", "three");

        putCursorOnParagraph(1);
        click(indentButton);
        putCursorOnParagraph(2);
        click(indentButton);

        assertEquals("outer item", 1, numberOfParagraph(0));
        assertEquals("first item of the sublist", 1, numberOfParagraph(1));
        assertEquals("second item of the sublist", 2, numberOfParagraph(2));
    }

    @Test
    public void theOuterListCarriesOnAfterASublist() {
        buildList("one", "two", "three");

        putCursorOnParagraph(1);
        click(indentButton);

        assertEquals(1, numberOfParagraph(0));
        assertEquals(1, numberOfParagraph(1));
        assertEquals("the outer list must not restart after the sublist",
                2, numberOfParagraph(2));
    }

    @Test
    public void outdentingBringsAnItemBackOut() {
        buildList("one", "two");

        putCursorOnParagraph(1);
        click(indentButton);
        assertEquals(1, levelOfParagraph(1));

        putCursorOnParagraph(1);
        click(outdentButton);

        assertEquals(0, levelOfParagraph(1));
        assertEquals("it is a sibling again", 2, numberOfParagraph(1));
    }

    @Test
    public void indentingIsCappedAtOneLevelBelowTheItemAbove() {
        buildList("one", "two");

        putCursorOnParagraph(1);
        click(indentButton);
        click(indentButton);
        click(indentButton);

        assertEquals("an item can only ever be one level deeper than the one above",
                1, levelOfParagraph(1));
    }

    @Test
    public void theFirstItemOfAListCannotBeIndented() {
        buildList("one", "two");

        putCursorOnParagraph(0);
        click(indentButton);

        assertEquals(0, levelOfParagraph(0));
    }

    @Test
    public void outdentingATopLevelItemDoesNothing() {
        buildList("one", "two");

        putCursorOnParagraph(1);
        click(outdentButton);

        assertEquals(0, levelOfParagraph(1));
        assertEquals(2, numberOfParagraph(1));
    }

    @Test
    public void indentingDoesNothingOutsideAList() {
        setTextAndCursor("just a paragraph", 4);

        click(indentButton);

        assertEquals("just a paragraph", editText.getText().toString());
    }

    @Test
    public void anIndentedItemIsDrawnFurtherIn() {
        buildList("one", "two");

        int before = itemOnParagraph(1).getLeadingMargin(true);
        putCursorOnParagraph(1);
        click(indentButton);
        int after = itemOnParagraph(1).getLeadingMargin(true);

        assertTrue("an indented item has to be drawn further in: "
                + before + " -> " + after, after > before);
    }

    @Test
    public void nestingSurvivesBeingSavedAndLoaded() {
        buildList("one", "two", "three");
        putCursorOnParagraph(1);
        click(indentButton);

        final String html = editText.getHtml();
        assertTrue("the sublist is not nested inside its item: " + html,
                html.contains("<li>") && html.indexOf("<ol>", html.indexOf("<li>")) > 0);

        onMain(new Runnable() {
            @Override
            public void run() {
                editText.setText("");
                editText.fromHtml(html);
            }
        });

        //
        // Items are looked up by their text: the parser separates items with a
        // blank paragraph where the editor does not, so paragraph numbers do not
        // line up across a round trip.
        assertEquals(0, itemContaining("one").getLevel());
        assertEquals(1, itemContaining("two").getLevel());
        assertEquals(0, itemContaining("three").getLevel());
        assertEquals(1, ((ListNumberSpan) itemContaining("one")).getNumber());
        assertEquals(1, ((ListNumberSpan) itemContaining("two")).getNumber());
        assertEquals("the outer list carries on after the sublist",
                2, ((ListNumberSpan) itemContaining("three")).getNumber());
    }

    /**
     * Returns the deepest list item covering the given text.
     */
    private AreListSpan itemContaining(String needle) {
        Editable editable = editText.getText();
        int at = editable.toString().indexOf(needle);
        assertTrue("\"" + needle + "\" is not in the document: " + editable, at >= 0);

        AreListSpan deepest = null;
        for (AreListSpan candidate :
                editable.getSpans(at, at + needle.length(), AreListSpan.class)) {
            if (deepest == null || candidate.getLevel() > deepest.getLevel()) {
                deepest = candidate;
            }
        }
        assertNotNull("\"" + needle + "\" is not a list item", deepest);
        return deepest;
    }

    // ---------------------------------------------------------------- helpers

    private void buildList(String... items) {
        setTextAndCursor(items[0], items[0].length());
        click(numberButton);
        for (int i = 1; i < items.length; i++) {
            typeNewLineRaw();
            typeRaw(items[i]);
        }
    }

    private void putCursorOnParagraph(final int paragraph) {
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

    private AreListSpan itemOnParagraph(int paragraph) {
        Editable editable = editText.getText();
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
        assertNotNull("paragraph " + paragraph + " is not a list item", deepest);
        return deepest;
    }

    private int levelOfParagraph(int paragraph) {
        return itemOnParagraph(paragraph).getLevel();
    }

    private int numberOfParagraph(int paragraph) {
        AreListSpan item = itemOnParagraph(paragraph);
        assertTrue("paragraph " + paragraph + " is not numbered",
                item instanceof ListNumberSpan);
        return ((ListNumberSpan) item).getNumber();
    }
}
