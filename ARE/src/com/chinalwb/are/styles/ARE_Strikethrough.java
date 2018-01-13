package com.chinalwb.are.styles;

import android.text.Editable;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.chinalwb.are.AREditText;
import com.chinalwb.are.Util;

public class ARE_Strikethrough extends ARE_ABS_Style {

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
	public void applyStyle(Editable editable, int start, int end) {
		if (this.mStrikethroughChecked) {
			if (end > start) {
				//
				// User inputs or user selects a range
				StrikethroughSpan[] spans = editable.getSpans(start, end, StrikethroughSpan.class);
				StrikethroughSpan existingStrikethroughSpan = null;
				if (spans.length > 0) {
					existingStrikethroughSpan = spans[0];
				}

				if (existingStrikethroughSpan == null) {
					checkAndMergeSpan(editable, start, end);
				} else {
					int existingStrikethroughSpanStart = editable.getSpanStart(existingStrikethroughSpan);
					int existingStrikethroughSpanEnd = editable.getSpanEnd(existingStrikethroughSpan);
					if (existingStrikethroughSpanStart <= start && existingStrikethroughSpanEnd >= end) {
						// The selection is just within an existing
						// strikethrough span
						// Do nothing for this case
					} else {
						checkAndMergeSpan(editable, start, end);
					}
				}
			} else {
				//
				// User deletes
				StrikethroughSpan[] spans = editable.getSpans(start, end, StrikethroughSpan.class);
				if (spans.length > 0) {
					StrikethroughSpan span = spans[0];

					int strikethroughStart = editable.getSpanStart(span);
					int strikethroughEnd = editable.getSpanEnd(span);
					Util.log("strikethroughStart == " + strikethroughStart + ", strikethroughEnd == "
							+ strikethroughEnd);

					if (strikethroughStart >= strikethroughEnd) {
						editable.removeSpan(span);

						this.mStrikethroughChecked = false;
						ARE_Helper.updateCheckStatus(this, false);
					} else {
						//
						// Do nothing, the default behavior is to extend
						// the span's area.
					}
				}
			}
		} else {
			//
			// User un-checks the UNDERLINE

			if (end > start) {
				//
				// User inputs or user selects a range
				StrikethroughSpan[] spans = editable.getSpans(start, end, StrikethroughSpan.class);
				if (spans.length > 0) {
					StrikethroughSpan span = spans[0];
					if (null != span) {
						//
						// User stops the UNDERLINE style, and wants to show
						// un-UNDERLINE characters
						int ess = editable.getSpanStart(span); // ess ==
																// existing span
																// start
						int ese = editable.getSpanEnd(span); // ese = existing
																// span end
						if (start >= ese) {
							// User inputs to the end of the existing
							// strikethrough span
							// End existing strikethrough span
							editable.removeSpan(span);
							editable.setSpan(span, ess, start - 1, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
						} else if (start == ess && end == ese) {
							// Case 1 desc:
							// *BBBBBB*
							// All selected, and un-check strikethrough
							editable.removeSpan(span);
						} else if (start > ess && end < ese) {
							// Case 2 desc:
							// BB*BB*BB
							// *BB* is selected, and un-check strikethrough
							editable.removeSpan(span);
							StrikethroughSpan spanLeft = new StrikethroughSpan();
							editable.setSpan(spanLeft, ess, start, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
							StrikethroughSpan spanRight = new StrikethroughSpan();
							editable.setSpan(spanRight, end, ese, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
						} else if (start == ess && end < ese) {
							// Case 3 desc:
							// *BBBB*BB
							// *BBBB* is selected, and un-check strikethrough
							editable.removeSpan(span);
							StrikethroughSpan newSpan = new StrikethroughSpan();
							editable.setSpan(newSpan, end, ese, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
						} else if (start > ess && end == ese) {
							// Case 4 desc:
							// BB*BBBB*
							// *BBBB* is selected, and un-check strikethrough
							editable.removeSpan(span);
							StrikethroughSpan newSpan = new StrikethroughSpan();
							editable.setSpan(newSpan, ess, start, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
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
				StrikethroughSpan[] spans = editable.getSpans(start, end, StrikethroughSpan.class);
				if (spans.length > 0) {
					StrikethroughSpan span = spans[0];
					if (null != span) {
						int strikethroughStart = editable.getSpanStart(span);
						int strikethroughEnd = editable.getSpanEnd(span);

						if (strikethroughStart >= strikethroughEnd) {
							//
							// Invalid case, this will never happen.
						} else {
							//
							// Do nothing, the default behavior is to extend
							// the span's area.
							// The proceeding characters should be also
							// UNDERLINE
							this.mStrikethroughChecked = true;
							ARE_Helper.updateCheckStatus(this, true);
						}
					}
				}
			}
		}
	}

	private void checkAndMergeSpan(Editable editable, int start, int end) {
		StrikethroughSpan leftSpan = null;
		StrikethroughSpan[] leftSpans = editable.getSpans(start, start, StrikethroughSpan.class);
		if (leftSpans.length > 0) {
			leftSpan = leftSpans[0];
		}

		StrikethroughSpan rightSpan = null;
		StrikethroughSpan[] rightSpans = editable.getSpans(end, end, StrikethroughSpan.class);
		if (rightSpans.length > 0) {
			rightSpan = rightSpans[0];
		}

		int leftSpanStart = editable.getSpanStart(leftSpan);
		int rightSpanEnd = editable.getSpanEnd(rightSpan);
		removeAllSpans(editable, start, end);
		if (leftSpan != null && rightSpan != null) {
			editable.removeSpan(leftSpan);
			editable.removeSpan(rightSpan);
			StrikethroughSpan strikethroughSpan = new StrikethroughSpan();
			editable.setSpan(strikethroughSpan, leftSpanStart, rightSpanEnd, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
		} else if (leftSpan != null && rightSpan == null) {
			StrikethroughSpan strikethroughSpan = new StrikethroughSpan();
			editable.setSpan(strikethroughSpan, leftSpanStart, end, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
		} else if (leftSpan == null && rightSpan != null) {
			StrikethroughSpan strikethroughSpan = new StrikethroughSpan();
			editable.setSpan(strikethroughSpan, start, rightSpanEnd, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
		} else {
			StrikethroughSpan strikethroughSpan = new StrikethroughSpan();
			editable.setSpan(strikethroughSpan, start, end, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
		}
	}

	private void removeAllSpans(Editable editable, int start, int end) {
		StrikethroughSpan[] allSpans = editable.getSpans(start, end, StrikethroughSpan.class);
		for (StrikethroughSpan span : allSpans) {
			editable.removeSpan(span);
		}
	}

	@Override
	public ImageView getImageView() {
		return this.mStrikethroughImageView;
	}

	@Override
	public void setChecked(boolean isChecked) {
		this.mStrikethroughChecked = isChecked;
	}
}
