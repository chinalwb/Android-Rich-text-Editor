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

public class ARE_Bold extends ARE_ABS_Style {

	private ImageView mBoldImageView;

	private boolean mBoldChecked;

	private AREditText mEditText;
	
	/**
	 * 
	 * @param boldImage
	 */
	public ARE_Bold(ImageView boldImage) {
		this.mBoldImageView = boldImage;
		setListenerForImageView(this.mBoldImageView);
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
				mBoldChecked = !mBoldChecked;
				ARE_Helper.updateCheckStatus(ARE_Bold.this, mBoldChecked);
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
		if (this.mBoldChecked) {
			if (end > start) {
				//
				// User inputs or user selects a range
				StyleSpan[] spans = editable.getSpans(start, end, StyleSpan.class);
				StyleSpan existingBoldSpan = null;
				if (spans.length > 0) {
					for (StyleSpan span : spans) {
						int spanStyle = span.getStyle();
						if (spanStyle == Typeface.BOLD) {
							existingBoldSpan = span;
							break;
						}
					}
				}

				if (existingBoldSpan == null) {
					checkAndMergeSpan(editable, start, end);
				} else {
					int existingBoldSpanStart = editable.getSpanStart(existingBoldSpan);
					int existingBoldSpanEnd = editable.getSpanEnd(existingBoldSpan);
					if (existingBoldSpanStart <= start && existingBoldSpanEnd >= end) {
						// The selection is just within an existing bold span
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
						if (spanStyle == Typeface.BOLD) {

							int boldStart = editable.getSpanStart(span);
							int boldEnd = editable.getSpanEnd(span);
							Util.log("boldStart == " + boldStart + ", boldEnd == " + boldEnd);

							if (boldStart >= boldEnd) {
								editable.removeSpan(span);

								this.mBoldChecked = false;
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
			// User un-checks the BOLD

			if (end > start) {
				//
				// User inputs or user selects a range
				StyleSpan[] spans = editable.getSpans(start, end, StyleSpan.class);
				if (spans.length > 0) {
					for (StyleSpan span : spans) {
						int spanStyle = span.getStyle();
						if (spanStyle == Typeface.BOLD) {
							//
							// User stops the BOLD style, and wants to show
							// un-BOLD characters
							int ess = editable.getSpanStart(span); // ess == existing span start
							int ese = editable.getSpanEnd(span); // ese = existing span end
							if (start >= ese) {
								// User inputs to the end of the existing bold span
								// End existing bold span
								editable.removeSpan(span);
								editable.setSpan(span, ess, start - 1, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
							}
							else if (start == ess && end == ese) {
								// Case 1 desc:
								// *BBBBBB*
								// All selected, and un-check bold
								editable.removeSpan(span);
							}
							else if (start > ess && end < ese) {
								// Case 2 desc:
								// BB*BB*BB
								// *BB* is selected, and un-check bold
								editable.removeSpan(span);
								StyleSpan spanLeft = new StyleSpan(Typeface.BOLD);
								editable.setSpan(spanLeft, ess, start, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
								StyleSpan spanRight = new StyleSpan(Typeface.BOLD);
								editable.setSpan(spanRight, end, ese, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
							}
							else if (start == ess && end < ese) {
								// Case 3 desc:
								// *BBBB*BB
								// *BBBB* is selected, and un-check bold
								editable.removeSpan(span);
								StyleSpan newSpan = new StyleSpan(Typeface.BOLD);
								editable.setSpan(newSpan, end, ese, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
							}
							else if (start > ess && end == ese) {
								// Case 4 desc:
								// BB*BBBB*
								// *BBBB* is selected, and un-check bold
								editable.removeSpan(span);
								StyleSpan newSpan = new StyleSpan(Typeface.BOLD);
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
						if (spanStyle == Typeface.BOLD) {

							int boldStart = editable.getSpanStart(span);
							int boldEnd = editable.getSpanEnd(span);

							if (boldStart >= boldEnd) {
								//
								// Invalid case, this will never happen.
							} else {
								//
								// Do nothing, the default behavior is to extend
								// the span's area.
								// The proceeding characters should be also BOLD
								this.mBoldChecked = true;
								ARE_Helper.updateCheckStatus(this, true);
							}
							break;
						}
					}
				}
			}
		}
	} // #End of method

	private void checkAndMergeSpan(Editable editable, int start, int end) {
		StyleSpan leftSpan = null;
		StyleSpan[] leftSpans = editable.getSpans(start, start, StyleSpan.class);
		if (leftSpans.length > 0) {
			for (StyleSpan span : leftSpans) {
				int spanStyle = span.getStyle();
				if (spanStyle == Typeface.BOLD) {
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
				if (spanStyle == Typeface.BOLD) {
					rightSpan = span;
					break;
				}
			}
		}
		
		int leftSpanStart = editable.getSpanStart(leftSpan);
		int rightSpanEnd = editable.getSpanEnd(rightSpan);
		removeAllSpans(editable, start, end);
		if (leftSpan != null && rightSpan != null) {
			StyleSpan boldSpan = new StyleSpan(Typeface.BOLD);
			editable.setSpan(boldSpan, leftSpanStart, rightSpanEnd, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
		}
		else if (leftSpan != null && rightSpan == null) {
			StyleSpan boldSpan = new StyleSpan(Typeface.BOLD);
			editable.setSpan(boldSpan, leftSpanStart, end, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
		}
		else if (leftSpan == null && rightSpan != null) {
			StyleSpan boldSpan = new StyleSpan(Typeface.BOLD);
			editable.setSpan(boldSpan, start, rightSpanEnd, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
		}
		else {
			StyleSpan boldSpan = new StyleSpan(Typeface.BOLD);
			editable.setSpan(boldSpan, start, end, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
		}
	}

	private void removeAllSpans(Editable editable, int start, int end) {
		StyleSpan[] allSpans = editable.getSpans(start, end, StyleSpan.class);
		for (StyleSpan span : allSpans) {
			int spanStyle = span.getStyle();
			if (spanStyle == Typeface.BOLD) {
				editable.removeSpan(span);
			}
		}
	}

	@Override
	public ImageView getImageView() {
		return this.mBoldImageView;
	}

	@Override
	public void setChecked(boolean isChecked) {
		this.mBoldChecked = isChecked;
	}

}
