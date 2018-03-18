package com.chinalwb.are;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;

public class AREditTextPlaceHolder extends AppCompatEditText {

	public AREditTextPlaceHolder(Context context) {
		this(context, null);
	}

	public AREditTextPlaceHolder(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public AREditTextPlaceHolder(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
		setupListeners();
	}

	public void enforceFocus() {
		this.setText(" ");  // 1
		this.requestFocus();
		this.setSelection(0); // 2. Work with 1 to make the cursor style looks correct
	}

	private void init() {
		this.setFocusableInTouchMode(true);
		this.setBackgroundColor(Color.WHITE);
		this.setInputType(EditorInfo.TYPE_CLASS_TEXT | EditorInfo.TYPE_TEXT_FLAG_MULTI_LINE
				| EditorInfo.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
	}

	private void setupListeners() {
		setupKeyListener();
	}

	private void setupKeyListener() {
		this.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == Constants.KEY_DEL) {
					// Only key down takes effect to avoid the other AREditText affects this one
					// As when key down on DEL when focus is set on AREditText next to this control
					// The AREditText will enforce the focus on this control, and so the ACTION_UP
					// of DEL key will go into this block, which is not really on this control in fact.
					if (event.getAction() == KeyEvent.ACTION_DOWN) {
						deleteImageLayout();
					}
				}
				else {
					prependTextToNext(keyCode, event);
					return true;
				}
				return false;
			}
		});
	}
	
	/**
	 * 
	 */
	private void deleteImageLayout() {
		LinearLayout imageLayout = (LinearLayout) this.getParent();
		if (null == imageLayout) {
			return;
		}
		LinearLayout parentLayout = (LinearLayout) imageLayout.getParent();
		if (null == parentLayout) {
			return;
		}
		int index = parentLayout.indexOfChild(imageLayout);
		int previousIndex = index - 1;
		int nextIndex = index + 1;
		AREditText previousEditText = (AREditText) parentLayout.getChildAt(previousIndex);
		Editable previousEditable = (Editable) previousEditText.getText();
		previousEditable.append(Constants.CHAR_NEW_LINE);
		int previousTextLength = previousEditable.length();
		AREditText nextEditText = (AREditText) parentLayout.getChildAt(nextIndex);
		Editable nextEditable = nextEditText.getText();
		nextEditable = Editable.Factory.getInstance().newEditable(nextEditable);
		
		// 1. remove image layout
		parentLayout.removeView(imageLayout);
		// 2. remove the AREditText after the image layout
		parentLayout.removeView(nextEditText);
		// 3. previous AREditText gets focus
		previousEditText.requestFocus();
		// 4. append the next AREditText content to the previous one 
		previousEditable.append(nextEditable);
		// 5. set the focus to the end of the previous AREditText
		previousEditText.setSelection(previousTextLength);
		// 6. remove next editable
		Util.log("Removed edittext == " + nextEditText);
		nextEditText.removeTextWatcher();
		parentLayout.removeView(nextEditText);
	}

	private void prependTextToNext(int keyCode, KeyEvent event) {
		LinearLayout imageLayout = (LinearLayout) this.getParent();
		if (null == imageLayout) {
			return;
		}
		LinearLayout parentLayout = (LinearLayout) imageLayout.getParent();
		if (null == parentLayout) {
			return;
		}

		int index = parentLayout.indexOfChild(imageLayout);
		int nextEditIndex = index + 1;
		AREditText nextEditText = (AREditText) parentLayout.getChildAt(nextEditIndex);
		if (null == nextEditText) {
			return;
		}
		
		nextEditText.requestFocus();
		
		//
		// If key code == 66, it means user clicks the Enter
		// Then just let the next edit text get focus but not
		// prepend the \n into the next edit text. Or else there
		// will be extra blank lines
		if (keyCode == 66) {
			return;
		}
		nextEditText.onKeyDown(keyCode, event);
	}
}
