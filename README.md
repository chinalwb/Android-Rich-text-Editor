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

Plan for 0.1.2 （Done, will update readme and release soon）:
---

* Maintain release, includes:
* Documents for installation and developement
* UI improvements
* Bugs fixing

More features you can open feature request but will need to wait for 0.1.3 if it is not urgent.

![image](https://github.com/chinalwb/are/blob/master/ARE/demo/new_012.png)

## How it works

* Two usage modes

  1. AREditor. EditText (the input area) and Toolbar (the styling tools) are in the same component. I.e.: if you include `<com.chinalwb.are.AREditor .../>` in layout XML, you'll see the input area and styling tools (Bold / Italic / Alignment / Bullet list / etc..) in your activity.

  2. AREditText + IARE_Toolbar (it's an interface). EditText (the input area) and Toolbar (the styling tools) are standalone component themselves. I.e.: you have the choice to decide whether to show the Toolbar, and where to place it, below or bottom of the AREditText, left or right to the AREditText, or any other alignment because the toolbar itself is a `HorizontalScrollView` (default implementation is `ARE_ToolbarDefault`), and also you can decide what toolitem to be added to the toolbar.

* Inside these two modes:

  1. AREditor: it extends `RelativeLayout`, it contains two child components: `AREditText` and `ARE_Toolbar`. 

     1. `AREditText` extends `AppCompatEditText`, in its `afterTextChanged` method of `TextWatcher`, it calls the `applyStyle` of `IARE_Style`.

     2. `ARE_Toolbar` extends `LinearLayout`, the tool items inside are`IARE_Style` instances, each instance contains an `ImageView`, which is being shown in the toolbar.

  2. AREditText + IARE_Toolbar.

     1. No need to repeat about `AREditText`

     2. `IARE_Toolbar`, it is an interface, which defines what a toolbar needs to implement. Such as: `addToolbarItem`, `getToolItems`, etc.. Inside `are`, there is a default implementation: `ARE_ToolbarDefault`,  it extends `HorizontalScrollView`, and has the implementation of all the methods in the `IARE_Toolbar` interface.

     3. `ARE_ToolbarDefault`, it is just a toolbar, i.e.: tool item container, what the component does the real work is `IARE_ToolItem`, which is another interface defines the behaviors of a tool item should have, like: `getView` returns a view to be shown in the toolbar, `getStyle` returns an `IARE_Style` to response to the text change.   Currently there are 19 toolitems implementation:

        1. ARE_ToolItem_Abstract
           ARE_ToolItem_AlignmentCenter
           ARE_ToolItem_AlignmentLeft
           ARE_ToolItem_AlignmentRight
           ARE_ToolItem_At
           ARE_ToolItem_Bold
           ARE_ToolItem_Hr
           ARE_ToolItem_Image
           ARE_ToolItem_Italic
           ARE_ToolItem_Link
           ARE_ToolItem_ListBullet
           ARE_ToolItem_ListNumber
           ARE_ToolItem_Quote
           ARE_ToolItem_Strikethrough
           ARE_ToolItem_Subscript
           ARE_ToolItem_Superscript
           ARE_ToolItem_Underline
           ARE_ToolItem_UpdaterDefault
           ARE_ToolItem_Video

        2. The abve tool items can be added to toolbar by calling: `addToolbarItem(IARE_ToolItem toolItem)`

* All styles are based on [Android Spans](https://developer.android.com/reference/android/text/style/package-summary)

## Integration

In your gradle.build of app module, add this in the root level:

```javascript
repositories {
    maven {
        url 'https://dl.bintray.com/chinalwb/are/'
    }
}
```

and this in the dependencies:

```groovy
    implementation 'com.github.bumptech.glide:glide:4.3.1'
    implementation 'com.github.chinalwb:are:0.1.2'
```



## Customization & Samples

#### Documentation for `AREditor` in layout XML

| Name             | Format  | Description                                                                                                                                                                                                                                                  |
|:----------------:|:-------:|:------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------:|
| expandMode       | enum    | FULL (default: Full screen editor) / MIN (min height editor, maxLines = 3)                                                                                                                                                                                   |
| hideToolbar      | boolean | Whether to hide the toolbar, by default toolbar will be shown. You may want to set it as true when you use `MIN` expand mode, `@`feature will still be available but other features won't work because those styles on toolbar has been hidden with toolbar. |
| toolbarAlignment | enum    | BOTTOM (default: at bottom of AREditor) / TOP (at top of AREditor)                                                                                                                                                                                           |

#### APIs for `AREditor` in Java

| Class    | Method              | Params                    | Description                                                                                                  |
|:--------:|:-------------------:|:-------------------------:|:------------------------------------------------------------------------------------------------------------:|
| AREditor | setExpandMode       | AREditor.ExpandMode       | Sets the edit area mode. Possible values are: ExpandMode.FULL (default) / ExpandMode.MIN                     |
| AREditor | setHideToolbar      | boolean                   | Sets as true to hide toolbar; sets false will show toolbar                                                   |
| AREditor | setToolbarAlignment | AREditor.ToolbarAlignment | Sets the toolbar position. Possible values are: `ToolbarAlignment.BOTTOM` (default) / `ToolbarAlignment.TOP` |

#### Samples for `AREditor`

XML:

```xml
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
        are:hideToolbar="true"
        are:toolbarAlignment="TOP" />

</RelativeLayout>
```

Java:

```
AREditor arEditor = this.findViewById(R.id.areditor);
arEditor.setExpandMode(AREditor.ExpandMode.FULL);
arEditor.setHideToolbar(false);
arEditor.setToolbarAlignment(AREditor.ToolbarAlignment.BOTTOM);
```

==========

### `AREditText` it self is an `AppCompatEditText` subclass, so anything applies to `AppCompatEditText` also works for `AREditText`.

==========

#### Documentation for `ARE_ToolbarDefault`

It extends `HorizontalScrollView`, so anything applies to `HorizontalScrollView` also works for `ARE_ToolbarDefault`.

#### APIs for `IARE_Toolbar`

| Class        | Method           | Params                                        | Description                                                                                                                                                                                     |
|:------------:|:----------------:|:---------------------------------------------:|:-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------:|
| IARE_Toolbar | addToolbarItem   | IARE_ToolItem                                 | Add a toolbar item to toolbar                                                                                                                                                                   |
| IARE_Toolbar | getToolItems     | -none-                                        | Returns all of the tool items in the toolbar                                                                                                                                                    |
| IARE_Toolbar | setEditText      | AREditText                                    | Binds AREditText with toolbar                                                                                                                                                                   |
| IARE_Toolbar | getEditText      | -none-                                        | Returns the bound AREditText                                                                                                                                                                    |
| IARE_Toolbar | onActivityResult | requestCode(int) resultCode(int) data(Intent) | For some styles like inert image or video or @ feature, you need open a new Activity, and need to handle the data via onActivityResult, in this method you can dispatch to the specific styles. |

#### APIs for `IARE_ToolItem`

| Class         | Method             | Params                                        | Description                                                                                 |
|:-------------:|:------------------:|:---------------------------------------------:|:-------------------------------------------------------------------------------------------:|
| IARE_ToolItem | getStyle           | -none-                                        | Each tool item is a style, and a style combines with specific span.                         |
| IARE_ToolItem | getView            | Context                                       | Each tool item has a view. If context is null, return the generated view.                   |
| IARE_ToolItem | onSelectionChanged | int selStart, int selEnd                      | Selection changed call back. Update tool item checked status                                |
| IARE_ToolItem | getToolbar         | -none-                                        | Returns the toolbar of this tool item.                                                      |
| IARE_ToolItem | setToolbar         | IARE_Toolbar                                  | Sets the toolbar for this tool item.                                                        |
| IARE_ToolItem | getToolItemUpdater | -none-                                        | Gets the tool item updater instance, will be called when style being checked and unchecked. |
| IARE_ToolItem | setToolItemUpdater | IARE_ToolItem_Updater                         | Sets the tool item updater.                                                                 |
| IARE_ToolItem | onActivityResult   | requestCode(int) resultCode(int) data(Intent) | Handle the dispatched event from IARE_Toolbar                                               |

#### Samples:

```xml
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:are="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="@android:color/holo_blue_dark">

    <ScrollView
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:layout_above="@+id/bottombar"
        android:layout_alignParentLeft="true"
        android:layout_alignParentStart="true"
        android:background="@android:color/white">

        <com.chinalwb.are.AREditText
            android:id="@+id/arEditText"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:gravity="top|left"
            android:hint="Your EditText goes here"
            android:textSize="50sp" />
    </ScrollView>

    <LinearLayout
        android:id="@+id/bottombar"
        android:layout_width="match_parent"
        android:layout_height="50dp"
        android:layout_alignParentBottom="true"
        android:orientation="horizontal"
        android:weightSum="1000">

        <com.chinalwb.are.styles.toolbar.ARE_ToolbarDefault
            android:id="@+id/areToolbar"
            android:layout_width="0dp"
            android:layout_height="match_parent"
            android:layout_weight="900"
            android:background="@color/colorPrimary"
            android:gravity="center_vertical" />

        <View
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:layout_weight="5"
            android:background="@color/colorPrimaryDark" />

        <LinearLayout
            android:layout_width="0dp"
            android:layout_height="match_parent"
            android:layout_weight="95"
            android:background="@color/colorAccent"
            android:gravity="center">

            <ImageView
                android:id="@+id/arrow"
                android:layout_width="30dp"
                android:layout_height="30dp"
                android:src="@drawable/arrow_right" />
        </LinearLayout>
    </LinearLayout>


</RelativeLayout>


```



## [More samples you can find at here.](https://github.com/chinalwb/Android-Rich-text-Editor/tree/master/ARE/app/src/main/java/com/chinalwb/are/demo)



## Available features demo:

![image](https://github.com/chinalwb/are/blob/master/ARE/demo/new_012_demos.png)





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
