package com.chinalwb.are.styles;


import android.text.style.BackgroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;

import com.chinalwb.are.AREditText;

public class ARE_BackgroundColor extends ARE_ABS_Style<BackgroundColorSpan> {

    private ImageView mBackgroundImageView;

    private boolean mBackgroundChecked;

    private int mColor;

    private AREditText mEditText;

    /**
     * @param backgroundImage
     */
    public ARE_BackgroundColor(ImageView backgroundImage, int backgroundColor) {
        super(backgroundImage.getContext());
        this.mBackgroundImageView = backgroundImage;
        this.mColor = backgroundColor;
        setListenerForImageView(this.mBackgroundImageView);
    }

    public void setEditText(AREditText editText) {
        this.mEditText = editText;
    }

    @Override
    public EditText getEditText() {
        return mEditText;
    }

    @Override
    public void setListenerForImageView(final ImageView imageView) {
        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mBackgroundChecked = !mBackgroundChecked;
                ARE_Helper.updateCheckStatus(ARE_BackgroundColor.this, mBackgroundChecked);
                if (null != mEditText) {
                    applyStyle(mEditText.getEditableText(), mEditText.getSelectionStart(), mEditText.getSelectionEnd());
                }
            }
        });
    }


    @Override
    public ImageView getImageView() {
        return this.mBackgroundImageView;
    }

    @Override
    public void setChecked(boolean isChecked) {
        this.mBackgroundChecked = isChecked;
    }

    @Override
    public boolean getIsChecked() {
        return this.mBackgroundChecked;
    }

    @Override
    public BackgroundColorSpan newSpan() {
        return new BackgroundColorSpan(this.mColor);
    }
}
