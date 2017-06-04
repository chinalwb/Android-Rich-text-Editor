package com.chinalwb.are;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * All Rights Reserved.
 * 
 * @author Wenbin Liu
 * 
 */
public class AREditText extends EditText {

	private ISelectionChangedListener selectionChangedListener;
	
	public AREditText(Context context) {
		this(context, null);
	}

	public AREditText(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public AREditText(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	private void init() {

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
