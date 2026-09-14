package com.chinalwb.are.styles;

import android.content.Context;
import android.widget.EditText;

import com.chinalwb.are.styles.toolbar.ARE_Toolbar;

public abstract class ARE_ABS_FreeStyle implements IARE_Style {

	protected Context mContext;
	protected ARE_Toolbar mToolbar;
	protected EditText mEditText;

	public ARE_ABS_FreeStyle(Context context) {
		mContext = context;
	}

	public ARE_ABS_FreeStyle(ARE_Toolbar toolbar) {
		this.mToolbar = toolbar;
		if (null != toolbar) {
			this.mContext = toolbar.getContext();
			this.mEditText = toolbar.getEditText();
		}
	}

	@Override
	public EditText getEditText() {
		if (null != mEditText) {
			return mEditText;
		}
		if (null != mToolbar) {
			return mToolbar.getEditText();
		}
		return null;
	}

	// Dummy implementation
	@Override
	public boolean getIsChecked() {
		return false;
	}
}
