package com.chinalwb.are.styles.windows;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

import com.chinalwb.are.R;
import com.chinalwb.are.Util;

public class ColorPickerWindow extends PopupWindow {

    private Context mContext;

    private View mRootView;

    public ColorPickerWindow(Context context) {
        mContext = context;
        this.mRootView = inflateContentView();
        this.setContentView(mRootView);
        int[] wh = Util.getScreenWidthAndHeight(context);
        this.setWidth(wh[0]);
        int h = Util.getPixelByDp(context, 100);
        this.setHeight(h);
        this.setBackgroundDrawable(new BitmapDrawable());
        this.setOutsideTouchable(true);
        this.setFocusable(true);
//        this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_UNCHANGED
//                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    private View inflateContentView() {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.are_color_picker, null);
        return view;
    }

    private <T extends View> T findViewById(int id) {
        return mRootView.findViewById(id);
    }

}
