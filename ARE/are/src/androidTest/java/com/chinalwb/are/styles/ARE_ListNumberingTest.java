package com.chinalwb.are.styles;

import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.chinalwb.are.spans.ListBulletSpan;
import com.chinalwb.are.spans.ListNumberSpan;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Numbering of ordered list items.
 */
@RunWith(AndroidJUnit4.class)
public class ARE_ListNumberingTest {

    private static final String ZWSP = "​";

    @Test
    public void renumber_numbersAFlatListFromOne() {
        Editable editable = new SpannableStringBuilder(
                ZWSP + "A\n" + ZWSP + "B\n" + ZWSP + "C");
        markAsNumbered(editable, 0);
        markAsNumbered(editable, 1);
        markAsNumbered(editable, 2);

        ARE_ListNumbering.renumber(editable);

        assertEquals(1, numberOfParagraph(editable, 0));
        assertEquals(2, numberOfParagraph(editable, 1));
        assertEquals(3, numberOfParagraph(editable, 2));
    }

    @Test
    public void renumber_restartsAfterAPlainParagraph() {
        Editable editable = new SpannableStringBuilder(
                ZWSP + "A\n" + ZWSP + "B\nplain\n" + ZWSP + "C\n" + ZWSP + "D");
        markAsNumbered(editable, 0);
        markAsNumbered(editable, 1);
        markAsNumbered(editable, 3);
        markAsNumbered(editable, 4);

        ARE_ListNumbering.renumber(editable);

        assertEquals(1, numberOfParagraph(editable, 0));
        assertEquals(2, numberOfParagraph(editable, 1));
        assertEquals(1, numberOfParagraph(editable, 3));
        assertEquals(2, numberOfParagraph(editable, 4));
    }

    @Test
    public void renumber_restartsAfterABulletItem() {
        Editable editable = new SpannableStringBuilder(
                ZWSP + "A\n" + ZWSP + "B\n" + ZWSP + "C");
        markAsNumbered(editable, 0);
        markAsBullet(editable, 1);
        markAsNumbered(editable, 2);

        ARE_ListNumbering.renumber(editable);

        assertEquals(1, numberOfParagraph(editable, 0));
        assertEquals(1, numberOfParagraph(editable, 2));
    }

    @Test
    public void renumber_isCorrectWhenSpansWereNotAddedInDocumentOrder() {
        Editable editable = new SpannableStringBuilder(
                ZWSP + "A\n" + ZWSP + "B\n" + ZWSP + "C");
        //
        // Editable#getSpans hands spans back in insertion order, not in document
        // order, so adding them backwards used to produce 3, 2, 1.
        markAsNumbered(editable, 2);
        markAsNumbered(editable, 1);
        markAsNumbered(editable, 0);

        ARE_ListNumbering.renumber(editable);

        assertEquals(1, numberOfParagraph(editable, 0));
        assertEquals(2, numberOfParagraph(editable, 1));
        assertEquals(3, numberOfParagraph(editable, 2));
    }

    @Test
    public void renumber_repairsNumbersThatHadAlreadyDrifted() {
        Editable editable = new SpannableStringBuilder(
                ZWSP + "A\n" + ZWSP + "B\n" + ZWSP + "C");
        setSpanOnParagraph(editable, 0, new ListNumberSpan(7));
        setSpanOnParagraph(editable, 1, new ListNumberSpan(7));
        setSpanOnParagraph(editable, 2, new ListNumberSpan(2));

        ARE_ListNumbering.renumber(editable);

        assertEquals(1, numberOfParagraph(editable, 0));
        assertEquals(2, numberOfParagraph(editable, 1));
        assertEquals(3, numberOfParagraph(editable, 2));
    }

    @Test
    public void renumber_dropsASecondSpanOnTheSameParagraph() {
        Editable editable = new SpannableStringBuilder(ZWSP + "A\n" + ZWSP + "B");
        markAsNumbered(editable, 0);
        markAsNumbered(editable, 0);
        markAsNumbered(editable, 1);

        ARE_ListNumbering.renumber(editable);

        ListNumberSpan[] onFirst = editable.getSpans(0, 2, ListNumberSpan.class);
        assertEquals("a paragraph must not draw two numbers", 1, onFirst.length);
        assertEquals(1, onFirst[0].getNumber());
        assertEquals(2, numberOfParagraph(editable, 1));
    }

    @Test
    public void renumber_toleratesTextWithoutAnyListSpan() {
        Editable editable = new SpannableStringBuilder("A\nB");

        ARE_ListNumbering.renumber(editable);

        assertEquals(0, editable.getSpans(0, editable.length(), ListNumberSpan.class).length);
    }

    @Test
    public void getNextNumber_continuesTheListAbove() {
        Editable editable = new SpannableStringBuilder(
                ZWSP + "A\n" + ZWSP + "B\n" + "new line");
        markAsNumbered(editable, 0);
        markAsNumbered(editable, 1);
        ARE_ListNumbering.renumber(editable);

        int thirdParagraphStart = editable.toString().lastIndexOf('\n') + 1;
        assertEquals(3, ARE_ListNumbering.getNextNumber(editable, thirdParagraphStart));
    }

    @Test
    public void getNextNumber_startsAtOneWhenTheParagraphAboveIsNotAListItem() {
        Editable editable = new SpannableStringBuilder("plain\nnew line");

        int secondParagraphStart = editable.toString().indexOf('\n') + 1;
        assertEquals(1, ARE_ListNumbering.getNextNumber(editable, secondParagraphStart));
    }

    @Test
    public void getNextNumber_startsAtOneAtTheStartOfTheText() {
        Editable editable = new SpannableStringBuilder("anything");

        assertEquals(1, ARE_ListNumbering.getNextNumber(editable, 0));
    }


    @Test
    public void renumber_keepsTheOuterListCountingAcrossASublist() {
        //
        // 1. a
        //    1. a1
        //    2. a2
        // 2. b
        //
        // The span of an item that has a sublist covers that sublist, which is how
        // the nesting is recognised. The outer list has to carry on counting after
        // the sublist instead of restarting at 1.
        Editable editable = new SpannableStringBuilder(
                ZWSP + "a\n\n" + ZWSP + "a1\n" + ZWSP + "a2\n\n" + ZWSP + "b\n\n");

        ListNumberSpan outerOne = new ListNumberSpan(1);
        ListNumberSpan innerOne = new ListNumberSpan(1);
        ListNumberSpan innerTwo = new ListNumberSpan(1);
        ListNumberSpan outerTwo = new ListNumberSpan(1);
        editable.setSpan(outerOne, 0, 11, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        editable.setSpan(innerOne, 4, 7, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        editable.setSpan(innerTwo, 8, 11, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        editable.setSpan(outerTwo, 13, 15, Spanned.SPAN_INCLUSIVE_INCLUSIVE);

        ARE_ListNumbering.renumber(editable);

        assertEquals("outer first item", 1, outerOne.getNumber());
        assertEquals("sublist first item", 1, innerOne.getNumber());
        assertEquals("sublist second item", 2, innerTwo.getNumber());
        assertEquals("the outer list must not restart after the sublist",
                2, outerTwo.getNumber());
    }

    @Test
    public void renumber_numbersEachNestingLevelOnItsOwn() {
        //
        // 1. a
        //    1. b
        //       1. c
        // 2. d
        Editable editable = new SpannableStringBuilder(
                ZWSP + "a\n\n" + ZWSP + "b\n\n" + ZWSP + "c\n\n" + ZWSP + "d\n\n");

        ListNumberSpan deepest = new ListNumberSpan(1);
        ListNumberSpan middle = new ListNumberSpan(1);
        ListNumberSpan outer = new ListNumberSpan(1);
        ListNumberSpan sibling = new ListNumberSpan(1);
        editable.setSpan(deepest, 8, 10, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        editable.setSpan(middle, 4, 10, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        editable.setSpan(outer, 0, 10, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        editable.setSpan(sibling, 12, 14, Spanned.SPAN_INCLUSIVE_INCLUSIVE);

        ARE_ListNumbering.renumber(editable);

        assertEquals(1, outer.getNumber());
        assertEquals(1, middle.getNumber());
        assertEquals(1, deepest.getNumber());
        assertEquals("the outermost list carries on past three levels of nesting",
                2, sibling.getNumber());
    }

    @Test
    public void renumber_stillRestartsWhenRealContentInterrupts() {
        //
        // A blank line does not break a list - a sublist leaves one behind - but a
        // paragraph with content in it does.
        Editable editable = new SpannableStringBuilder(
                ZWSP + "a\n\nplain\n\n" + ZWSP + "b");

        ListNumberSpan first = new ListNumberSpan(1);
        ListNumberSpan second = new ListNumberSpan(1);
        editable.setSpan(first, 0, 2, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        editable.setSpan(second, 10, 12, Spanned.SPAN_INCLUSIVE_INCLUSIVE);

        ARE_ListNumbering.renumber(editable);

        assertEquals(1, first.getNumber());
        assertEquals(1, second.getNumber());
    }

    // ---------------------------------------------------------------- helpers

    private static void markAsNumbered(Editable editable, int paragraph) {
        setSpanOnParagraph(editable, paragraph, new ListNumberSpan(1));
    }

    private static void markAsBullet(Editable editable, int paragraph) {
        setSpanOnParagraph(editable, paragraph, new ListBulletSpan());
    }

    private static void setSpanOnParagraph(Editable editable, int paragraph, Object span) {
        int start = com.chinalwb.are.Util.getParagraphStart(editable, paragraph);
        int end = com.chinalwb.are.Util.getParagraphEnd(editable, paragraph);
        if (end > start && editable.charAt(end - 1) == '\n') {
            end--;
        }
        editable.setSpan(span, start, end, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
    }

    private static int numberOfParagraph(Editable editable, int paragraph) {
        int start = com.chinalwb.are.Util.getParagraphStart(editable, paragraph);
        int end = com.chinalwb.are.Util.getParagraphEnd(editable, paragraph);
        if (end > start && editable.charAt(end - 1) == '\n') {
            end--;
        }
        ListNumberSpan[] spans = editable.getSpans(start, end, ListNumberSpan.class);
        assertTrue("paragraph " + paragraph + " has no number span", spans.length > 0);
        return spans[0].getNumber();
    }
}
