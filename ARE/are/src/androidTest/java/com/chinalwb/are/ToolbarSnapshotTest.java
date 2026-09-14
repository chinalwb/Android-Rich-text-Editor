package com.chinalwb.are;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.chinalwb.are.styles.toolbar.ARE_ToolbarDefault;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_AlignmentCenter;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_AlignmentLeft;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_AlignmentRight;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_BackgroundColor;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_Bold;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_FontColor;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_FontSize;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_Hr;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_Image;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_Italic;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_Link;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_ListBullet;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_ListNumber;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_Quote;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_Strikethrough;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_Subscript;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_Superscript;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_Underline;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_Video;
import com.chinalwb.are.styles.toolitems.IARE_ToolItem;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.FileOutputStream;

import static org.junit.Assert.assertTrue;

/** Renders the composable toolbar so the icon set can be judged in place. */
@RunWith(AndroidJUnit4.class)
public class ToolbarSnapshotTest {

    @Test
    public void renderToolbar() {
        final File[] written = new File[1];
        ActivityScenario<TestHostActivity> scenario =
                ActivityScenario.launch(TestHostActivity.class);
        try {
            scenario.onActivity(new ActivityScenario.ActivityAction<TestHostActivity>() {
                @Override
                public void perform(TestHostActivity activity) {
                    AREditText editText = new AREditText(activity);
                    ARE_ToolbarDefault toolbar = new ARE_ToolbarDefault(activity);
                    activity.getContent().addView(toolbar);

                    IARE_ToolItem[] items = {
                            new ARE_ToolItem_Bold(), new ARE_ToolItem_Italic(),
                            new ARE_ToolItem_Underline(), new ARE_ToolItem_Strikethrough(),
                            new ARE_ToolItem_FontSize(), new ARE_ToolItem_FontColor(),
                            new ARE_ToolItem_BackgroundColor(), new ARE_ToolItem_Quote(),
                            new ARE_ToolItem_ListNumber(), new ARE_ToolItem_ListBullet(),
                            new ARE_ToolItem_AlignmentLeft(), new ARE_ToolItem_AlignmentCenter(),
                            new ARE_ToolItem_AlignmentRight(), new ARE_ToolItem_Hr(),
                            new ARE_ToolItem_Link(), new ARE_ToolItem_Subscript(),
                            new ARE_ToolItem_Superscript(), new ARE_ToolItem_Image(),
                            new ARE_ToolItem_Video(),
                    };
                    for (IARE_ToolItem item : items) {
                        toolbar.addToolbarItem(item);
                    }
                    toolbar.setEditText(editText);

                    //
                    // Two styles switched on, to show the active treatment.
                    items[0].getView(activity).setSelected(true);
                    items[8].getView(activity).setSelected(true);

                    ViewGroup container = (ViewGroup) toolbar.getChildAt(0);
                    container.measure(
                            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                    container.layout(0, 0, container.getMeasuredWidth(),
                            container.getMeasuredHeight());

                    Bitmap bitmap = Bitmap.createBitmap(
                            Math.max(1, container.getMeasuredWidth()),
                            Math.max(1, container.getMeasuredHeight()),
                            Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(bitmap);
                    canvas.drawColor(Color.parseColor("#FFF8F9FA"));
                    container.draw(canvas);

                    try {
                        File out = new File(activity.getExternalFilesDir(null),
                                "toolbar.png");
                        FileOutputStream stream = new FileOutputStream(out);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
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
        assertTrue("toolbar not rendered", written[0] != null && written[0].exists());
    }
}
