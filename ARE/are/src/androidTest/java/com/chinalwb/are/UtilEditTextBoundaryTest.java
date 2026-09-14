package com.chinalwb.are;

import android.content.Context;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.Selection;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * The paragraph boundaries a block style gets for a real {@link EditText}.
 *
 * <p>These used to be read off the {@link android.text.Layout}, which describes
 * <i>visual</i> lines: a wrapped paragraph reported the wrap point as its start
 * and end, so a bullet or a quote only covered the part of the line that happened
 * to fit on screen.</p>
 */
@RunWith(AndroidJUnit4.class)
public class UtilEditTextBoundaryTest {

    @Test
    public void boundariesCoverTheWholeParagraphWhenTheLineWraps() {
        final String longLine =
                "a very long first paragraph that is far too wide to fit on one single line";
        final String text = longLine + "\nsecond";

        EditText editText = createLaidOutEditText(text, 120);

        assertNotNull("the test needs a laid out EditText", editText.getLayout());
        assertTrue("the first paragraph has to wrap for this test to mean anything",
                editText.getLayout().getLineCount() > 2);

        //
        // Cursor in the middle of the wrapped first paragraph.
        Selection.setSelection(editText.getText(), longLine.length() - 5);
        int line = Util.getCurrentCursorLine(editText);

        assertEquals("the cursor is in the first paragraph", 0, line);
        assertEquals(0, Util.getThisLineStart(editText, line));
        assertEquals("the paragraph ends after its '\\n', not at the wrap point",
                longLine.length() + 1, Util.getThisLineEnd(editText, line));
    }

    @Test
    public void boundariesAreAvailableBeforeTheFirstLayoutPass() {
        //
        // Right after an edit the layout has not been rebuilt yet, and the list
        // styles ask for the boundaries in exactly that moment.
        EditText editText = createEditText("AAA\nBBB");
        Selection.setSelection(editText.getText(), 5);

        int line = Util.getCurrentCursorLine(editText);

        assertEquals(1, line);
        assertEquals(4, Util.getThisLineStart(editText, line));
        assertEquals(7, Util.getThisLineEnd(editText, line));
    }

    @Test
    public void boundariesFollowTheTextImmediatelyAfterAnInsert() {
        EditText editText = createLaidOutEditText("AAA\nBBB", 400);
        Selection.setSelection(editText.getText(), 4);

        //
        // The layout still describes the text as it was before this insert.
        editText.getText().insert(4, "​");

        int line = Util.getCurrentCursorLine(editText);
        assertEquals(1, line);
        assertEquals(4, Util.getThisLineStart(editText, line));
        assertEquals(8, Util.getThisLineEnd(editText, line));
    }

    @Test
    public void cursorLineIsMinusOneWhenThereIsNoCursor() {
        //
        // EditText#setText places the cursor at 0, so an Editable that was never
        // given a selection is what the "no cursor" case really looks like.
        Editable editable = new SpannableStringBuilder("AAA");
        assertEquals(-1, Selection.getSelectionStart(editable));

        assertEquals(-1, Util.getParagraphIndex(editable, Selection.getSelectionStart(editable)));
        assertEquals(-1, Util.getCurrentCursorLine(null));
    }

    @Test
    public void boundariesAreSafeWhenThereIsNoCursorLine() {
        EditText editText = createEditText("AAA\nBBB");

        assertEquals(0, Util.getThisLineStart(editText, -1));
        assertEquals(-1, Util.getThisLineEnd(editText, -1));
        assertEquals(0, Util.getThisLineStart(null, 0));
        assertEquals(-1, Util.getThisLineEnd(null, 0));
    }

    @Test
    public void removeZeroWidthMarker_deletesOnlyAMarker() {
        EditText editText = createEditText("​AAA\nBBB");

        assertTrue(Util.removeZeroWidthMarker(editText.getText(), 0));
        assertEquals("AAA\nBBB", editText.getText().toString());

        assertTrue("a plain character must be left alone",
                !Util.removeZeroWidthMarker(editText.getText(), 0));
        assertEquals("AAA\nBBB", editText.getText().toString());
    }

    @Test
    public void removeZeroWidthMarker_toleratesOffsetsOutsideTheText() {
        EditText editText = createEditText("AAA");

        assertTrue(!Util.removeZeroWidthMarker(editText.getText(), 99));
        assertTrue(!Util.removeZeroWidthMarker(editText.getText(), -1));
        assertEquals("AAA", editText.getText().toString());
    }

    // ---------------------------------------------------------------- helpers

    private static EditText createEditText(final String text) {
        final EditText[] holder = new EditText[1];
        InstrumentationRegistry.getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                Context context =
                        InstrumentationRegistry.getInstrumentation().getTargetContext();
                EditText editText = new EditText(context);
                //
                // TextView#checkForResize reads its layout params on every text
                // change, so an unparented EditText still needs them.
                editText.setLayoutParams(new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
                editText.setText(text);
                holder[0] = editText;
            }
        });
        return holder[0];
    }

    private static EditText createLaidOutEditText(final String text, final int widthPx) {
        final EditText editText = createEditText(text);
        InstrumentationRegistry.getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                editText.measure(
                        View.MeasureSpec.makeMeasureSpec(widthPx, View.MeasureSpec.EXACTLY),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                editText.layout(0, 0, editText.getMeasuredWidth(), editText.getMeasuredHeight());
            }
        });
        return editText;
    }
}
