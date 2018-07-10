package com.chinalwb.are.render;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.text.Spanned;
import android.util.AttributeSet;
import android.util.TypedValue;

import com.chinalwb.are.Constants;
import com.chinalwb.are.Util;
import com.chinalwb.are.android.inner.Html;
import com.chinalwb.are.events.AREMovementMethod;
import com.chinalwb.are.strategies.AreClickStrategy;
import com.chinalwb.are.strategies.defaults.DefaultClickStrategy;

import java.util.HashMap;

/**
 * @author dlink
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2018/5/20
 * @discription null
 * @usage null
 */
public class AreTextView extends AppCompatTextView {

    private static HashMap<String, Spanned> spannedHashMap = new HashMap<>();

    private AreClickStrategy mClickStrategy;

    Context mContext;

    public AreTextView(Context context) {
        this(context, null);
    }

    public AreTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AreTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        this.setTextSize(TypedValue.COMPLEX_UNIT_SP, Constants.DEFAULT_FONT_SIZE);
        initGlobalValues();
        initMovementMethod();
    }

    private void initGlobalValues() {
        int[] wh = Util.getScreenWidthAndHeight(mContext);
        Constants.SCREEN_WIDTH = wh[0];
        Constants.SCREEN_HEIGHT = wh[1];
    }

    private void initMovementMethod() {
        if (this.mClickStrategy == null) {
            this.mClickStrategy = new DefaultClickStrategy();
        }
        this.setMovementMethod(new AREMovementMethod(this.mClickStrategy));
    }

    public void fromHtml(String html) {
        Spanned spanned = getSpanned(html);
        setText(spanned);
    }

    private Spanned getSpanned(String html) {
        Html.sContext = mContext;
        Html.ImageGetter imageGetter = new AreImageGetter(mContext, this);
        Html.TagHandler tagHandler = new AreTagHandler();
        return Html.fromHtml(html, Html.FROM_HTML_SEPARATOR_LINE_BREAK_PARAGRAPH, imageGetter, tagHandler);
    }

    /**
     * Use cache will take more RAM, you need to call clear cache when you think it is safe to do that.
     * You may need cache when working with {@link android.widget.ListView} or RecyclerView
     *
     * @param html
     */
    public void fromHtmlWithCache(String html) {
        Spanned spanned = null;
        if (spannedHashMap.containsKey(html)) {
            spanned = spannedHashMap.get(html);
        }
        if (spanned == null) {
            spanned = getSpanned(html);
            spannedHashMap.put(html, spanned);
        }
        if (spanned != null) {
            setText(spanned);
        }
    }

    public static void clearCache() {
        spannedHashMap.clear();
    }

    public void setClickStrategy(AreClickStrategy clickStrategy) {
        this.mClickStrategy = clickStrategy;
    }
}
