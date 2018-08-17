package com.chinalwb.are.styles.toolitems.styles;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;

import com.chinalwb.are.AREditText;
import com.chinalwb.are.Util;
import com.chinalwb.are.spans.AreBoldSpan;
import com.chinalwb.are.styles.ARE_ABS_Style;
import com.chinalwb.are.styles.ARE_Helper;
import com.chinalwb.are.styles.toolitems.IARE_ToolItem_Updater;

import java.nio.charset.CharsetEncoder;

public class ARE_Style_Bold extends ARE_ABS_Style<AreBoldSpan> {

    private ImageView mBoldImageView;

    private boolean mBoldChecked;

    private AREditText mEditText;

    private IARE_ToolItem_Updater mCheckUpdater;

    /**
     * @param boldImage
     */
    public ARE_Style_Bold(AREditText editText, ImageView boldImage, IARE_ToolItem_Updater checkUpdater) {
        super(editText.getContext());
        this.mEditText = editText;
        this.mBoldImageView = boldImage;
        this.mCheckUpdater = checkUpdater;
        setListenerForImageView(this.mBoldImageView);
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
                mBoldChecked = !mBoldChecked;
                if (mCheckUpdater != null) {
                    mCheckUpdater.onCheckStatusUpdate(mBoldChecked);
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
