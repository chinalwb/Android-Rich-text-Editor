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
* 插入视频
* 插入网络图片
* 插入分割线
* 所有样式均支持导出HTML文件
* 加载HTML内容并继续编辑或显示


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

##如何使用?
[直接集成ARE源代码到开发环境看这个](https://github.com/chinalwb/Android-Rich-text-Editor/blob/master/Usage.md)。

我打算在所有功能完成之后再将其作为Gradle插件提交到maven， 所以暂时还是需要把代码检出之后手动引用到项目中。
```
<com.chinalwb.are.AREditor xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/areditor"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="@android:color/white" />
```

下载最新的demo版apk: 

[Click ARE_20180702_0.1.1.apk to download](https://github.com/chinalwb/Android-Rich-text-Editor/releases/download/v0.1.0/ARE_20180702_0.1.1.apk)

已知问题:
* 背景色 - 当给文字加上背景色之后光标闪烁效果消失

-------------------
如果你觉得我的代码对你有帮助或者你已经在使用此项目中的部分功能，麻烦点※以表支持，我会贡献更多功能的代码，多谢支持。
功能定制化或任何建议联系我的QQ邮箱：329055754@qq.com
