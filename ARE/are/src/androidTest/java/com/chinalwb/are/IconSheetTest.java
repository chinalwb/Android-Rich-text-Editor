package com.chinalwb.are;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.FileOutputStream;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Draws every toolbar icon into one sheet so the set can be looked at, and fails
 * if any of them cannot be inflated.
 */
@RunWith(AndroidJUnit4.class)
public class IconSheetTest {

    private static final String[] ICONS = {
            "are_ic_bold", "are_ic_italic", "are_ic_underline", "are_ic_strikethrough",
            "are_ic_fontsize", "are_ic_foregroundcolor", "are_ic_background",
            "are_ic_fontface", "are_ic_superscript", "are_ic_subscript",
            "are_ic_quote", "are_ic_at", "are_ic_listbullet", "are_ic_listnumber",
            "are_ic_alignleft", "are_ic_aligncenter", "are_ic_alignright",
            "are_ic_indentleft", "are_ic_indentright", "are_ic_hr",
            "are_ic_image", "are_ic_video", "are_ic_emoji",
            "are_ic_link", "are_ic_unlink",
    };

    @Test
    public void everyIconInflatesAndIsDrawn() throws Exception {
        final File[] written = new File[1];
        ActivityScenario<TestHostActivity> scenario =
                ActivityScenario.launch(TestHostActivity.class);
        try {
            scenario.onActivity(new ActivityScenario.ActivityAction<TestHostActivity>() {
                @Override
                public void perform(TestHostActivity activity) {
                    int cell = 96;
                    int cols = 5;
                    int rows = (ICONS.length + cols - 1) / cols;
                    int label = 22;
                    Bitmap sheet = Bitmap.createBitmap(cols * cell,
                            rows * (cell + label), Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(sheet);
                    canvas.drawColor(Color.WHITE);

                    Paint text = new Paint(Paint.ANTI_ALIAS_FLAG);
                    text.setColor(Color.GRAY);
                    text.setTextSize(13);
                    text.setTextAlign(Paint.Align.CENTER);

                    for (int i = 0; i < ICONS.length; i++) {
                        int id = activity.getResources().getIdentifier(
                                ICONS[i], "drawable", activity.getPackageName());
                        assertTrue("missing drawable " + ICONS[i], id != 0);
                        Drawable drawable = ContextCompat.getDrawable(activity, id);
                        assertNotNull("could not inflate " + ICONS[i], drawable);

                        int col = i % cols;
                        int row = i / cols;
                        int left = col * cell;
                        int top = row * (cell + label);
                        int pad = 12;
                        drawable.setBounds(left + pad, top + pad,
                                left + cell - pad, top + cell - pad);
                        drawable.setTint(Color.parseColor("#FF202124"));
                        drawable.draw(canvas);
                        canvas.drawText(ICONS[i].replace("are_ic_", ""),
                                left + cell / 2f, top + cell + 14, text);
                    }

                    try {
                        File out = new File(activity.getExternalFilesDir(null),
                                "icon_sheet.png");
                        FileOutputStream stream = new FileOutputStream(out);
                        sheet.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        stream.close();
                        written[0] = out;
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        } finally {
            scenario.close();
        }
        assertTrue("sheet not written", written[0] != null && written[0].exists());
    }
}
