package com.chinalwb.are.demo;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.chinalwb.are.AREditor;
import com.chinalwb.are.strategies.VideoStrategy;

import static com.chinalwb.are.demo.TextViewActivity.HTML_TEXT;

public class ARE_MinTopActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_are__min_top);
        initView();
    }

    private AREditor arEditor;

    private VideoStrategy mVideoStrategy = new VideoStrategy() {
        @Override
        public String uploadVideo(Uri uri) {
            try {
                Thread.sleep(3000); // Do upload here
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "http://www.xx.com/x.mp4";
        }

        @Override
        public String uploadVideo(String videoPath) {
            try {
                Thread.sleep(3000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "http://www.xx.com/x.mp4";
        }
    };



    private void initView() {
        this.arEditor = this.findViewById(R.id.areditor);
        this.arEditor.setVideoStrategy(mVideoStrategy);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        this.arEditor.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuId = item.getItemId();
        if (menuId == com.chinalwb.are.R.id.action_save) {
            String html = this.arEditor.getHtml();
            DemoUtil.saveHtml(this, html);
            return true;
        }
        if (menuId == R.id.action_show_tv) {
            String html = "";
            html = this.arEditor.getHtml();
            Intent intent = new Intent(this, TextViewActivity.class);
            intent.putExtra(HTML_TEXT, html);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
