# ARE
Android Rich text Editor
===================
> (In order to make it better match the search key word: android rich text editor, I renamed it from ARE to Android Rich text Editor)

If you are looking for a good rich text editor on Android, DON'T MISS THIS ONE!

It's still in progress now, but I believe it will be (one of the) best open source Android rich text editor.

This is implemented by Java

 ![image](https://github.com/chinalwb/are/blob/master/ARE/demo/demo3.gif)
 
Release 0.0.1:
* Editing at ARE:

 ![image](https://github.com/chinalwb/are/blob/master/ARE/demo/ARE_editing.png)

 HTML Viewer shows:

 ![image](https://github.com/chinalwb/are/blob/master/ARE/demo/HTMLViewer.png)

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



Released [v0.0.1](https://github.com/chinalwb/are/releases/tag/v0.0.1), plan for v0.0.2, will be before 2018-02-28:
-----------------
* Background color - Done
* Hyper link - Done
* @
* Save as HTML file

Plan for v0.0.3, target on 2018-03-31:
-----------------
* Indent right
* Indent left
* Quote
* Save as HTML file

In progress items:
-----------------
* Background color
* Hyper link
* @
* Indent right
* Indent left
* Quote
* Emoji icon
* Font size
* Font family
* Foreground color


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

[Click ARE_20180114_0.0.1.apk to download](https://github.com/chinalwb/are/blob/master/ARE/demo/ARE_20180114_0.0.1.apk)

Known issues:
* Background color - cursor invisible when put it in the range of BackgroundColorSpan
-------------------
You can contact me at 329055754@qq.com if you need any customization or any suggestion.
