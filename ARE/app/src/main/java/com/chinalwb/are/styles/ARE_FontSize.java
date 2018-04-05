package com.chinalwb.are.styles;

import android.annotation.SuppressLint;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;

import com.chinalwb.are.AREditText;
import com.chinalwb.are.Constants;
import com.chinalwb.are.Util;
import com.chinalwb.are.spans.AreFontSizeSpan;
import com.chinalwb.are.spans.AreForegroundColorSpan;
import com.chinalwb.are.styles.toolbar.ARE_Toolbar;
import com.chinalwb.are.styles.windows.FontSizeChangeListener;
import com.chinalwb.are.styles.windows.FontsizePickerWindow;

public class ARE_FontSize extends ARE_ABS_Dynamic_Style<AreFontSizeSpan> implements FontSizeChangeListener {

	private ImageView mFontsizeImageView;

	private AREditText mEditText;

	private int mSize = Constants.DEFAULT_FONT_SIZE;

	private FontsizePickerWindow mFontPickerWindow;

	/**
	 * @param fontSizeImage
	 */
	public ARE_FontSize(ImageView fontSizeImage) {
		this.mFontsizeImageView = fontSizeImage;
		setListenerForImageView(this.mFontsizeImageView);
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
			showFontsizePickerWindow();
			}
		});
	}

	@SuppressLint("NewAPI")
	private void showFontsizePickerWindow() {
		if (mFontPickerWindow == null) {
			mFontPickerWindow = new FontsizePickerWindow(mContext, this);
		}
		mFontPickerWindow.setFontSize(mSize);
		mFontPickerWindow.showAsDropDown(ARE_Toolbar.getInstance(),0, 0, Gravity.BOTTOM);
	}

	@Override
	protected void changeSpanInsideStyle(Editable editable, int start, int end, AreFontSizeSpan existingSpan) {
		int currentColor = existingSpan.getSize();
		if (currentColor != mSize) {
			applyNewStyle(editable, start, end, mSize);
		}
	}

	@Override
	public AreFontSizeSpan newSpan() {
		return new AreFontSizeSpan(mSize);
	}

	@Override
	public ImageView getImageView() {
		return this.mFontsizeImageView;
	}

	@Override
	public void setChecked(boolean isChecked) {
		// Do nothing.
	}

	@Override
	public boolean getIsChecked() {
		return true;
	}

	@Override
	public void onFontSizeChange(int fontSize) {
		mSize = fontSize;
		if (null != mEditText) {
			Editable editable = mEditText.getEditableText();
			int start = mEditText.getSelectionStart();
			int end = mEditText.getSelectionEnd();

			if (end > start) {
				applyNewStyle(editable, start, end, mSize);
			}
		}
	}

	@Override
	protected void featureChangedHook(int lastSpanFontSize) {
		mSize = lastSpanFontSize;
		mFontPickerWindow.setFontSize(mSize);
	}

	@Override
	protected AreFontSizeSpan newSpan(int size) {
		return new AreFontSizeSpan(size);
	}
}
