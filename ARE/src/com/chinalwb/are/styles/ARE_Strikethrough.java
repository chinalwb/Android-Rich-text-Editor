package com.chinalwb.are.styles;


import android.text.Editable;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.chinalwb.are.Util;

public class ARE_Strikethrough extends ARE_ABS_Style {
  
  private ImageView mStrikethroughImageView;
  
  private boolean mStrikethroughChecked;
  
  /**
   * 
   * @param StrikethroughImage
   */
  public ARE_Strikethrough(ImageView StrikethroughImage) {
    this.mStrikethroughImageView = StrikethroughImage;
    setListenerForImageView(this.mStrikethroughImageView);
  }
  
  @Override
  public void setListenerForImageView(final ImageView imageView) {
    imageView.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        mStrikethroughChecked = !mStrikethroughChecked;
        ARE_Helper.updateCheckStatus(ARE_Strikethrough.this, mStrikethroughChecked);
      }
    });
  }

  @Override
  public void applyStyle(Editable editable, int start, int end) {
    if (this.mStrikethroughChecked) {
      if (end > start) {
        // 
        // User inputs
        boolean hasStrikethroughSpan = false;
        StrikethroughSpan[] spans = editable.getSpans(start, end, StrikethroughSpan.class);
        if (spans.length > 0) {
          hasStrikethroughSpan = true;
        }
        
        if (!hasStrikethroughSpan) {
          StrikethroughSpan underlineSpan = new StrikethroughSpan();
          editable.setSpan(underlineSpan, start, end, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        }
      }
      else {
        //
        // User deletes
        StrikethroughSpan[] spans = editable.getSpans(start, end, StrikethroughSpan.class);
        if (spans.length > 0) {

          StrikethroughSpan span = spans[0];
          if (null != span) {
            int italicStart = editable.getSpanStart(span);
            int italicEnd = editable.getSpanEnd(span);
            Util.log("italicStart == " + italicStart + ", italicEnd == " + italicEnd);
            
            if (italicStart >= italicEnd) {
              editable.removeSpan(span);
              
              this.mStrikethroughChecked = false;
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
      // User un-checks the Strikethrough
      
      if (end > start) {
        // 
        // User inputs
        StrikethroughSpan[] spans = editable.getSpans(start, end, StrikethroughSpan.class);
        if (spans.length > 0) {
          StrikethroughSpan span = spans[0];
          if (null != span) {
            //
            // User stops the Strikethrough style, and wants to show un-Strikethrough characters
            // Remove the Strikethrough span -- to stop the previous Strikethrough style
            // Make new Strikethrough span, with new START and END settings. 
            // The new START should be the same as before, 
            // the new END should be (N - 1), i.e.: the cursor of typing position - 1.
            int italicStart = editable.getSpanStart(span);
            editable.removeSpan(span);
            int newStrikethroughEnd = start;
            editable.setSpan(span, italicStart, newStrikethroughEnd, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
          }
        }
      }
      else {
        //
        // User deletes
        StrikethroughSpan[] spans = editable.getSpans(start, end, StrikethroughSpan.class);
        if (spans.length > 0) {
          StrikethroughSpan span = spans[0];
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
              // The proceeding characters should be also Strikethrough
              this.mStrikethroughChecked = true;
              ARE_Helper.updateCheckStatus(this, true);                
            }
          }
        }
      }
    }
  }

	@Override
	public ImageView getImageView() {
		return this.mStrikethroughImageView;
	}
	
	@Override
	public void setChecked(boolean isChecked) {
		this.mStrikethroughChecked = isChecked;
	}
}
