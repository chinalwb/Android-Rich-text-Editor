package com.chinalwb.are.styles.toolitems.styles;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;

import com.chinalwb.are.AREditText;
import com.chinalwb.are.spans.AreItalicSpan;
import com.chinalwb.are.styles.ARE_ABS_Style;
import com.chinalwb.are.styles.ARE_Helper;
import com.chinalwb.are.styles.toolitems.IARE_ToolItem_Updater;

import javax.microedition.khronos.egl.EGLDisplay;

public class ARE_Style_Italic extends ARE_ABS_Style<AreItalicSpan> {

	private ImageView mItalicImageView;

	private boolean mItalicChecked;

	private AREditText mEditText;

	private IARE_ToolItem_Updater mCheckUpdater;

	/**
	 *
	 * @param italicImage
	 */
	public ARE_Style_Italic(AREditText editText, ImageView italicImage, IARE_ToolItem_Updater checkUpdater) {
	    super(editText.getContext());
		this.mEditText = editText;
		this.mItalicImageView = italicImage;
		this.mCheckUpdater = checkUpdater;
		setListenerForImageView(this.mItalicImageView);
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
				mItalicChecked = !mItalicChecked;
				if (null != mCheckUpdater) {
					mCheckUpdater.onCheckStatusUpdate(mItalicChecked);
				}
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
