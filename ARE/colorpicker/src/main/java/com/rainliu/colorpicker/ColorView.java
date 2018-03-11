package com.rainliu.colorpicker;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by wliu on 2018/3/11.
 */

public class ColorView extends LinearLayout {

    public static final String ATTR_VIEW_WIDTH = "ATTR_VIEW_WIDTH";

    public static final String ATTR_VIEW_HEIGHT = "ATTR_VIEW_HEIGHT";

    public static final String ATTR_MARGIN_LEFT = "ATTR_MARGIN_LEFT";

    public static final String ATTR_MARGIN_RIGHT = "ATTR_MARGIN_RIGHT";

    private Context mContext;

    private int mColorViewWidth = 0;

    private int mColorViewHeight = 0;

    private int mColorViewMarginLeft = 0;

    private int mColorViewMarginRight = 0;

    private int mColor;

    private boolean mChecked;

    private View mCheckView = null;

    public ColorView(Context context, int color, Bundle attributeBundle) {
        super(context);
        this.mContext = context;
        this.mColor = color;

        mColorViewWidth = attributeBundle.getInt(ATTR_VIEW_WIDTH, 40);
        mColorViewHeight = attributeBundle.getInt(ATTR_VIEW_HEIGHT, 40);
        mColorViewMarginLeft = attributeBundle.getInt(ATTR_MARGIN_LEFT, 2);
        mColorViewMarginRight = attributeBundle.getInt(ATTR_MARGIN_RIGHT, 2);
        initView();
    }

    private void initView() {
        mCheckView = getCheckView();
        LayoutParams layoutParams = new LayoutParams(mColorViewWidth, mColorViewHeight);
        layoutParams.setMargins(mColorViewMarginLeft, 0, mColorViewMarginRight, 0);
        this.setLayoutParams(layoutParams);
        this.setBackgroundColor(mColor);
        this.setGravity(Gravity.CENTER);
        this.addView(mCheckView);
        initCheckStatus();
    }

    private void initCheckStatus() {
        if (mCheckView == null) {
            return;
        }

        if (mChecked) {
            mCheckView.setVisibility(View.VISIBLE);
        } else {
            mCheckView.setVisibility(View.GONE);
        }
    }

    public void setColor(int color) {
        this.mColor = color;
        initView();
    }

    public int getColor() {
        return this.mColor;
    }

    public void setCheckView(View checkedView) {
        this.mCheckView = checkedView;
    }

    public View getCheckView() {
        if (this.mCheckView == null) {
            this.mCheckView = new ColorCheckedView(mContext);
        }
        return this.mCheckView;
    }

    public void setChecked(boolean checked) {
        this.mChecked = checked;
        initCheckStatus();
    }

    public boolean getChecked() {
        return this.mChecked;
    }
}
