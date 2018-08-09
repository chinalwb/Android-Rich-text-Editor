package com.chinalwb.are;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.Spannable;
import android.text.Spanned;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.chinalwb.are.android.inner.Html;
import com.chinalwb.are.render.AreImageGetter;
import com.chinalwb.are.render.AreTagHandler;
import com.chinalwb.are.spans.AreListSpan;
import com.chinalwb.are.strategies.AtStrategy;
import com.chinalwb.are.strategies.VideoStrategy;
import com.chinalwb.are.styles.toolbar.ARE_Toolbar;

/**
 * All Rights Reserved.
 *
 * @author Wenbin Liu
 *
 */
public class AREditor extends RelativeLayout {

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
	 * The are editor.
	 */
	private AREditText mAre;

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
		LayoutInflater layoutInflater = LayoutInflater.from(this.mContext);
		if (null != layoutInflater) {
			layoutInflater.inflate(getLayoutId(), this, true);
		} else {
			return;
		}

		this.initViews();
	} // # End of init()

	private int getLayoutId() {
		return R.layout.areditor;
	}

	/**
	 * Init views.
	 */
	private void initViews() {
		this.mToolbar = this.findViewById(R.id.toolbar);
		this.mAre = this.findViewById(R.id.are);
		this.mToolbar.setEditText(mAre);
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
	public void setAtStrategy(AtStrategy atStrategy) {
		this.mAre.setAtStrategy(atStrategy);
	}

	public void setVideoStrategy(VideoStrategy videoStrategy) {
        this.mAre.setVideoStrategy(videoStrategy);
	}

}
