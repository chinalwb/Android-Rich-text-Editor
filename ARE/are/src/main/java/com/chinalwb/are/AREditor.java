package com.chinalwb.are;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Editable;
import android.text.Layout;
import android.text.Spanned;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.chinalwb.are.android.inner.Html;
import com.chinalwb.are.render.AreImageGetter;
import com.chinalwb.are.render.AreTagHandler;
import com.chinalwb.are.strategies.AtStrategy;
import com.chinalwb.are.strategies.VideoStrategy;
import com.chinalwb.are.styles.toolbar.ARE_Toolbar;

import org.w3c.dom.Attr;

/**
 * All Rights Reserved.
 *
 * @author Wenbin Liu
 *
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
		this.init();
	}

	/*
	 * --------------------------------------------
	 *  Business Methods Area
	 * --------------------------------------------
	 */
	/**
	 * Initialization.
	 */
	private void init() {
		initSelf();
		addToolbar();
		addEditText();
	} // # End of init()

	private void initSelf() {
		LayoutParams rootLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		this.setLayoutParams(rootLayoutParams);
	}

	private void addToolbar() {
		this.mToolbar = new ARE_Toolbar(mContext);
		this.mToolbar.setId(R.id.are_toolbar);
		LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		int ruleVerb = mToolbarAlignment == ToolbarAlignment.BOTTOM ? RelativeLayout.ALIGN_PARENT_BOTTOM : RelativeLayout.ALIGN_PARENT_TOP;
		layoutParams.addRule(ruleVerb, this.getId());
		this.addView(this.mToolbar, layoutParams);
	}

	private void addEditText() {
		mAreScrollView = new ScrollView(mContext);
		int ruleVerb = mToolbarAlignment == ToolbarAlignment.BOTTOM ? RelativeLayout.ABOVE : RelativeLayout.BELOW;
		LayoutParams scrollViewLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		scrollViewLayoutParams.addRule(ruleVerb, mToolbar.getId());

		mAre = new AREditText(mContext);
		LayoutParams editTextLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		mAreScrollView.addView(mAre, editTextLayoutParams);
        this.mToolbar.setEditText(mAre);

		this.addView(mAreScrollView, scrollViewLayoutParams);
	}

	private void relayoutToolbar() {
		this.removeView(mToolbar);
		LayoutParams toolbarLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		int ruleVerb = mToolbarAlignment == ToolbarAlignment.BOTTOM ? RelativeLayout.ALIGN_PARENT_BOTTOM : RelativeLayout.ALIGN_PARENT_TOP;
		toolbarLayoutParams.addRule(ruleVerb, this.getId());
		this.addView(mToolbar, toolbarLayoutParams);

		this.removeView(mAreScrollView);
		ruleVerb = mToolbarAlignment == ToolbarAlignment.BOTTOM ? RelativeLayout.ABOVE : RelativeLayout.BELOW;
		LayoutParams scrollViewLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		scrollViewLayoutParams.addRule(ruleVerb, mToolbar.getId());
		this.addView(mAreScrollView, scrollViewLayoutParams);
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

	private static  void appendAREditText(AREditText editText, StringBuffer html) {
		String editTextHtml = Html.toHtml(editText.getEditableText(), Html.TO_HTML_PARAGRAPH_LINES_INDIVIDUAL);
		html.append(editTextHtml);
	}

	public AREditText getARE() {
		return this.mAre;
	}

	/**
	 *
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

	public void hideToolbar() {

	}

	public void showToolbar() {

	}

	public void setToolbarAlignment(ToolbarAlignment alignment) {
		mToolbarAlignment = alignment;
		relayoutToolbar();
	}

	public void setAtStrategy(AtStrategy atStrategy) {
		this.mAre.setAtStrategy(atStrategy);
	}

	public void setVideoStrategy(VideoStrategy videoStrategy) {
		this.mAre.setVideoStrategy(videoStrategy);
	}

}
