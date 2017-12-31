package com.chinalwb.are;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

/**
 * All Rights Reserved.
 * 
 * @author Wenbin Liu
 * 
 */
public class AREditText extends EditText {

	private ISelectionChangedListener selectionChangedListener;
	
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
		init();
	}

	private void init() {
		this.setFocusableInTouchMode(true);
		this.setBackgroundColor(Color.WHITE);
		this.setInputType(EditorInfo.TYPE_TEXT_FLAG_NO_SUGGESTIONS | EditorInfo.TYPE_TEXT_FLAG_MULTI_LINE);
		int padding = 8;
		padding = Util.dpToPix(mContext, padding);
		this.setPadding(padding,padding, padding, padding);
		
		this.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				Log.e("AREditText", "has focus == " + hasFocus);
			}
		});
	}
	
	@Override
	protected void onSelectionChanged(int selStart, int selEnd) {
		if (null != this.selectionChangedListener) {
			this.selectionChangedListener.onSelectionChanged(selStart, selEnd);
		}
	}

	/**
	 * 
	 * @param selectionChangedListener
	 */
	public void setSelectionChangedListener(ISelectionChangedListener selectionChangedListener) {
		this.selectionChangedListener = selectionChangedListener;
	}
	
	/**
	 * 
	 * 
	 * @author Wenbin Liu
	 *
	 */
	public interface ISelectionChangedListener {
		public void onSelectionChanged(int selStart, int selEnd);
	}

}
