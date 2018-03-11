package com.chinalwb.are.styles;

import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;

import com.chinalwb.are.AREditText;
import com.chinalwb.are.Constants;
import com.chinalwb.are.styles.toolbar.ARE_Toolbar;

public class ARE_FontColor extends ARE_ABS_FreeStyle {

	private ImageView mFontColorImageView;

	private AREditText mEditText;

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
				// Show color selector view.
				ARE_Toolbar.getInstance().toggleColorPalette();
			}
		});
	}

	@Override
	public void applyStyle(Editable editable, int start, int end) {

	} // #End of method

	@Override
	public ImageView getImageView() {
		return this.mFontColorImageView;
	}

	@Override
	public void setChecked(boolean isChecked) {
		// Do nothing.
	}

	@Override
	public EditText getEditText() {
		return this.mEditText;
	}

}
