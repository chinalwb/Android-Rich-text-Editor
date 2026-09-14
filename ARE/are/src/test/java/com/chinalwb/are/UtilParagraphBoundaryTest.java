package com.chinalwb.are;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * The paragraph boundaries every block level style (list / quote / alignment /
 * indent) is built on.
 */
public class UtilParagraphBoundaryTest {

    @Test
    public void paragraphIndex_countsNewLinesBeforeTheOffset() {
        String text = "AAA\nBBB\nCCC";

        assertEquals(0, Util.getParagraphIndex(text, 0));
        assertEquals(0, Util.getParagraphIndex(text, 3));
        // The '\n' itself still belongs to the paragraph it closes.
        assertEquals(1, Util.getParagraphIndex(text, 4));
        assertEquals(1, Util.getParagraphIndex(text, 7));
        assertEquals(2, Util.getParagraphIndex(text, 8));
        assertEquals(2, Util.getParagraphIndex(text, text.length()));
    }

    @Test
    public void paragraphIndex_clampsOffsetsOutsideTheText() {
        String text = "AAA\nBBB";

        assertEquals(1, Util.getParagraphIndex(text, 999));
        assertEquals(-1, Util.getParagraphIndex(text, -1));
        assertEquals(-1, Util.getParagraphIndex(null, 0));
    }

    @Test
    public void paragraphStart_returnsOffsetAfterThePrecedingNewLine() {
        String text = "AAA\nBBB\nCCC";

        assertEquals(0, Util.getParagraphStart(text, 0));
        assertEquals(4, Util.getParagraphStart(text, 1));
        assertEquals(8, Util.getParagraphStart(text, 2));
    }

    @Test
    public void paragraphEnd_includesTheTrailingNewLine() {
        String text = "AAA\nBBB\nCCC";

        // Consistent with Layout#getLineEnd: the '\n' is part of the paragraph.
        assertEquals(4, Util.getParagraphEnd(text, 0));
        assertEquals(8, Util.getParagraphEnd(text, 1));
        // The last paragraph has no '\n' to include.
        assertEquals(11, Util.getParagraphEnd(text, 2));
    }

    @Test
    public void paragraphBoundaries_handleEmptyParagraphs() {
        String text = "AAA\n\nCCC";

        assertEquals(4, Util.getParagraphStart(text, 1));
        assertEquals(5, Util.getParagraphEnd(text, 1));
        assertEquals(5, Util.getParagraphStart(text, 2));
        assertEquals(8, Util.getParagraphEnd(text, 2));
    }

    @Test
    public void paragraphBoundaries_handleEmptyText() {
        assertEquals(0, Util.getParagraphStart("", 0));
        assertEquals(0, Util.getParagraphEnd("", 0));
        assertEquals(0, Util.getParagraphIndex("", 0));
    }

    @Test
    public void paragraphBoundaries_areClampedForIndexesPastTheEnd() {
        String text = "AAA\nBBB";

        assertEquals(text.length(), Util.getParagraphStart(text, 9));
        assertEquals(text.length(), Util.getParagraphEnd(text, 9));
    }

    @Test
    public void paragraphStart_ofNegativeIndexIsTheStartOfTheText() {
        String text = "AAA\nBBB";

        // Callers pass -1 when there is no cursor.
        assertEquals(0, Util.getParagraphStart(text, -1));
    }

    @Test
    public void paragraphCount_ignoresTheTrailingNewLine() {
        assertEquals(1, Util.getParagraphCount("AAA"));
        assertEquals(2, Util.getParagraphCount("AAA\nBBB"));
        // "AAA\n" is one paragraph plus the empty line the cursor sits on.
        assertEquals(1, Util.getParagraphCount("AAA\n"));
        assertEquals(3, Util.getParagraphCount("AAA\nBBB\nCCC"));
    }

    @Test
    public void boundariesRoundTrip_forEveryOffsetOfAText() {
        String text = "first\nsecond line\n\nfourth";

        for (int offset = 0; offset <= text.length(); offset++) {
            int paragraph = Util.getParagraphIndex(text, offset);
            int start = Util.getParagraphStart(text, paragraph);
            int end = Util.getParagraphEnd(text, paragraph);

            assertEquals("paragraph of offset " + offset + " must contain it",
                    true, start <= offset && offset <= end);
        }
    }
}
