package com.chinalwb.are.styles.toolitems.styles;

import android.text.Editable;
import android.text.Spannable;
import android.text.Spanned;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;

import com.chinalwb.are.AREditText;
import com.chinalwb.are.Constants;
import com.chinalwb.are.Util;
import com.chinalwb.are.spans.ListBulletSpan;
import com.chinalwb.are.spans.ListNumberSpan;
import com.chinalwb.are.styles.ARE_ABS_FreeStyle;
import com.chinalwb.are.styles.ARE_ListNumber;

/**
 * All Rights Reserved.
 * 
 * @author Wenbin Liu
 * 
 */
public class ARE_Style_ListBullet extends ARE_ABS_FreeStyle {

	private AREditText mEditText;

	private ImageView mListBulletImageView;

	public ARE_Style_ListBullet(AREditText editText, ImageView imageView) {
		super(editText.getContext());
		this.mEditText = editText;
		this.mListBulletImageView = imageView;
		setListenerForImageView(this.mListBulletImageView);
	}

	@Override
	public EditText getEditText() {
		return this.mEditText;
	}

	@Override
	public void setListenerForImageView(final ImageView imageView) {
		imageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				EditText editText = getEditText();
				int currentLine = Util.getCurrentCursorLine(editText);
				int start = Util.getThisLineStart(editText, currentLine);
				int end = Util.getThisLineEnd(editText, currentLine);

				Editable editable = editText.getText();

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
				
				int selectionStart = editText.getSelectionStart();
				int selectionEnd = editText.getSelectionEnd();
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
										Spannable.SPAN_INCLUSIVE_INCLUSIVE);
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
									Spanned.SPAN_INCLUSIVE_INCLUSIVE);
						}
					}
					makeLineAsBullet();
				} // #End of if it is in ListItemSpans..
			} // #End of user types \n
		} else {
			//
			// User deletes
			ListBulletSpan theFirstSpan = listSpans[0];
			if (listSpans.length > 0) {
				FindFirstAndLastBulletSpan findFirstAndLastBulletSpan = new FindFirstAndLastBulletSpan(editable, listSpans).invoke();
				theFirstSpan = findFirstAndLastBulletSpan.getFirstTargetSpan();
			}
			int spanStart = editable.getSpanStart(theFirstSpan);
			int spanEnd = editable.getSpanEnd(theFirstSpan);

			Util.log("Delete spanStart = " + spanStart + ", spanEnd = " + spanEnd);

			if (spanStart >= spanEnd) {
				Util.log("case 1");
				//
				// User deletes the last char of the span
				// So we think he wants to remove the span
				for (ListBulletSpan listSpan : listSpans) {
					editable.removeSpan(listSpan);
				}

				//
				// To delete the previous span's \n
				// So the focus will go to the end of previous span
				if (spanStart > 0) {
					editable.delete(spanStart - 1, spanEnd);
				}
			} else if (start == spanStart) {
				return;
			} else if (start == spanEnd) {
				Util.log("case 3");
				//
				// User deletes the first char of the span
				// So we think he wants to remove the span
				if (editable.length() > start) {
					if (editable.charAt(start) == Constants.CHAR_NEW_LINE) {
						// The error case to handle
						Util.log("case 3-1");
						ListBulletSpan[] spans = editable.getSpans(start, start, ListBulletSpan.class);
						Util.log(" spans len == " + spans.length);
						if (spans.length > 0) {
							mergeForward(editable, theFirstSpan, spanStart, spanEnd);
						}
					} else {
						mergeForward(editable, theFirstSpan, spanStart, spanEnd);
					}
				}
			} else if (start > spanStart && end < spanEnd) {
				//
				// Handle this case:
				// *. AAA1
				// *. BBB2
				// *. CCC3
				//
				// User deletes '1' / '2' / '3'
				// Or any other character inside of a span
				//
				// For this case we won't need do anything
				// As we need to keep the span styles as they are
				return;
			}
		}

		logAllBulletListItems(editable);
	} // # End of applyStyle(..)

	protected void mergeForward(Editable editable, ListBulletSpan listSpan, int spanStart, int spanEnd) {
		Util.log("merge forward 1");
		if (editable.length() <= spanEnd + 1) {
			return;
		}
		Util.log("merge forward 2");
		ListBulletSpan[] targetSpans = editable.getSpans(
				spanEnd, spanEnd + 1, ListBulletSpan.class);
		if (targetSpans == null || targetSpans.length == 0) {
			return;
		}

		FindFirstAndLastBulletSpan findFirstAndLastBulletSpan = new FindFirstAndLastBulletSpan(editable, targetSpans).invoke();
		ListBulletSpan firstTargetSpan = findFirstAndLastBulletSpan.getFirstTargetSpan();
		ListBulletSpan lastTargetSpan = findFirstAndLastBulletSpan.getLastTargetSpan();
		int targetStart = editable.getSpanStart(firstTargetSpan);
		int targetEnd = editable.getSpanEnd(lastTargetSpan);
		Util.log("merge to remove span start == " + targetStart + ", target end = " + targetEnd);

		int targetLength = targetEnd - targetStart;
		spanEnd = spanEnd + targetLength;

		for (ListBulletSpan targetSpan : targetSpans) {
			editable.removeSpan(targetSpan);
		}
		ListBulletSpan[] compositeSpans = editable.getSpans(spanStart, spanEnd, ListBulletSpan.class);
		for (ListBulletSpan lns : compositeSpans) {
			editable.removeSpan(lns);
		}
		editable.setSpan(listSpan, spanStart, spanEnd, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
		Util.log("merge span start == " + spanStart + " end == " + spanEnd);
	}

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
		EditText editText = getEditText();
		int currentLine = Util.getCurrentCursorLine(editText);
		int start = Util.getThisLineStart(editText, currentLine);
		Editable editable = editText.getText();
		editable.insert(start, Constants.ZERO_WIDTH_SPACE_STR);
		start = Util.getThisLineStart(editText, currentLine);
		int end = Util.getThisLineEnd(editText, currentLine);

		if (end < 1) {
			return null;
		}
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
        editable.delete(lastListNumberSpanEnd + 1, lastListNumberSpanEnd + 1);
        // -- End: Change the content to trigger the editable redraw
        
		ARE_ListNumber.reNumberBehindListItemSpans(lastListNumberSpanEnd + 1, editable, 0);
		
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

	private class FindFirstAndLastBulletSpan {
		private Editable editable;
		private ListBulletSpan[] targetSpans;
		private ListBulletSpan firstTargetSpan;
		private ListBulletSpan lastTargetSpan;

		public FindFirstAndLastBulletSpan(Editable editable, ListBulletSpan... targetSpans) {
			this.editable = editable;
			this.targetSpans = targetSpans;
		}

		public ListBulletSpan getFirstTargetSpan() {
			return firstTargetSpan;
		}

		public ListBulletSpan getLastTargetSpan() {
			return lastTargetSpan;
		}

		public FindFirstAndLastBulletSpan invoke() {
			firstTargetSpan = targetSpans[0];
			lastTargetSpan = targetSpans[0];
			if (targetSpans.length > 0) {
                int firstTargetSpanStart = editable.getSpanStart(firstTargetSpan);
                int lastTargetSpanEnd = editable.getSpanEnd(firstTargetSpan);
                for (ListBulletSpan lns : targetSpans) {
                    int lnsStart = editable.getSpanStart(lns);
                    int lnsEnd = editable.getSpanEnd(lns);
                    if (lnsStart < firstTargetSpanStart) {
                        firstTargetSpan = lns;
                        firstTargetSpanStart = lnsStart;
                    }
                    if (lnsEnd > lastTargetSpanEnd) {
                        lastTargetSpan = lns;
                        lastTargetSpanEnd = lnsEnd;
                    }
                }
            }
			return this;
		}
	}
}
