package com.chinalwb.are.styles;

import android.view.View;

public class ARE_Helper {

    /**
     * Updates the check status.
     *
     * @param areStyle
     * @param checked
     */
    public static void updateCheckStatus(IARE_Style areStyle, boolean checked) {
        areStyle.setChecked(checked);
        View imageView = areStyle.getImageView();
        imageView.setSelected(checked);
    }


}
