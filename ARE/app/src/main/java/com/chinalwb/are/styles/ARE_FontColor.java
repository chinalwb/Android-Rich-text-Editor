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
import com.chinalwb.are.styles.toolbar.ARE_Toolbar;
import com.rainliu.colorpicker.ColorPickerListener;

public class ARE_FontColor extends ARE_ABS_Style<ForegroundColorSpan> {

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
					applyNewColor(editable, start, end);
				}
			}
		}
	};

	protected void applyNewColor(Editable editable, int start, int end) {
		ForegroundColorSpan startSpan = null;
		int startSpanStart = Integer.MAX_VALUE;
		ForegroundColorSpan endSpan = null;
		int endSpanStart = -1;
		int endSpanEnd = -1;

		int detectStart = start;
		if (start > 0) {
			detectStart = start - 1;
		}
		int detectEnd = end;
		if (end < editable.length()) {
			detectEnd = end + 1;
		}
		ForegroundColorSpan[] existingSpans = editable.getSpans(detectStart, detectEnd, ForegroundColorSpan.class);
		if (existingSpans != null && existingSpans.length > 0) {
            for (ForegroundColorSpan span : existingSpans) {
                int spanStart = editable.getSpanStart(span);

                if (spanStart < startSpanStart) {
                    startSpanStart = spanStart;
                    startSpan = span;
                }

                if (spanStart > endSpanStart) {
                    endSpanStart = spanStart;
                    endSpan = span;
                    endSpanEnd = editable.getSpanEnd(span);
                }
            } // End for

            for (ForegroundColorSpan span : existingSpans) {
                editable.removeSpan(span);
            }

            int startSpanColor = startSpan.getForegroundColor();
            int endSpanColor = endSpan.getForegroundColor();

            if (startSpanColor == mColor && endSpanColor == mColor) {
				editable.setSpan(newSpan(), startSpanStart, endSpanEnd, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
			} else if (startSpanColor == mColor) {
				editable.setSpan(new ForegroundColorSpan(startSpanColor), startSpanStart, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
				editable.setSpan(new ForegroundColorSpan(endSpanColor), end, endSpanEnd, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
			} else if (endSpanColor == mColor) {
				editable.setSpan(new ForegroundColorSpan(startSpanColor), startSpanStart, start, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
				editable.setSpan(new ForegroundColorSpan(endSpanColor), start, endSpanEnd, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
			} else {
				editable.setSpan(new ForegroundColorSpan(startSpanColor), startSpanStart, start, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
				editable.setSpan(new ForegroundColorSpan(endSpanColor), end, endSpanEnd, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
				editable.setSpan(newSpan(), start, end, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
			}
        } else {
			editable.setSpan(newSpan(), start, end, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
		}
		logAllFontColorSpans(editable);
	}

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

	protected void changeSpanInsideStyle(Editable editable, int start, int end, ForegroundColorSpan existingSpan) {
		int currentColor = existingSpan.getForegroundColor();
		if (currentColor != mColor) {
			Util.log("color changed before: " + currentColor + ", new == " + mColor);
			applyNewColor(editable, start, end);
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
	protected void extendPreviousSpan(Editable editable, int pos) {
		ForegroundColorSpan[] pSpans = editable.getSpans(pos, pos, ForegroundColorSpan.class);
		if (pSpans != null && pSpans.length > 0) {
			ForegroundColorSpan lastSpan = pSpans[0];
			int start = editable.getSpanStart(lastSpan);
			int end = editable.getSpanEnd(lastSpan);
			editable.removeSpan(lastSpan);
			int lastSpanColor = lastSpan.getForegroundColor();
			mColor = lastSpanColor;
			ARE_Toolbar.getInstance().setColorPaletteColor(mColor);
			editable.setSpan(new ForegroundColorSpan(lastSpanColor), start, end, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
		}
	}

	@Override
	public ForegroundColorSpan newSpan() {
		return new ForegroundColorSpan(this.mColor);
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

}
