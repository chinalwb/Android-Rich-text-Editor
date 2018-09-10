package com.chinalwb.are.styles;

import com.chinalwb.are.Util;
import com.chinalwb.are.styles.toolbar.ARE_Toolbar;

import android.content.Context;
import android.text.Editable;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.widget.EditText;

import java.lang.reflect.ParameterizedType;

public abstract class ARE_ABS_Style<E> implements IARE_Style {

	protected Context mContext;

	protected Class<E> clazzE;

	public ARE_ABS_Style() {
		this(null);
	}

	public ARE_ABS_Style(Context context) {
		mContext = context;
		if (mContext == null) {
			this.mContext = ARE_Toolbar.getInstance().getContext();
		}
		clazzE = (Class<E>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}

	@Override
	public EditText getEditText() {
		return ARE_Toolbar.getInstance().getEditText();
	}

	@Override
	public void applyStyle(Editable editable, int start, int end) {
		if (getIsChecked()) {
			if (end > start) {
				//
				// User inputs or user selects a range
				E[] spans = editable.getSpans(start, end, clazzE);
				E existingESpan = null;
				if (spans.length > 0) {
					existingESpan = spans[0];
				}

				if (existingESpan == null) {
					checkAndMergeSpan(editable, start, end, clazzE);
				} else {
					int existingESpanStart = editable.getSpanStart(existingESpan);
					int existingESpanEnd = editable.getSpanEnd(existingESpan);
					if (existingESpanStart <= start && existingESpanEnd >= end) {
						// The selection is just within an existing E span
						// Do nothing for this case
						changeSpanInsideStyle(editable, start, end, existingESpan);
					} else {
						checkAndMergeSpan(editable, start, end, clazzE);
					}
				}
			} else {
				//
				// User deletes
				E[] spans = editable.getSpans(start, end, clazzE);
				if (spans.length > 0) {
					E span = spans[0];
					int lastSpanStart = editable.getSpanStart(span);
					for (E e : spans) {
						int lastSpanStartTmp = editable.getSpanStart(e);
						if (lastSpanStartTmp > lastSpanStart) {
							lastSpanStart = lastSpanStartTmp;
							span = e;
						}
					}

					int eStart = editable.getSpanStart(span);
					int eEnd = editable.getSpanEnd(span);
					Util.log("eSpan start == " + eStart + ", eSpan end == " + eEnd);

					if (eStart >= eEnd) {
						editable.removeSpan(span);
						extendPreviousSpan(editable, eStart);

						setChecked(false);
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
			// User un-checks the style
			if (end > start) {
				//
				// User inputs or user selects a range
				E[] spans = editable.getSpans(start, end, clazzE);
				if (spans.length > 0) {
					E span = spans[0];
					if (null != span) {
						//
						// User stops the style, and wants to show
						// un-UNDERLINE characters
						int ess = editable.getSpanStart(span); // ess == existing span start
						int ese = editable.getSpanEnd(span); // ese = existing span end
						if (start >= ese) {
							// User inputs to the end of the existing e span
							// End existing e span
							editable.removeSpan(span);
							editable.setSpan(span, ess, start - 1, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
						} else if (start == ess && end == ese) {
							// Case 1 desc:
							// *BBBBBB*
							// All selected, and un-check e
							editable.removeSpan(span);
						} else if (start > ess && end < ese) {
							// Case 2 desc:
							// BB*BB*BB
							// *BB* is selected, and un-check e
							editable.removeSpan(span);
							E spanLeft = newSpan();
							editable.setSpan(spanLeft, ess, start, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
							E spanRight = newSpan();
							editable.setSpan(spanRight, end, ese, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
						} else if (start == ess && end < ese) {
							// Case 3 desc:
							// *BBBB*BB
							// *BBBB* is selected, and un-check e
							editable.removeSpan(span);
							E newSpan = newSpan();
							editable.setSpan(newSpan, end, ese, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
						} else if (start > ess && end == ese) {
							// Case 4 desc:
							// BB*BBBB*
							// *BBBB* is selected, and un-check e
							editable.removeSpan(span);
							E newSpan = newSpan();
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
				E[] spans = editable.getSpans(start, end, clazzE);
				if (spans.length > 0) {
					E span = spans[0];
					if (null != span) {
						int eStart = editable.getSpanStart(span);
						int eEnd = editable.getSpanEnd(span);

						if (eStart >= eEnd) {
							//
							// Invalid case, this will never happen.
						} else {
							//
							// Do nothing, the default behavior is to extend
							// the span's area.
							// The proceeding characters should be also
							// UNDERLINE
							setChecked(true);
							ARE_Helper.updateCheckStatus(this, true);
						}
					}
				}
			}
		}
	}

	protected void changeSpanInsideStyle(Editable editable, int start, int end, E e) {
		// Do nothing by default
		Log.e("ARE", "in side a span!!");
	}

	private void checkAndMergeSpan(Editable editable, int start, int end, Class<E> clazzE) {
		E leftSpan = null;
		E[] leftSpans = editable.getSpans(start, start, clazzE);
		if (leftSpans.length > 0) {
			leftSpan = leftSpans[0];
		}

		E rightSpan = null;
		E[] rightSpans = editable.getSpans(end, end, clazzE);
		if (rightSpans.length > 0) {
			rightSpan = rightSpans[0];
		}


		int leftSpanStart = editable.getSpanStart(leftSpan);
		int rightSpanEnd = editable.getSpanEnd(rightSpan);
		removeAllSpans(editable, start, end, clazzE);
		if (leftSpan != null && rightSpan != null) {
			E eSpan = newSpan();
			editable.setSpan(eSpan, leftSpanStart, rightSpanEnd, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
		} else if (leftSpan != null && rightSpan == null) {
			E eSpan = newSpan();
			editable.setSpan(eSpan, leftSpanStart, end, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
		} else if (leftSpan == null && rightSpan != null) {
			E eSpan = newSpan();
			editable.setSpan(eSpan, start, rightSpanEnd, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
		} else {
			E eSpan = newSpan();
			editable.setSpan(eSpan, start, end, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
		}
	}

	private void removeAllSpans(Editable editable, int start, int end, Class<E> clazzE) {
		E[] allSpans = editable.getSpans(start, end, clazzE);
		for (E span : allSpans) {
			editable.removeSpan(span);
		}
	}

	protected void extendPreviousSpan(Editable editable, int pos) {
		// Do nothing by default
	}

    public abstract E newSpan();
}
