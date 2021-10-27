[![Download](https://api.bintray.com/packages/chinalwb/are/are/images/download.svg)](https://bintray.com/chinalwb/are/are/_latestVersion) 
[![GitHub license](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](http://www.apache.org/licenses/LICENSE-2.0)
 
 
 # Android Rich text Editor ([中文说明见这里](https://github.com/chinalwb/Android-Rich-text-Editor/blob/master/README-zh.md))

If you are looking for a good rich text editor on Android, DON'T MISS THIS ONE!

It's still in progress now, welcome fork and join me!

I published [colorpicker](https://github.com/chinalwb/SimpleColorPicker) and [emojipicker](https://github.com/chinalwb/SimpleEmojiPicker) as standalone components so they can be reused in other projects easily.

*Hey guys! If you're reading this, I believe you are kind of struggling to find out some solution for rich text editor on Android, and specifically, you want to use the Android native APIs to implement. If I was right, maybe I can help. But starting from the very beginning of this project, I open-sourced all of the code, and have helped tens to even hundreds of friends out from their troubles, for free! Now I have to charge for the future helps, starting from $49 per feature implementation; $19 per feature contact. And the improvement will all be open-source to help others out. Reach out 329055754@qq.com for details. Thank you!*

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

## Released 0.2.0: migrated to AndroidX
* Update Glide to 4.11.0
* The last non-androidx version is 0.1.10

## Released 0.1.8: added support for multiple ARE instances in one page

## Released 0.1.7: update glide to 4.9.0

## In 0.1.6
* Maintain release, includes:
* Added foreground color style to ARE_Toolbar_Default
* Added background color style to ARE_Toolbar_Default
* Bugs fixing

![image](https://github.com/chinalwb/are/blob/master/ARE/demo/new_012.png)



##### Plan for next version:

* Undo
* Redo

Let me know what features you want to have in the next version if there is any, thanks.



## How ARE works

* Two usage modes

  1. AREditor. EditText (the input area) and Toolbar (the styling tools) are in the same component. I.e.: if you include `<com.chinalwb.are.AREditor .../>` in layout XML, you'll see the input area and styling tools (Bold / Italic / Alignment / Bullet list / etc..) in your activity.

  2. AREditText + IARE_Toolbar (it's an interface). EditText (the input area) and Toolbar (the styling tools) are standalone component themselves. I.e.: you have the choice to decide whether to show the Toolbar, and where to place it, below or bottom of the AREditText, left or right to the AREditText, or any other alignment because the toolbar itself is a `HorizontalScrollView` (default implementation is `ARE_ToolbarDefault`), and also you can decide what toolitem to be added to the toolbar.

* Inside these two modes:

  1. AREditor: it extends `RelativeLayout`, it contains two child components: `AREditText` and `ARE_Toolbar`. 

     1. `AREditText` extends `AppCompatEditText`, in its `afterTextChanged` method of `TextWatcher`, it calls the `applyStyle` of `IARE_Style`.

     2. `ARE_Toolbar` extends `LinearLayout`, the tool items inside are `IARE_Style` instances, each instance contains an `ImageView`, which is being shown in the toolbar.

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

        2. The above tool items can be added to toolbar by calling: `addToolbarItem(IARE_ToolItem toolItem)`

        3. If you want to add your own tool item, you just need to implement your `IARE_ToolItem`, for example, if you want to add a tool item to change font family, then you can define ARE_ToolItem_FontFamily and implements the methods in `IARE_ToolItem`. You can check out any of the above ToolItems as reference.

        4. Specially if you want to add a new feature like `@`, such as `##`, check out [ARE_ToolItem_At.java](https://github.com/chinalwb/Android-Rich-text-Editor/blob/master/ARE/are/src/main/java/com/chinalwb/are/styles/toolitems/ARE_ToolItem_At.java), which is a demo for tool item without an image at toolbar.

* All styles are based on [Android Spans](https://developer.android.com/reference/android/text/style/package-summary)

## Integration

In your gradle.build of app module, add this in the dependencies:

```groovy
    implementation 'com.github.bumptech.glide:glide:4.9.0'
    implementation 'com.github.chinalwb:are:0.1.7'
```

or, as `are` is still not stable enough to handle kinds of issues in different business scenarios, I'd like recommend to import `are` into your project directly and add it as a local module dependency.

```
	implementation project(':are')
```

## Customization & Samples

#### Documentation for `AREditor` in layout XML

| Name             | Format  | Description                                                                                                                                                                                                                                                  |
|:----------------:|:-------:|:------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------:|
| expandMode       | enum    | FULL (default: Full screen editor) / MIN (min height editor, maxLines = 3)                                                                                                                                                                                   |
| hideToolbar      | boolean | Whether to hide the toolbar, by default toolbar will be shown. You may want to set it as true when you use `MIN` expand mode, `@`feature will still be available but other features won't work because those styles on toolbar has been hidden with toolbar. |
| toolbarAlignment | enum    | BOTTOM (default: at bottom of AREditor) / TOP (at top of AREditor)                                                                                                                                                                                           |

#### APIs for `AREditor` in Java

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

### `AREditText` it self is an `AppCompatEditText` subclass, so anything applies to `AppCompatEditText` also works for `AREditText`.

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
| IARE_Toolbar | onActivityResult | requestCode(int) resultCode(int) data(Intent) | For some styles like insert image or video or @ feature, you need open a new Activity, and need to handle the data via onActivityResult, in this method you can dispatch to the specific styles. |

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

#### Sample in Java for setting up custom toolbar

```java

public class ARE_DefaultToolbarActivity extends AppCompatActivity {

    private IARE_Toolbar mToolbar;

    private AREditText mEditText;

    private boolean scrollerAtEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_are__default_toolbar);

        initToolbar();
    }


    private void initToolbar() {
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

        mEditText = this.findViewById(R.id.arEditText);
        mEditText.setToolbar(mToolbar);

        setHtml();

        initToolbarArrow();
    }

    private void setHtml() {
        String html = "<p style=\"text-align: center;\"><strong>New Feature in 0.1.2</strong></p>\n" +
                "<p style=\"text-align: center;\">&nbsp;</p>\n" +
                "<p style=\"text-align: left;\"><span style=\"color: #3366ff;\">In this release, you have a new usage with ARE.</span></p>\n" +
                "<p style=\"text-align: left;\">&nbsp;</p>\n" +
                "<p style=\"text-align: left;\"><span style=\"color: #3366ff;\">AREditText + ARE_Toolbar, you are now able to control the position of the input area and where to put the toolbar at and, what ToolItems you'd like to have in the toolbar. </span></p>\n" +
                "<p style=\"text-align: left;\">&nbsp;</p>\n" +
                "<p style=\"text-align: left;\"><span style=\"color: #3366ff;\">You can not only define the Toolbar (and it's style), you can also add your own ARE_ToolItem with your style into ARE.</span></p>\n" +
                "<p style=\"text-align: left;\">&nbsp;</p>\n" +
                "<p style=\"text-align: left;\"><span style=\"color: #ff00ff;\"><em><strong>Why not give it a try now?</strong></em></span></p>";
        mEditText.fromHtml(html);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuId = item.getItemId();
        if (menuId == com.chinalwb.are.R.id.action_save) {
            String html = this.mEditText.getHtml();
            DemoUtil.saveHtml(this, html);
            return true;
        }
        if (menuId == R.id.action_show_tv) {
            String html = this.mEditText.getHtml();
            Intent intent = new Intent(this, TextViewActivity.class);
            intent.putExtra(HTML_TEXT, html);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mToolbar.onActivityResult(requestCode, resultCode, data);
    }
}
```

#### APIs for `AREditor`

| Class    | Method   | Params | Description                                            |
|:--------:|:--------:|:------:|:------------------------------------------------------:|
| AREditor | fromHtml | String | Load html to AREditor                                  |
| AREditor | getHtml  | -none- | Returns the HTML source of current content in AREditor |
| AREditor | getARE   | -none- | Returns the AREditText instance in this AREditor       |

#### APIs for `AREditText`

| Class      | Method     | Params       | Description                                                                                                              |
|:----------:|:----------:|:------------:|:------------------------------------------------------------------------------------------------------------------------:|
| AREditText | fromHtml   | -none-       | Load html to AREditor                                                                                                    |
| AREditText | getHtml    | -none-       | Returns the HTML source of current content in AREditor                                                                   |
| AREditText | setToolbar | IARE_Toolbar | Sets the IARE_Toolbar instance (only necessary when works as separated component and works together with custom toolbar) |

## [More samples you can find at here.](https://github.com/chinalwb/Android-Rich-text-Editor/tree/master/ARE/app/src/main/java/com/chinalwb/are/demo)

## Available features demo:

![image](https://github.com/chinalwb/are/blob/master/ARE/demo/new_012_demos.png)

![image](https://github.com/chinalwb/are/blob/master/ARE/demo/new-demo.gif)

In progress items:
---

* Audio
* Font family
* Indent right
* Indent left
* Save editings to local SQLite
* Notes list
* Headline - deferred, can be done with font size and center style

## You can download the APK here:

[Click ARE_20190524_0.1.6.apk to download](https://github.com/chinalwb/Android-Rich-text-Editor/releases/download/0.1.6/ARE_20190524_0.1.6.apk)

Known issues:

* Background color - cursor invisible when put it in the range of BackgroundColorSpan

Thanks [@Yasujizr](https://github.com/Yasujizr) providing the logo for ARE. [@Yasujizr](https://github.com/Yasujizr) I hope to get a new logo now :) Could you please help me?

---

If you find my work is helpful to you or you are start using my code, you don't need to buy me a coffee, just could you please send me a "✨"? Your * encourages me to make more features open source, thanks for your support.
You can contact me at 329055754@qq.com if you need any customization or any suggestion.
