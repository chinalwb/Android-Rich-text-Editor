package com.chinalwb.are;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.text.Editable;
import android.text.Layout.Alignment;
import android.text.TextWatcher;
import android.text.style.BackgroundColorSpan;
import android.text.style.CharacterStyle;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.chinalwb.are.AREditText.ISelectionChangedListener;
import com.chinalwb.are.styles.ARE_Alignment;
import com.chinalwb.are.styles.ARE_At;
import com.chinalwb.are.styles.ARE_BackgroundColor;
import com.chinalwb.are.styles.ARE_Bold;
import com.chinalwb.are.styles.ARE_Emoji;
import com.chinalwb.are.styles.ARE_Fontface;
import com.chinalwb.are.styles.ARE_Fontsize;
import com.chinalwb.are.styles.ARE_Helper;
import com.chinalwb.are.styles.ARE_Image;
import com.chinalwb.are.styles.ARE_IndentLeft;
import com.chinalwb.are.styles.ARE_IndentRight;
import com.chinalwb.are.styles.ARE_Italic;
import com.chinalwb.are.styles.ARE_ListBullet;
import com.chinalwb.are.styles.ARE_ListNumber;
import com.chinalwb.are.styles.ARE_Strikethrough;
import com.chinalwb.are.styles.ARE_Underline;
import com.chinalwb.are.styles.IARE_Style;

/**
 * All Rights Reserved.
 * 
 * @author Wenbin Liu
 *
 */
public class AREditor extends RelativeLayout implements ISelectionChangedListener {

  private static boolean LOG = false;
  
  /**
   * Request code for selecting an image.
   */
  public static final int REQ_IMAGE = 1;
  
  /* -------------------------------------------- *
   * Instance Fields Area
   * -------------------------------------------- */

  /**
   * Supported styles list.
   */
  private ArrayList<IARE_Style> mStylesList = new ArrayList<IARE_Style>();
  
  /**
   * Context.
   */
  private Context mContext;
  
  /**
   * Edit Text.
   */
  private AREditText mEditText;
  
  /**
   * Editable instance get from {@link #mEditText}
   */
  private Editable mEditable;

  /**
   * Emoji Style
   */
  private ARE_Emoji mEmojiStyle;

  /**
   * Font-size Style
   */
  private ARE_Fontsize mFontsizeStyle;
  
  /**
   * Font-face Style
   */
  private ARE_Fontface mFontfaceStyle;
  
  /**
   * Bold Style
   */
  private ARE_Bold mBoldStyle;
  
  /**
   * Italic Style
   */
  private ARE_Italic mItalicStyle;
  
  /**
   * Underline Style
   */
  private ARE_Underline mUnderlineStyle;
  
  /**
   * Strikethrough Style
   */
  private ARE_Strikethrough mStrikethroughStyle;
  
  /**
   * Background color Style
   */
  private ARE_BackgroundColor mBackgroundColoStyle;
  
  /**
   * List number Style
   */
  private ARE_ListNumber mListNumberStyle;
  
  /**
   * List bullet Style
   */
  private ARE_ListBullet mListBulletStyle;
  
  /**
   * Indent to right Style.
   */
  private ARE_IndentRight mIndentRightStyle;
  
  /**
   * Indent to left Style.
   */
  private ARE_IndentLeft mIndentLeftStyle;
  
  /**
   * Align left.
   */
  private ARE_Alignment mAlignLeft;
  
  /**
   * Align center.
   */
  private ARE_Alignment mAlignCenter;
  
  /**
   * Align right.
   */
  private ARE_Alignment mAlignRight;
  
  /**
   * Insert image style.
   */
  private ARE_Image mImageStyle;
  
  /**
   * At @ mention.
   */
  private ARE_At mAtStyle;
  
  /**
   * Emoji button.
   */
  private ImageView mEmojiImageView;
  
  /**
   * Absolute font size button. 
   */
  private ImageView mFontsizeImageView;
  
  /**
   * Absolute font face button. 
   */
  private ImageView mFontfaceImageView;
  
  /**
   * Bold button.
   */
  private ImageView mBoldImageView;
  
  /**
   * Italic button.
   */
  private ImageView mItalicImageView;
  
  /**
   * Underline button.
   */
  private ImageView mUnderlineImageView;
  
  /**
   * Strikethrough button.
   */
  private ImageView mStrikethroughImageView;
  
  /**
   * Background button.
   */
  private ImageView mBackgroundImageView;
  
  /**
   * List number button.
   */
  private ImageView mRteListNumber;
  
  /**
   * List bullet button.
   */
  private ImageView mRteListBullet;
  
  /**
   * Increase. Indent to right.
   */
  private ImageView mRteIndentRight;
  
  /**
   * Align left.
   */
  private ImageView mRteAlignLeft;
  
  /**
   * Align center.
   */
  private ImageView mRteAlignCenter;
  
  /**
   * Align right.
   */
  private ImageView mRteAlignRight;
  
  /**
   * Decrease. Indent to left.
   */
  private ImageView mRteIndentLeft;
  
  /**
   * Insert image button.
   */
  private ImageView mRteInsertImage;
  
  /**
   * @ mention image button.
   */
  private ImageView mRteAtImage;
  
  /* -------------------------------------------- *
   * Constructors Area
   * -------------------------------------------- */
  
  /**
   * Constructor.
   * 
   * @param context
   */
  public AREditor(Context context) {
    this(context, null);
  }

  /**
   * Constructor.
   * 
   * @param context
   * @param attrs
   */
  public AREditor(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  /**
   * Constructor.
   * 
   * @param context
   * @param attrs
   * @param defStyleAttr
   */
  public AREditor(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    this.mContext = context;
    this.init();
  }

  /* -------------------------------------------- *
   * Business Methods Area
   * -------------------------------------------- */
  /**
   * Initialization.
   */
  private void init() {
    LayoutInflater layoutInflater = LayoutInflater.from(this.mContext);
    if (null != layoutInflater) {
      layoutInflater.inflate(R.layout.areditor, this, true);      
    }
    else {
      return;
    }
    
    this.initViews();
    this.initStyles();
    this.setupListener();
  } // # End of init()
  
  /**
   * Init views.
   */
  private void initViews() {
    this.mEditText = (AREditText) this.findViewById(R.id.are);
    
    this.mEditable = this.mEditText.getEditableText();
    
    this.mEmojiImageView = (ImageView) this.findViewById(R.id.rteEmoji);
    
    this.mFontsizeImageView = (ImageView) this.findViewById(R.id.rteFontsize);
    
    this.mFontfaceImageView = (ImageView) this.findViewById(R.id.rteFontface);
    
    this.mBoldImageView = (ImageView) this.findViewById(R.id.rteBold);
    
    this.mItalicImageView = (ImageView) this.findViewById(R.id.rteItalic);
    
    this.mUnderlineImageView = (ImageView) this.findViewById(R.id.rteUnderline);
    
    this.mStrikethroughImageView = (ImageView) this.findViewById(R.id.rteStrikethrough);

    this.mBackgroundImageView = (ImageView) this.findViewById(R.id.rteBackground);
    
    this.mRteListNumber = (ImageView) this.findViewById(R.id.rteListNumber);
    
    this.mRteListBullet = (ImageView) this.findViewById(R.id.rteListBullet);
    
    this.mRteIndentRight = (ImageView) this.findViewById(R.id.rteIndentRight);
    
    this.mRteIndentLeft = (ImageView) this.findViewById(R.id.rteIndentLeft);
    
    this.mRteAlignLeft = (ImageView) this.findViewById(R.id.rteAlignLeft);
    
    this.mRteAlignCenter = (ImageView) this.findViewById(R.id.rteAlignCenter);
    
    this.mRteAlignRight = (ImageView) this.findViewById(R.id.rteAlignRight);
    
    this.mRteInsertImage = (ImageView) this.findViewById(R.id.rteInsertImage);
    
    this.mRteAtImage = (ImageView) this.findViewById(R.id.rteAt);
  }
  
  
  /**
   * 
   */
  private void initStyles() {
    this.mEmojiStyle = new ARE_Emoji(this.mEmojiImageView, this.mEditText);
    this.mFontsizeStyle = new ARE_Fontsize(this.mFontsizeImageView, this.mEditText);
    this.mFontfaceStyle = new ARE_Fontface(this.mFontfaceImageView, this.mEditText);
    this.mBoldStyle = new ARE_Bold(this.mBoldImageView);
    this.mItalicStyle = new ARE_Italic(this.mItalicImageView);
    this.mUnderlineStyle = new ARE_Underline(this.mUnderlineImageView);
    this.mStrikethroughStyle = new ARE_Strikethrough(this.mStrikethroughImageView);
    this.mBackgroundColoStyle = new ARE_BackgroundColor(this.mBackgroundImageView, Color.GREEN);
    this.mListNumberStyle = new ARE_ListNumber(this.mRteListNumber, this.mEditText);
    this.mListBulletStyle = new ARE_ListBullet(this.mRteListBullet, this.mEditText);
    this.mIndentRightStyle = new ARE_IndentRight(this.mRteIndentRight, this.mEditText);
    this.mIndentLeftStyle = new ARE_IndentLeft(this.mRteIndentLeft, this.mEditText);
    this.mAlignLeft = new ARE_Alignment(this.mRteAlignLeft, this.mEditText, Alignment.ALIGN_NORMAL);
    this.mAlignCenter = new ARE_Alignment(this.mRteAlignCenter, this.mEditText, Alignment.ALIGN_CENTER);
    this.mAlignRight = new ARE_Alignment(this.mRteAlignRight, this.mEditText, Alignment.ALIGN_OPPOSITE);
    this.mImageStyle = new ARE_Image(this.mRteInsertImage, this.mEditText);
    this.mAtStyle = new ARE_At(this.mRteAtImage, this.mEditText);
    
    this.mStylesList.add(this.mEmojiStyle);
    this.mStylesList.add(this.mFontsizeStyle);
    this.mStylesList.add(this.mFontfaceStyle);
    this.mStylesList.add(this.mBoldStyle);
    this.mStylesList.add(this.mItalicStyle);
    this.mStylesList.add(this.mUnderlineStyle);
    this.mStylesList.add(this.mStrikethroughStyle);
    this.mStylesList.add(this.mBackgroundColoStyle);
    this.mStylesList.add(this.mListNumberStyle);
    this.mStylesList.add(this.mListBulletStyle);
    this.mStylesList.add(this.mIndentRightStyle);
    this.mStylesList.add(this.mIndentLeftStyle);
    this.mStylesList.add(this.mAlignLeft);
    this.mStylesList.add(this.mAlignCenter);
    this.mStylesList.add(this.mAlignRight);
    this.mStylesList.add(this.mImageStyle);
    this.mStylesList.add(this.mAtStyle);
  }
  
  /**
   * Sets up listeners for controls.
   */
  private void setupListener() {
	//
	// 
	this.mEditText.setSelectionChangedListener(this); 
	  
    TextWatcher textWatcher = new TextWatcher() {

      int startPos = 0;
      int endPos = 0;
      
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        if (LOG) {
          Util.log("beforeTextChanged:: s = " + s + ", start = " + start + ", count = " + count + ", after = " + after);
        }
        // DO NOTHING FOR NOW
      }
      
      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (LOG) {
          Util.log("onTextChanged:: s = " + s + ", start = " + start + ", count = " + count + ", before = " + before);  
        }
        this.startPos = start;
        this.endPos = start + count;
      }
      
      @Override
      public void afterTextChanged(Editable s) {
        if (LOG) {
          Util.log("afterTextChanged:: s = " + s);  
        }
        
        for (IARE_Style style : mStylesList) {
          style.applyStyle(mEditable, startPos, endPos);
        }
      }
    };
    
    this.mEditText.addTextChangedListener(textWatcher);
    
    
    
  } // #End of setupListener()

  
  
  /* ----------------------------------------- *
   * Rich Text Style Area 
   * ----------------------------------------- */
  
  @Override
	public void onSelectionChanged(int selStart, int selEnd) {
		boolean boldExists = false;
		boolean italicsExists = false;
		boolean underlinedExists = false;
		boolean striketrhoughExists = false;
		boolean backgroundColorExists = false;

		//
		// Two cases:
		// 1. Selection is just a pure cursor
		// 2. Selection is a range
		if (selStart > 0 && selStart == selEnd) {
			CharacterStyle[] styleSpans = this.mEditable.getSpans(selStart - 1,
					selStart, CharacterStyle.class);

			for (int i = 0; i < styleSpans.length; i++) {
				if (styleSpans[i] instanceof StyleSpan) {
					if (((StyleSpan) styleSpans[i]).getStyle() == android.graphics.Typeface.BOLD) {
						boldExists = true;
					} 
					else if (((StyleSpan) styleSpans[i]).getStyle() == android.graphics.Typeface.ITALIC) {
						italicsExists = true;
					} 
					else if (((StyleSpan) styleSpans[i]).getStyle() == android.graphics.Typeface.BOLD_ITALIC) {
						// TODO
					}
				} 
				else if (styleSpans[i] instanceof UnderlineSpan) {
					underlinedExists = true;
				} 
				else if (styleSpans[i] instanceof StrikethroughSpan) {
					striketrhoughExists = true;
				} 
				else if (styleSpans[i] instanceof BackgroundColorSpan) {
					backgroundColorExists = true;
				}
			}
		} 
		else {
			//
			// Selection is a range
			CharacterStyle[] styleSpans = this.mEditable.getSpans(selStart, selEnd,
			          CharacterStyle.class);

			for (int i = 0; i < styleSpans.length; i++) {

		        if (styleSpans[i] instanceof StyleSpan) {
		          if (((StyleSpan) styleSpans[i]).getStyle() == android.graphics.Typeface.BOLD) {
		            if (this.mEditable.getSpanStart(styleSpans[i]) <= selStart
		                && this.mEditable.getSpanEnd(styleSpans[i]) >= selEnd) {
		              boldExists = true;
		            }
		          }
		          else if (((StyleSpan) styleSpans[i]).getStyle() == android.graphics.Typeface.ITALIC) {
		            if (this.mEditable.getSpanStart(styleSpans[i]) <= selStart
		                && this.mEditable.getSpanEnd(styleSpans[i]) >= selEnd) {
		              italicsExists = true;
		            }
		          }
		          else if (((StyleSpan) styleSpans[i]).getStyle() == android.graphics.Typeface.BOLD_ITALIC) {
		            if (this.mEditable.getSpanStart(styleSpans[i]) <= selStart
		                && this.mEditable.getSpanEnd(styleSpans[i]) >= selEnd) {
		              italicsExists = true;
		              boldExists = true;
		            }
		          }
		        }
		        else if (styleSpans[i] instanceof UnderlineSpan) {
		          if (this.mEditable.getSpanStart(styleSpans[i]) <= selStart
		              && this.mEditable.getSpanEnd(styleSpans[i]) >= selEnd) {
		            underlinedExists = true;
		          }
		        }
		        else if (styleSpans[i] instanceof StrikethroughSpan) {
		          if (this.mEditable.getSpanStart(styleSpans[i]) <= selStart
		              && this.mEditable.getSpanEnd(styleSpans[i]) >= selEnd) {
		            striketrhoughExists = true;
		          }
		        }
		        else if (styleSpans[i] instanceof BackgroundColorSpan) {
		          if (this.mEditable.getSpanStart(styleSpans[i]) <= selStart
		              && this.mEditable.getSpanEnd(styleSpans[i]) >= selEnd) {
		            backgroundColorExists = true;
		          }
		        }
			}
		}
		
		//
		// Set style checked status
		ARE_Helper.updateCheckStatus(this.mBoldStyle, boldExists);
		ARE_Helper.updateCheckStatus(this.mItalicStyle, italicsExists);
		ARE_Helper.updateCheckStatus(this.mUnderlineStyle, underlinedExists);
		ARE_Helper.updateCheckStatus(this.mStrikethroughStyle, striketrhoughExists);
		ARE_Helper.updateCheckStatus(this.mBackgroundColoStyle, backgroundColorExists);
	} // #End of method:: onSelectionChanged
  
  /**
   * 
   * @param requestCode
   * @param resultCode
   * @param data
   */
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (resultCode == Activity.RESULT_OK) {
      if (REQ_IMAGE == requestCode) {
        Uri uri = data.getData();
        this.mImageStyle.insertImage(uri);        
      }
    }
  } // #End of onActivityResult(..)
  
}
