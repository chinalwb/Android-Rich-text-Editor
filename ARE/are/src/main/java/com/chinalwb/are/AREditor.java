package com.chinalwb.are;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.Editable;
import android.text.Spanned;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.chinalwb.are.android.inner.Html;
import com.chinalwb.are.render.AreImageGetter;
import com.chinalwb.are.render.AreTagHandler;
import com.chinalwb.are.strategies.AtStrategy;
import com.chinalwb.are.strategies.ImageStrategy;
import com.chinalwb.are.strategies.VideoStrategy;
import com.chinalwb.are.styles.toolbar.ARE_Toolbar;

/**
 * All Rights Reserved.
 *
 * @author Wenbin Liu
 */
public class AREditor extends RelativeLayout {

    /**
     * The toolbar alignment.
     */
    public enum ToolbarAlignment {
        /**
         * (Default) Below input area.
         */
        BOTTOM,

        /**
         * Above the input area.
         */
        TOP,
    }

    /**
     * The mode of the editor
     */
    public enum ExpandMode {
        /**
         * Take up all height available
         */
        FULL,

        /**
         * Take up min height
         */
        MIN,
    }

	/*
     * --------------------------------------------
	 * Instance Fields Area
	 * --------------------------------------------
	 */

    /**
     * Context.
     */
    private Context mContext;

    /**
     * The toolbar.
     */
    private ARE_Toolbar mToolbar;

    /**
     * The scroll view out of AREditText.
     */
    private ScrollView mAreScrollView;

    /**
     * The are editor.
     */
    private AREditText mAre;

    /**
     * The alignment of toolbar.
     */
    private ToolbarAlignment mToolbarAlignment = ToolbarAlignment.BOTTOM;

    /**
     * The expand mode of ARE.
     */
    private ExpandMode mExpandMode = ExpandMode.FULL;

    /**
     * Whether to hide the toolbar.
     */
    private boolean mHideToolbar = false;

	/*
	 * --------------------------------------------
	 * Constructors Area
	 * --------------------------------------------
	 */

    /**
     * Constructor.
     *
     * @param context
     */
    public AREditor(Context context) {
        this(context, null);
    }

    /**
     * Constructor.
     *
     * @param context
     * @param attrs
     */
    public AREditor(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * Constructor.
     *
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public AREditor(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        this.init(attrs);
    }

	/*
	 * --------------------------------------------
	 *  Business Methods Area
	 * --------------------------------------------
	 */

    /**
     * Initialization.
     */
    private void init(AttributeSet attrs) {
        initGlobal();
        initAttrs(attrs);

        doLayout();

    } // # End of init()

    private void initGlobal() {
        this.mToolbar = new ARE_Toolbar(mContext);
        this.mToolbar.setId(R.id.are_toolbar);

        this.mAreScrollView = new ScrollView(mContext);
        this.mAreScrollView.setId(R.id.are_scrollview);
    }

    private void initAttrs(AttributeSet attrs) {
        TypedArray ta = mContext.obtainStyledAttributes(attrs, R.styleable.are);

        // Toolbar alignment
        int toolbarAlignmentInt = ta.getInt(R.styleable.are_toolbarAlignment, ToolbarAlignment.BOTTOM.ordinal());
        this.mToolbarAlignment = ToolbarAlignment.values()[toolbarAlignmentInt];

        // Expand mode
        int expandMode = ta.getInt(R.styleable.are_expandMode, ExpandMode.FULL.ordinal());
        this.mExpandMode = ExpandMode.values()[expandMode];

        // Sets hide toolbar
        this.mHideToolbar = ta.getBoolean(R.styleable.are_hideToolbar, mHideToolbar);

        ta.recycle();
    }

    private void addToolbar(boolean isBelow, int belowId, boolean isFull) {
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        if (mExpandMode == ExpandMode.FULL) {
            int ruleVerb = mToolbarAlignment == ToolbarAlignment.BOTTOM ? RelativeLayout.ALIGN_PARENT_BOTTOM : RelativeLayout.ALIGN_PARENT_TOP;
            layoutParams.addRule(ruleVerb, this.getId());
        } else if (isBelow) {
            layoutParams.addRule(BELOW, belowId);
        }

        mToolbar.setLayoutParams(layoutParams);
        this.addView(this.mToolbar);

        if (mHideToolbar) {
            mToolbar.setVisibility(View.GONE);
        } else {
            mToolbar.setVisibility(View.VISIBLE);
        }
    }

    private void addEditText(boolean isBelow, int belowId) {
        int height = mExpandMode == ExpandMode.FULL ? LayoutParams.MATCH_PARENT : LayoutParams.WRAP_CONTENT;
        int lines = mExpandMode == ExpandMode.FULL ? -1 : 3;

        LayoutParams scrollViewLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, height);
        if (isBelow) {
            scrollViewLayoutParams.addRule(BELOW, belowId);
        }

        mAreScrollView.setLayoutParams(scrollViewLayoutParams);

        mAre = new AREditText(mContext);
        if (lines > 0) {
            mAre.setMaxLines(lines);
        }
        LayoutParams editTextLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, height);
        mAreScrollView.addView(mAre, editTextLayoutParams);
        this.mToolbar.setEditText(mAre);

        if (mExpandMode == ExpandMode.FULL) {
            mAreScrollView.setBackgroundColor(Color.WHITE);
        }

        this.addView(mAreScrollView);
    }

    private void doLayout() {
        if (this.indexOfChild(mToolbar) > -1) {
            this.removeView(mToolbar);
        }
        if (this.indexOfChild(mAreScrollView) > -1) {
            mAreScrollView.removeAllViews();
            this.removeView(mAreScrollView);
        }
        if (mToolbarAlignment == ToolbarAlignment.BOTTOM) {
            // EditText
            // Toolbar
            addEditText(false, -1);
            addToolbar(true, mAreScrollView.getId(), true);
        } else {
            // Toolbar is up, so EditText is below
            // EditText is below
            addToolbar(false, -1, false);
            addEditText(true, mToolbar.getId());
        }
    }

    /**
     * Sets html content to EditText.
     *
     * @param html
     * @return
     */
    public void fromHtml(String html) {
        Html.sContext = mContext;
        Html.ImageGetter imageGetter = new AreImageGetter(mContext, this.mAre);
        Html.TagHandler tagHandler = new AreTagHandler();
        Spanned spanned = Html.fromHtml(html, Html.FROM_HTML_SEPARATOR_LINE_BREAK_PARAGRAPH, imageGetter, tagHandler);
        AREditText.stopMonitor();
        this.mAre.getEditableText().append(spanned);
        AREditText.startMonitor();
//        log();
    }

    private void log() {
        Editable editable = this.mAre.getEditableText();
        Object[] spans = editable.getSpans(0, editable.length(), Object.class);
        for (Object span : spans) {
            int spanStart = editable.getSpanStart(span);
            int spanEnd = editable.getSpanEnd(span);
            Util.log("span == " + span + ", start == " + spanStart + ", end == " + spanEnd);
        }
    }

    /**
     *
     */
    public String getHtml() {
        StringBuffer html = new StringBuffer();
        html.append("<html><body>");
        appendAREditText(mAre, html);
        html.append("</body></html>");
        String htmlContent = html.toString().replaceAll(Constants.ZERO_WIDTH_SPACE_STR_ESCAPE, "");
        System.out.println(htmlContent);
        return htmlContent;
    }

    private static void appendAREditText(AREditText editText, StringBuffer html) {
        String editTextHtml = Html.toHtml(editText.getEditableText(), Html.TO_HTML_PARAGRAPH_LINES_INDIVIDUAL);
        html.append(editTextHtml);
    }

    public AREditText getARE() {
        return this.mAre;
    }

    /**
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        this.mToolbar.onActivityResult(requestCode, resultCode, data);
    } // #End of onActivityResult(..)

	/* ----------------------
	 * Customization part
	 * ---------------------- */

    public void setToolbarAlignment(ToolbarAlignment alignment) {
        mToolbarAlignment = alignment;
        doLayout();
    }

    public void setExpandMode(ExpandMode mode) {
        mExpandMode = mode;
        doLayout();
    }

    public void setHideToolbar(boolean hideToolbar) {
        mHideToolbar = hideToolbar;
        doLayout();
    }

    public void setAtStrategy(AtStrategy atStrategy) {
        this.mAre.setAtStrategy(atStrategy);
    }

    public void setVideoStrategy(VideoStrategy videoStrategy) {
        this.mAre.setVideoStrategy(videoStrategy);
    }

    public void setImageStrategy(ImageStrategy imageStrategy) {
        this.mAre.setImageStrategy(imageStrategy);
    }

}
