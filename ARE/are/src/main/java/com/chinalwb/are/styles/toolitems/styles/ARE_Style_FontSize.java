package com.chinalwb.are.styles.toolitems.styles;

import android.text.Editable;
import android.view.View;
import android.widget.ImageView;

import com.chinalwb.are.AREditText;
import com.chinalwb.are.Constants;
import com.chinalwb.are.spans.AreFontSizeSpan;
import com.chinalwb.are.styles.ARE_ABS_Dynamic_Style;
import com.chinalwb.are.styles.toolbar.ARE_Toolbar;
import com.chinalwb.are.styles.windows.FontSizeChangeListener;
import com.chinalwb.are.styles.windows.FontsizePickerWindow;

import javax.microedition.khronos.egl.EGLDisplay;

public class ARE_Style_FontSize extends ARE_ABS_Dynamic_Style<AreFontSizeSpan> implements FontSizeChangeListener {

    private ImageView mFontsizeImageView;

    private AREditText mEditText;

    private int mSize = Constants.DEFAULT_FONT_SIZE;

    private FontsizePickerWindow mFontPickerWindow;

    private static final int DEFAULT_FONT_SIZE = 18;

    private boolean mIsChecked;

    /**
     * @param fontSizeImage
     */
    public ARE_Style_FontSize(AREditText editText, ImageView fontSizeImage) {
        super(editText.getContext());
        this.mEditText = editText;
        this.mFontsizeImageView = fontSizeImage;
        setListenerForImageView(this.mFontsizeImageView);
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
                showFontsizePickerWindow();
            }
        });
    }

    private void showFontsizePickerWindow() {
        if (mFontPickerWindow == null) {
            mFontPickerWindow = new FontsizePickerWindow(mContext, this);
        }
        mFontPickerWindow.setFontSize(mSize);
        mFontPickerWindow.showAsDropDown(mFontsizeImageView,0, 0);
    }

    @Override
    protected void changeSpanInsideStyle(Editable editable, int start, int end, AreFontSizeSpan existingSpan) {
        int currentSize = existingSpan.getSize();
        if (currentSize != mSize) {
            applyNewStyle(editable, start, end, mSize);
        }
    }

    @Override
    public AreFontSizeSpan newSpan() {
        return new AreFontSizeSpan(mSize);
    }

    @Override
    public ImageView getImageView() {
        return this.mFontsizeImageView;
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
    public void onFontSizeChange(int fontSize) {
        mIsChecked = true;
        mSize = fontSize;
        if (null != mEditText) {
            Editable editable = mEditText.getEditableText();
            int start = mEditText.getSelectionStart();
            int end = mEditText.getSelectionEnd();

            if (end > start) {
                applyNewStyle(editable, start, end, mSize);
            }
        }
    }

    @Override
    protected void featureChangedHook(int lastSpanFontSize) {
        mSize = lastSpanFontSize;
        if (mFontPickerWindow != null) {
            mFontPickerWindow.setFontSize(mSize);
        }
    }

    @Override
    protected AreFontSizeSpan newSpan(int size) {
        return new AreFontSizeSpan(size);
    }
}
