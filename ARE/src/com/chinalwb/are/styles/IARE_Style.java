package com.chinalwb.are.styles;

import android.text.Editable;
import android.widget.ImageView;

public interface IARE_Style {

  /**
   * For styles like Bold / Italic / Underline, by clicking the ImageView,
   * we should change the UI, so user can notice that this style takes 
   * effect now.
   * 
   * @param imageView
   */
  public void setListenerForImageView(ImageView imageView);
  
  /**
   * Apply the style to the change start at {@link start} end at {@link end}.
   * 
   * @param editText
   * @param start
   * @param end
   */
  public void applyStyle(Editable editable, int start, int end);
  
  /**
   * Returns the {@link ImageView} of this style.
   * 
   * @return
   */
  public ImageView getImageView();
  
  /**
   * Sets if this style is checked.
   * 
   * @param isChecked
   */
  public void setChecked(boolean isChecked);
  
}
