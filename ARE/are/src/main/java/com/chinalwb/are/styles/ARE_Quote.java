package com.chinalwb.are.styles;

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
import com.chinalwb.are.spans.AreQuoteSpan;

public class ARE_Quote implements IARE_Style {

    private ImageView mQuoteImageView;

    private boolean mQuoteChecked;

    private AREditText mEditText;

    private boolean mRemovedNewLine;


    /**
     * @param quoteImageView
     */
    public ARE_Quote(ImageView quoteImageView) {
        this.mQuoteImageView = quoteImageView;
        setListenerForImageView(this.mQuoteImageView);
    }

    /**
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
                mQuoteChecked = !mQuoteChecked;
                ARE_Helper.updateCheckStatus(ARE_Quote.this, mQuoteChecked);
                if (null != mEditText) {
                    if (mQuoteChecked) {
                        makeLineAsQuote();
                    } else {
                        removeQuote();
                    }
                }
            }
        });
    }

    /**
     * @return
     */
    private void makeLineAsQuote() {
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

        AreQuoteSpan[] existingQuoteSpans = editable.getSpans(start, end, AreQuoteSpan.class);
        if (existingQuoteSpans != null && existingQuoteSpans.length > 0) {
            return;
        }
        if (start > 2) {
            existingQuoteSpans = editable.getSpans(start - 2, start, AreQuoteSpan.class);
            if (existingQuoteSpans != null && existingQuoteSpans.length > 0) {
                // Merge forward
                int quoteStart = editable.getSpanStart(existingQuoteSpans[0]);
                editable.setSpan(existingQuoteSpans[0], quoteStart, end, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                return;
            }
        }
        AreQuoteSpan quoteSpan = new AreQuoteSpan();
        editable.setSpan(quoteSpan, start, end,
                Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        ARE_Helper.updateCheckStatus(ARE_Quote.this, true);
    }

    private void removeQuote() {
        EditText editText = getEditText();
        Editable editable = editText.getText();
        int currentLine = Util.getCurrentCursorLine(editText);
        int start = Util.getThisLineStart(editText, currentLine);
        int end = Util.getThisLineEnd(editText, currentLine);
        AreQuoteSpan[] quoteSpans = null;
        if (start == 0) {
            quoteSpans = editable.getSpans(start, end, AreQuoteSpan.class);
            editable.removeSpan(quoteSpans[0]);
            return;
        } else {
            quoteSpans = editable.getSpans(start - 1, end, AreQuoteSpan.class);
        }
        if (quoteSpans == null || quoteSpans.length == 0) {
            quoteSpans = editable.getSpans(start, end, AreQuoteSpan.class);
            if (quoteSpans != null && quoteSpans.length == 0) {
                editable.removeSpan(quoteSpans[0]);
                return;
            }
        }
        int quoteStart = editable.getSpanStart(quoteSpans[0]);
        editable.removeSpan(quoteSpans[0]);
        if (start > quoteStart) {
            editable.setSpan(quoteSpans[0], quoteStart, start - 1, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        }
    }

    @Override
    public void applyStyle(Editable editable, int start, int end) {
//        Util.log("Quote apply style, start == " + start + ", end == " + end + ", is quote checked == " + mQuoteChecked);
//        if (!mQuoteChecked) {
//            return;
//        }

        AreQuoteSpan[] quoteSpans = editable.getSpans(start, end, AreQuoteSpan.class);
        if (null == quoteSpans || quoteSpans.length == 0) {
            return;
        }

        // Handle \n and backspace
        if (end > start) {
            // User inputs
            char c = editable.charAt(end - 1);
            if (c == Constants.CHAR_NEW_LINE) {
                editable.append(Constants.ZERO_WIDTH_SPACE_STR);
            }
        } else {
            // User deletes
            AreQuoteSpan quoteSpan = quoteSpans[0];
            int spanStart = editable.getSpanStart(quoteSpan);
            int spanEnd = editable.getSpanEnd(quoteSpan);
            Util.log("Delete spanStart = " + spanStart + ", spanEnd = "
                    + spanEnd + " ,, start == " + start);
            if (spanStart == spanEnd) {
                setChecked(false);
                ARE_Helper.updateCheckStatus(ARE_Quote.this, false);
                removeQuote();
            }
            if (end > 2) {
                if (mRemovedNewLine) {
                    mRemovedNewLine = false;
                    return;
                }
                char pChar = editable.charAt(end - 1);
                if (pChar == Constants.CHAR_NEW_LINE) {
                    //
                    // This case
                    // |aa
                    // |
                    // When user deletes at the first of the 2nd line (i.e.: ZERO_WIDTH_STR)
                    // We want to put cursor to the end of the previous line "aa"
                    mRemovedNewLine = true;
                    editable.delete(end - 1, end);
                }
            }
        }
    }

    @Override
    public ImageView getImageView() {
        return this.mQuoteImageView;
    }

    @Override
    public void setChecked(boolean isChecked) {
        this.mQuoteChecked = isChecked;
    }

    @Override
    public boolean getIsChecked() {
        return this.mQuoteChecked;
    }

    @Override
    public EditText getEditText() {
        return this.mEditText;
    }

}
