# ARE
Android Rich text Editor
===================
If you are looking for a good rich text editor on Android, DON'T MISS THIS ONE!

It's still in progress now, but I believe it will be (one of the) best open source Android rich text editor.

This is implemented by Java

 ![image](https://github.com/chinalwb/are/blob/master/ARE/demo/demo2.gif)
 
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

Release plan for 0.0.1, will be before 2018-01-20:
-----------------
* Current supported styles
* Add the support for selecting a text range for B / I / U / S
* Save as HTML file

Release plan for 0.0.2, will be before 2018-02-28:
-----------------
* Quote
* Hyper link
* @
* Save as HTML file

In progress items:
-----------------
* Background color
* Indent right
* Indent left
* Emoji icon
* Font size
* Font family
* Quote
* Hyper link
* Foreground color
* @

Known issues:
-----------------
* Indent cooperates with List

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

-------------------

