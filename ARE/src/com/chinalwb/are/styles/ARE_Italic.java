package com.chinalwb.are.styles;


import android.graphics.Typeface;
import android.text.Editable;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.chinalwb.are.Util;

public class ARE_Italic extends ARE_ABS_Style {

  private ImageView mItalicImageView;
  
  private boolean mItalicChecked;
  
  /**
   * 
   * @param italicImage
   */
  public ARE_Italic(ImageView italicImage) {
    this.mItalicImageView = italicImage;
    setListenerForImageView(this.mItalicImageView);
  }
  
  @Override
  public void setListenerForImageView(final ImageView imageView) {
    imageView.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        mItalicChecked = !mItalicChecked;
        ARE_Helper.updateCheckStatus(ARE_Italic.this, mItalicChecked);
      }
    });
  }
  
  @Override
  public void applyStyle(Editable editable, int start, int end) {
    if (this.mItalicChecked) {
      if (end > start) {
        // 
        // User inputs
        boolean hasItalicSpan = false;
        StyleSpan[] spans = editable.getSpans(start, end, StyleSpan.class);
        if (spans.length > 0) {
          for (StyleSpan span : spans) {
            int spanStyle = span.getStyle();
            if (spanStyle == Typeface.ITALIC) {
              hasItalicSpan = true;
              break;
            }
          }
        }
        
        if (!hasItalicSpan) {
          StyleSpan italicSpan = new StyleSpan(Typeface.ITALIC);
          editable.setSpan(italicSpan, start, end, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        }
      }
      else {
        //
        // User deletes
        StyleSpan[] spans = editable.getSpans(start, end, StyleSpan.class);
        if (spans.length > 0) {
          for (StyleSpan span : spans) {
            int spanStyle = span.getStyle();
            if (spanStyle == Typeface.ITALIC) {
              
              int italicStart = editable.getSpanStart(span);
              int italicEnd = editable.getSpanEnd(span);
              Util.log("italicStart == " + italicStart + ", italicEnd == " + italicEnd);
              
              if (italicStart >= italicEnd) {
                editable.removeSpan(span);
                
                this.mItalicChecked = false;
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
      // User un-checks the ITALIC
      
      if (end > start) {
        // 
        // User inputs
        StyleSpan[] spans = editable.getSpans(start, end, StyleSpan.class);
        if (spans.length > 0) {
          for (StyleSpan span : spans) {
            int spanStyle = span.getStyle();
            if (spanStyle == Typeface.ITALIC) {
              //
              // User stops the ITALIC style, and wants to show un-ITALIC characters
              // Remove the ITALIC span -- to stop the previous ITALIC style
              // Make new ITALIC span, with new START and END settings. 
              // The new START should be the same as before, 
              // the new END should be (N - 1), i.e.: the cursor of typing position - 1.
              int italicStart = editable.getSpanStart(span);
              editable.removeSpan(span);
              int newItalicEnd = start;
              editable.setSpan(span, italicStart, newItalicEnd, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
              
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
            if (spanStyle == Typeface.ITALIC) {

              int italicStart = editable.getSpanStart(span);
              int italicEnd = editable.getSpanEnd(span);
              
              if (italicStart >= italicEnd) {
                // 
                // Invalid case, this will never happen.
              }
              else {
                //
                // Do nothing, the default behavior is to extend the span's area.
                // The proceeding characters should be also ITALIC
                this.mItalicChecked = true;
                ARE_Helper.updateCheckStatus(this, true);           
              }
              break;
            }
          }
        }
      }
    }
  }

	@Override
	public ImageView getImageView() {
		return this.mItalicImageView;
	}
	
	@Override
	public void setChecked(boolean isChecked) {
		this.mItalicChecked = isChecked;
	}
}
