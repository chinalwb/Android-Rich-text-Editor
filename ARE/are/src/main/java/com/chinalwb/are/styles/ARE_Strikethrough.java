package com.chinalwb.are.styles;

import android.text.Editable;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.chinalwb.are.AREditText;
import com.chinalwb.are.Util;

public class ARE_Strikethrough extends ARE_ABS_Style<StrikethroughSpan> {

	private ImageView mStrikethroughImageView;

	private boolean mStrikethroughChecked;

	private AREditText mEditText;

	/**
	 * 
	 * @param StrikethroughImage
	 */
	public ARE_Strikethrough(ImageView StrikethroughImage) {
		this.mStrikethroughImageView = StrikethroughImage;
		setListenerForImageView(this.mStrikethroughImageView);
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
				mStrikethroughChecked = !mStrikethroughChecked;
				ARE_Helper.updateCheckStatus(ARE_Strikethrough.this, mStrikethroughChecked);
				if (null != mEditText) {
					applyStyle(mEditText.getEditableText(), mEditText.getSelectionStart(), mEditText.getSelectionEnd());
				}
			}
		});
	}

	@Override
	public ImageView getImageView() {
		return this.mStrikethroughImageView;
	}

	@Override
	public void setChecked(boolean isChecked) {
		this.mStrikethroughChecked = isChecked;
	}

	@Override
	public boolean getIsChecked() {
		return this.mStrikethroughChecked;
	}

	@Override
	public StrikethroughSpan newSpan() {
		return new StrikethroughSpan();
	}
}
