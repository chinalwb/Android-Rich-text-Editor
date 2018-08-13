package com.chinalwb.are.styles.toolitems;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chinalwb.are.AREditText;
import com.chinalwb.are.R;
import com.chinalwb.are.Util;
import com.chinalwb.are.styles.IARE_Style;
import com.chinalwb.are.styles.toolitems.styles.ARE_Style_Bold;
import com.chinalwb.are.styles.toolitems.styles.ARE_Style_Italic;

/**
 * Created by wliu on 13/08/2018.
 */

public class ARE_ToolItem_Italic extends ARE_ToolItem_Abstract {
    @Override
    public IARE_Style getStyle() {
        AREditText editText = this.getToolbar().getEditText();
        return new ARE_Style_Italic(editText, (ImageView) mToolItemView);
    }

    @Override
    public View getView(Context context) {
        if (mToolItemView == null) {
            ImageView imageView = new ImageView(context);
            int size = Util.getPixelByDp(context, 40);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(size, size);
            imageView.setLayoutParams(params);
            imageView.setImageResource(R.drawable.italic);
            imageView.bringToFront();
            mToolItemView = imageView;
        }

        return mToolItemView;
    }

    @Override
    public void onSelectionChanged(int selStart, int selEnd) {
        Util.log("Italic on selection changed");
    }
}
