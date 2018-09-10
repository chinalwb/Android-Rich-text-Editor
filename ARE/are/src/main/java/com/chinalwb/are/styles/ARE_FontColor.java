package com.chinalwb.are.styles;

import android.graphics.Color;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;

import com.chinalwb.are.AREditText;
import com.chinalwb.are.Constants;
import com.chinalwb.are.Util;
import com.chinalwb.are.spans.AreForegroundColorSpan;
import com.chinalwb.are.styles.toolbar.ARE_Toolbar;
import com.chinalwb.are.colorpicker.ColorPickerListener;

public class ARE_FontColor extends ARE_ABS_Dynamic_Style<AreForegroundColorSpan> {

	private ImageView mFontColorImageView;

	private AREditText mEditText;

	private int mColor = -1;

	private ColorPickerListener mColorPickerListener = new ColorPickerListener() {
		@Override
		public void onPickColor(int color) {
			mColor = color;
			if (null != mEditText) {
				Editable editable = mEditText.getEditableText();
				int start = mEditText.getSelectionStart();
				int end = mEditText.getSelectionEnd();

				if (end > start) {
					applyNewStyle(editable, start, end, mColor);
				}
			}
		}
	};



	/**
	 *
	 * @param fontColorImage
	 */
	public ARE_FontColor(ImageView fontColorImage) {
		this.mFontColorImageView = fontColorImage;
		setListenerForImageView(this.mFontColorImageView);
	}

	/**
	 * @param editText
	 */
	public void setEditText(AREditText editText) {
		this.mEditText = editText;
	}


	@Override
	public void setListenerForImageView(final ImageView imageView) {
		imageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
		    	ARE_Toolbar.getInstance().toggleColorPalette(mColorPickerListener);
			}
		});
	}

	@Override
	protected void changeSpanInsideStyle(Editable editable, int start, int end, AreForegroundColorSpan existingSpan) {
		int currentColor = existingSpan.getForegroundColor();
		if (currentColor != mColor) {
			Util.log("color changed before: " + currentColor + ", new == " + mColor);
			applyNewStyle(editable, start, end, mColor);
			logAllFontColorSpans(editable);
		}
	}

	private void logAllFontColorSpans(Editable editable) {
		ForegroundColorSpan[] listItemSpans = editable.getSpans(0,
				editable.length(), ForegroundColorSpan.class);
		for (ForegroundColorSpan span : listItemSpans) {
			int ss = editable.getSpanStart(span);
			int se = editable.getSpanEnd(span);
			Util.log("List All: " + " :: start == " + ss + ", end == " + se);
		}
	}

	@Override
	public AreForegroundColorSpan newSpan() {
		return new AreForegroundColorSpan(this.mColor);
	}

	@Override
	public ImageView getImageView() {
		return this.mFontColorImageView;
	}

	@Override
	public void setChecked(boolean isChecked) {
		// Do nothing
	}

	@Override
	public boolean getIsChecked() {
		return this.mColor != -1;
	}

	@Override
	public EditText getEditText() {
		return this.mEditText;
	}

	@Override
	protected AreForegroundColorSpan newSpan(int color) {
		return new AreForegroundColorSpan(color);
	}

	@Override
	protected void featureChangedHook(int lastSpanColor) {
		mColor = lastSpanColor;
		ARE_Toolbar.getInstance().setColorPaletteColor(mColor);
	}
}
