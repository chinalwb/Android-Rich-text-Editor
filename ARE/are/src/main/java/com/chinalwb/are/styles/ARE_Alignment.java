package com.chinalwb.are.styles;

import android.text.Editable;
import android.text.Layout.Alignment;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.AlignmentSpan;
import android.text.style.AlignmentSpan.Standard;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;

import com.chinalwb.are.Constants;
import com.chinalwb.are.Util;

public class ARE_Alignment extends ARE_ABS_FreeStyle {

	  private ImageView mAlignmentImageView;
	  
	  private Alignment mAlignment;
	  
	  public ARE_Alignment(ImageView imageView, Alignment alignment) {
	    this.mAlignmentImageView = imageView;
	    this.mAlignment = alignment;
	    setListenerForImageView(this.mAlignmentImageView);
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
				
				Editable editable = editText.getEditableText();
				
				Standard[] alignmentSpans = editable.getSpans(start, end, Standard.class);
				if (null != alignmentSpans) {
					for (Standard span : alignmentSpans) {
						editable.removeSpan(span);
					}
				}
				
				AlignmentSpan alignCenterSpan = new Standard(mAlignment);
				if (start == end) {
					editable.insert(start, Constants.ZERO_WIDTH_SPACE_STR);
					end = Util.getThisLineEnd(editText, currentLine);
				}
				editable.setSpan(alignCenterSpan, start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
			}
		});
	}

	@Override
	public void applyStyle(Editable editable, int start, int end) {
		AlignmentSpan[] alignmentSpans = editable.getSpans(start, end, AlignmentSpan.class);
		if (null == alignmentSpans || alignmentSpans.length == 0) {
			return;
		}
		
		Alignment alignment = alignmentSpans[0].getAlignment();
		if (mAlignment != alignment) {
			return;
		}

		if (end > start) {
			//
			// User inputs
			//
			// To handle the \n case
			char c = editable.charAt(end - 1);
			if (c == Constants.CHAR_NEW_LINE) {
				int alignmentSpansSize = alignmentSpans.length;
				int previousAlignmentSpanIndex = alignmentSpansSize - 1;
				if (previousAlignmentSpanIndex > -1) {
					AlignmentSpan previousAlignmentSpan = alignmentSpans[previousAlignmentSpanIndex];
					int lastAlignmentSpanStartPos = editable.getSpanStart(previousAlignmentSpan);
					if (end > lastAlignmentSpanStartPos) {
						editable.removeSpan(previousAlignmentSpan);
						editable.setSpan(previousAlignmentSpan, lastAlignmentSpanStartPos, end - 1, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
					}
				
					markLineAsAlignmentSpan(mAlignment);
				}  
			} // #End of user types \n
		} 
		else {
			//
			// User deletes
			int spanStart = editable.getSpanStart(alignmentSpans[0]);
			int spanEnd = editable.getSpanEnd(alignmentSpans[0]);

			if (spanStart >= spanEnd) {
				//
				// User deletes the last char of the span
				// So we think he wants to remove the span
				editable.removeSpan(alignmentSpans[0]);

				//
				// To delete the previous span's \n
				// So the focus will go to the end of previous span
				if (spanStart > 0) {
					editable.delete(spanStart - 1, spanEnd);
				}
			}
		}
	}

	private void markLineAsAlignmentSpan(Alignment alignment) {
		EditText editText = getEditText();
		int currentLine = Util.getCurrentCursorLine(editText);
		int start = Util.getThisLineStart(editText, currentLine);
		int end = Util.getThisLineEnd(editText, currentLine);
		Editable editable = editText.getText();
		editable.insert(start, Constants.ZERO_WIDTH_SPACE_STR);
		start = Util.getThisLineStart(editText, currentLine);
		end = Util.getThisLineEnd(editText, currentLine);

		if (end < 1) {
			return;
		}

		if (editable.charAt(end - 1) == Constants.CHAR_NEW_LINE) {
			end--;
		}

		AlignmentSpan alignmentSpan = new Standard(alignment);
		editable.setSpan(alignmentSpan, start, end,	Spannable.SPAN_INCLUSIVE_INCLUSIVE);
	}

	@Override
	public ImageView getImageView() {
		return this.mAlignmentImageView;
	}

	@Override
	public void setChecked(boolean isChecked) {
		// TODO Auto-generated method stub
	}
}
