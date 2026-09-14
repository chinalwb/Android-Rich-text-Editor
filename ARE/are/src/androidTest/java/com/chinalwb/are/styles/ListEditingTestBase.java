package com.chinalwb.are.styles;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.Selection;
import android.view.View;
import android.widget.ImageView;

import androidx.test.core.app.ActivityScenario;

import com.chinalwb.are.AREditText;
import com.chinalwb.are.TestHostActivity;
import com.chinalwb.are.styles.toolbar.IARE_Toolbar;
import com.chinalwb.are.styles.toolitems.IARE_ToolItem;
import com.chinalwb.are.styles.toolitems.IARE_ToolItem_Updater;
import com.chinalwb.are.styles.toolitems.styles.ARE_Style_ListBullet;
import com.chinalwb.are.styles.toolitems.styles.ARE_Style_ListNumber;

import org.junit.After;
import org.junit.Before;

import java.util.ArrayList;
import java.util.List;

/** Shared editor setup for the list tests. */
public abstract class ListEditingTestBase {

    protected ActivityScenario<TestHostActivity> scenario;
    protected AREditText editText;
    protected ImageView numberButton;
    protected ImageView bulletButton;

    @Before
    public void setUpEditor() {
        scenario = ActivityScenario.launch(TestHostActivity.class);
        scenario.onActivity(new ActivityScenario.ActivityAction<TestHostActivity>() {
            @Override
            public void perform(TestHostActivity activity) {
                Context context = activity;
                editText = new AREditText(context);
                activity.getContent().addView(editText);

                numberButton = new ImageView(context);
                bulletButton = new ImageView(context);

                IARE_Style numberStyle = new ARE_Style_ListNumber(editText, numberButton);
                IARE_Style bulletStyle = new ARE_Style_ListBullet(editText, bulletButton);

                FakeToolbar toolbar = new FakeToolbar(editText);
                toolbar.addToolbarItem(new FakeToolItem(numberStyle, numberButton));
                toolbar.addToolbarItem(new FakeToolItem(bulletStyle, bulletButton));
                editText.setToolbar(toolbar);
            }
        });
    }

    @After
    public void tearDownEditor() {
        if (scenario != null) {
            scenario.close();
        }
    }

    protected AREditText editText() {
        return editText;
    }

    protected ImageView numberButton() {
        return numberButton;
    }

    protected void setTextAndCursor(final String value, final int cursor) {
        onMain(new Runnable() {
            @Override
            public void run() {
                editText.setText(value);
                Selection.setSelection(editText.getText(), cursor);
            }
        });
    }

    protected void click(final View view) {
        onMain(new Runnable() {
            @Override
            public void run() {
                view.performClick();
            }
        });
    }

    protected void typeNewLineRaw() {
        onMain(new Runnable() {
            @Override
            public void run() {
                Editable editable = editText.getText();
                int at = Selection.getSelectionEnd(editable);
                if (at < 0) {
                    at = editable.length();
                }
                editable.insert(at, "\n");
            }
        });
    }

    protected void typeRaw(final String value) {
        onMain(new Runnable() {
            @Override
            public void run() {
                Editable editable = editText.getText();
                int at = Selection.getSelectionEnd(editable);
                if (at < 0) {
                    at = editable.length();
                }
                editable.insert(at, value);
            }
        });
    }

    protected void onMain(final Runnable runnable) {
        scenario.onActivity(new ActivityScenario.ActivityAction<TestHostActivity>() {
            @Override
            public void perform(TestHostActivity activity) {
                runnable.run();
            }
        });
    }

    /** Minimal toolbar: enough for AREditText to register the styles. */
    protected static class FakeToolbar implements IARE_Toolbar {
        private final List<IARE_ToolItem> items = new ArrayList<>();
        private AREditText editText;

        FakeToolbar(AREditText editText) {
            this.editText = editText;
        }

        @Override
        public void addToolbarItem(IARE_ToolItem toolbarItem) {
            toolbarItem.setToolbar(this);
            items.add(toolbarItem);
        }

        @Override
        public List<IARE_ToolItem> getToolItems() {
            return items;
        }

        @Override
        public void setEditText(AREditText editText) {
            this.editText = editText;
        }

        @Override
        public AREditText getEditText() {
            return editText;
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            // Not used by these tests
        }
    }

    protected static class FakeToolItem implements IARE_ToolItem {
        private final IARE_Style style;
        private final View view;
        private IARE_Toolbar toolbar;

        FakeToolItem(IARE_Style style, View view) {
            this.style = style;
            this.view = view;
        }

        @Override
        public IARE_Style getStyle() {
            return style;
        }

        @Override
        public View getView(Context context) {
            return view;
        }

        @Override
        public void onSelectionChanged(int selStart, int selEnd) {
            // Not used by these tests
        }

        @Override
        public IARE_Toolbar getToolbar() {
            return toolbar;
        }

        @Override
        public void setToolbar(IARE_Toolbar toolbar) {
            this.toolbar = toolbar;
        }

        @Override
        public IARE_ToolItem_Updater getToolItemUpdater() {
            return null;
        }

        @Override
        public void setToolItemUpdater(IARE_ToolItem_Updater toolItemUpdater) {
            // Not used by these tests
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            // Not used by these tests
        }
    }
}
