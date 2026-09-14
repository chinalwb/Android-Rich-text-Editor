package com.chinalwb.are.demo.toolitems;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chinalwb.are.Util;
import com.chinalwb.are.demo.R;
import com.chinalwb.are.styles.IARE_Style;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_Bold;

public class ARE_ToolItem_MyBold extends ARE_ToolItem_Bold {

    @Override
    public View getView(Context context) {
        if (null == context) {
            return mToolItemView;
        }
        if (mToolItemView == null) {
            //
            // createToolItemView gives a custom tool item the same box, icon size,
            // tint and active treatment as the built in ones.
            mToolItemView = createToolItemView(context,
                    R.drawable.my_bold, R.string.demo_tool_my_bold);
        }

        return mToolItemView;
    }
}
