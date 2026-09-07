package com.chinalwb.are;

import android.text.Editable;
import android.text.Layout;
import android.text.style.AlignmentSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.SubscriptSpan;
import android.text.style.SuperscriptSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.chinalwb.are.spans.AreAtSpan;
import com.chinalwb.are.spans.AreHrSpan;
import com.chinalwb.are.spans.AreQuoteSpan;
import com.chinalwb.are.spans.ListBulletSpan;
import com.chinalwb.are.spans.ListNumberSpan;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Saving a document as HTML and loading it again.
 *
 * <p>Two things have to hold. The formatting has to survive the trip, and the trip
 * has to be a fixed point: a document that is opened and saved without being
 * edited must come out byte for byte the same, however many times it goes round.
 * It used to grow a blank line on every cycle.</p>
 */
@RunWith(AndroidJUnit4.class)
public class HtmlRoundTripTest {

    /** name, html, the span types that must be present after parsing */
    private static final Object[][] SAMPLES = {
            {"plain", "<p>hello</p>", new Class[]{}},
            {"bold", "<p><b>hello</b> plain</p>", new Class[]{StyleSpan.class}},
            {"italic", "<p><i>hello</i></p>", new Class[]{StyleSpan.class}},
            {"underline", "<p><u>hello</u></p>", new Class[]{UnderlineSpan.class}},
            {"strikethrough", "<p><span style=\"text-decoration:line-through;\">x</span></p>",
                    new Class[]{StrikethroughSpan.class}},
            {"boldItalic", "<p><b><i>both</i></b></p>", new Class[]{StyleSpan.class}},
            {"textColour", "<p><span style=\"color:#FF5722;\">red</span></p>",
                    new Class[]{ForegroundColorSpan.class}},
            {"highlight", "<p><span style=\"background-color:#FFFF00;\">hl</span></p>",
                    new Class[]{BackgroundColorSpan.class}},
            {"link", "<p>see <a href=\"http://example.com\">site</a></p>",
                    new Class[]{URLSpan.class}},
            {"alignment", "<p style=\"text-align:center;\">mid</p>",
                    new Class[]{AlignmentSpan.class}},
            {"quote", "<blockquote><p>quoted</p></blockquote>",
                    new Class[]{AreQuoteSpan.class}},
            {"rule", "<p>a</p><hr /><p>b</p>", new Class[]{AreHrSpan.class}},
            {"superscript", "<p>x<sup>2</sup></p>", new Class[]{SuperscriptSpan.class}},
            {"subscript", "<p>x<sub>1</sub></p>", new Class[]{SubscriptSpan.class}},
            {"bulletList", "<ul><li>one</li><li>two</li></ul>",
                    new Class[]{ListBulletSpan.class}},
            {"numberedList", "<ol><li>one</li><li>two</li><li>three</li></ol>",
                    new Class[]{ListNumberSpan.class}},
            {"listThenText", "<ol><li>one</li></ol><p>after</p>",
                    new Class[]{ListNumberSpan.class}},
            {"twoLists", "<ol><li>a</li></ol><p>x</p><ol><li>b</li></ol>",
                    new Class[]{ListNumberSpan.class}},
            {"mention", "<p><a href=\"#\" ukey=\"7\" uname=\"Ann\" style=\"color:#FF00FF;\">@Ann</a> hi</p>",
                    new Class[]{AreAtSpan.class}},
            {"mixed", "<p><b>a</b></p><p><i>b</i></p><ul><li>c</li></ul>",
                    new Class[]{StyleSpan.class, ListBulletSpan.class}},
    };

    private ActivityScenario<TestHostActivity> scenario;

    @Before
    public void setUp() {
        scenario = ActivityScenario.launch(TestHostActivity.class);
    }

    @After
    public void tearDown() {
        if (scenario != null) {
            scenario.close();
        }
    }

    @Test
    public void formattingSurvivesTheRoundTrip() {
        for (Object[] sample : SAMPLES) {
            String name = (String) sample[0];
            String html = (String) sample[1];
            Class<?>[] expected = (Class<?>[]) sample[2];

            Editor first = load(html);
            for (Class<?> type : expected) {
                assertTrue(name + ": " + type.getSimpleName() + " missing after parsing",
                        first.spans(type).length > 0);
            }

            Editor second = load(first.html);
            assertEquals(name + ": the text itself changed", first.text, second.text);
            for (Class<?> type : expected) {
                assertTrue(name + ": " + type.getSimpleName() + " lost in the round trip",
                        second.spans(type).length > 0);
            }
        }
    }

    @Test
    public void savingAndLoadingIsAFixedPoint() {
        for (Object[] sample : SAMPLES) {
            String name = (String) sample[0];
            Editor first = load((String) sample[1]);
            Editor second = load(first.html);

            assertEquals(name + ": saving an unedited document changed it",
                    first.html, second.html);
        }
    }

    @Test
    public void repeatedSaveAndLoadDoesNotGrowTheDocument() {
        for (Object[] sample : SAMPLES) {
            String name = (String) sample[0];
            String html = load((String) sample[1]).html;

            for (int cycle = 0; cycle < 5; cycle++) {
                Editor next = load(html);
                assertEquals(name + ": document grew on cycle " + (cycle + 1)
                                + "\n  was: " + html + "\n  now: " + next.html,
                        html, next.html);
                html = next.html;
            }
        }
    }

    @Test
    public void exportedHtmlCarriesNoEditingMarkers() {
        for (Object[] sample : SAMPLES) {
            String name = (String) sample[0];
            String html = load((String) sample[1]).html;

            assertFalse(name + ": exported html contains a zero width space",
                    html.indexOf(Constants.ZERO_WIDTH_SPACE_INT) >= 0);
            assertFalse(name + ": exported html contains an escaped zero width space",
                    html.contains(Constants.ZERO_WIDTH_SPACE_STR_ESCAPE));
        }
    }

    @Test
    public void exportIsDeterministic() {
        for (Object[] sample : SAMPLES) {
            String name = (String) sample[0];
            assertEquals(name + ": the same document exported differently twice",
                    load((String) sample[1]).html, load((String) sample[1]).html);
        }
    }

    // ------------------------------------------------------- targeted checks

    @Test
    public void listNumbersSurviveTheRoundTrip() {
        Editor editor = load(load("<ol><li>one</li><li>two</li><li>three</li></ol>").html);

        ListNumberSpan[] items = editor.spans(ListNumberSpan.class);
        assertEquals(3, items.length);
        //
        // Numbering is assigned on parse, so it has to come back as 1, 2, 3 and not
        // as whatever order the spans happen to be handed back in.
        com.chinalwb.are.styles.ARE_ListNumbering.renumber(editor.editable);
        int[] numbers = new int[3];
        for (int paragraph = 0; paragraph < 3; paragraph++) {
            int start = Util.getParagraphStart(editor.editable, paragraph);
            int end = Util.getParagraphEnd(editor.editable, paragraph);
            if (end > start && editor.editable.charAt(end - 1) == '\n') {
                end--;
            }
            numbers[paragraph] =
                    editor.editable.getSpans(start, end, ListNumberSpan.class)[0].getNumber();
        }
        assertEquals(1, numbers[0]);
        assertEquals(2, numbers[1]);
        assertEquals(3, numbers[2]);
    }

    @Test
    public void twoListsSeparatedByTextStayTwoLists() {
        Editor editor = load(load("<ol><li>a</li></ol><p>x</p><ol><li>b</li></ol>").html);

        assertEquals(2, editor.spans(ListNumberSpan.class).length);
        assertTrue("the paragraph between the lists was lost",
                editor.text.contains("x"));
    }

    @Test
    public void linkTargetSurvivesTheRoundTrip() {
        Editor editor = load(load("<p><a href=\"http://example.com/a?b=c\">site</a></p>").html);

        URLSpan[] links = editor.spans(URLSpan.class);
        assertEquals(1, links.length);
        assertEquals("http://example.com/a?b=c", links[0].getURL());
    }

    @Test
    public void mentionKeepsItsAttributes() {
        String html = load("<p><a href=\"#\" ukey=\"7\" uname=\"Ann\" "
                + "style=\"color:#FF00FF;\">@Ann</a> hi</p>").html;

        String lower = html.toLowerCase();
        assertTrue("the mention key was dropped: " + html, lower.contains("ukey=\"7\""));
        assertTrue("the mention name was dropped: " + html, lower.contains("uname=\"ann\""));
    }

    @Test
    public void colourValuesSurviveTheRoundTrip() {
        Editor editor = load(load("<p><span style=\"color:#FF5722;\">red</span></p>").html);

        ForegroundColorSpan[] colours = editor.spans(ForegroundColorSpan.class);
        assertEquals(1, colours.length);
        assertEquals(0xFFFF5722, colours[0].getForegroundColor());
    }

    @Test
    public void alignmentSurvivesTheRoundTrip() {
        Editor editor = load(load("<p style=\"text-align:center;\">mid</p>").html);

        AlignmentSpan[] alignments = editor.spans(AlignmentSpan.class);
        assertEquals(1, alignments.length);
        assertEquals(Layout.Alignment.ALIGN_CENTER, alignments[0].getAlignment());
    }

    @Test
    public void ruleIsNotWrappedInAParagraph() {
        String html = load("<p>a</p><hr /><p>b</p>").html;

        //
        // <hr> is a block element. Wrapping it in a <p> is invalid html, and the
        // parser used to restructure it and leave a stray space behind.
        assertFalse("the rule is wrapped in a paragraph: " + html,
                html.contains("<p><hr"));
        assertTrue("the rule was lost: " + html, html.contains("<hr"));
    }

    @Test
    public void anEmptyDocumentStaysEmpty() {
        Editor editor = load("");

        assertEquals("", editor.text);
        assertEquals(load(editor.html).html, editor.html);
    }

    // --------------------------------------------------------------- harness

    /** One load of a document, and what came out of it. */
    private static class Editor {
        Editable editable;
        String text;
        String html;

        <T> T[] spans(Class<T> type) {
            return editable.getSpans(0, editable.length(), type);
        }
    }

    private Editor load(final String html) {
        final Editor result = new Editor();
        scenario.onActivity(new ActivityScenario.ActivityAction<TestHostActivity>() {
            @Override
            public void perform(TestHostActivity activity) {
                AREditText editor = new AREditText(activity);
                activity.getContent().addView(editor);
                editor.fromHtml(html);
                result.editable = editor.getText();
                result.text = editor.getText().toString();
                result.html = editor.getHtml();
            }
        });
        return result;
    }
}
