package com.chinalwb.are.styles;


import android.text.Editable;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.chinalwb.are.Util;

public class ARE_BackgroundColor extends ARE_ABS_Style {
  
  private ImageView mBackgroundImageView;
  
  private boolean mBackgroundChecked;
  
  private int mColor;
  
  /**
   * 
   * @param backgroundImage
   */
  public ARE_BackgroundColor(ImageView backgroundImage, int backgroundColor) {
    this.mBackgroundImageView = backgroundImage;
    this.mColor = backgroundColor;
    setListenerForImageView(this.mBackgroundImageView);
  }
  
  @Override
  public void setListenerForImageView(final ImageView imageView) {
    imageView.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        mBackgroundChecked = !mBackgroundChecked;
        ARE_Helper.updateCheckStatus(ARE_BackgroundColor.this, mBackgroundChecked);
      }
    });
  }

  @Override
  public void applyStyle(Editable editable, int start, int end) {
    if (this.mBackgroundChecked) {
      if (end > start) {
        // 
        // User inputs
        boolean hasBackgroundColorSpan = false;
        BackgroundColorSpan[] spans = editable.getSpans(start, end, BackgroundColorSpan.class);
        if (spans.length > 0) {
          hasBackgroundColorSpan = true;
        }
        
        if (!hasBackgroundColorSpan) {
          BackgroundColorSpan backgroundColorSpan = new BackgroundColorSpan(this.mColor);
          editable.setSpan(backgroundColorSpan, start, end, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        }
      }
      else {
        //
        // User deletes
        BackgroundColorSpan[] spans = editable.getSpans(start, end, BackgroundColorSpan.class);
        if (spans.length > 0) {

          BackgroundColorSpan span = spans[0];
          if (null != span) {
            int backgroundStart = editable.getSpanStart(span);
            int backgroundEnd = editable.getSpanEnd(span);
            Util.log("backgroundStart == " + backgroundStart + ", backgroundEnd == " + backgroundEnd);
            
            if (backgroundStart >= backgroundEnd) {
              editable.removeSpan(span);
              
              this.mBackgroundChecked = false;
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
        BackgroundColorSpan[] spans = editable.getSpans(start, end, BackgroundColorSpan.class);
        if (spans.length > 0) {
          BackgroundColorSpan span = spans[0];
          if (null != span) {
            //
            // User stops the Strikethrough style, and wants to show un-Strikethrough characters
            // Remove the Strikethrough span -- to stop the previous Strikethrough style
            // Make new Strikethrough span, with new START and END settings. 
            // The new START should be the same as before, 
            // the new END should be (N - 1), i.e.: the cursor of typing position - 1.
            int backgroundStart = editable.getSpanStart(span);
            editable.removeSpan(span);
            int newStrikethroughEnd = start;
            editable.setSpan(span, backgroundStart, newStrikethroughEnd, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
          }
        }
      }
      else {
        //
        // User deletes
        BackgroundColorSpan[] spans = editable.getSpans(start, end, BackgroundColorSpan.class);
        if (spans.length > 0) {
          BackgroundColorSpan span = spans[0];
          if (null != span) {
            int backgroundStart = editable.getSpanStart(span);
            int backgroundEnd = editable.getSpanEnd(span);
            
            if (backgroundStart >= backgroundEnd) {
              // 
              // Invalid case, this will never happen.
            }
            else {
              //
              // Do nothing, the default behavior is to extend the span's area.
              // The proceeding characters should be also Strikethrough
              this.mBackgroundChecked = true;
              ARE_Helper.updateCheckStatus(this, true);                
            }
          }
        }
      }
    }
  }

	@Override
	public ImageView getImageView() {
		return this.mBackgroundImageView;
	}
	
	@Override
	public void setChecked(boolean isChecked) {
		this.mBackgroundChecked = isChecked;
	}
}
