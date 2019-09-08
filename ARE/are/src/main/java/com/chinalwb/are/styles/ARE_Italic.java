package com.chinalwb.are.styles;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;

import com.chinalwb.are.AREditText;
import com.chinalwb.are.spans.AreItalicSpan;
import com.chinalwb.are.styles.toolbar.ARE_Toolbar;

public class ARE_Italic extends ARE_ABS_Style<AreItalicSpan> {

	private ImageView mItalicImageView;

	private boolean mItalicChecked;

	private AREditText mEditText;

	public ARE_Italic(ImageView italicImage) {
		super(italicImage.getContext());
		this.mItalicImageView = italicImage;
		setListenerForImageView(this.mItalicImageView);
	}

	public void setEditText(AREditText editText) {
		this.mEditText = editText;
	}

	@Override
	public EditText getEditText() {
		return mEditText;
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
