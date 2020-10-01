package com.chinalwb.are.styles.toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.chinalwb.are.AREditText;
import com.chinalwb.are.R;
import com.chinalwb.are.Util;
import com.chinalwb.are.styles.toolitems.IARE_ToolItem;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.DrawableRes;

/**
 * Created by wliu on 13/08/2018.
 */
public class ARE_ToolbarDefault extends HorizontalScrollView implements IARE_Toolbar {

    private Context mContext;

    private LinearLayout mContainer;

    private List<IARE_ToolItem> mToolItems = new ArrayList<>();

    private AREditText mAREditText;

    @DrawableRes
    private int mIconBackground;
    private int mIconSize;
    private int mIconMargin;

    public ARE_ToolbarDefault(Context context) {
        this(context, null);
    }

    public ARE_ToolbarDefault(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ARE_ToolbarDefault(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
        initSelf(context, attrs);
    }

    @Override
    public void addToolbarItem(IARE_ToolItem toolbarItem) {
        toolbarItem.setToolbar(this, mIconBackground, mIconSize, mIconMargin);
        mToolItems.add(toolbarItem);
        View view = toolbarItem.getView(mContext);
        if (view != null) {
            mContainer.addView(view);
        }
    }

    @Override
    public List<IARE_ToolItem> getToolItems() {
        return mToolItems;
    }

    @Override
    public void setEditText(AREditText editText) {
        this.mAREditText = editText;
    }

    @Override
    public AREditText getEditText() {
        return mAREditText;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        for (IARE_ToolItem toolItem : this.mToolItems) {
            toolItem.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void initSelf(Context context, AttributeSet attrs) {
        mContainer = new LinearLayout(mContext);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        mContainer.setGravity(Gravity.CENTER_VERTICAL);
        mContainer.setLayoutParams(params);
        this.addView(mContainer);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ARE_ToolbarDefault);
        mIconBackground = ta.getResourceId(R.styleable.ARE_ToolbarDefault_toolbarIconBackground, R.drawable.background_icon);
        mIconSize = ta.getDimensionPixelSize(R.styleable.ARE_ToolbarDefault_toolbarIconSize, Util.getPixelByDp(context, 40));
        mIconMargin = ta.getDimensionPixelSize(R.styleable.ARE_ToolbarDefault_toolbarIconMargin, 0);

        ta.recycle();
    }
}
