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

public class ARE_Italic extends ARE_ABS_Style {

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
					applyStyle(mEditText.getEditableText(),
							mEditText.getSelectionStart(),
							mEditText.getSelectionEnd());
				}
			}
		});
	}

	@Override
	public void applyStyle(Editable editable, int start, int end) {
		if (this.mItalicChecked) {
			if (end > start) {
				//
				// User inputs or user selects a range
				StyleSpan[] spans = editable.getSpans(start, end, StyleSpan.class);
				StyleSpan existingItalicSpan = null;
				if (spans.length > 0) {
					for (StyleSpan span : spans) {
						int spanStyle = span.getStyle();
						if (spanStyle == Typeface.ITALIC) {
							existingItalicSpan = span;
							break;
						}
					}
				}

				if (existingItalicSpan == null) {
					checkAndMergeSpan(editable, start, end);
				} else {
					int existingItalicSpanStart = editable.getSpanStart(existingItalicSpan);
					int existingItalicSpanEnd = editable.getSpanEnd(existingItalicSpan);
					if (existingItalicSpanStart <= start && existingItalicSpanEnd >= end) {
						// The selection is just within an existing italic span
						// Do nothing for this case
					}
					else {
						checkAndMergeSpan(editable, start, end);
					}
				}
			} else {
				//
				// User deletes
				StyleSpan[] spans = editable.getSpans(start, end, StyleSpan.class);
				if (spans.length > 0) {
					for (StyleSpan span : spans) {
						int spanStyle = span.getStyle();
						if (spanStyle == Typeface.ITALIC) {

							int italicStart = editable.getSpanStart(span);
							int italicEnd = editable.getSpanEnd(span);
							Util.log("italicStart == " + italicStart + ", italicEnd == " + italicEnd);

							if (italicStart >= italicEnd) {
								editable.removeSpan(span);

								this.mItalicChecked = false;
								ARE_Helper.updateCheckStatus(this, false);
							} else {
								//
								// Do nothing, the default behavior is to extend
								// the span's area.
							}

							break;
						}
					}
				}
			}
		} else {
			//
			// User un-checks the ITALIC

			if (end > start) {
				//
				// User inputs or user selects a range
				StyleSpan[] spans = editable.getSpans(start, end, StyleSpan.class);
				if (spans.length > 0) {
					for (StyleSpan span : spans) {
						int spanStyle = span.getStyle();
						if (spanStyle == Typeface.ITALIC) {
							//
							// User stops the ITALIC style, and wants to show
							// un-ITALIC characters
							int ess = editable.getSpanStart(span); // ess == existing span start
							int ese = editable.getSpanEnd(span); // ese = existing span end
							if (start >= ese) {
								// User inputs to the end of the existing italic span
								// End existing italic span
								editable.removeSpan(span);
								editable.setSpan(span, ess, start - 1, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
							}
							else if (start == ess && end == ese) {
								// Case 1 desc:
								// *BBBBBB*
								// All selected, and un-check italic
								editable.removeSpan(span);
							}
							else if (start > ess && end < ese) {
								// Case 2 desc:
								// BB*BB*BB
								// *BB* is selected, and un-check italic
								editable.removeSpan(span);
								StyleSpan spanLeft = new StyleSpan(Typeface.ITALIC);
								editable.setSpan(spanLeft, ess, start, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
								StyleSpan spanRight = new StyleSpan(Typeface.ITALIC);
								editable.setSpan(spanRight, end, ese, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
							}
							else if (start == ess && end < ese) {
								// Case 3 desc:
								// *BBBB*BB
								// *BBBB* is selected, and un-check italic
								editable.removeSpan(span);
								StyleSpan newSpan = new StyleSpan(Typeface.ITALIC);
								editable.setSpan(newSpan, end, ese, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
							}
							else if (start > ess && end == ese) {
								// Case 4 desc:
								// BB*BBBB*
								// *BBBB* is selected, and un-check italic
								editable.removeSpan(span);
								StyleSpan newSpan = new StyleSpan(Typeface.ITALIC);
								editable.setSpan(newSpan, ess, start, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
							}
							

							break;
						}
					}
				}
			} else if (end == start) {
				// 
				// User changes focus position
				// Do nothing for this case
			} else {
				//
				// User deletes
				StyleSpan[] spans = editable.getSpans(start, end, StyleSpan.class);
				if (spans.length > 0) {
					for (StyleSpan span : spans) {
						int spanStyle = span.getStyle();
						if (spanStyle == Typeface.ITALIC) {

							int italicStart = editable.getSpanStart(span);
							int italicEnd = editable.getSpanEnd(span);

							if (italicStart >= italicEnd) {
								//
								// Invalid case, this will never happen.
							} else {
								//
								// Do nothing, the default behavior is to extend
								// the span's area.
								// The proceeding characters should be also ITALIC
								this.mItalicChecked = true;
								ARE_Helper.updateCheckStatus(this, true);
							}
							break;
						}
					}
				}
			}
		}
	}
	
	private void checkAndMergeSpan(Editable editable, int start, int end) {
		StyleSpan leftSpan = null;

		StyleSpan[] leftSpans = editable.getSpans(start, start, StyleSpan.class);
		if (leftSpans.length > 0) {
			for (StyleSpan span : leftSpans) {
				int spanStyle = span.getStyle();
				if (spanStyle == Typeface.ITALIC) {
					leftSpan = span;
					break;
				}
			}
		}
		
		StyleSpan rightSpan = null;
		StyleSpan[] rightSpans = editable.getSpans(end, end, StyleSpan.class);
		if (rightSpans.length > 0) {
			for (StyleSpan span : rightSpans) {
				int spanStyle = span.getStyle();
				if (spanStyle == Typeface.ITALIC) {
					rightSpan = span;
					break;
				}
			}
		}
	
		
		if (leftSpan != null && rightSpan != null) {
			int leftSpanStart = editable.getSpanStart(leftSpan);
			int rightSpanEnd = editable.getSpanEnd(rightSpan);
			editable.removeSpan(leftSpan);
			editable.removeSpan(rightSpan);
			StyleSpan italicSpan = new StyleSpan(Typeface.ITALIC);
			editable.setSpan(italicSpan, leftSpanStart, rightSpanEnd, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
		}
		else if (leftSpan != null && rightSpan == null) {
			int leftSpanStart = editable.getSpanStart(leftSpan);
			editable.removeSpan(leftSpan);
			StyleSpan italicSpan = new StyleSpan(Typeface.ITALIC);
			editable.setSpan(italicSpan, leftSpanStart, end, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
		}
		else if (leftSpan == null && rightSpan != null) {
			int rightSpanEnd = editable.getSpanEnd(rightSpan);
			editable.removeSpan(rightSpan);
			StyleSpan italicSpan = new StyleSpan(Typeface.ITALIC);
			editable.setSpan(italicSpan, start, rightSpanEnd, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
		}
		else {
			StyleSpan italicSpan = new StyleSpan(Typeface.ITALIC);
			editable.setSpan(italicSpan, start, end, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
		}
	}

	@Override
	public ImageView getImageView() {
		return this.mItalicImageView;
	}

	@Override
	public void setChecked(boolean isChecked) {
		this.mItalicChecked = isChecked;
	}
}
