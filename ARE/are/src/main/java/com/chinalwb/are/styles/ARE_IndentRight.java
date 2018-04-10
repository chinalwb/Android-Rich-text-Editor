package com.chinalwb.are.styles;

import android.text.Editable;
import android.text.Spannable;
import android.text.Spanned;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;

import com.chinalwb.are.Constants;
import com.chinalwb.are.Util;
import com.chinalwb.are.spans.AreLeadingMarginSpan;

public class ARE_IndentRight extends ARE_ABS_FreeStyle {

	public ARE_IndentRight(ImageView atImageView) {
		setListenerForImageView(atImageView);
	}

	@Override
	public void setListenerForImageView(ImageView imageView) {
		imageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				EditText editText = getEditText();
				int currentLine = Util.getCurrentCursorLine(editText);
				int start = Util.getThisLineStart(editText, currentLine);
				int end = Util.getThisLineEnd(editText, currentLine);

				Editable editable = editText.getText();
				// Checks if current line has leading margin already
				// If any, remove;
				// Then apply new leading margin.
				AreLeadingMarginSpan[] existingLeadingSpans = editable.getSpans(start, end, AreLeadingMarginSpan.class);
				if (null != existingLeadingSpans && existingLeadingSpans.length == 1) {
					AreLeadingMarginSpan currentLeadingMarginSpan = existingLeadingSpans[0];
					int originalEnd = editable.getSpanEnd(currentLeadingMarginSpan);
					editable.removeSpan(currentLeadingMarginSpan);
				
					currentLeadingMarginSpan.increaseLevel();
					editable.setSpan(currentLeadingMarginSpan, start, originalEnd, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
				}
				else {
					editable.insert(start, Constants.ZERO_WIDTH_SPACE_STR);
					start = Util.getThisLineStart(editText, currentLine);
					end = Util.getThisLineEnd(editText, currentLine);
					AreLeadingMarginSpan leadingMarginSpan = new AreLeadingMarginSpan(mContext);
					leadingMarginSpan.increaseLevel();
					editable.setSpan(leadingMarginSpan, start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
				}
				
				logAllLeadingSpans(editable);
			}
		});
	}

	@Override
	public void applyStyle(Editable editable, int start, int end) {
		AreLeadingMarginSpan[] leadingSpans = editable.getSpans(start, end, AreLeadingMarginSpan.class);
		if (null == leadingSpans || leadingSpans.length == 0) {
			return;
		}

		if (end > start) {
			//
			// User inputs
			//
			// To handle the \n case
			char c = editable.charAt(end - 1);
			if (c == Constants.CHAR_NEW_LINE) {
				int leadingSpanSize = leadingSpans.length;
				int previousLeadingSpanIndex = leadingSpanSize - 1;
				if (previousLeadingSpanIndex > -1) {
					AreLeadingMarginSpan previousLeadingSpan = leadingSpans[previousLeadingSpanIndex];
					int lastLeadingItemSpanStartPos = editable.getSpanStart(previousLeadingSpan);

					//
					// Handle this case:
					//
					// -> A
					// 
					// User types \n after 'A'
					// Then
					// We should see:
					// -> A
					// -> 
					//
					// We need to end the first span
					// Then start the 2nd span
					// Then reNumber the following list item spans
					if (end > lastLeadingItemSpanStartPos) {
						editable.removeSpan(previousLeadingSpan);
						editable.setSpan(previousLeadingSpan, lastLeadingItemSpanStartPos, end - 1, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
					}
				
					makeLineAsLeadingSpan(previousLeadingSpan.getLevel());
				} // #End of if it is in ListItemSpans..
			} // #End of user types \n
		} 
		else {
			//
			// User deletes
			int spanStart = editable.getSpanStart(leadingSpans[0]);
			int spanEnd = editable.getSpanEnd(leadingSpans[0]);

			Util.log("Delete spanStart = " + spanStart + ", spanEnd = " + spanEnd);

			if (spanStart >= spanEnd) {
				//
				// User deletes the last char of the span
				// So we think he wants to remove the span
				editable.removeSpan(leadingSpans[0]);

				//
				// To delete the previous span's \n
				// So the focus will go to the end of previous span
				if (spanStart > 0) {
					editable.delete(spanStart - 1, spanEnd);
				}
			}
		}

		logAllLeadingSpans(editable);
	}

	
	private AreLeadingMarginSpan makeLineAsLeadingSpan(int level) {
		EditText editText = getEditText();
		int currentLine = Util.getCurrentCursorLine(editText);
		int start = Util.getThisLineStart(editText, currentLine);
		int end = Util.getThisLineEnd(editText, currentLine);
		Editable editable = editText.getText();
		editable.insert(start, Constants.ZERO_WIDTH_SPACE_STR);
		start = Util.getThisLineStart(editText, currentLine);
		end = Util.getThisLineEnd(editText, currentLine);

		if (editable.charAt(end - 1) == Constants.CHAR_NEW_LINE) {
			end--;
		}

		AreLeadingMarginSpan leadingMarginSpan = new AreLeadingMarginSpan(mContext);
		leadingMarginSpan.setLevel(level);
		editable.setSpan(leadingMarginSpan, start, end,	Spannable.SPAN_INCLUSIVE_INCLUSIVE);
		return leadingMarginSpan;
	}

	private void logAllLeadingSpans(Editable editable) {
		AreLeadingMarginSpan[] leadingSpans = editable.getSpans(0,
				editable.length(), AreLeadingMarginSpan.class);
		for (AreLeadingMarginSpan span : leadingSpans) {
			int ss = editable.getSpanStart(span);
			int se = editable.getSpanEnd(span);
			Util.log("List All: Level = " + span.getLevel() + " :: start == " + ss + ", end == " + se);
		}
	}
	
	
	@Override
	public ImageView getImageView() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setChecked(boolean isChecked) {
		// TODO Auto-generated method stub

	}

}
