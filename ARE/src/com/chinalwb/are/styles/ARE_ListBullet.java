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
import com.chinalwb.are.spans.ListBulletSpan;
import com.chinalwb.are.spans.ListNumberSpan;

/**
 * All Rights Reserved.
 * 
 * @author Wenbin Liu
 * 
 */
public class ARE_ListBullet implements IARE_Style {

	private ImageView mListBulletImageView;

	private EditText mEditText;

	public ARE_ListBullet(ImageView imageView, EditText editText) {
		this.mListBulletImageView = imageView;
		this.mEditText = editText;
		setListenerForImageView(this.mListBulletImageView);
	}

	@Override
	public void setListenerForImageView(final ImageView imageView) {
		imageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int currentLine = Util.getCurrentCursorLine(mEditText);
				int start = Util.getThisLineStart(mEditText, currentLine);
				int end = Util.getThisLineEnd(mEditText, currentLine);

				Editable editable = mEditText.getText();

				//
				// Check if there is any ListNumberSpan first.
				// If there is ListNumberSpan, it means this case:
				// User has typed in:
				//
				// 1. aa
				// 2. bb
				// 3. cc
				//
				// Then user clicks the Bullet icon at 1 or 2 or any other item
				// He wants to change current ListNumberSpan to ListBulletSpan
				// 
				// So it becomes:
				// For example: user clicks Bullet icon at 2:
				// 1. aa
				// * bb
				// 1. cc
				//
				// Note that "cc" has been restarted from 1
				
				int selectionStart = mEditText.getSelectionStart();
				int selectionEnd = mEditText.getSelectionEnd();
				ListNumberSpan[] listNumberSpans = editable.getSpans(selectionStart,
						selectionEnd, ListNumberSpan.class);
				if (null != listNumberSpans && listNumberSpans.length > 0) {
					changeListNumberSpanToListBulletSpan(editable, listNumberSpans);
					return;
				}

				//
				// Normal cases
				// 
				ListBulletSpan[] listBulletSpans = editable.getSpans(start,
						end, ListBulletSpan.class);
				if (null == listBulletSpans || listBulletSpans.length == 0) {
					//
					// Current line is not list item span
					// By clicking the image view, we should make it as
					// BulletListItemSpan
					// And ReOrder
					//
					// ------------- CASE 1 ---------------
					// Case 1:
					// Nothing types in, user just clicks the List image
					// For this case we need to mark it as BulletListItemSpan

					//
					// -------------- CASE 2 --------------
					// Case 2:
					// Before or after the current line, there are already
					// BulletListItemSpan have been made
					// Like:
					// 1. AAA
					// BBB
					// 1. CCC
					//
					// User puts cursor to the 2nd line: BBB
					// And clicks the List image
					// For this case we need to make current line as
					// BulletListItemSpan
					// And, we should also reOrder them as:
					//
					// 1. AAA
					// 2. BBB
					// 3. CCC
					//

					//
					// Case 2
					//
					// There are list item spans ahead current editing
					ListBulletSpan[] aheadListItemSpans = editable.getSpans(
							start - 2, start - 1, ListBulletSpan.class);
					if (null != aheadListItemSpans
							&& aheadListItemSpans.length > 0) {
						ListBulletSpan previousListItemSpan = aheadListItemSpans[aheadListItemSpans.length - 1];
						if (null != previousListItemSpan) {
							int pStart = editable
									.getSpanStart(previousListItemSpan);
							int pEnd = editable
									.getSpanEnd(previousListItemSpan);

							//
							// Handle this case:
							// 1. A
							// B
							// C
							// 1. D
							//
							// User puts focus to B and click List icon, to
							// change it to:
							// 2. B
							//
							// Then user puts focus to C and click List icon, to
							// change it to:
							// 3. C
							// For this one, we need to finish the span "2. B"
							// correctly
							// Which means we need to set the span end to a
							// correct value
							// This is doing this.
							if (editable.charAt(pEnd - 1) == Constants.CHAR_NEW_LINE) {
								editable.removeSpan(previousListItemSpan);
								editable.setSpan(previousListItemSpan, pStart,
										pEnd - 1,
										Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
							}

							makeLineAsBullet();
						}
					} else {
						//
						// Case 1
						makeLineAsBullet();
						return;
					}
				} else {
					//
					// Current line is list item span
					// By clicking the image view, we should remove the
					// BulletListItemSpan
					//
					editable.removeSpan(listBulletSpans[0]);
				}
			}
		});
	}

	@Override
	public void applyStyle(Editable editable, int start, int end) {
		logAllBulletListItems(editable);
		ListBulletSpan[] listSpans = editable.getSpans(start, end,
				ListBulletSpan.class);
		if (null == listSpans || listSpans.length == 0) {
			return;
		}

		if (end > start) {
			//
			// User inputs
			//
			// To handle the \n case

			// int totalLen = editable.toString().length();
			// Util.log("ListNumber - total len == " + totalLen);
			char c = editable.charAt(end - 1);
			if (c == Constants.CHAR_NEW_LINE) {
				int listSpanSize = listSpans.length;
				int previousListSpanIndex = listSpanSize - 1;
				if (previousListSpanIndex > -1) {
					ListBulletSpan previousListSpan = listSpans[previousListSpanIndex];
					int lastListItemSpanStartPos = editable
							.getSpanStart(previousListSpan);
					int lastListItemSpanEndPos = editable
							.getSpanEnd(previousListSpan);
					CharSequence listItemSpanContent = editable.subSequence(
							lastListItemSpanStartPos, lastListItemSpanEndPos);

					if (isEmptyListItemSpan(listItemSpanContent)) {
						//
						// Handle this case:
						// 1. A
						// 2. <User types \n here, at an empty span>
						//
						// The 2 chars are:
						// 1. ZERO_WIDTH_SPACE_STR
						// 2. \n
						//
						// We need to remove current span and do not re-create
						// span.
						editable.removeSpan(previousListSpan);

						//
						// Deletes the ZERO_WIDTH_SPACE_STR and \n
						editable.delete(lastListItemSpanStartPos,
								lastListItemSpanEndPos);
						return;
					} else {
						//
						// Handle this case:
						//
						// 1. A
						// 2. C
						// 3. D
						//
						// User types \n after 'A'
						// Then
						// We should see:
						// 1. A
						// 2.
						// 3. C
						// 4. D
						//
						// We need to end the first span
						// Then start the 2nd span
						// Then reNumber the following list item spans
						if (end > lastListItemSpanStartPos) {
							editable.removeSpan(previousListSpan);
							editable.setSpan(previousListSpan,
									lastListItemSpanStartPos, end - 1,
									Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
						}
					}
					makeLineAsBullet();
				} // #End of if it is in ListItemSpans..
			} // #End of user types \n
		} else {
			//
			// User deletes
			int spanStart = editable.getSpanStart(listSpans[0]);
			int spanEnd = editable.getSpanEnd(listSpans[0]);

			Util.log("Delete spanStart = " + spanStart + ", spanEnd = "
					+ spanEnd);

			if (spanStart >= spanEnd) {
				//
				// User deletes the last char of the span
				// So we think he wants to remove the span
				editable.removeSpan(listSpans[0]);

				//
				// To delete the previous span's \n
				// So the focus will go to the end of previous span
				if (spanStart > 0) {
					editable.delete(spanStart - 1, spanEnd);
				}
			}
		}

		logAllBulletListItems(editable);
	} // # End of applyStyle(..)

	private void logAllBulletListItems(Editable editable) {
		ListBulletSpan[] listItemSpans = editable.getSpans(0,
				editable.length(), ListBulletSpan.class);
		for (ListBulletSpan span : listItemSpans) {
			int ss = editable.getSpanStart(span);
			int se = editable.getSpanEnd(span);
			Util.log("List All: " + " :: start == " + ss + ", end == " + se);
		}
	}

	/**
	 * Check if this is an empty span.
	 * 
	 * <B>OLD COMMENT: and whether it is at the end of the spans list</B>
	 * 
	 * @param listItemSpanContent
	 * @return
	 */
	private boolean isEmptyListItemSpan(CharSequence listItemSpanContent) {
		int spanLen = listItemSpanContent.length();
		if (spanLen == 2) {
			//
			// This case:
			// 1. A
			// 2.
			//
			// Line 2 is empty
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 
	 * @return
	 */
	private ListBulletSpan makeLineAsBullet() {
		int currentLine = Util.getCurrentCursorLine(mEditText);
		int start = Util.getThisLineStart(mEditText, currentLine);
		int end = Util.getThisLineEnd(mEditText, currentLine);
		Editable editable = mEditText.getText();
		editable.insert(start, Constants.ZERO_WIDTH_SPACE_STR);
		start = Util.getThisLineStart(mEditText, currentLine);
		end = Util.getThisLineEnd(mEditText, currentLine);

		if (editable.charAt(end - 1) == Constants.CHAR_NEW_LINE) {
			end--;
		}

		ListBulletSpan BulletListItemSpan = new ListBulletSpan();
		editable.setSpan(BulletListItemSpan, start, end,
				Spannable.SPAN_INCLUSIVE_INCLUSIVE);

		return BulletListItemSpan;
	}

	/**
	 * Change the selected {@link ListNumberSpan} to {@link ListBulletSpan}
	 * 
	 * @param listNumberSpans
	 */
	private void changeListNumberSpanToListBulletSpan(
			Editable editable,
			ListNumberSpan[] listNumberSpans) {

		if (null == listNumberSpans || listNumberSpans.length == 0) {
			return;
		}

		// - 
		// Handle this case:
		// User has:
		// 
		// 1. AA
		// 2. BB
		// 3. CC
		// 4. DD
		//
		// Then user clicks Bullet icon at line 2:
		//
		// So it should change to:
		// 1. AA
		// * BB
		// 1. CC
		// 2. DD 
		// 
		// So this is for handling the line after 2nd line.
		// "CC" starts from 1 again.
		// 
		// - Restart the count after the bullet span
		int len = listNumberSpans.length;
		ListNumberSpan lastListNumberSpan = listNumberSpans[len - 1];
		int lastListNumberSpanEnd = editable.getSpanEnd(lastListNumberSpan);
		
		// -- Change the content to trigger the editable redraw
        editable.insert(lastListNumberSpanEnd, Constants.ZERO_WIDTH_SPACE_STR); 
        editable.delete(lastListNumberSpanEnd, lastListNumberSpanEnd + 1);
        // -- End: Change the content to trigger the editable redraw
        
		ARE_ListNumber.reNumberBehindListItemSpans(lastListNumberSpanEnd, editable, 0);
		
		// 
		// - Replace all ListNumberSpan to ListBulletSpan
		for (ListNumberSpan listNumberSpan : listNumberSpans) {
			int start = editable.getSpanStart(listNumberSpan);
			int end = editable.getSpanEnd(listNumberSpan);
			
			editable.removeSpan(listNumberSpan);
			ListBulletSpan listBulletSpan = new ListBulletSpan();
			editable.setSpan(listBulletSpan, start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
		}
		
	} // #End of changeListNumberSpansToListBulletSpans(..)

	@Override
	public ImageView getImageView() {
		// Do nothing
		return null;
	}

	@Override
	public void setChecked(boolean isChecked) {
		// Do nothing
	}

}
