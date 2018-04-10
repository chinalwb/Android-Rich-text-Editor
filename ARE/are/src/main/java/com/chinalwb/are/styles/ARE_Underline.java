package com.chinalwb.are.styles;

import android.text.Editable;
import android.text.Spanned;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.chinalwb.are.AREditText;
import com.chinalwb.are.Util;
import com.chinalwb.are.spans.AreUnderlineSpan;

public class ARE_Underline extends ARE_ABS_Style<AreUnderlineSpan> {

	private ImageView mUnderlineImageView;

	private boolean mUnderlineChecked;

	private AREditText mEditText;

	/**
	 * 
	 * @param UnderlineImage
	 */
	public ARE_Underline(ImageView UnderlineImage) {
		this.mUnderlineImageView = UnderlineImage;
		setListenerForImageView(this.mUnderlineImageView);
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
				mUnderlineChecked = !mUnderlineChecked;
				ARE_Helper.updateCheckStatus(ARE_Underline.this, mUnderlineChecked);
				if (null != mEditText) {
					applyStyle(mEditText.getEditableText(), mEditText.getSelectionStart(), mEditText.getSelectionEnd());
				}
			}
		});
	}

	@Override
	public ImageView getImageView() {
		return this.mUnderlineImageView;
	}

	@Override
	public void setChecked(boolean isChecked) {
		this.mUnderlineChecked = isChecked;
	}

	@Override
	public boolean getIsChecked() {
		return this.mUnderlineChecked;
	}

	@Override
	public AreUnderlineSpan newSpan() {
		return new AreUnderlineSpan();
	}
}
