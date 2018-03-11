package com.rainliu.colorpicker;

import android.content.Context;
import android.graphics.Color;
import android.text.Layout;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by wliu on 2018/3/11.
 */

public class ColorCheckedView extends View {

    private Context mContext;

    public ColorCheckedView(Context context) {
        super(context);
        this.mContext = context;
        initView();
    }

    private void initView() {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(5,5);
        layoutParams.gravity = Gravity.CENTER;
        this.setLayoutParams(layoutParams);
        this.setBackgroundColor(Color.WHITE);
    }
}
