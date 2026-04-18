package com.chinalwb.are.styles.toolitems.styles;

import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.ColorInt;

import com.chinalwb.are.AREditText;
import com.chinalwb.are.Util;
import com.chinalwb.are.colorpicker.ColorPickerListener;
import com.chinalwb.are.colorpicker.ColorPickerView;
import com.chinalwb.are.spans.AreForegroundColorSpan;
import com.chinalwb.are.styles.ARE_ABS_Dynamic_Style;
import com.chinalwb.are.styles.windows.ColorPickerWindow;

public class ARE_Style_FontColor extends ARE_ABS_Dynamic_Style<AreForegroundColorSpan> implements ColorPickerListener {

    private ImageView mFontColorImageView;

    private final ColorPickerView colorPickerView;

    private AREditText mEditText;

    private ColorPickerWindow mColorPickerWindow;

    private int mColor;

    @ColorInt private int mColorPickerBackgroundColor;

    private boolean mIsChecked;

    /**
     * @param fontColorImage
     * @param colorPickerView
     */
    public ARE_Style_FontColor(AREditText editText, ImageView fontColorImage, ColorPickerView colorPickerView) {
        super(editText.getContext());
        this.mEditText = editText;
        this.mFontColorImageView = fontColorImage;
        this.colorPickerView = colorPickerView;
        setListenerForImageView(this.mFontColorImageView);
        if (this.colorPickerView != null) colorPickerView.setColorPickerListener(this);
    }


    /**
     * @param editText
     */
    public void setEditText(AREditText editText) {
        this.mEditText = editText;
    }

    @Override
    public EditText getEditText() {
        return mEditText;
    }

    @Override
    public void setListenerForImageView(final ImageView imageView) {
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (colorPickerView != null) {
                    toggleColorPickerView();
                } else {
                    showFontColorPickerWindow();
                }
            }
        });
    }

    private void toggleColorPickerView() {
        boolean isVisible = colorPickerView.getVisibility() == View.VISIBLE;
        colorPickerView.setVisibility(isVisible ? View.GONE : View.VISIBLE);
    }

    private void showFontColorPickerWindow() {
        if (mColorPickerWindow == null) {
            mColorPickerWindow = new ColorPickerWindow(mContext, this);
        }
        int yOff = Util.getPixelByDp(mContext, -5);
        mColorPickerWindow.showAsDropDown(mFontColorImageView, 0, yOff);
        mColorPickerWindow.setBackgroundColor(mColorPickerBackgroundColor);
    }

    @Override
    public AreForegroundColorSpan newSpan() {
        return new AreForegroundColorSpan(mColor);
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
        setColorChecked(lastSpanFontColor);
    }

    public void setColorChecked(int color) {
        if (colorPickerView != null) {
            colorPickerView.setColor(color);
        } else if (mColorPickerWindow != null) {
            mColorPickerWindow.setColor(color);
        }
    }

    @Override
    protected AreForegroundColorSpan newSpan(int color) {
        return new AreForegroundColorSpan(color);
    }

    @Override
    public void onPickColor(int color) {
        mIsChecked = true;

        mFontColorImageView.post(new Runnable() {
            @Override
            public void run() {
                mFontColorImageView.setColorFilter(mColor);
            }
        });

        mColor = color;
        if (null != mEditText) {
            Editable editable = mEditText.getEditableText();
            int start = mEditText.getSelectionStart();
            int end = mEditText.getSelectionEnd();

            if (end >= start) {
                applyNewStyle(editable, start, end, color);
            }
        }
    }

    public void setColorPickerBackgroundColor(int colorPickerBackgroundColor) {
        mColorPickerBackgroundColor = colorPickerBackgroundColor;
    }
}
