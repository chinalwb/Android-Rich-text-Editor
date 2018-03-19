# ARE
Android Rich text Editor ([中文说明见这里](https://github.com/chinalwb/Android-Rich-text-Editor/blob/master/README-zh.md))
===================
> (In order to make it better match the search key word: android rich text editor, I renamed it from ARE to Android Rich text Editor)

If you are looking for a good rich text editor on Android, DON'T MISS THIS ONE!

It's still in progress now, but I believe it will be (one of the) best open source Android rich text editor.

For 0.0.3 features, I plan to publish [colorpicker](https://github.com/chinalwb/SimpleColorPicker) and emojipicker as standalone components so they can be reused in other projects easily.

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
* Quote - NEW in 0.0.3
* Foreground color - NEW in 0.0.3
* Emoji icon - NEW in 0.0.3
* All styles support save as HTML file


Released [v0.0.3](https://github.com/chinalwb/are/releases/tag/v0.0.3) Plan for v0.0.4, target on 2018-04-30:
-----------------
* Font size
* Superscript
* Subscript

Demo for quote feature (NEW in 0.0.3):
-----------------
 ![image](https://github.com/chinalwb/are/blob/master/ARE/demo/quote_demo.png)
 
Demo for font color feature (NEW in 0.0.3):
-----------------
 ![image](https://github.com/chinalwb/are/blob/master/ARE/demo/fontcolor_demo.png)

Demo for emoji feature (NEW in 0.0.3):
-----------------
 ![image](https://github.com/chinalwb/are/blob/master/ARE/demo/emoji.gif)
 
In progress items:
-----------------
* Font size
* Superscript
* Subscript
* Font family
* Indent right
* Indent left


How to use?
Before the general release, you need to checkout the code and add this to a layout file and include the layout file in your Activity. Just refer to the MainActivity.java in the source code.
```
<com.chinalwb.are.AREditor xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/areditor"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="@android:color/white" />
```
You can download the APK here:

[Click ARE_20180318_0.0.3.apk to download](https://github.com/chinalwb/Android-Rich-text-Editor/releases/download/v0.0.3/ARE_20180318_0.0.3.apk)

Known issues:
* Background color - cursor invisible when put it in the range of BackgroundColorSpan
-------------------
If you find my work is helpful to you or you are start using my code, you don't need to buy me a coffee, just could you please send me a "✨"? Your * encourages me to make more features open source, thanks for your support.
You can contact me at 329055754@qq.com if you need any customization or any suggestion.
