# ARE
Android Rich text Editor ([中文说明见这里](https://github.com/chinalwb/Android-Rich-text-Editor/blob/master/README-zh.md))
===================

If you are looking for a good rich text editor on Android, DON'T MISS THIS ONE!

It's still in progress now, welcome fork and join me!

I published [colorpicker](https://github.com/chinalwb/SimpleColorPicker) and [emojipicker](https://github.com/chinalwb/SimpleEmojiPicker) as standalone components so they can be reused in other projects easily.


This is implemented by Java

 ![image](https://github.com/chinalwb/are/blob/master/ARE/demo/are_demo.gif)
 

Supported styles:
------------------
* Bold
* Italic
* Underline
* Strikethrough
* Numeric list
* Bullet list
* Align left
* Align center
* Align right
* Insert image
* Background color
* Hyper link
* @
* Quote
* Foreground color
* Emoji icon
* Superscript
* Subscript 
* Font size 
* Video
* Image from internet
* Dividing line
* All styles support save as HTML file
* Load from HTML then continue editing or displaying - New in 0.1.0

Plan for 0.1.2:
----
* Maintain release, includes:
1. Documents for installation and developement
2. UI improvements
3. Bugs fixing

More features you can open feature request but will need to wait for 0.1.3 if it is not urgent.

 
In progress items:
----------------
* Audio
* Font family
* Indent right
* Indent left
* Save editings to local SQLite
* Notes list
* Headline - defered, can be done with font size and center style


How to use?
--------------
Check out [usage guideline](https://github.com/chinalwb/Android-Rich-text-Editor/blob/master/Usage.md):
Before the general release, you need to checkout the code and add this to a layout file and include the layout file in your Activity. Just refer to the MainActivity.java in the source code.
```
<com.chinalwb.are.AREditor xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/areditor"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="@android:color/white" />
```

How to use the callback function for uploading video file?
```
public interface VideoStrategy {

    /**
     * Upload the video to server and return the url of the video at server
     *
     * @param uri
     * @return
     */
    public String uploadVideo(Uri uri);
}
```

Do upload implementation:
```
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
    };
```

Set to ARE:
```
this.arEditor = this.findViewById(R.id.areditor);
this.arEditor.setVideoStrategy(mVideoStrategy);
```

Load from html code:
-----
```
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
                "    <p style=\"text-align:center;\"><video src=\"http://www.xx.com/x.mp4\" uri=\"/storage/emulated/0/Download/wx_camera_1519181163870 (1).mp4\" controls=\"controls\"></video></p>" +
                "    <p style=\"text-align:start;\"><img src=\"http://a.hiphotos.baidu.com/image/h%3D300/sign=13dc7fee3512b31bd86ccb29b6193674/730e0cf3d7ca7bcb6a172486b2096b63f624a82f.jpg\" /></p>" +
                "    </body></html>";
        this.arEditor.fromHtml(html);
```

You can download the APK here:

[Click ARE_20180702_0.1.1.apk to download](https://github.com/chinalwb/Android-Rich-text-Editor/releases/download/v0.1.1/ARE_20180702_0.1.1.apk)

Known issues:
* Background color - cursor invisible when put it in the range of BackgroundColorSpan

Thanks [@Yasujizr](https://github.com/Yasujizr) providing the logo for ARE. 

-------------------
If you find my work is helpful to you or you are start using my code, you don't need to buy me a coffee, just could you please send me a "✨"? Your * encourages me to make more features open source, thanks for your support.
You can contact me at 329055754@qq.com if you need any customization or any suggestion.
