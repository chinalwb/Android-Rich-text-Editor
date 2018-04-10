package com.chinalwb.are.styles;

import android.graphics.Typeface;
import android.text.Editable;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.chinalwb.are.AREditText;
import com.chinalwb.are.Util;
import com.chinalwb.are.spans.AreItalicSpan;

public class ARE_Italic extends ARE_ABS_Style<AreItalicSpan> {

	private ImageView mItalicImageView;

	private boolean mItalicChecked;

	private AREditText mEditText;

	/**
	 * 
	 * @param italicImage
	 */
	public ARE_Italic(ImageView italicImage) {
		this.mItalicImageView = italicImage;
		setListenerForImageView(this.mItalicImageView);
	}

	/**
	 * 
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
				mItalicChecked = !mItalicChecked;
				ARE_Helper.updateCheckStatus(ARE_Italic.this, mItalicChecked);
				if (null != mEditText) {
					applyStyle(mEditText.getEditableText(), mEditText.getSelectionStart(), mEditText.getSelectionEnd());
				}
			}
		});
	}

	@Override
	public ImageView getImageView() {
		return this.mItalicImageView;
	}

	@Override
	public void setChecked(boolean isChecked) {
		this.mItalicChecked = isChecked;
	}

	@Override
	public boolean getIsChecked() {
		return this.mItalicChecked;
	}

	@Override
	public AreItalicSpan newSpan() {
		return new AreItalicSpan();
	}
}
