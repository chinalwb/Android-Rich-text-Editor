package com.chinalwb.are.styles;

import android.text.Editable;
import android.text.Layout;
import android.text.Selection;
import android.view.View;
import android.widget.ImageView;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.chinalwb.are.AREditText;
import com.chinalwb.are.TestHostActivity;
import com.chinalwb.are.Util;
import com.chinalwb.are.spans.AreQuoteSpan;
import com.chinalwb.are.styles.toolitems.styles.ARE_Style_Alignment;
import com.chinalwb.are.styles.toolitems.styles.ARE_Style_Quote;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Quote and alignment are applied per paragraph, including when the paragraph is
 * too long to fit on one visual line.
 */
@RunWith(AndroidJUnit4.class)
public class BlockStyleBoundaryTest {

    private static final String LONG_PARAGRAPH =
            "a very long paragraph that certainly does not fit onto a single line of a "
                    + "narrow editor and therefore wraps several times over";

    @Test
    public void quoteCoversTheWholeWrappedParagraph() {
        final Editable[] result = new Editable[1];
        withEditor(LONG_PARAGRAPH + "\nsecond", new EditorAction() {
            @Override
            public void run(TestHostActivity activity, AREditText editText) {
                ImageView button = new ImageView(activity);
                ARE_Style_Quote quote = new ARE_Style_Quote(editText, button, null);

                assertTrue("the paragraph has to wrap for this test to mean anything",
                        editText.getLayout().getLineCount() > 2);

                Selection.setSelection(editText.getText(), 5);
                button.performClick();
                result[0] = editText.getText();
            }
        });

        AreQuoteSpan[] spans =
                result[0].getSpans(0, result[0].length(), AreQuoteSpan.class);
        assertEquals(1, spans.length);
        assertEquals(0, result[0].getSpanStart(spans[0]));
        assertTrue("the quote must reach the end of the paragraph, not the wrap point: "
                        + result[0].getSpanEnd(spans[0]),
                result[0].getSpanEnd(spans[0]) >= LONG_PARAGRAPH.length());
    }

    @Test
    public void alignmentCoversTheWholeWrappedParagraph() {
        final Editable[] result = new Editable[1];
        withEditor(LONG_PARAGRAPH + "\nsecond", new EditorAction() {
            @Override
            public void run(TestHostActivity activity, AREditText editText) {
                ImageView button = new ImageView(activity);
                ARE_Style_Alignment alignment = new ARE_Style_Alignment(
                        editText, button, Layout.Alignment.ALIGN_CENTER);

                Selection.setSelection(editText.getText(), 5);
                button.performClick();
                result[0] = editText.getText();
            }
        });

        android.text.style.AlignmentSpan[] spans =
                result[0].getSpans(0, result[0].length(),
                        android.text.style.AlignmentSpan.class);
        assertEquals(1, spans.length);
        assertTrue("the alignment must reach the end of the paragraph: "
                        + result[0].getSpanEnd(spans[0]),
                result[0].getSpanEnd(spans[0]) >= LONG_PARAGRAPH.length());
    }

    @Test
    public void quoteOnTheSecondParagraphLeavesTheFirstAlone() {
        final Editable[] result = new Editable[1];
        final String text = "first\n" + LONG_PARAGRAPH;
        withEditor(text, new EditorAction() {
            @Override
            public void run(TestHostActivity activity, AREditText editText) {
                ImageView button = new ImageView(activity);
                new ARE_Style_Quote(editText, button, null);

                Selection.setSelection(editText.getText(), text.length() - 3);
                button.performClick();
                result[0] = editText.getText();
            }
        });

        AreQuoteSpan[] spans =
                result[0].getSpans(0, result[0].length(), AreQuoteSpan.class);
        assertEquals(1, spans.length);
        assertEquals("the quote starts at the second paragraph",
                Util.getParagraphStart(result[0], 1), result[0].getSpanStart(spans[0]));
    }

    // ---------------------------------------------------------------- helpers

    private interface EditorAction {
        void run(TestHostActivity activity, AREditText editText);
    }

    private static void withEditor(final String text, final EditorAction action) {
        ActivityScenario<TestHostActivity> scenario =
                ActivityScenario.launch(TestHostActivity.class);
        try {
            scenario.onActivity(new ActivityScenario.ActivityAction<TestHostActivity>() {
                @Override
                public void perform(TestHostActivity activity) {
                    AREditText editText = new AREditText(activity);
                    activity.getContent().addView(editText);
                    editText.setText(text);
                    editText.measure(
                            View.MeasureSpec.makeMeasureSpec(400, View.MeasureSpec.EXACTLY),
                            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                    editText.layout(0, 0, editText.getMeasuredWidth(),
                            editText.getMeasuredHeight());
                    action.run(activity, editText);
                }
            });
        } finally {
            scenario.close();
        }
    }
}
