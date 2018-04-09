package com.chinalwb.are.styles.windows;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;

import com.chinalwb.are.R;
import com.chinalwb.are.Util;

public class FontsizePickerWindow extends PopupWindow {

    private static final int FONT_SIZE_BASE = 12;

    private Context mContext;

    private View mRootView;

    private TextView mPreview;

    private SeekBar mSeekbar;

    private FontSizeChangeListener mListener;

    public FontsizePickerWindow(Context context, FontSizeChangeListener fontSizeChangeListener) {
        mContext = context;
        mListener = fontSizeChangeListener;
        this.mRootView = inflateContentView();
        this.setContentView(mRootView);
        int[] wh = Util.getScreenWidthAndHeight(context);
        this.setWidth(wh[0]);
        int h = Util.getPixelByDp(context, 100);
        this.setHeight(h);
        this.setBackgroundDrawable(new BitmapDrawable());
        this.setOutsideTouchable(true);
        this.setFocusable(true);
        this.initView();
        this.setupListeners();
//        this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_UNCHANGED
//                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    private View inflateContentView() {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.are_fontsize_picker, null);
        return view;
    }

    private <T extends View> T findViewById(int id) {
        return mRootView.findViewById(id);
    }

    public void setFontSize(int size) {
        size = size - FONT_SIZE_BASE;
        mSeekbar.setProgress(size);
    }

    private void initView() {
        this.mPreview = findViewById(R.id.are_fontsize_preview);
        this.mSeekbar = findViewById(R.id.are_fontsize_seekbar);
    }

    private void setupListeners() {
        this.mSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                changePreviewText(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void changePreviewText(int progress) {
        int size = FONT_SIZE_BASE + progress;
        mPreview.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
        mPreview.setText(size + "sp: Preview");
        if (mListener != null) {
            mListener.onFontSizeChange(size);
        }
    }

}
