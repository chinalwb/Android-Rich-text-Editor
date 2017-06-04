package com.chinalwb.are.styles;

import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;

import com.chinalwb.are.Constants;

public class ARE_Fontsize implements IARE_Style {

  private ImageView mFontsizeImageView;
  
  private EditText mEditText;
  
  /**
   * 
   * @param fontSizeImage
   */
  public ARE_Fontsize(ImageView fontSizeImage, EditText editText) {
    this.mFontsizeImageView = fontSizeImage;
    this.mEditText = editText;
    setListenerForImageView(this.mFontsizeImageView);
  }
  
  @Override
  public void setListenerForImageView(final ImageView imageView) {
    imageView.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
    	  int selectionStart = mEditText.getSelectionStart();
    	  int selectionEnd = mEditText.getSelectionEnd();
    	  
    	  AbsoluteSizeSpan absoluteSizeSpan = new AbsoluteSizeSpan(30, true);
    	  if (selectionStart == selectionEnd) {
        	  SpannableStringBuilder ssb = new SpannableStringBuilder();
        	  ssb.append(Constants.ZERO_WIDTH_SPACE_STR);
        	  ssb.setSpan(absoluteSizeSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        	  
        	  mEditText.getEditableText().replace(selectionStart, selectionEnd, ssb);    		  
    	  }
    	  else {
    		  mEditText.getEditableText().setSpan(absoluteSizeSpan, selectionStart, selectionEnd, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
    	  }
      }
    });
  }
  
  @Override
  public void applyStyle(Editable editable, int start, int end) {
	  // Do nothing
  } // #End of method 

	@Override
	public ImageView getImageView() {
		return this.mFontsizeImageView;
	}
	
	@Override
	public void setChecked(boolean isChecked) {
		// Do nothing.
	}

}
