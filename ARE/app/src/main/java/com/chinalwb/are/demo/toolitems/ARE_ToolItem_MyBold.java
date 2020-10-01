package com.chinalwb.are.demo.toolitems;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chinalwb.are.demo.R;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_Bold;

public class ARE_ToolItem_MyBold extends ARE_ToolItem_Bold {

    @Override
    public View getView(Context context) {
        if (null == context) {
            return mToolItemView;
        }
        if (mToolItemView == null) {
            mToolItemView = createIcon(context, R.drawable.my_bold);
        }

        return mToolItemView;
    }
}
