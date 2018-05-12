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
* Superscript - NEW in 0.0.4
* Subscript - NEW in 0.0.4
* Font size - NEW in 0.0.4
* All styles support save as HTML file


Released [v0.0.4](https://github.com/chinalwb/are/releases/tag/v0.0.4) Plan for v0.0.7, ~~target on 2018-04-30 (Hope we can make it):~~ Sorry have to defer, new targret is on 2018-05-31:
-----
* Video - Done
* Image from internet - In progress
* Headline
* Dividing line
* Image bugs fixing

Demo for inserting Video (you can find customization approach below):
----
 ![image](https://github.com/chinalwb/are/blob/master/ARE/demo/video_demo.gif)


Further plan for v0.1.0, target on 2018-06-15:
-----------------
* Load from html

Demo for Font size (New in 0.0.4):
-----------------
 ![image](https://github.com/chinalwb/are/blob/master/ARE/demo/font_size_demo.gif)
 
Demo for Superscript & Subscript feature (New in 0.0.4):
-----------------
 ![image](https://github.com/chinalwb/are/blob/master/ARE/demo/subscript_superscript_demo.png)

Demo for quote feature (0.0.3):
-----------------
 ![image](https://github.com/chinalwb/are/blob/master/ARE/demo/quote_demo.png)
 
Demo for font color feature (0.0.3):
-----------------
 ![image](https://github.com/chinalwb/are/blob/master/ARE/demo/fontcolor_demo.png)

Demo for emoji feature (0.0.3):
-----------------
 ![image](https://github.com/chinalwb/are/blob/master/ARE/demo/emoji.gif)
 
In progress items:
-----------------
* Video - Done
* Audio
* Image from internet
* Font family
* Indent right
* Indent left
* Save editings to local SQLite
* Notes list
* Load from HTML


How to use?
--------------
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

You can download the APK here:

[Click ARE_20180405_0.0.4.apk to download](https://github.com/chinalwb/Android-Rich-text-Editor/releases/download/v0.0.4/ARE_20180405_0.0.4.apk)

Known issues:
* Background color - cursor invisible when put it in the range of BackgroundColorSpan

Thanks [@Yasujizr](https://github.com/Yasujizr) providing the logo for ARE. 

-------------------
If you find my work is helpful to you or you are start using my code, you don't need to buy me a coffee, just could you please send me a "✨"? Your * encourages me to make more features open source, thanks for your support.
You can contact me at 329055754@qq.com if you need any customization or any suggestion.
