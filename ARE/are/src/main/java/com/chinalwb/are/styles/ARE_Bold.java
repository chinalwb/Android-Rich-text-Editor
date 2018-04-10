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
import com.chinalwb.are.spans.AreBoldSpan;

public class ARE_Bold extends ARE_ABS_Style<AreBoldSpan> {

    private ImageView mBoldImageView;

    private boolean mBoldChecked;

    private AREditText mEditText;

    /**
     * @param boldImage
     */
    public ARE_Bold(ImageView boldImage) {
        this.mBoldImageView = boldImage;
        setListenerForImageView(this.mBoldImageView);
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
    public ImageView getImageView() {
        return this.mBoldImageView;
    }

    @Override
    public void setChecked(boolean isChecked) {
        this.mBoldChecked = isChecked;
    }

    @Override
    public boolean getIsChecked() {
        return this.mBoldChecked;
    }

    @Override
    public AreBoldSpan newSpan() {
        return new AreBoldSpan();
    }
}
