package com.chinalwb.are.styles;

import android.view.View;
import android.widget.ImageView;

import com.chinalwb.are.AREditText;
import com.chinalwb.are.spans.AreSuperscriptSpan;

/**
 * Created by wliu on 2018/4/3.
 */

public class ARE_Superscript extends ARE_ABS_Style<AreSuperscriptSpan> {

    private ImageView mSuperscriptImage;

    private boolean mSuperscriptChecked;

    private AREditText mEditText;

    /**
     * @param imageView
     */
    public ARE_Superscript(ImageView imageView) {
        this.mSuperscriptImage = imageView;
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
                ARE_Helper.updateCheckStatus(ARE_Superscript.this, mSuperscriptChecked);
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
