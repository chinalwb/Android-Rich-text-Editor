package com.chinalwb.are.styles.toolitems.styles;

import android.text.style.BackgroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;

import com.chinalwb.are.AREditText;
import com.chinalwb.are.styles.ARE_ABS_Style;
import com.chinalwb.are.styles.toolitems.IARE_ToolItem_Updater;

public class ARE_Style_BackgroundColor extends ARE_ABS_Style<BackgroundColorSpan> {

    private ImageView mBackgroundImageView;

    private boolean mBackgroundChecked;

    private AREditText mEditText;

    private IARE_ToolItem_Updater mCheckUpdater;

    private int mColor;

    /**
     * @param backgroundImage
     */
    public ARE_Style_BackgroundColor(AREditText editText, ImageView backgroundImage, IARE_ToolItem_Updater checkUpdater, int backgroundColor) {
        super(editText.getContext());
        this.mEditText = editText;
        this.mBackgroundImageView = backgroundImage;
        this.mCheckUpdater = checkUpdater;
        this.mColor = backgroundColor;
        setListenerForImageView(this.mBackgroundImageView);
    }

    @Override
    public EditText getEditText() {
        return this.mEditText;
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
                mBackgroundChecked = !mBackgroundChecked;
                if (mCheckUpdater != null) {
                    mCheckUpdater.onCheckStatusUpdate(mBackgroundChecked);
                }
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
        return new BackgroundColorSpan(mColor);
    }
}
