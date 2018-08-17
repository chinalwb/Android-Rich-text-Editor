package com.chinalwb.are.styles.toolitems.styles;

import android.view.View;
import android.widget.ImageView;

import com.chinalwb.are.AREditText;
import com.chinalwb.are.spans.AreSubscriptSpan;
import com.chinalwb.are.styles.ARE_ABS_Style;
import com.chinalwb.are.styles.ARE_Helper;
import com.chinalwb.are.styles.toolitems.IARE_ToolItem_Updater;


public class ARE_Style_Subscript extends ARE_ABS_Style<AreSubscriptSpan> {

    private ImageView mSubscriptImage;

    private boolean mSubscriptChecked;

    private AREditText mEditText;

    private IARE_ToolItem_Updater mCheckUpdater;

    /**
     * @param imageView image view
     */
    public ARE_Style_Subscript(AREditText editText, ImageView imageView, IARE_ToolItem_Updater checkUpdater) {
        super(editText.getContext());
        this.mEditText = editText;
        this.mSubscriptImage = imageView;
        this.mCheckUpdater = checkUpdater;
        setListenerForImageView(this.mSubscriptImage);
    }

    /**
     * @param editText edit text
     */
    public void setEditText(AREditText editText) {
        this.mEditText = editText;
    }

    @Override
    public void setListenerForImageView(final ImageView imageView) {
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSubscriptChecked = !mSubscriptChecked;
                ARE_Helper.updateCheckStatus(ARE_Style_Subscript.this, mSubscriptChecked);
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
        return mSubscriptImage;
    }

    @Override
    public void setChecked(boolean isChecked) {
        this.mSubscriptChecked = isChecked;
    }

    @Override
    public boolean getIsChecked() {
        return this.mSubscriptChecked;
    }

    @Override
    public AreSubscriptSpan newSpan() {
        return new AreSubscriptSpan();
    }
}
