package com.chinalwb.are;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.text.Spanned;
import android.util.AttributeSet;

import com.chinalwb.are.android.inner.Html;
import com.chinalwb.are.render.AreImageGetter;
import com.chinalwb.are.render.AreTagHandler;

/**
 * @author dlink
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2018/5/20
 * @discription null
 * @usage null
 */
public class ARETextView extends AppCompatTextView {
    Context mContext;

    public ARETextView(Context context) {
        super(context);
        mContext = context;
    }

    public ARETextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public ARETextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    public void fromHtml(String html) {
        Html.sContext = mContext;
        Html.ImageGetter imageGetter = new AreImageGetter(mContext, this);
        Html.TagHandler tagHandler = new AreTagHandler();
        Spanned spanned = Html.fromHtml(html, Html.FROM_HTML_SEPARATOR_LINE_BREAK_PARAGRAPH, imageGetter, tagHandler);
        setText(spanned);
    }

}
