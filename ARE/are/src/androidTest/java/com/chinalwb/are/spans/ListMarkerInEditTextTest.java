package com.chinalwb.are.spans;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.Spanned;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Where list markers land once a real {@link EditText} lays the text out - the x
 * the framework hands to a leading margin span is not necessarily 0.
 */
@RunWith(AndroidJUnit4.class)
public class ListMarkerInEditTextTest {

    private static class RecordingCanvas extends Canvas {
        final List<String> texts = new ArrayList<>();
        final List<Float> xs = new ArrayList<>();

        RecordingCanvas(Bitmap bitmap) {
            super(bitmap);
        }

        @Override
        public void drawText(String text, float x, float y, Paint paint) {
            texts.add(text);
            xs.add(x);
            super.drawText(text, x, y, paint);
        }
    }

    @Test
    public void everyMarkerOfALongListIsDrawnInsideTheView() {
        StringBuilder content = new StringBuilder();
        for (int i = 1; i <= 12; i++) {
            if (i > 1) {
                content.append('\n');
            }
            content.append('​').append("item ").append(i);
        }

        final EditText editText = createEditText(content.toString(), 24f);
        InstrumentationRegistry.getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                for (int paragraph = 0; paragraph < 12; paragraph++) {
                    int start = com.chinalwb.are.Util.getParagraphStart(
                            editText.getText(), paragraph);
                    int end = com.chinalwb.are.Util.getParagraphEnd(
                            editText.getText(), paragraph);
                    if (end > start && editText.getText().charAt(end - 1) == '\n') {
                        end--;
                    }
                    editText.getText().setSpan(new ListNumberSpan(paragraph + 1),
                            start, end, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                }
                measureAndLayout(editText, 1080);
            }
        });

        RecordingCanvas canvas = drawToCanvas(editText);

        assertTrue("no marker was drawn", canvas.texts.size() >= 12);
        for (int i = 0; i < canvas.texts.size(); i++) {
            String marker = canvas.texts.get(i);
            float x = canvas.xs.get(i);
            assertTrue("marker '" + marker + "' is drawn off the left edge at " + x,
                    x >= 0);
        }
        assertEquals("1.", canvas.texts.get(0));
        assertEquals("12.", canvas.texts.get(11));
    }


    @Test
    public void markersFitInsideARealAREditText() {
        //
        // AREditText picks its own text size and padding; the markers have to fit
        // at exactly those defaults, which is what the editor really renders.
        StringBuilder content = new StringBuilder();
        for (int i = 1; i <= 12; i++) {
            if (i > 1) {
                content.append('\n');
            }
            content.append('\u200B').append("item ").append(i);
        }

        final com.chinalwb.are.AREditText[] holder = new com.chinalwb.are.AREditText[1];
        final androidx.test.core.app.ActivityScenario<com.chinalwb.are.TestHostActivity> scenario =
                androidx.test.core.app.ActivityScenario.launch(
                        com.chinalwb.are.TestHostActivity.class);
        try {
            final String text = content.toString();
            scenario.onActivity(
                    new androidx.test.core.app.ActivityScenario.ActivityAction<com.chinalwb.are.TestHostActivity>() {
                @Override
                public void perform(com.chinalwb.are.TestHostActivity activity) {
                    com.chinalwb.are.AREditText editText =
                            new com.chinalwb.are.AREditText(activity);
                    activity.getContent().addView(editText);
                    editText.setText(text);
                    for (int paragraph = 0; paragraph < 12; paragraph++) {
                        int start = com.chinalwb.are.Util.getParagraphStart(
                                editText.getText(), paragraph);
                        int end = com.chinalwb.are.Util.getParagraphEnd(
                                editText.getText(), paragraph);
                        if (end > start && editText.getText().charAt(end - 1) == '\n') {
                            end--;
                        }
                        editText.getText().setSpan(new ListNumberSpan(paragraph + 1),
                                start, end, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                    }
                    measureAndLayout(editText, 1080);
                    holder[0] = editText;
                }
            });

            RecordingCanvas canvas = drawToCanvas(holder[0]);

            assertTrue("no marker was drawn", canvas.texts.size() >= 12);
            for (int i = 0; i < canvas.texts.size(); i++) {
                assertTrue("marker '" + canvas.texts.get(i)
                                + "' is drawn off the left edge at " + canvas.xs.get(i),
                        canvas.xs.get(i) >= 0);
            }
        } finally {
            scenario.close();
        }
    }

    // ---------------------------------------------------------------- helpers

    private static EditText createEditText(final String text, final float textSizeSp) {
        final EditText[] holder = new EditText[1];
        InstrumentationRegistry.getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                Context context =
                        InstrumentationRegistry.getInstrumentation().getTargetContext();
                EditText editText = new EditText(context);
                editText.setLayoutParams(new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
                editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSizeSp);
                editText.setPadding(0, 0, 0, 0);
                editText.setText(text);
                holder[0] = editText;
            }
        });
        return holder[0];
    }

    private static void measureAndLayout(EditText editText, int widthPx) {
        editText.measure(
                View.MeasureSpec.makeMeasureSpec(widthPx, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        editText.layout(0, 0, editText.getMeasuredWidth(), editText.getMeasuredHeight());
    }

    private static RecordingCanvas drawToCanvas(final EditText editText) {
        final RecordingCanvas[] holder = new RecordingCanvas[1];
        InstrumentationRegistry.getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = Bitmap.createBitmap(
                        Math.max(1, editText.getWidth()),
                        Math.max(1, editText.getHeight()),
                        Bitmap.Config.ARGB_8888);
                RecordingCanvas canvas = new RecordingCanvas(bitmap);
                editText.draw(canvas);
                holder[0] = canvas;
            }
        });
        return holder[0];
    }
}
