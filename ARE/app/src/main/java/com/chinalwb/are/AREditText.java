package com.chinalwb.are;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.style.BackgroundColorSpan;
import android.text.style.CharacterStyle;
import android.text.style.QuoteSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.chinalwb.are.spans.AreUnderlineSpan;
import com.chinalwb.are.styles.ARE_Helper;
import com.chinalwb.are.styles.IARE_Style;
import com.chinalwb.are.styles.toolbar.ARE_Toolbar;

/**
 * All Rights Reserved.
 * 
 * @author Wenbin Liu
 * 
 */
public class AREditText extends AppCompatEditText {

	private static boolean LOG = false;
	
	/**
	 * When the user 1st time presses DEL and the focus is at a start pos = 0
	 * Then mark this as true;
	 * When the user 2nd time presses DEL and the focus is at start pos = 0
	 * Then go to the previous edit text.
	 * 
	 * Needs to reset it as false when user types other keys or user changes
	 * the edit text.
	 */
	private static boolean sToDetectDel = false;

	private ARE_Toolbar sToolbar;

	private static List<IARE_Style> sStylesList;

	private Context mContext;

	public AREditText(Context context) {
		this(context, null);
	}

	public AREditText(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public AREditText(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		mContext = context;
		sToolbar = ARE_Toolbar.getInstance();
		sToolbar.setEditText(this);
		sStylesList = sToolbar.getStylesList();
		init();
		setupListener();
	}

	private void init() {
		this.setFocusableInTouchMode(true);
		this.setBackgroundColor(Color.WHITE);
		this.setInputType(EditorInfo.TYPE_CLASS_TEXT | EditorInfo.TYPE_TEXT_FLAG_MULTI_LINE
				| EditorInfo.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
		int padding = 8;
		padding = Util.getPixelByDp(mContext, padding);
		this.setPadding(padding, padding, padding, padding);
		this.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
		this.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					ARE_Toolbar.getInstance().setEditText(AREditText.this);
				}
			}
		});
	}

	/**
	 * Sets up listeners for controls.
	 */
	private void setupListener() {
		setupKeyListener();
		setupTextWatcher();
	} // #End of setupListener()

	private void setupKeyListener() {
		this.setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == Constants.KEY_DEL) {
					int startPos = AREditText.this.getSelectionStart();
					if (startPos == 0) {
						if (sToDetectDel) {
							focusToPrevious();
							sToDetectDel = false;
							return true;
						}
						
						sToDetectDel = true;
					}
				}
				else {
					sToDetectDel = false;
				}
				return false;
			}
		});
	}
	
	private void focusToPrevious() {
		LinearLayout parentLayout = (LinearLayout) this.getParent();
		int index = parentLayout.indexOfChild(this);
		if (index > 0) {
			int previousIndex = index - 1;
			LinearLayout imageLayout = (LinearLayout) parentLayout.getChildAt(previousIndex);
			AREditTextPlaceHolder placeHolderEdit = (AREditTextPlaceHolder) imageLayout.getChildAt(1);
			placeHolderEdit.enforceFocus();
		}
	}
	
	/**
	 * Monitoring text changes.
	 */
	private void setupTextWatcher() {
		TextWatcher textWatcher = new TextWatcher() {

			int startPos = 0;
			int endPos = 0;

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				if (LOG) {
					Util.log("beforeTextChanged:: s = " + s + ", start = " + start + ", count = " + count
							+ ", after = " + after);
				}
				// DO NOTHING FOR NOW
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (LOG) {
					Util.log("onTextChanged:: s = " + s + ", start = " + start + ", count = " + count + ", before = "
							+ before);
				}
				this.startPos = start;
				this.endPos = start + count;
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (LOG) {
					Util.log("afterTextChanged:: s = " + s);
				}
				
				if (endPos <= startPos) {
					Util.log("User deletes: start == " + startPos + " endPos == " + endPos);
				}

				for (IARE_Style style : sStylesList) {
					style.applyStyle(getEditableText(), startPos, endPos);
				}
			}
		};

		this.addTextChangedListener(textWatcher);
	}

	/*
	 * ----------------------------------------- * Rich Text Style Area
	 * -----------------------------------------
	 */

	@Override
	public void onSelectionChanged(int selStart, int selEnd) {
		if (sToolbar == null) {
			return;
		}
		
		boolean boldExists = false;
		boolean italicsExists = false;
		boolean underlinedExists = false;
		boolean striketrhoughExists = false;
		boolean backgroundColorExists = false;
		boolean quoteExists = false;

		//
		// Two cases:
		// 1. Selection is just a pure cursor
		// 2. Selection is a range
		Editable editable = this.getEditableText();
		if (selStart > 0 && selStart == selEnd) {
			CharacterStyle[] styleSpans = editable.getSpans(selStart - 1, selStart, CharacterStyle.class);

			for (int i = 0; i < styleSpans.length; i++) {
				if (styleSpans[i] instanceof StyleSpan) {
					if (((StyleSpan) styleSpans[i]).getStyle() == android.graphics.Typeface.BOLD) {
						boldExists = true;
					} else if (((StyleSpan) styleSpans[i]).getStyle() == android.graphics.Typeface.ITALIC) {
						italicsExists = true;
					} else if (((StyleSpan) styleSpans[i]).getStyle() == android.graphics.Typeface.BOLD_ITALIC) {
						// TODO
					}
				} else if (styleSpans[i] instanceof AreUnderlineSpan) {
					underlinedExists = true;
				} else if (styleSpans[i] instanceof StrikethroughSpan) {
					striketrhoughExists = true;
				} else if (styleSpans[i] instanceof BackgroundColorSpan) {
					backgroundColorExists = true;
				}
			}

			QuoteSpan[] quoteSpans = editable.getSpans(selStart - 1, selStart, QuoteSpan.class);
			if (quoteSpans != null && quoteSpans.length > 0) {
				quoteExists = true;
			}
			// To check Quote!!
		} else {
			//
			// Selection is a range
			CharacterStyle[] styleSpans = editable.getSpans(selStart, selEnd, CharacterStyle.class);

			for (int i = 0; i < styleSpans.length; i++) {

				if (styleSpans[i] instanceof StyleSpan) {
					if (((StyleSpan) styleSpans[i]).getStyle() == android.graphics.Typeface.BOLD) {
						if (editable.getSpanStart(styleSpans[i]) <= selStart
								&& editable.getSpanEnd(styleSpans[i]) >= selEnd) {
							boldExists = true;
						}
					} else if (((StyleSpan) styleSpans[i]).getStyle() == android.graphics.Typeface.ITALIC) {
						if (editable.getSpanStart(styleSpans[i]) <= selStart
								&& editable.getSpanEnd(styleSpans[i]) >= selEnd) {
							italicsExists = true;
						}
					} else if (((StyleSpan) styleSpans[i]).getStyle() == android.graphics.Typeface.BOLD_ITALIC) {
						if (editable.getSpanStart(styleSpans[i]) <= selStart
								&& editable.getSpanEnd(styleSpans[i]) >= selEnd) {
							italicsExists = true;
							boldExists = true;
						}
					}
				} else if (styleSpans[i] instanceof AreUnderlineSpan) {
					if (editable.getSpanStart(styleSpans[i]) <= selStart
							&& editable.getSpanEnd(styleSpans[i]) >= selEnd) {
						underlinedExists = true;
					}
				} else if (styleSpans[i] instanceof StrikethroughSpan) {
					if (editable.getSpanStart(styleSpans[i]) <= selStart
							&& editable.getSpanEnd(styleSpans[i]) >= selEnd) {
						striketrhoughExists = true;
					}
				} else if (styleSpans[i] instanceof BackgroundColorSpan) {
					if (editable.getSpanStart(styleSpans[i]) <= selStart
							&& editable.getSpanEnd(styleSpans[i]) >= selEnd) {
						backgroundColorExists = true;
					}
				}
			}
		}

		QuoteSpan[] quoteSpans = editable.getSpans(selStart, selEnd, QuoteSpan.class);
		if (quoteSpans != null && quoteSpans.length > 0) {
			if (editable.getSpanStart(quoteSpans[0]) <= selStart
					&& editable.getSpanEnd(quoteSpans[0]) >= selEnd) {
				quoteExists = true;
			}
		}

		//
		// Set style checked status
		ARE_Helper.updateCheckStatus(sToolbar.getBoldStyle(), boldExists);
		ARE_Helper.updateCheckStatus(sToolbar.getItalicStyle(), italicsExists);
		ARE_Helper.updateCheckStatus(sToolbar.getUnderlineStyle(), underlinedExists);
		ARE_Helper.updateCheckStatus(sToolbar.getStrikethroughStyle(), striketrhoughExists);
		ARE_Helper.updateCheckStatus(sToolbar.getBackgroundColoStyle(), backgroundColorExists);
		ARE_Helper.updateCheckStatus(sToolbar.getQuoteStyle(), quoteExists);
	} // #End of method:: onSelectionChanged
}
