package com.chinalwb.are.styles.toolbar;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.text.Layout.Alignment;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chinalwb.are.AREditText;
import com.chinalwb.are.R;
import com.chinalwb.are.models.AtItem;
import com.chinalwb.are.styles.ARE_Alignment;
import com.chinalwb.are.styles.ARE_At;
import com.chinalwb.are.styles.ARE_BackgroundColor;
import com.chinalwb.are.styles.ARE_Bold;
import com.chinalwb.are.styles.ARE_Emoji;
import com.chinalwb.are.styles.ARE_FontColor;
import com.chinalwb.are.styles.ARE_Fontface;
import com.chinalwb.are.styles.ARE_Fontsize;
import com.chinalwb.are.styles.ARE_Image;
import com.chinalwb.are.styles.ARE_IndentLeft;
import com.chinalwb.are.styles.ARE_IndentRight;
import com.chinalwb.are.styles.ARE_Italic;
import com.chinalwb.are.styles.ARE_Link;
import com.chinalwb.are.styles.ARE_ListBullet;
import com.chinalwb.are.styles.ARE_ListNumber;
import com.chinalwb.are.styles.ARE_Quote;
import com.chinalwb.are.styles.ARE_Strikethrough;
import com.chinalwb.are.styles.ARE_Underline;
import com.chinalwb.are.styles.IARE_Style;
import com.rainliu.colorpicker.ColorPickerListener;
import com.rainliu.colorpicker.ColorPickerView;

public class ARE_Toolbar extends LinearLayout {

	private static ARE_Toolbar sInstance;
	
	/**
	 * Request code for selecting an image.
	 */
	public static final int REQ_IMAGE = 1;

	/**
	 * Request code for choosing a people to @.
	 */
	public static final int REQ_AT = 2;
	
	private Context mContext;
	
	private AREditText mEditText;
	
	/**
	 * Supported styles list.
	 */
	private ArrayList<IARE_Style> mStylesList = new ArrayList<IARE_Style>();

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
	 * Quote style
	 */
	private ARE_Quote mQuoteStyle;

	/**
	 * Font color Style
	 */
	private ARE_FontColor mFontColorStyle;

	/**
	 * Background color Style
	 */
	private ARE_BackgroundColor mBackgroundColoStyle;

	/**
	 * Link Style
	 */
	private ARE_Link mLinkStyle;

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
	 * Quote button.
	 */
	private ImageView mQuoteImageView;

	/**
	 * The color palette.
	 */
	private ColorPickerView mColorPalette;

	/**
	 * Foreground color image view.
	 */
	private ImageView mFontColorImageView;

	/**
	 * Background button.
	 */
	private ImageView mBackgroundImageView;

	/**
	 * Add link button.
	 */
	private ImageView mLinkImageView;

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

	public ARE_Toolbar(Context context) {
		this(context, null);
	}

	public ARE_Toolbar(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ARE_Toolbar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.mContext = context;
		sInstance = this;
		init();
	}

	private void init() {
		LayoutInflater layoutInflater = LayoutInflater.from(this.mContext);
		layoutInflater.inflate(R.layout.are_toolbar, this, true);
		this.setOrientation(LinearLayout.VERTICAL);
		initViews();
		initStyles();
	}

	private void initViews() {

		this.mEmojiImageView = (ImageView) this.findViewById(R.id.rteEmoji);

		this.mFontsizeImageView = (ImageView) this.findViewById(R.id.rteFontsize);

		this.mFontfaceImageView = (ImageView) this.findViewById(R.id.rteFontface);

		this.mBoldImageView = (ImageView) this.findViewById(R.id.rteBold);

		this.mItalicImageView = (ImageView) this.findViewById(R.id.rteItalic);

		this.mUnderlineImageView = (ImageView) this.findViewById(R.id.rteUnderline);

		this.mQuoteImageView = (ImageView) this.findViewById(R.id.rteQuote);

		this.mColorPalette = (ColorPickerView) this.findViewById(R.id.rteColorPalette);

		this.mFontColorImageView = (ImageView) this.findViewById(R.id.rteFontColor);

		this.mStrikethroughImageView = (ImageView) this.findViewById(R.id.rteStrikethrough);

		this.mBackgroundImageView = (ImageView) this.findViewById(R.id.rteBackground);

		this.mLinkImageView = (ImageView) this.findViewById(R.id.rteLink);

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
		this.mEmojiStyle = new ARE_Emoji(this.mEmojiImageView);
		this.mFontsizeStyle = new ARE_Fontsize(this.mFontsizeImageView);
		this.mFontfaceStyle = new ARE_Fontface(this.mFontfaceImageView);
		this.mBoldStyle = new ARE_Bold(this.mBoldImageView);
		this.mItalicStyle = new ARE_Italic(this.mItalicImageView);
		this.mUnderlineStyle = new ARE_Underline(this.mUnderlineImageView);
		this.mQuoteStyle = new ARE_Quote(this.mQuoteImageView);
		this.mFontColorStyle = new ARE_FontColor(this.mFontColorImageView);
		this.mStrikethroughStyle = new ARE_Strikethrough(this.mStrikethroughImageView);
		this.mBackgroundColoStyle = new ARE_BackgroundColor(this.mBackgroundImageView, Color.YELLOW);
		this.mLinkStyle = new ARE_Link(this.mLinkImageView);
		this.mListNumberStyle = new ARE_ListNumber(this.mRteListNumber);
		this.mListBulletStyle = new ARE_ListBullet(this.mRteListBullet);
		this.mIndentRightStyle = new ARE_IndentRight(this.mRteIndentRight);
		this.mIndentLeftStyle = new ARE_IndentLeft(this.mRteIndentLeft);
		this.mAlignLeft = new ARE_Alignment(this.mRteAlignLeft, Alignment.ALIGN_NORMAL);
		this.mAlignCenter = new ARE_Alignment(this.mRteAlignCenter, Alignment.ALIGN_CENTER);
		this.mAlignRight = new ARE_Alignment(this.mRteAlignRight, Alignment.ALIGN_OPPOSITE);
		this.mImageStyle = new ARE_Image(this.mRteInsertImage);
		this.mAtStyle = new ARE_At();

		this.mStylesList.add(this.mEmojiStyle);
		this.mStylesList.add(this.mFontsizeStyle);
		this.mStylesList.add(this.mFontfaceStyle);
		this.mStylesList.add(this.mBoldStyle);
		this.mStylesList.add(this.mItalicStyle);
		this.mStylesList.add(this.mUnderlineStyle);
		this.mStylesList.add(this.mQuoteStyle);
		this.mStylesList.add(this.mFontColorStyle);
		this.mStylesList.add(this.mStrikethroughStyle);
		this.mStylesList.add(this.mBackgroundColoStyle);
		this.mStylesList.add(this.mLinkStyle);
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

	public static ARE_Toolbar getInstance() {
		return sInstance;
	}
	
	public void setEditText(AREditText editText) {
		this.mEditText = editText;
		bindToolbar();
	}
	
	private void bindToolbar() {
		this.mBoldStyle.setEditText(this.mEditText);
		this.mItalicStyle.setEditText(this.mEditText);
		this.mUnderlineStyle.setEditText(this.mEditText);
		this.mQuoteStyle.setEditText(this.mEditText);
		this.mFontColorStyle.setEditText(this.mEditText);
		this.mStrikethroughStyle.setEditText(this.mEditText);
		this.mBackgroundColoStyle.setEditText(this.mEditText);
		this.mLinkStyle.setEditText(this.mEditText);
		this.mAtStyle.setEditText(this.mEditText);
	}
	
	public EditText getEditText() {
		return this.mEditText;
	}
	
	public IARE_Style getBoldStyle() {
		return this.mBoldStyle;
	}
	
	public ARE_Italic getItalicStyle() {
		return this.mItalicStyle;
	}
	
	public ARE_Underline getUnderlineStyle() {
		return mUnderlineStyle;
	}

	public ARE_Quote getQuoteStyle() {
		return mQuoteStyle;
	}

	public ARE_FontColor getTextColorStyle() { return  this.mFontColorStyle; }
	
	public ARE_Strikethrough getStrikethroughStyle() {
		return mStrikethroughStyle;
	}
	
	public ARE_BackgroundColor getBackgroundColoStyle() {
		return mBackgroundColoStyle;
	}

	public List<IARE_Style> getStylesList() {
		return this.mStylesList;
	}

	public void toggleColorPalette(ColorPickerListener colorPickerListener) {
		int visibility = this.mColorPalette.getVisibility();
		this.mColorPalette.setColorPickerListener(colorPickerListener);
		if (View.VISIBLE == visibility) {
			this.mColorPalette.setVisibility(View.GONE);
		} else {
			this.mColorPalette.setVisibility(View.VISIBLE);
		}
	}

	public void setColorPaletteColor(int color) {
		this.mColorPalette.setColor(color);
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			if (REQ_IMAGE == requestCode) {
				Uri uri = data.getData();
				this.mImageStyle.insertImage(uri);
			} else if (REQ_AT == requestCode) {
				AtItem atItem = (AtItem) data.getSerializableExtra(ARE_At.EXTRA_TAG);
				if (null == atItem) { return; }
				this.mAtStyle.insertAt(atItem);
			}
		}
	}

}
