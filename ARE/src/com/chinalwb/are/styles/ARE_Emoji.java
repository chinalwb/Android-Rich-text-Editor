package com.chinalwb.are.styles;

import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;

import com.chinalwb.are.R;
import com.chinalwb.are.Util;
import com.chinalwb.are.spans.EmojiSpan;

public class ARE_Emoji implements IARE_Style {

	private ImageView mEmojiImageView;

	private EditText mEditText;
	
	/**
	 * 
	 * @param emojiImageView
	 */
	public ARE_Emoji(ImageView emojiImageView, EditText editText) {
		this.mEmojiImageView = emojiImageView;
		this.mEditText = editText;
		setListenerForImageView(this.mEmojiImageView);
	}

	@Override
	public void setListenerForImageView(ImageView imageView) {
		imageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Insert emoji
			  int selectionStart = mEditText.getSelectionStart();
			  int selectionEnd = mEditText.getSelectionEnd();
			  
			  Editable editable = mEditText.getText();
			  EmojiSpan span = new EmojiSpan(mEditText.getContext(), R.drawable.emoji);
			  SpannableStringBuilder ssb = new SpannableStringBuilder();
			  ssb.append(com.chinalwb.are.Constants.ZERO_WIDTH_SPACE_STR);
			  ssb.setSpan(span, 0, ssb.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			  editable.replace(selectionStart, selectionEnd, ssb);
			  
			  logAllEmojiSpans(editable);
			}
		});
	}

	@Override
	public void applyStyle(Editable editable, int start, int end) {
		// Do nothing
	}

	@Override
	public ImageView getImageView() {
		return this.mEmojiImageView;
	}

	@Override
	public void setChecked(boolean isChecked) {
		// Do nothing
	}
	
	 private void logAllEmojiSpans(Editable editable) {
	    EmojiSpan[] emojiSpans = editable.getSpans(0,
	        editable.length(), EmojiSpan.class);
	    for (EmojiSpan span : emojiSpans) {
	      int ss = editable.getSpanStart(span);
	      int se = editable.getSpanEnd(span);
	      Util.log("List All: " + " :: start == " + ss + ", end == " + se);
	    }
	  }

}
