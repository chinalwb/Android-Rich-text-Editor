package com.chinalwb.are.styles;

import com.chinalwb.are.Util;

import android.graphics.Typeface;
import android.text.Editable;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class ARE_Bold implements IARE_Style {

  private ImageView mBoldImageView;
  
  private boolean mBoldChecked;
  
  /**
   * 
   * @param boldImage
   */
  public ARE_Bold(ImageView boldImage) {
    this.mBoldImageView = boldImage;
    setListenerForImageView(this.mBoldImageView);
  }
  
  @Override
  public void setListenerForImageView(final ImageView imageView) {
    imageView.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        mBoldChecked = !mBoldChecked;
        ARE_Helper.updateCheckStatus(ARE_Bold.this, mBoldChecked);
      }
    });
  }
  
  @Override
  public void applyStyle(Editable editable, int start, int end) {
    if (this.mBoldChecked) {
      if (end > start) {
        // 
        // User inputs
        boolean hasBoldSpan = false;
        StyleSpan[] spans = editable.getSpans(start, end, StyleSpan.class);
        if (spans.length > 0) {
          for (StyleSpan span : spans) {
            int spanStyle = span.getStyle();
            if (spanStyle == Typeface.BOLD) {
              hasBoldSpan = true;
              break;
            }
          }
        }
        
        if (!hasBoldSpan) {
          StyleSpan boldSpan = new StyleSpan(Typeface.BOLD);
          editable.setSpan(boldSpan, start, end, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        }
      }
      else {
        //
        // User deletes
        StyleSpan[] spans = editable.getSpans(start, end, StyleSpan.class);
        if (spans.length > 0) {
          for (StyleSpan span : spans) {
            int spanStyle = span.getStyle();
            if (spanStyle == Typeface.BOLD) {
              
              int boldStart = editable.getSpanStart(span);
              int boldEnd = editable.getSpanEnd(span);
              Util.log("boldStart == " + boldStart + ", boldEnd == " + boldEnd);
              
              if (boldStart >= boldEnd) {
                editable.removeSpan(span);
                
                this.mBoldChecked = false;
                ARE_Helper.updateCheckStatus(this, false);
              }
              else {
                // 
                // Do nothing, the default behavior is to extend the span's area.
              }
              
              break;
            }
          }
        }
      }
    }
    else {
      //
      // User un-checks the BOLD
      
      if (end > start) {
        // 
        // User inputs
        StyleSpan[] spans = editable.getSpans(start, end, StyleSpan.class);
        if (spans.length > 0) {
          for (StyleSpan span : spans) {
            int spanStyle = span.getStyle();
            if (spanStyle == Typeface.BOLD) {
              //
              // User stops the BOLD style, and wants to show un-BOLD characters
              // Remove the BOLD span -- to stop the previous BOLD style
              // Make new BOLD span, with new START and END settings. 
              // The new START should be the same as before, 
              // the new END should be (N - 1), i.e.: the cursor of typing position - 1.
              int boldStart = editable.getSpanStart(span);
              editable.removeSpan(span);
              int newBoldEnd = start;
              editable.setSpan(span, boldStart, newBoldEnd, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
              
              break;
            }
          }
        }
      }
      else {
        //
        // User deletes
        StyleSpan[] spans = editable.getSpans(start, end, StyleSpan.class);
        if (spans.length > 0) {
          for (StyleSpan span : spans) {
            int spanStyle = span.getStyle();
            if (spanStyle == Typeface.BOLD) {

              int boldStart = editable.getSpanStart(span);
              int boldEnd = editable.getSpanEnd(span);
              
              if (boldStart >= boldEnd) {
                // 
                // Invalid case, this will never happen.
              }
              else {
                //
                // Do nothing, the default behavior is to extend the span's area.
                // The proceeding characters should be also BOLD
                this.mBoldChecked = true;
                ARE_Helper.updateCheckStatus(this, true);                
              }
              break;
            }
          }
        }
      }
    }
  } // #End of method 

	@Override
	public ImageView getImageView() {
		return this.mBoldImageView;
	}
	
	@Override
	public void setChecked(boolean isChecked) {
		this.mBoldChecked = isChecked;
	}

}
