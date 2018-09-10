package com.chinalwb.are.demo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class IndexActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        initViews();
    }

    private void initViews() {
        Button defaultToolbarButton = this.findViewById(R.id.defaultToolbar);
        openPage(defaultToolbarButton, ARE_DefaultToolbarActivity.class);

        Button fullBottomButton = this.findViewById(R.id.fullBottomButton);
        openPage(fullBottomButton, ARE_FullBottomActivity.class);

        Button fullTopButton = this.findViewById(R.id.fullTopButton);
        openPage(fullTopButton, ARE_FullTopActivity.class);

        Button minBottomButton = this.findViewById(R.id.minBottomButton);
        openPage(minBottomButton, ARE_MinBottomActivity.class);

        Button minTopButton = this.findViewById(R.id.minTopButton);
        openPage(minTopButton, ARE_MinTopActivity.class);

        Button minHideButton = this.findViewById(R.id.minHideButton);
        openPage(minHideButton, ARE_MinHideActivity.class);
    }

    private void openPage(Button button, final Class activity) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IndexActivity.this, activity);
                startActivity(intent);
            }
        });
    }
}
