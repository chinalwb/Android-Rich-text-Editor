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

import java.util.List;

/**
 * All Rights Reserved.
 *
 * @author Wenbin Liu
 */
public class ARE_ListNumber extends ARE_ABS_FreeStyle {

    private ImageView mListNumberImageView;

    private boolean toMergeForward = false;

    public ARE_ListNumber(ImageView imageView) {
        this.mListNumberImageView = imageView;
        setListenerForImageView(this.mListNumberImageView);
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
                // * aa
                // * bb
                // * cc
                //
                // Then user clicks the Number icon at 1 or 2 or any other item
                // He wants to change current ListBulletSpan to ListNumberSpan
                //
                // So it becomes:
                // For example: user clicks Number icon at 2:
                // * aa
                // 1. bb
                // * cc

                int selectionStart = editText.getSelectionStart();
                int selectionEnd = editText.getSelectionEnd();
                ListBulletSpan[] listBulletSpans = editable.getSpans(selectionStart,
                        selectionEnd, ListBulletSpan.class);
                if (null != listBulletSpans && listBulletSpans.length > 0) {
                    changeListBulletSpanToListNumberSpan(editable, listBulletSpans);
                    return;
                }

                ListNumberSpan[] listNumberSpans = editable.getSpans(start, end,
                        ListNumberSpan.class);
                if (null == listNumberSpans || listNumberSpans.length == 0) {
                    //
                    // Current line is not list item span
                    // By clicking the image view, we should make it as
                    // ListItemSpan
                    // And ReOrder
                    //
                    // ------------ CASE 1 ------------------
                    // Case 1:
                    // Nothing types in, user just clicks the List image
                    // For this case we need to mark it as ListItemSpan

                    //
                    // ------------ CASE 2 ------------------
                    // Case 2:
                    // Before or after the current line, there are already
                    // ListItemSpan have been made
                    // Like:
                    // 1. AAA
                    // BBB
                    // 1. CCC
                    //
                    // User puts cursor to the 2nd line: BBB
                    // And clicks the List image
                    // For this case we need to make current line as
                    // ListItemSpan
                    // And, we should also reOrder them as:
                    //
                    // 1. AAA
                    // 2. BBB
                    // 3. CCC
                    //

                    // if (end > 0) {} // #End of if (end > 0)

                    //
                    // Case 2
                    //
                    // There are list item spans ahead current editing
                    int thisNumber = 1;
                    ListNumberSpan[] aheadListItemSpans = editable.getSpans(
                            start - 2, start - 1, ListNumberSpan.class);
                    if (null != aheadListItemSpans
                            && aheadListItemSpans.length > 0) {
                        ListNumberSpan previousListItemSpan = aheadListItemSpans[aheadListItemSpans.length - 1];
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

                            int previousNumber = previousListItemSpan
                                    .getNumber();
                            thisNumber = previousNumber + 1;
                            makeLineAsList(thisNumber);
                        }
                    } else {
                        //
                        // Case 1
                        thisNumber = 1;
                        makeLineAsList(1);
                    }

                    //
                    // Case 2
                    //
                    // Handle behind list item spans
                    // reorder them
                    // int totalLength = editable.toString().length();
                    // if (totalLength > end) {}
                    reNumberBehindListItemSpans(end, editable, thisNumber);
                } else {
                    //
                    // Current line is list item span
                    // By clicking the image view, we should remove the
                    // ListItemSpan
                    ListNumberSpan currentLineListItemSpan = listNumberSpans[0];
                    int spanEnd = editable.getSpanEnd(currentLineListItemSpan);
                    editable.removeSpan(currentLineListItemSpan);

                    //
                    // Change the content to trigger the editable redraw
                    editable.insert(spanEnd, Constants.ZERO_WIDTH_SPACE_STR);
                    editable.delete(spanEnd, spanEnd + 1);

                    //
                    // The new list should start from 1
                    reNumberBehindListItemSpans(spanEnd, editable, 0);
                }
            }
        });
    }

    @Override
    public void applyStyle(Editable editable, int start, int end) {
        // logAllListItems(editable, true);
        ListNumberSpan[] listSpans = editable.getSpans(start, end,
                ListNumberSpan.class);
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
                    ListNumberSpan previousListSpan = listSpans[previousListSpanIndex];
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

                        //
                        // Restart the counting for the list item spans after
                        // previousListSpan
                        reNumberBehindListItemSpans(lastListItemSpanStartPos,
                                editable, 0);
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
                    int lastListItemNumber = previousListSpan.getNumber();
                    int thisNumber = lastListItemNumber + 1;
                    ListNumberSpan newListItemSpan = makeLineAsList(thisNumber);
                    end = editable.getSpanEnd(newListItemSpan);
                    reNumberBehindListItemSpans(end, editable, thisNumber);
                } // #End of if it is in ListItemSpans..
            } // #End of user types \n
        } else {
            //
            // User deletes
            int spanStart = editable.getSpanStart(listSpans[0]);
            int spanEnd = editable.getSpanEnd(listSpans[0]);
            ListNumberSpan theFirstSpan = listSpans[0];
            if (listSpans.length > 1) {
                int theFirstSpanNumber = theFirstSpan.getNumber();
                for (ListNumberSpan lns : listSpans) {
                    if (lns.getNumber() < theFirstSpanNumber) {
                        theFirstSpan = lns;
                    }
                }
                spanStart = editable.getSpanStart(theFirstSpan);
                spanEnd = editable.getSpanEnd(theFirstSpan);
            }


            Util.log("Delete spanStart = " + spanStart + ", spanEnd = "
                    + spanEnd + " ,, start == " + start);
            if (spanStart >= spanEnd) {
                Util.log("case 1");
                //
                // User deletes the last char of the span
                // So we think he wants to remove the span
                for (ListNumberSpan listSpan : listSpans) {
                    editable.removeSpan(listSpan);
                }

                //
                // To delete the previous span's \n
                // So the focus will go to the end of previous span
                if (spanStart > 0) {
                    editable.delete(spanStart - 1, spanEnd);
                }

                if (editable.length() > spanEnd) {
                    ListNumberSpan[] spansBehind = editable.getSpans(spanEnd, spanEnd + 1, ListNumberSpan.class);
                    if (spansBehind.length > 0) {
                        int removedNumber = theFirstSpan.getNumber();
                        reNumberBehindListItemSpans(spanStart, editable,
                                removedNumber - 1);
                    }
                }
            } else if (start == spanStart) {
                Util.log("case 2");
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
                        ListNumberSpan[] spans = editable.getSpans(start, start, ListNumberSpan.class);
                        Util.log(" spans len == " + spans.length);
                        if (spans.length > 0) {
                            Util.log("case 3-1-1");
                            mergeForward(editable, theFirstSpan, spanStart, spanEnd);
                        } else {
                            Util.log("case 3-1-2");
                            editable.removeSpan(spans[0]);
                        }
                    } else {
                        mergeForward(editable, theFirstSpan, spanStart, spanEnd);
                    }
                }
            } else if (start > spanStart && end < spanEnd) {
                Util.log("case 4");
                //
                // Handle this case:
                // 1. AAA1
                // 2. BBB2
                // 3. CCC3
                //
                // User deletes '1' / '2' / '3'
                // Or any other character inside of a span
                //
                // For this case we won't need do anything
                // As we need to keep the span styles as they are
                return;
            } else {
                Util.log("case X");
                if (editable.length() > start) {
                    Util.log("start char == " + (int) editable.charAt(start));
                }
                //
                // Handle this case:
                // 1. A
                // 2. B
                // x
                // 1. C
                // 2. D
                //
                // When user deletes the "x"
                // Then merge two lists, so it should be changed to:
                // 1. A
                // 2. B
                // 3. C
                // 4. D
                //
                // mergeLists();
                int previousNumber = theFirstSpan.getNumber();
                reNumberBehindListItemSpans(end, editable, previousNumber);
            }
        }
    } // # End of applyStyle(..)

    protected void mergeForward(Editable editable, ListNumberSpan listSpan, int spanStart, int spanEnd) {
        Util.log("merge forward 1");
        if (editable.length() <= spanEnd + 1) {
            return;
        }
        Util.log("merge forward 2");
        ListNumberSpan[] targetSpans = editable.getSpans(spanEnd, spanEnd + 1, ListNumberSpan.class);
        // logAllListItems(editable, false);
        if (targetSpans == null || targetSpans.length == 0) {
            reNumberBehindListItemSpans(spanEnd, editable, listSpan.getNumber());
            return;
        }
        ListNumberSpan firstTargetSpan = targetSpans[0];
        ListNumberSpan lastTargetSpan = targetSpans[0];

        if (targetSpans.length > 0) {
            int firstTargetSpanNumber = firstTargetSpan.getNumber();
            int lastTargetSpanNumber = lastTargetSpan.getNumber();
            for (ListNumberSpan lns : targetSpans) {
                int lnsNumber = lns.getNumber();
                if (lnsNumber < firstTargetSpanNumber) {
                    firstTargetSpan = lns;
                    firstTargetSpanNumber = lnsNumber;
                }
                if (lnsNumber > lastTargetSpanNumber) {
                    lastTargetSpan = lns;
                    lastTargetSpanNumber = lnsNumber;
                }
            }
        }
        int targetStart = editable.getSpanStart(firstTargetSpan);
        int targetEnd = editable.getSpanEnd(lastTargetSpan);
        Util.log("merge to remove span start == " + targetStart + ", target end = " + targetEnd + ", target number = " + firstTargetSpan.getNumber());

        int targetLength = targetEnd - targetStart;
        spanEnd = spanEnd + targetLength;
        for (ListNumberSpan targetSpan : targetSpans) {
            editable.removeSpan(targetSpan);
        }
        ListNumberSpan[] compositeSpans = editable.getSpans(spanStart, spanEnd, ListNumberSpan.class);
        for (ListNumberSpan lns : compositeSpans) {
            editable.removeSpan(lns);
        }
        editable.setSpan(listSpan, spanStart, spanEnd,
                    Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        Util.log("merge span start == " + spanStart + " end == " + spanEnd);
        reNumberBehindListItemSpans(spanEnd, editable, listSpan.getNumber());
    }

    private void logAllListItems(Editable editable, boolean printDetail) {
        ListNumberSpan[] listItemSpans = editable.getSpans(0,
                editable.length(), ListNumberSpan.class);
        for (ListNumberSpan span : listItemSpans) {
            int ss = editable.getSpanStart(span);
            int se = editable.getSpanEnd(span);
            int flag = editable.getSpanFlags(span);
            Util.log("List All: " + span.getNumber() + " :: start == " + ss
                    + ", end == " + se + ", flag == " + flag);
           if (printDetail) {
               for (int i = ss; i < se; i++) {
                   Util.log("char at " + i + " = " + editable.charAt(i) + " int = " + ((int) (editable.charAt(i))));
               }

               if (editable.length() > se) {
                   Util.log("char at " + se + " = " + editable.charAt(se)+ " int = " + ((int) (editable.charAt(se))));
               }
           }
        }
    }

    /**
     * Check if this is an empty span.
     * <p>
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
     *
     */
    private ListNumberSpan makeLineAsList(int num) {
        EditText editText = getEditText();
        int currentLine = Util.getCurrentCursorLine(editText);
        int start = Util.getThisLineStart(editText, currentLine);
        int end = Util.getThisLineEnd(editText, currentLine);
        Editable editable = editText.getText();
        editable.insert(start, Constants.ZERO_WIDTH_SPACE_STR);
        start = Util.getThisLineStart(editText, currentLine);
        end = Util.getThisLineEnd(editText, currentLine);

        if (end > 0 && editable.charAt(end - 1) == Constants.CHAR_NEW_LINE) {
            end--;
        }

        ListNumberSpan listItemSpan = new ListNumberSpan(num);
        editable.setSpan(listItemSpan, start, end,
                Spannable.SPAN_INCLUSIVE_INCLUSIVE);

        return listItemSpan;
    }

    /**
     * @param end
     * @param editable
     * @param thisNumber
     */
    public static void reNumberBehindListItemSpans(int end, Editable editable,
                                                   int thisNumber) {
        ListNumberSpan[] behindListItemSpans = editable.getSpans(end + 1,
                end + 2, ListNumberSpan.class);
        if (null != behindListItemSpans && behindListItemSpans.length > 0) {
            int total = behindListItemSpans.length;
            int index = 0;
            for (ListNumberSpan listItemSpan : behindListItemSpans) {
                int newNumber = ++thisNumber;
                Util.log("Change old number == " + listItemSpan.getNumber()
                        + " to new number == " + newNumber);
                listItemSpan.setNumber(newNumber);
                ++index;
                if (total == index) {
                    int newSpanEnd = editable.getSpanEnd(listItemSpan);
                    reNumberBehindListItemSpans(newSpanEnd, editable, newNumber);
                }
            }
        }
    }


    /**
     * Change the selected {@link ListBulletSpan} to {@link ListNumberSpan}
     *
     * @param editable
     * @param listBulletSpans
     */
    protected void changeListBulletSpanToListNumberSpan(Editable editable,
                                                        ListBulletSpan[] listBulletSpans) {

        if (null == listBulletSpans || listBulletSpans.length == 0) {
            return;
        }


        // -
        // Handle this case:
        // User has:
        //
        // * AA
        // * BB
        // 1. CC
        // 2. DD
        //
        // Then user clicks Bullet icon at line 2:
        //
        // So it should change to:
        // * AA
        // 1. BB
        // 2. CC
        // 3. DD
        //
        // So this is for handling the line after 2nd line.
        // "CC" should be changed from 1 to 2
        //
        // - Restart the count after the bullet span
        int len = listBulletSpans.length;
        ListBulletSpan lastListBulletSpan = listBulletSpans[len - 1];

        // -- Remember the last list number span end
        // -- Because this list number span will be replaced with
        // -- ListBulletSpan after the loop, we won't be able to
        // -- get the last ListNumberSpan end after the replacement.
        // --
        // -- After this pos (lastListNumberSpanEnd), if there are
        // -- any ListNumberSpan, we would like to concat them with
        // -- our current editing : i.e.: we are changing the
        // -- ListBulletSpan to ListNumberSpan
        // -- If after the changing, the last ListNumberSpan's number
        // -- is X, then the following ListNumberSpan should starts
        // -- from X + 1.
        int lastListNumberSpanEnd = editable.getSpanEnd(lastListBulletSpan);

        //
        // - Replace all ListBulletSpan to ListNumberSpan
        //
        int previousListNumber = 0;

        //
        // Gets the previous list span number
        //
        // For handling this case:
        //
        // 1. AA
        // * BB
        //
        // When user clicks Number icon at line 2
        // It should change to:
        // 1. AA
        // 2. BB
        //
        // So the number of the new generated ListNumberSpan should
        // start from the previous ListNumberSpan
        ListBulletSpan firstListBulletSpan = listBulletSpans[0];
        int firstListBulletSpanStart = editable.getSpanStart(firstListBulletSpan);
        if (firstListBulletSpanStart > 2) {
            ListNumberSpan[] previousListNumberSpans = editable.getSpans(
                    firstListBulletSpanStart - 2,
                    firstListBulletSpanStart - 1,
                    ListNumberSpan.class);
            if (null != previousListNumberSpans && previousListNumberSpans.length > 0) {
                previousListNumber = previousListNumberSpans[previousListNumberSpans.length - 1].getNumber();
            }
        }

        for (ListBulletSpan listBulletSpan : listBulletSpans) {
            int start = editable.getSpanStart(listBulletSpan);
            int end = editable.getSpanEnd(listBulletSpan);

            editable.removeSpan(listBulletSpan);
            previousListNumber++;
            ListNumberSpan listNumberSpan = new ListNumberSpan(previousListNumber);
            editable.setSpan(listNumberSpan, start, end,
                    Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        }

        // -- Change the content to trigger the editable redraw
        editable.insert(lastListNumberSpanEnd, Constants.ZERO_WIDTH_SPACE_STR);
        editable.delete(lastListNumberSpanEnd + 1, lastListNumberSpanEnd + 1);
        // -- End: Change the content to trigger the editable redraw

        ARE_ListNumber.reNumberBehindListItemSpans(lastListNumberSpanEnd + 1,
                editable, previousListNumber);
    }

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