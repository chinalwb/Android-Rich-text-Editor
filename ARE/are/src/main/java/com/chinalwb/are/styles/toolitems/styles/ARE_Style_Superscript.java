package com.chinalwb.are.styles.toolitems.styles;

import android.view.View;
import android.widget.ImageView;

import com.chinalwb.are.AREditText;
import com.chinalwb.are.spans.AreSuperscriptSpan;
import com.chinalwb.are.styles.ARE_ABS_Style;
import com.chinalwb.are.styles.ARE_Helper;
import com.chinalwb.are.styles.toolitems.IARE_ToolItem_Updater;

/**
 * Created by wliu on 2018/4/3.
 */

public class ARE_Style_Superscript extends ARE_ABS_Style<AreSuperscriptSpan> {

    private ImageView mSuperscriptImage;

    private boolean mSuperscriptChecked;

    private AREditText mEditText;

    private IARE_ToolItem_Updater mCheckUpdater;

    /**
     * @param imageView
     */
    public ARE_Style_Superscript(AREditText editText, ImageView imageView, IARE_ToolItem_Updater checkUpdater) {
        super(editText.getContext());
        this.mEditText = editText;
        this.mSuperscriptImage = imageView;
        this.mCheckUpdater = checkUpdater;
        setListenerForImageView(this.mSuperscriptImage);
    }

    /**
     * @param editText
     */
    public void setEditText(AREditText editText) {
        this.mEditText = editText;
    }

    @Override
    public void setListenerForImageView(final ImageView imageView) {
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSuperscriptChecked = !mSuperscriptChecked;
                if (mCheckUpdater != null) {
                    mCheckUpdater.onCheckStatusUpdate(mSuperscriptChecked);
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
        return mSuperscriptImage;
    }

    @Override
    public void setChecked(boolean isChecked) {
        this.mSuperscriptChecked = isChecked;
    }

    @Override
    public boolean getIsChecked() {
        return this.mSuperscriptChecked;
    }

    @Override
    public AreSuperscriptSpan newSpan() {
        return new AreSuperscriptSpan();
    }
}
