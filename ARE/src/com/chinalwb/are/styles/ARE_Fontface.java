package com.chinalwb.are.styles;

import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;

import com.chinalwb.are.Constants;
import com.chinalwb.are.spans.MyTypefaceSpan;

public class ARE_Fontface implements IARE_Style {

  private ImageView mFontfaceImageView;
  
  private EditText mEditText;
  
  /**
   * 
   * @param fontSizeImage
   */
  public ARE_Fontface(ImageView fontSizeImage, EditText editText) {
    this.mFontfaceImageView = fontSizeImage;
    this.mEditText = editText;
    setListenerForImageView(this.mFontfaceImageView);
  }
  
  @Override
  public void setListenerForImageView(final ImageView imageView) {
    imageView.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
    	  int selectionStart = mEditText.getSelectionStart();
    	  int selectionEnd = mEditText.getSelectionEnd();
    	  
    	  AssetManager assetManager = mEditText.getContext().getAssets();
    	  Typeface typeface = Typeface.createFromAsset(assetManager, "fonts/walkway.ttf"); 
    	  MyTypefaceSpan typefaceSpan = new MyTypefaceSpan(typeface);
    	  if (selectionStart == selectionEnd) {
        	  SpannableStringBuilder ssb = new SpannableStringBuilder();
        	  ssb.append(Constants.ZERO_WIDTH_SPACE_STR);
        	  ssb.setSpan(typefaceSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        	  
        	  mEditText.getEditableText().replace(selectionStart, selectionEnd, ssb);    		  
    	  }
    	  else {
    		  mEditText.getEditableText().setSpan(typefaceSpan, selectionStart, selectionEnd, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
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
		return this.mFontfaceImageView;
	}
	
	@Override
	public void setChecked(boolean isChecked) {
		// Do nothing.
	}

}
