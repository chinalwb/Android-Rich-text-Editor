package com.chinalwb.are.styles.toolitems.styles;

import android.text.style.StrikethroughSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;

import com.chinalwb.are.AREditText;
import com.chinalwb.are.spans.AreUnderlineSpan;
import com.chinalwb.are.styles.ARE_ABS_Style;
import com.chinalwb.are.styles.toolitems.IARE_ToolItem_Updater;

public class ARE_Style_Strikethrough extends ARE_ABS_Style<StrikethroughSpan> {

	private ImageView mStrikethroughImageView;

	private boolean mStrikethroughChecked;

	private AREditText mEditText;

	private IARE_ToolItem_Updater mCheckUpdater;

	/**
	 *
	 * @param italicImage
	 */
	public ARE_Style_Strikethrough(AREditText editText, ImageView italicImage, IARE_ToolItem_Updater checkUpdater) {
	    super(editText.getContext());
		this.mEditText = editText;
		this.mStrikethroughImageView = italicImage;
		this.mCheckUpdater = checkUpdater;
		setListenerForImageView(this.mStrikethroughImageView);
	}

	@Override
	public EditText getEditText() {
		return this.mEditText;
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
				if (null != mCheckUpdater) {
					mCheckUpdater.onCheckStatusUpdate(mStrikethroughChecked);
				}
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
