package com.chinalwb.are;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.chinalwb.are.android.inner.Html;
import com.chinalwb.are.styles.toolbar.ARE_Toolbar;
import com.rainliu.colorpicker.ColorPickerView;

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
	 * The color palette for foreground color.
	 */
	private ColorPickerView mColorPalette;
	
	/**
	 * The root linear layout.
	 */
	private LinearLayout mRootLinearLayout;

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
			layoutInflater.inflate(R.layout.areditor, this, true);
		} else {
			return;
		}

		this.initViews();
	} // # End of init()

	/**
	 * Init views.
	 */
	private void initViews() {
		this.mToolbar = (ARE_Toolbar) this.findViewById(R.id.toolbar);
		this.mRootLinearLayout = (LinearLayout) this.findViewById(R.id.rootLinearLayout);
	}

	/**
	 * 
	 */
	public String getHtml() {
		int childCount = this.mRootLinearLayout.getChildCount();
		StringBuffer html = new StringBuffer();
		html.append("<html>");
		for (int i = 0; i < childCount; i++) {
			View child = this.mRootLinearLayout.getChildAt(i);
			if (child instanceof AREditText) {
				appendAREditText((AREditText) child, html);
			}
			else if (child instanceof LinearLayout) {
				appendLinearLayout((LinearLayout) child, html);
			}
		}
		html.append("</html>");
		String htmlContent = html.toString().replaceAll(Constants.ZERO_WIDTH_SPACE_STR_ESCAPE, "");
		System.out.println(htmlContent);
		return htmlContent;
	}

	private void appendAREditText(AREditText editText, StringBuffer html) {
		String editTextHtml = Html.toHtml(editText.getEditableText(), Html.TO_HTML_PARAGRAPH_LINES_INDIVIDUAL);
		html.append(editTextHtml);
	}
	
	private void appendLinearLayout(LinearLayout linearLayout, StringBuffer html) {
		View child = linearLayout.getChildAt(0);
		if (child instanceof ImageView) {
			appendImage((ImageView) child, html);
		}
	}
	
	private void appendImage(ImageView imageView, StringBuffer html) {
		StringBuffer imageHtml = new StringBuffer();
		imageHtml.append("<img src=\"");
		Uri imageUri = (Uri) imageView.getTag();
		imageHtml.append(imageUri.toString());
		imageHtml.append("\" />");
		html.append(imageHtml);
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

}
