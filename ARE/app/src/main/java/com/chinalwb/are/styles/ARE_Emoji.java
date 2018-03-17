package com.chinalwb.are.styles;

import android.app.Activity;
import android.app.VoiceInteractor;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;

import com.chinalwb.are.R;
import com.chinalwb.are.Util;
import com.chinalwb.are.spans.EmojiSpan;
import com.chinalwb.are.styles.emoji.EmojiGridViewAdapter;
import com.chinalwb.are.styles.emoji.EmojiGroup;
import com.chinalwb.are.styles.emoji.EmojiPagerAdapter;
import com.chinalwb.are.styles.emoji.EmojiPanel;
import com.chinalwb.are.styles.toolbar.ARE_Toolbar;

import java.util.ArrayList;

public class ARE_Emoji extends ARE_ABS_FreeStyle {

	private ImageView mEmojiImageView;

	private AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			int resId = ((EmojiGridViewAdapter.ViewHolder) view.getTag()).resId;
			insertEmoji(resId);
		}
	};

	/**
	 * 
	 * @param emojiImageView
	 */
	public ARE_Emoji(ImageView emojiImageView) {
		this.mEmojiImageView = emojiImageView;
		initEmojiPanel();
		setListenerForImageView(this.mEmojiImageView);
	}

	private void initEmojiPanel() {
		EmojiPanel emojiPanel = new EmojiPanel(mContext);
		emojiPanel.setId(R.id.emojiPanelId);
		FragmentManager fragmentManager = ((AppCompatActivity) mContext).getSupportFragmentManager();
		ArrayList<EmojiGroup> emojiGroups = initEmojiGroups();
		EmojiPagerAdapter adapter = new EmojiPagerAdapter(mContext, emojiGroups, listener, fragmentManager);
		emojiPanel.setAdapter(adapter);
		ARE_Toolbar.getInstance().setEmojiPanel(emojiPanel);
	}

	private ArrayList<EmojiGroup> initEmojiGroups() {
		EmojiGroup group1 = new EmojiGroup();
		group1.numColumns = 7;
		int[] imageResIds1 = new int[50];
		for (int i = 0; i < 50; i++) {
			int pos = i % 3;
			int id = R.drawable.fontface;
			switch (pos) {
				case 0:
					id = R.drawable.fontface;
					break;
				case 1:
					id = R.drawable.emoji;
					break;
				case 2:
					id = R.drawable.at;
					break;
				default:
					break;
			}
			imageResIds1[i] = id;
		}
		group1.imageResIds = imageResIds1;


		EmojiGroup group2 = new EmojiGroup();
		group2.numColumns = 4;
		int[] imageResIds2 = new int[15];
		for (int i = 0; i < 15; i++) {
			int pos = i % 5;
			int id = R.drawable.cartoon_man_in_love;
			switch (pos) {
				case 0:
					id = R.drawable.cartoon_man_is_smoking;
					break;
				case 1:
					id = R.drawable.cartoon_man_is_urinating;
					break;
				case 2:
					id = R.drawable.cartoon_man_laughing;
					break;
				default:
					id = R.drawable.cartoon_man_with_flower_bouquet;
					break;
			}
			imageResIds2[i] = id;
		}
		group2.imageResIds = imageResIds2;

		ArrayList<EmojiGroup> groups = new ArrayList<>();
		groups.add(group1);
		groups.add(group2);

		return groups;
	}

	@Override
	public void setListenerForImageView(ImageView imageView) {
		imageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				 showEmojiPanel();
			}
		});
	}

	private void showEmojiPanel() {
		// Hide keyboard
		// Get keyboard height
		// Show emoji panel
		 ARE_Toolbar.getInstance().toggleEmojiPanel(true);
	}

	@NonNull
	protected void insertEmoji(int emojiId) {
		EditText editText = getEditText();
		// Insert emoji
		int selectionStart = editText.getSelectionStart();
		int selectionEnd = editText.getSelectionEnd();

		Editable editable = editText.getText();
		EmojiSpan span = new EmojiSpan(editText.getContext(), emojiId);
		SpannableStringBuilder ssb = new SpannableStringBuilder();
		ssb.append(com.chinalwb.are.Constants.ZERO_WIDTH_SPACE_STR);
		ssb.setSpan(span, 0, ssb.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		editable.replace(selectionStart, selectionEnd, ssb);

		logAllEmojiSpans(editable);
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
		EmojiSpan[] emojiSpans = editable.getSpans(0, editable.length(), EmojiSpan.class);
		for (EmojiSpan span : emojiSpans) {
			int ss = editable.getSpanStart(span);
			int se = editable.getSpanEnd(span);
			Util.log("List All: " + " :: start == " + ss + ", end == " + se);
		}
	}
}
