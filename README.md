# ARE

Android Rich text Editor ([中文说明见这里](https://github.com/chinalwb/Android-Rich-text-Editor/blob/master/README-zh.md))
===

If you are looking for a good rich text editor on Android, DON'T MISS THIS ONE!

It's still in progress now, welcome fork and join me!

I published [colorpicker](https://github.com/chinalwb/SimpleColorPicker) and [emojipicker](https://github.com/chinalwb/SimpleEmojiPicker) as standalone components so they can be reused in other projects easily.

This is implemented by Java

 ![image](https://github.com/chinalwb/are/blob/master/ARE/demo/are_demo.gif)

Supported styles:
---

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
---

* Maintain release, includes:
* Documents for installation and developement
* UI improvements
* Bugs fixing

More features you can open feature request but will need to wait for 0.1.3 if it is not urgent.

## Attributes

| Name             | Format | Description                                                                |
|:----------------:|:------:|:--------------------------------------------------------------------------:|
| expandMode       | enum   | FULL (default: Full screen editor) / MIN (min height editor, maxLines = 3) |
| toolbarAlignment | enum   | BOTTOM (default: at bottom of AREditor) / TOP (at top of AREditor)         |

## APIs

| Class    | Method              | Params                    | Description                                                                                                  |
|:--------:|:-------------------:|:-------------------------:|:------------------------------------------------------------------------------------------------------------:|
|          |                     |                           |                                                                                                              |
| AREditor | setExpandMode       | AREditor.ExpandMode       | Sets the edit area mode. Possible values are: ExpandMode.FULL (default) / ExpandMode.MIN                     |
| AREditor | setToolbarAlignment | AREditor.ToolbarAlignment | Sets the toolbar position. Possible values are: `ToolbarAlignment.BOTTOM` (default) / `ToolbarAlignment.TOP` |

## Samples

XML:

```
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:are="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="@android:color/holo_blue_dark"
    >

    <TextView
        android:id="@+id/xView"
        android:layout_above="@+id/areditor"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:layout_marginTop="1dp"
        android:background="@color/colorAccent"
        android:gravity="center"
        android:textSize="50sp"
        android:text="Your ListView may go here"
        />

    <com.chinalwb.are.AREditor
        android:id="@+id/areditor"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_alignParentBottom="true"
        android:background="@android:color/holo_green_dark"
        are:expandMode="MIN"
        are:toolbarAlignment="TOP" />

</RelativeLayout>
```

Java:

```
AREditor arEditor = this.findViewById(R.id.areditor);
arEditor.setExpandMode(AREditor.ExpandMode.FULL);
arEditor.setToolbarAlignment(AREditor.ToolbarAlignment.BOTTOM);
```

In progress items:
---

* Audio
* Font family
* Indent right
* Indent left
* Save editings to local SQLite
* Notes list
* Headline - defered, can be done with font size and center style

How to use?
---

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

You can download the APK here:

[Click ARE_20180702_0.1.1.apk to download](https://github.com/chinalwb/Android-Rich-text-Editor/releases/download/v0.1.1/ARE_20180702_0.1.1.apk)

Known issues:

* Background color - cursor invisible when put it in the range of BackgroundColorSpan

Thanks [@Yasujizr](https://github.com/Yasujizr) providing the logo for ARE. 

---

If you find my work is helpful to you or you are start using my code, you don't need to buy me a coffee, just could you please send me a "✨"? Your * encourages me to make more features open source, thanks for your support.
You can contact me at 329055754@qq.com if you need any customization or any suggestion.
