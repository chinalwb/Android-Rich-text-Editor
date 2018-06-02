# ARE
Android富文本编辑器 
===================

如果你正在寻找一个Android上使用的富文本编辑器，那么请你花1分钟时间看完这个说明。

这个项目总体进度已经发布7个小版本，更多功能正在进行中。由于我自己在项目中开发富文本编辑器的时候，从GitHub上找不到符合我们需求的，几年之后我对比了GitHub上新提交的富文本编辑器之后，感到仍然跟我们的需求不一致，不是用户体验不好就是功能较少，尤其是列表编辑功能。于是我自己开始了这个项目。

项目用Java实现，应用Android的Span实现富文本多种样式的编辑。

 ![image](https://github.com/chinalwb/are/blob/master/ARE/demo/are_demo.gif)

目前支持的样式:
------------------
* 加粗 - Bold
* 斜体 - Italic
* 下划线 - Underline
* 删除线 - Strikethrough
* 有序列表 - Numeric list
* 无序列表 - Bullet list
* 左对齐 - Align left
* 居中对齐 - Align center
* 右对齐 - Align right
* 插入图片 - Insert image
* 文字背景色 - Background color
* 插入超链接 - Hyper link 
* @功能 - @ 
* 引用 
* 文字颜色（前景色） 
* 插入表情 
* 上角标 
* 下角标 
* 字体大小  
* 插入视频 - NEW in 0.0.7
* 插入网络图片 - NEW in 0.0.7
* 插入分割线 - NEW in 0.0.7
* 所有样式均支持导出HTML文件


Released [v0.0.7](https://github.com/chinalwb/are/releases/tag/v0.0.7) v0.1.0在2018-06-02完成，近期即将发布:
-----
* Load from html - In progress
> Done styles:
> * Bold
> * Italic
> * Underline
> * Strikethrough
> * Align left
> * Align center
> * Align right
> * Background color
> * Foreground color
> * Hyper link
> * Quote
> * Superscript
> * Subscript
> * Dividing line
> * Font size
> * Image
> * @
> * Emoji icon
> * Video
> * Numeric list
> * Bullet list


从html中加载的代码:
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

从html加载演示: (已完成在 0.1.0):
----
 ![image](https://github.com/chinalwb/are/blob/master/ARE/demo/load_from_html_demo.gif)

插入视频演示: (New in 0.0.7):
----
 ![image](https://github.com/chinalwb/are/blob/master/ARE/demo/video_demo.gif)

插入网络图片演示: (New in 0.0.7):
----
 ![image](https://github.com/chinalwb/are/blob/master/ARE/demo/image_from_internet_demo.gif)
 
插入分割线演示: (New in 0.0.7):
----
 ![image](https://github.com/chinalwb/are/blob/master/ARE/demo/hr_demo.gif)


字体大小功能演示 (0.0.4):
-----------------
 ![image](https://github.com/chinalwb/are/blob/master/ARE/demo/font_size_demo.gif)
 
上标下标功能演示 (0.0.4):
-----------------
 ![image](https://github.com/chinalwb/are/blob/master/ARE/demo/subscript_superscript_demo.png)
 
插入引用功能演示 (0.0.3):
-----------------
 ![image](https://github.com/chinalwb/are/blob/master/ARE/demo/quote_demo.png)
 
文字前景色功能演示 (0.0.3):
-----------------
 ![image](https://github.com/chinalwb/are/blob/master/ARE/demo/fontcolor_demo.png)

插入表情功能演示 (0.0.3):
-----------------
 ![image](https://github.com/chinalwb/are/blob/master/ARE/demo/emoji.gif)

计划中但正在做的功能:
-----------------
* 从 HTML 加载内容到ARE
* 嵌入音频
* 字体样式 - Font family
* 右缩进 - Indent right
* 左缩进 - Indent left
* 保存编辑内容到SQLite
* 显示保存文件列表
* 插入标题 - 推迟 - 暂时可用字体+居中完成所需效果

如何使用?
我打算在所有功能完成之后再将其作为Gradle插件提交到maven， 所以暂时还是需要把代码检出之后手动引用到项目中。
```
<com.chinalwb.are.AREditor xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/areditor"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="@android:color/white" />
```

如何使用上传视频文件的回调方法？
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

实现你的上传逻辑:
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

设置到ARE:
```
this.arEditor = this.findViewById(R.id.areditor);
this.arEditor.setVideoStrategy(mVideoStrategy);
```

如果想看效果但嫌运行代码太麻烦，可以下载最新的apk: (0.0.7 暂未提供APK)

[Click ARE_20180405_0.0.4.apk to download](https://github.com/chinalwb/Android-Rich-text-Editor/releases/download/v0.0.4/ARE_20180405_0.0.4.apk)

已知问题:
* 背景色 - 当给文字加上背景色之后光标闪烁效果消失

-------------------
如果你觉得我的代码对你有帮助或者你已经在使用此项目中的部分功能，麻烦点※以表支持，我会贡献更多功能的代码，多谢支持。
功能定制化或任何建议联系我的QQ邮箱：329055754@qq.com
