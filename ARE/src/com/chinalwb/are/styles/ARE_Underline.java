package com.chinalwb.are.styles;


import android.text.Editable;
import android.text.Spanned;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.chinalwb.are.Util;

public class ARE_Underline implements IARE_Style {
  
  private ImageView mUnderlineImageView;
  
  private boolean mUnderlineChecked;
  
  /**
   * 
   * @param UnderlineImage
   */
  public ARE_Underline(ImageView UnderlineImage) {
    this.mUnderlineImageView = UnderlineImage;
    setListenerForImageView(this.mUnderlineImageView);
  }
  
  @Override
  public void setListenerForImageView(final ImageView imageView) {
    imageView.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        mUnderlineChecked = !mUnderlineChecked;
        ARE_Helper.updateCheckStatus(ARE_Underline.this, mUnderlineChecked);
      }
    });
  }

  @Override
  public void applyStyle(Editable editable, int start, int end) {
    if (this.mUnderlineChecked) {
      if (end > start) {
        // 
        // User inputs
        boolean hasUnderlineSpan = false;
        UnderlineSpan[] spans = editable.getSpans(start, end, UnderlineSpan.class);
        if (spans.length > 0) {
          hasUnderlineSpan = true;
        }
        
        if (!hasUnderlineSpan) {
          UnderlineSpan underlineSpan = new UnderlineSpan();
          editable.setSpan(underlineSpan, start, end, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        }
      }
      else {
        //
        // User deletes
        UnderlineSpan[] spans = editable.getSpans(start, end, UnderlineSpan.class);
        if (spans.length > 0) {

          UnderlineSpan span = spans[0];
          if (null != span) {
            int italicStart = editable.getSpanStart(span);
            int italicEnd = editable.getSpanEnd(span);
            Util.log("italicStart == " + italicStart + ", italicEnd == " + italicEnd);
            
            if (italicStart >= italicEnd) {
              editable.removeSpan(span);
              
              this.mUnderlineChecked = false;
              ARE_Helper.updateCheckStatus(this, false);
            }
            else {
              // 
              // Do nothing, the default behavior is to extend the span's area.
            }
          }
        }
      }
    }
    else {
      //
      // User un-checks the UNDERLINE
      
      if (end > start) {
        // 
        // User inputs
        UnderlineSpan[] spans = editable.getSpans(start, end, UnderlineSpan.class);
        if (spans.length > 0) {
          UnderlineSpan span = spans[0];
          if (null != span) {
            //
            // User stops the UNDERLINE style, and wants to show un-UNDERLINE characters
            // Remove the UNDERLINE span -- to stop the previous UNDERLINE style
            // Make new UNDERLINE span, with new START and END settings. 
            // The new START should be the same as before, 
            // the new END should be (N - 1), i.e.: the cursor of typing position - 1.
            int italicStart = editable.getSpanStart(span);
            editable.removeSpan(span);
            int newUnderlineEnd = start;
            editable.setSpan(span, italicStart, newUnderlineEnd, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
          }
        }
      }
      else {
        //
        // User deletes
        UnderlineSpan[] spans = editable.getSpans(start, end, UnderlineSpan.class);
        if (spans.length > 0) {
          UnderlineSpan span = spans[0];
          if (null != span) {
            int italicStart = editable.getSpanStart(span);
            int italicEnd = editable.getSpanEnd(span);
            
            if (italicStart >= italicEnd) {
              // 
              // Invalid case, this will never happen.
            }
            else {
              //
              // Do nothing, the default behavior is to extend the span's area.
              // The proceeding characters should be also UNDERLINE
              this.mUnderlineChecked = true;
              ARE_Helper.updateCheckStatus(this, true);                
            }
          }
        }
      }
    }
  }

	@Override
	public ImageView getImageView() {
		return this.mUnderlineImageView;
	}
	
	@Override
	public void setChecked(boolean isChecked) {
		this.mUnderlineChecked = isChecked;
	}
}
