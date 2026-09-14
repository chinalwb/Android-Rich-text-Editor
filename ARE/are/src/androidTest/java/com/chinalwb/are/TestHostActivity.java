package com.chinalwb.are;

import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * An empty AppCompat Activity for tests that need to build real editor views.
 */
public class TestHostActivity extends AppCompatActivity {

    private FrameLayout content;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        content = new FrameLayout(this);
        setContentView(content);
    }

    public FrameLayout getContent() {
        return content;
    }
}
