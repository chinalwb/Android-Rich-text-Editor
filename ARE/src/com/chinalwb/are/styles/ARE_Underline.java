package com.chinalwb.are.styles;

import android.text.Editable;
import android.text.Spanned;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.chinalwb.are.AREditText;
import com.chinalwb.are.Util;
import com.chinalwb.are.spans.AreUnderlineSpan;

public class ARE_Underline extends ARE_ABS_Style {

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
	public void applyStyle(Editable editable, int start, int end) {
		if (this.mUnderlineChecked) {
			if (end > start) {
				//
				// User inputs or user selects a range
				AreUnderlineSpan[] spans = editable.getSpans(start, end, AreUnderlineSpan.class);
				AreUnderlineSpan existingUnderlineSpan = null;
				if (spans.length > 0) {
					existingUnderlineSpan = spans[0];
				}

				if (existingUnderlineSpan == null) {
					checkAndMergeSpan(editable, start, end);
				} else {
					int existingUnderlineSpanStart = editable.getSpanStart(existingUnderlineSpan);
					int existingUnderlineSpanEnd = editable.getSpanEnd(existingUnderlineSpan);
					if (existingUnderlineSpanStart <= start && existingUnderlineSpanEnd >= end) {
						// The selection is just within an existing underline
						// span
						// Do nothing for this case
					} else {
						checkAndMergeSpan(editable, start, end);
					}
				}
			} else {
				//
				// User deletes
				AreUnderlineSpan[] spans = editable.getSpans(start, end, AreUnderlineSpan.class);
				if (spans.length > 0) {
					AreUnderlineSpan span = spans[0];

					int underlineStart = editable.getSpanStart(span);
					int underlineEnd = editable.getSpanEnd(span);
					Util.log("underlineStart == " + underlineStart + ", underlineEnd == " + underlineEnd);

					if (underlineStart >= underlineEnd) {
						editable.removeSpan(span);

						this.mUnderlineChecked = false;
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
				AreUnderlineSpan[] spans = editable.getSpans(start, end, AreUnderlineSpan.class);
				if (spans.length > 0) {
					AreUnderlineSpan span = spans[0];
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
							// User inputs to the end of the existing underline
							// span
							// End existing underline span
							editable.removeSpan(span);
							editable.setSpan(span, ess, start - 1, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
						} else if (start == ess && end == ese) {
							// Case 1 desc:
							// *BBBBBB*
							// All selected, and un-check underline
							editable.removeSpan(span);
						} else if (start > ess && end < ese) {
							// Case 2 desc:
							// BB*BB*BB
							// *BB* is selected, and un-check underline
							editable.removeSpan(span);
							AreUnderlineSpan spanLeft = new AreUnderlineSpan();
							editable.setSpan(spanLeft, ess, start, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
							AreUnderlineSpan spanRight = new AreUnderlineSpan();
							editable.setSpan(spanRight, end, ese, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
						} else if (start == ess && end < ese) {
							// Case 3 desc:
							// *BBBB*BB
							// *BBBB* is selected, and un-check underline
							editable.removeSpan(span);
							AreUnderlineSpan newSpan = new AreUnderlineSpan();
							editable.setSpan(newSpan, end, ese, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
						} else if (start > ess && end == ese) {
							// Case 4 desc:
							// BB*BBBB*
							// *BBBB* is selected, and un-check underline
							editable.removeSpan(span);
							AreUnderlineSpan newSpan = new AreUnderlineSpan();
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
				AreUnderlineSpan[] spans = editable.getSpans(start, end, AreUnderlineSpan.class);
				if (spans.length > 0) {
					AreUnderlineSpan span = spans[0];
					if (null != span) {
						int underlineStart = editable.getSpanStart(span);
						int underlineEnd = editable.getSpanEnd(span);

						if (underlineStart >= underlineEnd) {
							//
							// Invalid case, this will never happen.
						} else {
							//
							// Do nothing, the default behavior is to extend
							// the span's area.
							// The proceeding characters should be also
							// UNDERLINE
							this.mUnderlineChecked = true;
							ARE_Helper.updateCheckStatus(this, true);
						}
					}
				}
			}
		}
	}

	private void checkAndMergeSpan(Editable editable, int start, int end) {
		AreUnderlineSpan leftSpan = null;
		AreUnderlineSpan[] leftSpans = editable.getSpans(start, start, AreUnderlineSpan.class);
		if (leftSpans.length > 0) {
			leftSpan = leftSpans[0];
		}

		AreUnderlineSpan rightSpan = null;
		AreUnderlineSpan[] rightSpans = editable.getSpans(end, end, AreUnderlineSpan.class);
		if (rightSpans.length > 0) {
			rightSpan = rightSpans[0];
		}
	

		if (leftSpan != null && rightSpan != null) {
			int leftSpanStart = editable.getSpanStart(leftSpan);
			int rightSpanEnd = editable.getSpanEnd(rightSpan);
			editable.removeSpan(leftSpan);
			editable.removeSpan(rightSpan);
			AreUnderlineSpan underlineSpan = new AreUnderlineSpan();
			editable.setSpan(underlineSpan, leftSpanStart, rightSpanEnd, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
		} else if (leftSpan != null && rightSpan == null) {
			int leftSpanStart = editable.getSpanStart(leftSpan);
			editable.removeSpan(leftSpan);
			AreUnderlineSpan underlineSpan = new AreUnderlineSpan();
			editable.setSpan(underlineSpan, leftSpanStart, end, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
		} else if (leftSpan == null && rightSpan != null) {
			int rightSpanEnd = editable.getSpanEnd(rightSpan);
			editable.removeSpan(rightSpan);
			AreUnderlineSpan underlineSpan = new AreUnderlineSpan();
			editable.setSpan(underlineSpan, start, rightSpanEnd, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
		} else {
			AreUnderlineSpan underlineSpan = new AreUnderlineSpan();
			editable.setSpan(underlineSpan, start, end, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
		}
	}

	@Override
	public ImageView getImageView() {
		return this.mUnderlineImageView;
	}

	@Override
	public void setChecked(boolean isChecked) {
		this.mUnderlineChecked = isChecked;
	}
}
