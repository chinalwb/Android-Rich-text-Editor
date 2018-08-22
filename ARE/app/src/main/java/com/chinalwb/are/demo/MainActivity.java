package com.chinalwb.are.demo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.chinalwb.are.AREditText;
import com.chinalwb.are.AREditor;
import com.chinalwb.are.models.AtItem;
import com.chinalwb.are.strategies.AtStrategy;
import com.chinalwb.are.strategies.VideoStrategy;
import com.chinalwb.are.styles.toolbar.ARE_Toolbar;
import com.chinalwb.are.styles.toolbar.ARE_ToolbarDefault;
import com.chinalwb.are.styles.toolbar.IARE_Toolbar;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_AlignmentCenter;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_AlignmentLeft;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_AlignmentRight;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_At;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_Bold;
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

import static com.chinalwb.are.demo.TextViewActivity.HTML_TEXT;

/**
 * All Rights Reserved.
 *
 * @author Wenbin Liu
 *
 */
public class MainActivity extends AppCompatActivity {

    private static boolean useOption1 = true;

    private static final int REQ_WRITE_EXTERNAL_STORAGE = 10000;

    private AREditor arEditor;

    private IARE_Toolbar mToolbar;

    private AREditText mEditText;

    private boolean scrollerAtEnd;

    private AtStrategy mAtStrategy = new AtStrategy() {
        @Override
        public void openAtPage() {
            Intent intent = new Intent(MainActivity.this, AtActivity.class);
            startActivityForResult(intent, ARE_Toolbar.REQ_AT);
        }

        @Override
        public boolean onItemSelected(AtItem item) {
            return false;
        }
    };

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        if (useOption1) {
            option1();
        } else {
            option2();
        }

        String html = "<html><body><p><b>aaaa</b></p><p><i>bbbb</i></p>\n" +
                "    <p><u>cccc</u></p>\n" +
                "    <p><span style=\"text-decoration:line-through;\">dddd</span></p>\n" +
                "    <p style=\"text-align:start;\">Alignleft</p>\n" +
                "    <p style=\"text-align:center;\">Align center</p>\n" +
                "    <p style=\"text-align:end;\">Align right</p>\n" +
                "    <p style=\"text-align:start;\">Align left</p>\n" +
                "    <p style=\"text-align:start;\">Hello left<span style=\"background-color:#FFFF00;\"> good?</span> yes</p>\n" +
                "    <p style=\"text-align:start;\">Text color <span style=\"color:#FF5722;\">red </span><span style=\"color:#4CAF50;\">green </span><span style=\"color:#2196F3;\">blue </span><span style=\"color:#9C27B0;\">purple</span><span style=\"color:#000000;\"> normal black</span></p>\n" +
                "    <br>\n" +
                "    <p style=\"text-align:start;\">Click to open <a href=\"http://www.qq.com\">QQ</a> website</p>\n" +
                "    <br><br>\n" +
                "    <blockquote><p style=\"text-align:start;\">Quote</p>\n" +
                "    <p style=\"text-align:start;\">Quote 2nd line</p>\n" +
                "    <br>\n" +
                "    </blockquote>\n" +
                "    <br><br>\n" +
                "    <p style=\"text-align:start;\">2X<sub>1</sub><sup>2 </sup>+3X<sub>1</sub><sup>2</sup>=5X<sub>1</sub><sup>2</sup></p>\n" +
                "    <br>\n" +
                "    <br>\n" +
                "    <p style=\"text-align:start;\"><hr /> </p>\n" +
                "    <p style=\"text-align:start;\">Text <span style=\"font-size:32px\";>SIZE </span><span style=\"font-size:18px\";><span style=\"font-size:21px\";>normal</span></span></p>\n" +
                "    <br>\n" +
                "    <p style=\"text-align:center;\"><img src=\"emoji|" + R.drawable.wx_d_8 + "\"></p>\n" +
                "    <p style=\"text-align:start;\">Image:</p>\n" +
                "    <p style=\"text-align:start;\"><img src=\"http://d.hiphotos.baidu.com/image/pic/item/6159252dd42a2834171827b357b5c9ea14cebfcf.jpg\" /></p>\n" +
                "    <p style=\"text-align:start;\"></p>\n" +
                "    <p><a href=\"#\" ukey=\"2131230814\" uname=\"Steve Jobs\" style=\"color:#FF00FF;\">@Steve Jobs</a>, <a href=\"#\" ukey=\"2131230815\" uname=\"Bill Gates\" style=\"color:#0000FF;\">@Bill Gates</a>, how are you?</p>" +
                "    <p style=\"text-align:start;\"><emoji src=\"2131230915\" /><emoji src=\"2131230936\" /><emoji src=\"2131230929\" /></p>" +
                "    <ul>" +
                "    <li>aa</li>" +
                "    <li>bb</li>\n" +
                "    <li>dd</li>\n" +
                "    <li>eea</li>\n" +
                "    </ul>" +
                "    <ol>\n" +
                "    <li>ddasdf</li>\n" +
                "    <li>sdf</li>\n" +
                "    <li>cc</li>\n" +
                "    </ol>" +
                "    <p style=\"text-align:center;\"><video src=\"http://ivi.bupt.edu.cn/hls/cctv6hd.m3u8\" controls=\"controls\"></video></p>" +
                "    <p style=\"text-align:start;\"><img src=\"http://a.hiphotos.baidu.com/image/h%3D300/sign=13dc7fee3512b31bd86ccb29b6193674/730e0cf3d7ca7bcb6a172486b2096b63f624a82f.jpg\" /></p>" +
                "    </body></html>";

        if (useOption1) {
            // mEditText.fromHtml(html);
        } else {
            arEditor.fromHtml(html);
        }
    }

    private void option2() {
        this.arEditor = this.findViewById(R.id.areditor);
//        this.arEditor.setHideToolbar(false);
//        this.arEditor.setExpandMode(AREditor.ExpandMode.FULL);
//        this.arEditor.setToolbarAlignment(AREditor.ToolbarAlignment.BOTTOM);
        this.arEditor.setAtStrategy(mAtStrategy);
        this.arEditor.setVideoStrategy(mVideoStrategy);
    }

    private void option1() {
        mToolbar = this.findViewById(R.id.areToolbar);
        IARE_ToolItem bold = new ARE_ToolItem_Bold();
        IARE_ToolItem italic = new ARE_ToolItem_Italic();
        IARE_ToolItem underline = new ARE_ToolItem_Underline();
        IARE_ToolItem strikethrough = new ARE_ToolItem_Strikethrough();
        IARE_ToolItem quote = new ARE_ToolItem_Quote();
        IARE_ToolItem listNumber = new ARE_ToolItem_ListNumber();
        IARE_ToolItem listBullet = new ARE_ToolItem_ListBullet();
        IARE_ToolItem hr = new ARE_ToolItem_Hr();
        IARE_ToolItem link = new ARE_ToolItem_Link();
        IARE_ToolItem subscript = new ARE_ToolItem_Subscript();
        IARE_ToolItem superscript = new ARE_ToolItem_Superscript();
        IARE_ToolItem left = new ARE_ToolItem_AlignmentLeft();
        IARE_ToolItem center = new ARE_ToolItem_AlignmentCenter();
        IARE_ToolItem right = new ARE_ToolItem_AlignmentRight();
        IARE_ToolItem image = new ARE_ToolItem_Image();
        IARE_ToolItem video = new ARE_ToolItem_Video();
        IARE_ToolItem at = new ARE_ToolItem_At();
        mToolbar.addToolbarItem(bold);
        mToolbar.addToolbarItem(italic);
        mToolbar.addToolbarItem(underline);
        mToolbar.addToolbarItem(strikethrough);
        mToolbar.addToolbarItem(quote);
        mToolbar.addToolbarItem(listNumber);
        mToolbar.addToolbarItem(listBullet);
        mToolbar.addToolbarItem(hr);
        mToolbar.addToolbarItem(link);
        mToolbar.addToolbarItem(subscript);
        mToolbar.addToolbarItem(superscript);
        mToolbar.addToolbarItem(left);
        mToolbar.addToolbarItem(center);
        mToolbar.addToolbarItem(right);
        mToolbar.addToolbarItem(image);
        mToolbar.addToolbarItem(video);
        mToolbar.addToolbarItem(at);

        mEditText = this.findViewById(R.id.yView);
        mEditText.setToolbar(mToolbar);

        initToolbarArrow();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(com.chinalwb.are.R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuId = item.getItemId();
        if (menuId == com.chinalwb.are.R.id.action_save) {
            if (useOption1) {
                String html = this.mEditText.getHtml();
                DemoUtil.saveHtml(this, html);
            } else {
                String html = this.arEditor.getHtml();
                DemoUtil.saveHtml(this, html);
            }
            return true;
        }
        if (menuId == R.id.action_show_tv) {
            String html = "";
            if (useOption1) {
                html = this.mEditText.getHtml();
            } else {
                html = this.arEditor.getHtml();
            }
            Intent intent = new Intent(this, TextViewActivity.class);
            intent.putExtra(HTML_TEXT, html);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initToolbarArrow() {
        final ImageView imageView = this.findViewById(R.id.arrow);
        if (this.mToolbar instanceof ARE_ToolbarDefault) {
            ((ARE_ToolbarDefault) mToolbar).getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
                @Override
                public void onScrollChanged() {
                    int scrollX = ((ARE_ToolbarDefault) mToolbar).getScrollX();
                    int scrollWidth = ((ARE_ToolbarDefault) mToolbar).getWidth();
                    int fullWidth = ((ARE_ToolbarDefault) mToolbar).getChildAt(0).getWidth();

                    if (scrollX + scrollWidth < fullWidth) {
                        imageView.setImageResource(R.drawable.arrow_right);
                        scrollerAtEnd = false;
                    } else {
                        imageView.setImageResource(R.drawable.arrow_left);
                        scrollerAtEnd = true;
                    }
                }
            });
        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (scrollerAtEnd) {
                    ((ARE_ToolbarDefault) mToolbar).smoothScrollBy(-Integer.MAX_VALUE, 0);
                    scrollerAtEnd = false;
                } else {
                    int hsWidth = ((ARE_ToolbarDefault) mToolbar).getChildAt(0).getWidth();
                    ((ARE_ToolbarDefault) mToolbar).smoothScrollBy(hsWidth, 0);
                    scrollerAtEnd = true;
                }
            }
        });
    }




    /**
     * The layout file.
     */
    private int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_WRITE_EXTERNAL_STORAGE) {
            String html = this.arEditor.getHtml();
            DemoUtil.saveHtml(this, html);
            return;
        }

        if (useOption1) {
            mToolbar.onActivityResult(requestCode, resultCode, data);
        } else {
            this.arEditor.onActivityResult(requestCode, resultCode, data);
        }
    }
}
