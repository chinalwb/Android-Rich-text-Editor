# ARE
Android富文本编辑器 
===================

如果你正在寻找一个Android上使用的富文本编辑器，那么请你花1分钟时间看完这个说明。

这个项目总体进度已经发布两个小版本，更多功能正在进行中。由于我自己在项目中开发富文本编辑器的时候，从GitHub上找不到符合我们需求的，几年之后我对比了GitHub上新提交的富文本编辑器之后，感到仍然跟我们的需求不一致，不是用户体验不好就是功能较少，尤其是列表编辑功能。于是我自己做了这个开源项目，我相信这个将会成为最好的开源Android富文本编辑器（之一？或者没有之一）

项目用Java实现，应用Android的Span实现富文本多种样式的编辑。

 ![image](https://github.com/chinalwb/are/blob/master/ARE/demo/demo3.gif)
 
Release 0.0.2:
* 编辑样式:

 ![image](https://github.com/chinalwb/are/blob/master/ARE/demo/ARE_editing.png)

 导出的HTML用HTML Viewer 打开:

 ![image](https://github.com/chinalwb/are/blob/master/ARE/demo/HTMLViewer.png)

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
* 文字背景色 - Background color - NEW in 0.0.2
* 插入超链接 - Hyper link - NEW in 0.0.2
* @功能 - @ - NEW in 0.0.2
* 所有样式都支持导出HTML
* 列表编辑功能的增强 - NEW in 0.0.2

@功能的demo:
-----------------
 ![image](https://github.com/chinalwb/are/blob/master/ARE/demo/at_demo.gif)
 
@功能导出的HTML文件:
 ![image](https://github.com/chinalwb/are/blob/master/ARE/demo/at_demo_html.png)


已经发布了 [v0.0.2](https://github.com/chinalwb/are/releases/tag/v0.0.2) 计划在2018-03-31之前发布0.0.3:
-----------------

* 插入引用 - Quote
* 插入表情 - Emoji icon
* 文字前景色 - Foreground color

计划中但正在做的功能:
-----------------
* 插入引用 - Quote
* 插入表情 - Emoji icon
* 文字前景色 - Foreground color
* 字体大小 - Font size
* 字体样式 - Font family
* 右缩进 - Indent right
* 左缩进 - Indent left


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
如果想看效果但嫌运行代码太麻烦，可以下载0.0.2的apk:

[Click ARE_20180213_0.0.2.apk to download](https://github.com/chinalwb/Android-Rich-text-Editor/releases/download/v0.0.2/ARE_20180213_0.0.2.apk)

已知问题:
* 背景色 - 当给文字加上背景色之后光标闪烁效果消失

-------------------
如果你觉得我的代码对你有帮助或者你已经在使用此项目中的部分功能，麻烦点※以表支持，我会贡献更多功能的代码，多谢支持。
功能定制化或任何建议联系我的QQ邮箱：329055754@qq.com
