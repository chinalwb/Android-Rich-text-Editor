package com.chinalwb.are.styles;

import com.chinalwb.are.styles.toolbar.ARE_Toolbar;

import android.content.Context;
import android.widget.EditText;

public abstract class ARE_ABS_Style implements IARE_Style {

	protected Context mContext;
	
	public ARE_ABS_Style() {
		this.mContext = ARE_Toolbar.getInstance().getContext();
	}
	
	@Override
	public EditText getEditText() {
		return ARE_Toolbar.getInstance().getEditText();
	}
}
