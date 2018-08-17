package com.chinalwb.are.styles.toolitems.styles;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.text.Editable;
import android.text.Spanned;
import android.widget.ImageView;

import com.chinalwb.are.AREditText;
import com.chinalwb.are.activities.Are_AtPickerActivity;
import com.chinalwb.are.models.AtItem;
import com.chinalwb.are.spans.AreAtSpan;
import com.chinalwb.are.strategies.AtStrategy;
import com.chinalwb.are.styles.ARE_ABS_FreeStyle;
import com.chinalwb.are.styles.toolbar.ARE_Toolbar;

public class ARE_Style_At extends ARE_ABS_FreeStyle {

	public static final int REQUEST_CODE = 1004;

	public static final String EXTRA_TAG = "atItem";

	private static final String AT = "@";

	private static int AT_INSERT_POS = -1;

	private AREditText mEditText;

	public ARE_Style_At(AREditText editText) {
	    super(editText.getContext());
		this.mEditText = editText;
	}

	@Override
	public void setListenerForImageView(ImageView imageView) {
	}

	@Override
	public void applyStyle(Editable editable, int start, int end) {
		if (end > start) {
			String typeString = editable.toString().substring(start, end);
			if (typeString.equals(AT)) {
				// Open contacts list
				openAtPicker();
				AT_INSERT_POS = end;
			}
		}
	}

	private void openAtPicker() {
		AtStrategy atStrategy = mEditText.getAtStrategy();
		if (atStrategy != null) {
			atStrategy.openAtPage();
			return;
		}
		Intent intent = new Intent(this.mContext, Are_AtPickerActivity.class);
		((Activity) this.mContext).startActivityForResult(intent, REQUEST_CODE);
	}

	public void insertAt(AtItem atItem) {
		AtStrategy atStrategy = mEditText.getAtStrategy();
		boolean consumed = false;
		if (atStrategy != null) {
			consumed = atStrategy.onItemSelected(atItem);
		}
		if (consumed) {
			return;
		}
		if (null == this.mEditText) {
			return;
		}
		if (atItem.mColor == Color.BLUE) {
			if (atItem.mName.startsWith("Steve")) { // For demo purpose
				atItem.mColor = Color.MAGENTA;
			}
		}
		AreAtSpan atSpan = new AreAtSpan(atItem);
		this.mEditText.getEditableText().insert(AT_INSERT_POS, atItem.mName);
		this.mEditText.getEditableText().setSpan(atSpan, AT_INSERT_POS - 1, AT_INSERT_POS + atItem.mName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
	}

	@Override
	public ImageView getImageView() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setChecked(boolean isChecked) {
		// TODO Auto-generated method stub

	}
}
