package com.chinalwb.are.styles;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.text.Editable;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chinalwb.are.AREditText;
import com.chinalwb.are.AREditTextPlaceHolder;
import com.chinalwb.are.Constants;
import com.chinalwb.are.styles.toolbar.ARE_Toolbar;

public class ARE_Image extends ARE_ABS_Style {

	private ImageView mInsertImageView;

	/**
	 * 
	 * @param emojiImageView
	 */
	public ARE_Image(ImageView emojiImageView) {
		this.mInsertImageView = emojiImageView;
		setListenerForImageView(this.mInsertImageView);
	}

	@Override
	public void setListenerForImageView(ImageView imageView) {
		imageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				openImageChooser();
			}
		});
	} // #End of setListenerForImageView(..)

	/**
	 * Open system image chooser page.
	 */
	private void openImageChooser() {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		((Activity) this.mContext).startActivityForResult(intent,
				ARE_Toolbar.REQ_IMAGE);
	}

	/**
	 * 
	 * @param uri
	 */
	public void insertImage(Uri uri) {
		// AreImageSpan imageSpan = new AreImageSpan(mContext, uri);
		//
		// int start = this.mEditText.getSelectionStart();
		// int end = this.mEditText.getSelectionEnd();
		//
		// Editable editable = this.mEditText.getEditableText();
		//
		// SpannableStringBuilder ssb = new SpannableStringBuilder();
		// ssb.append(Constants.ZERO_WIDTH_SPACE_STR);
		// ssb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		//
		// editable.replace(start, end, ssb);s

		EditText editText = this.getEditText();
		
		LinearLayout rootContainerLayout = (LinearLayout) editText.getParent();
		LinearLayout imageContainerLayout = new LinearLayout(mContext);
		LinearLayout.LayoutParams imageContainerLayoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT, // Width
				LinearLayout.LayoutParams.WRAP_CONTENT // Height
		);
		imageContainerLayout.setGravity(Gravity.CENTER); // @Todo
		imageContainerLayout.setOrientation(LinearLayout.HORIZONTAL);
		imageContainerLayout.setLayoutParams(imageContainerLayoutParams);

		//
		// 1. Left
		// AREditText leftEditText = new AREditText(mContext);
		// LinearLayout.LayoutParams leftEditTextLayoutParams = new
		// LinearLayout.LayoutParams(
		// LinearLayout.LayoutParams.WRAP_CONTENT, // Width
		// LinearLayout.LayoutParams.MATCH_PARENT // Height
		// );
		// leftEditText.setText(" ");
		// leftEditText.setGravity(Gravity.BOTTOM);
		// leftEditText.setFocusableInTouchMode(true);
		// leftEditText.setLayoutParams(leftEditTextLayoutParams);
		// imageContainerLayout.addView(leftEditText);

		//
		// 2. Image
		ImageView imageView = new ImageView(mContext);
		LinearLayout.LayoutParams imageViewLayoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT, // Width
				LinearLayout.LayoutParams.WRAP_CONTENT // Height
		);
		imageView.setImageURI(uri);
		imageView.setTag(uri);
		imageView.setLayoutParams(imageViewLayoutParams);
		imageContainerLayout.addView(imageView);

		//
		// 3. Right
		final AREditTextPlaceHolder rightEditText = new AREditTextPlaceHolder(mContext);
		LinearLayout.LayoutParams rightEditTextLayoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT, // Width
				LinearLayout.LayoutParams.MATCH_PARENT // Height
		);
		rightEditText.setPadding(5, 5, 5, 5);
		rightEditText.setText(Constants.ZERO_WIDTH_SPACE_STR);
		rightEditText.setGravity(Gravity.BOTTOM);
		rightEditText.setFocusableInTouchMode(true);
		rightEditText.setLayoutParams(rightEditTextLayoutParams);
		imageContainerLayout.addView(rightEditText);

		imageContainerLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				rightEditText.enforceFocus();
			}
		});

		int index = rootContainerLayout.indexOfChild(editText);
		rootContainerLayout.addView(imageContainerLayout, index + 1);
		
		AREditText newEditText = new AREditText(mContext);
		LinearLayout.LayoutParams editTextLayoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		newEditText.setLayoutParams(editTextLayoutParams);
		rootContainerLayout.addView(newEditText, index + 2);
		
		
		
		Editable editable = editText.getEditableText();
		int selectionStart = editText.getSelectionStart();
		int selectionEnd = editText.getSelectionEnd();
		int length = editable.length();
		if (selectionEnd < length) {
			CharSequence textBeforeFocus = editable.subSequence(0, selectionStart);
			CharSequence textAfterFocus = editable.subSequence(selectionEnd, length);
			
			editText.setText(textBeforeFocus);
			newEditText.setText(textAfterFocus);
		}
		
		newEditText.requestFocus();
		newEditText.setSelection(0);
	}

	@Override
	public void applyStyle(Editable editable, int start, int end) {
		// Do nothing
	}

	@Override
	public ImageView getImageView() {
		return this.mInsertImageView;
	}

	@Override
	public void setChecked(boolean isChecked) {
		// Do nothing
	}
}
