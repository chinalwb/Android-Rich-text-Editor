# ARE
(Want to be the best open source) Android Rich text Editor
===================

This is implemented by Java

 ![image](https://github.com/chinalwb/are/blob/master/ARE/demo/demo2.gif)
 
New feature:
* To HTML, added saveHtml()

 ![image](https://github.com/chinalwb/are/blob/master/ARE/demo/saveHtml.png)

 HTML print as:
```
 <html>
  <p style="text-align:center;"><b>Todo list</b></p> 
  <p style="text-align:start;">Today I need to work on these things list as below, some are must do, some are optional, let me list them here so I can have a clear picture for what to do:</p> 
  <ol> 
   <li>React Native</li> 
   <li>Weex</li> 
   <li>Gulp</li> 
   <li>Webpack</li> 
  </ol> 
  <p style="text-align:start;">Hope I can have all them done today!</p> 
  <p style="text-align:start;"></p> 
  <p style="text-align:start;"></p> 
  <img src="file:///mnt/sdcard/Download/20170830155756-d629c9cd.jpg" />
  <p style="text-align:start;"><i>Have a nice day.</i></p> 
</html>
```

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
* @


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

[Click ARE_20180101.apk to download](https://github.com/chinalwb/are/blob/master/ARE/demo/ARE_20180101.apk)

-------------------
* The first release plans to be roll out before 2018/1/31.
