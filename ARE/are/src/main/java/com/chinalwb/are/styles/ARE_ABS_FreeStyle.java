package com.chinalwb.are.styles;

import android.content.Context;
import android.text.Editable;
import android.text.Spanned;
import android.widget.EditText;

import com.chinalwb.are.Util;
import com.chinalwb.are.styles.toolbar.ARE_Toolbar;

import java.lang.reflect.ParameterizedType;

public abstract class ARE_ABS_FreeStyle implements IARE_Style {

	protected Context mContext;

	public ARE_ABS_FreeStyle(Context context) {
		mContext = context;
		if (mContext == null) {
			this.mContext = ARE_Toolbar.getInstance().getContext();
		}
	}

	public ARE_ABS_FreeStyle() {
		this.mContext = ARE_Toolbar.getInstance().getContext();
	}
	
	@Override
	public EditText getEditText() {
		return ARE_Toolbar.getInstance().getEditText();
	}


	// Dummy implementation
	@Override
	public boolean getIsChecked() {
		return false;
	}
}
