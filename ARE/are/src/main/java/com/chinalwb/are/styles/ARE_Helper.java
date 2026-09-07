package com.chinalwb.are.styles;

import android.view.View;

public class ARE_Helper {

  /**
   * Updates the check status.
   *
   * <p>The button is put into the selected state rather than being painted
   * directly, so the "this style is on" treatment lives in
   * {@code are_tool_item_background} and {@code are_tool_item_tint} and stays the
   * same for every tool item - and can be themed by the host app.</p>
   *
   * @param areStyle
   * @param checked
   */
  public static void updateCheckStatus(IARE_Style areStyle, boolean checked) {
    areStyle.setChecked(checked);

    View imageView = areStyle.getImageView();
    if (null == imageView) {
      //
      // Some styles - the list ones for instance - have no button of their own.
      return;
    }
    imageView.setSelected(checked);
  }

}
