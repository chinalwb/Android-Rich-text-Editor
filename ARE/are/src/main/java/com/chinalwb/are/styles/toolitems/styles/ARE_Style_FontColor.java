package com.chinalwb.are.styles.toolitems.styles;

import android.text.Editable;
import android.view.View;
import android.widget.ImageView;

import com.chinalwb.are.AREditText;
import com.chinalwb.are.Constants;
import com.chinalwb.are.spans.AreFontSizeSpan;
import com.chinalwb.are.spans.AreForegroundColorSpan;
import com.chinalwb.are.styles.ARE_ABS_Dynamic_Style;
import com.chinalwb.are.styles.windows.ColorPickerWindow;

public class ARE_Style_FontColor extends ARE_ABS_Dynamic_Style<AreForegroundColorSpan> {

    private ImageView mFontColorImageView;

    private AREditText mEditText;

    private ColorPickerWindow mColorPickerWindow;

    private int mColor;

    private boolean mIsChecked;

    /**
     * @param fontSizeImage
     */
    public ARE_Style_FontColor(AREditText editText, ImageView fontSizeImage) {
        super(editText.getContext());
        this.mEditText = editText;
        this.mFontColorImageView = fontSizeImage;
        setListenerForImageView(this.mFontColorImageView);
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
                showFontColorPickerWindow();
            }
        });
    }

    private void showFontColorPickerWindow() {
        if (mColorPickerWindow == null) {
            mColorPickerWindow = new ColorPickerWindow(mContext);
        }
        mColorPickerWindow.showAsDropDown(mFontColorImageView, 0, 0);
    }

    @Override
    public AreForegroundColorSpan newSpan() {
        return new AreForegroundColorSpan(-1);
    }

    @Override
    public ImageView getImageView() {
        return this.mFontColorImageView;
    }

    @Override
    public void setChecked(boolean isChecked) {
        // Do nothing.
    }

    @Override
    public boolean getIsChecked() {
        return mIsChecked;
    }

    @Override
    protected void featureChangedHook(int lastSpanFontColor) {
        mColor = lastSpanFontColor;
        if (mColorPickerWindow != null) {
//            mColorPickerWindow.setFontSize(mSize);
        }
    }

    @Override
    protected AreForegroundColorSpan newSpan(int color) {
        return new AreForegroundColorSpan(color);
    }
}
