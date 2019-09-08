package com.chinalwb.are.styles;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;

import com.chinalwb.are.R;
import com.chinalwb.are.Util;
import com.chinalwb.are.spans.AreImageSpan;
import com.chinalwb.are.spans.EmojiSpan;

import com.chinalwb.are.styles.toolbar.ARE_Toolbar;
import com.chinalwb.are.emojipanel.EmojiGridViewAdapter;
import com.chinalwb.are.emojipanel.EmojiGroup;
import com.chinalwb.are.emojipanel.EmojiGroupDesc;
import com.chinalwb.are.emojipanel.EmojiPagerAdapter;
import com.chinalwb.are.emojipanel.EmojiPanel;

import java.util.ArrayList;

public class ARE_Emoji extends ARE_ABS_FreeStyle {

	private ImageView mEmojiImageView;

	public ARE_Emoji(ARE_Toolbar toolbar) {
		super(toolbar);
	}

	private AdapterView.OnItemClickListener listenerA = new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			int resId = ((EmojiGridViewAdapter.ViewHolder) view.getTag()).resId;
			insertEmoji(resId);
		}
	};

	private AdapterView.OnItemClickListener listenerB = new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			int resId = ((EmojiGridViewAdapter.ViewHolder) view.getTag()).resId;
			insertEmoji(resId);
		}
	};

	private AdapterView.OnItemClickListener listenerC = new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			int resId = ((EmojiGridViewAdapter.ViewHolder) view.getTag()).resId;
			mToolbar.getImageStyle().insertImage(resId, AreImageSpan.ImageType.RES);
		}
	};

	/**
	 * 
	 * @param emojiImageView
	 */
	public ARE_Emoji(ImageView emojiImageView, ARE_Toolbar toolbar) {
		super(toolbar);
		this.mEmojiImageView = emojiImageView;
		initEmojiPanel();
		setListenerForImageView(this.mEmojiImageView);
	}

	private void initEmojiPanel() {
		EmojiPanel emojiPanel = new EmojiPanel(mContext);
		emojiPanel.setId(R.id.emojiPanelId);
		FragmentManager fragmentManager = ((AppCompatActivity) mContext).getSupportFragmentManager();
		ArrayList<EmojiGroup> emojiGroups = initEmojiGroups();
		EmojiPagerAdapter adapter = new EmojiPagerAdapter(mContext, emojiGroups, fragmentManager);
		emojiPanel.setAdapter(adapter);
		mToolbar.setEmojiPanel(emojiPanel);
	}

	private ArrayList<EmojiGroup> initEmojiGroups() {
		EmojiGroup group1 = new EmojiGroup();
		EmojiGroupDesc desc1 = new EmojiGroupDesc();
		desc1.numColumns = 7;
		int[] imageResIds1 = {
				R.drawable.wx_56_56_56_1_1,
				R.drawable.wx_56_56_56_1_2,
				R.drawable.wx_56_56_56_1_3,
				R.drawable.wx_56_56_56_1_4,
				R.drawable.wx_56_56_56_1_5,
				R.drawable.wx_56_56_56_1_6,
				R.drawable.wx_56_56_56_1_7,
				R.drawable.wx_56_56_56_1_8,
				R.drawable.wx_56_56_56_1_9,
				R.drawable.wx_56_56_56_1_10,
				R.drawable.wx_56_56_56_1_11,
				R.drawable.wx_56_56_56_1_12,
				R.drawable.wx_56_56_56_1_13,
				R.drawable.wx_56_56_56_1_14,
				R.drawable.wx_56_56_56_1_15,
				R.drawable.wx_56_56_56_2_1,
				R.drawable.wx_56_56_56_2_2,
				R.drawable.wx_56_56_56_2_3,
				R.drawable.wx_56_56_56_2_4,
				R.drawable.wx_56_56_56_2_5,
				R.drawable.wx_56_56_56_2_6,
				R.drawable.wx_56_56_56_2_7,
				R.drawable.wx_56_56_56_2_8,
				R.drawable.wx_56_56_56_2_9,
				R.drawable.wx_56_56_56_2_10,
				R.drawable.wx_56_56_56_2_11,
				R.drawable.wx_56_56_56_2_12,
				R.drawable.wx_56_56_56_2_13,
				R.drawable.wx_56_56_56_2_14,
				R.drawable.wx_56_56_56_2_15,
		};
		desc1.imageResIds = imageResIds1;
		desc1.size = 56;
		desc1.padding = 5;
		group1.desc = desc1;
		group1.listener = listenerA;

		EmojiGroup group2 = new EmojiGroup();
		EmojiGroupDesc desc2 = new EmojiGroupDesc();
		desc2.numColumns = 6;
		int[] imageResIds2 = {
				R.drawable.wx_48_48_48_1_1,
				R.drawable.wx_48_48_48_1_2,
				R.drawable.wx_48_48_48_1_3,
				R.drawable.wx_48_48_48_1_4,
				R.drawable.wx_48_48_48_1_5,
				R.drawable.wx_48_48_48_1_6,
				R.drawable.wx_48_48_48_1_7,
				R.drawable.wx_48_48_48_1_8,
				R.drawable.wx_48_48_48_1_9,
				R.drawable.wx_48_48_48_1_10,
				R.drawable.wx_48_48_48_1_11,
				R.drawable.wx_48_48_48_1_12,
				R.drawable.wx_48_48_48_1_13,
				R.drawable.wx_48_48_48_1_14,
				R.drawable.wx_48_48_48_1_15,
				R.drawable.wx_48_48_48_2_1,
				R.drawable.wx_48_48_48_2_2,
				R.drawable.wx_48_48_48_2_3,
				R.drawable.wx_48_48_48_2_4,
				R.drawable.wx_48_48_48_2_5,
				R.drawable.wx_48_48_48_2_6,
				R.drawable.wx_48_48_48_2_7,
				R.drawable.wx_48_48_48_2_8,
				R.drawable.wx_48_48_48_2_9,
				R.drawable.wx_48_48_48_2_10,
				R.drawable.wx_48_48_48_2_11,
				R.drawable.wx_48_48_48_2_12,
				R.drawable.wx_48_48_48_2_13,
				R.drawable.wx_48_48_48_2_14,
				R.drawable.wx_48_48_48_2_15,
		};
		desc2.size = 48;
		desc2.padding = 3;
		desc2.imageResIds = imageResIds2;
		group2.listener = listenerB;
		group2.desc = desc2;

		EmojiGroup group3 = new EmojiGroup();
		EmojiGroupDesc desc3 = new EmojiGroupDesc();
		desc3.numColumns = 4;
		desc3.size = 90;
		int[] imageResIds3 = {
				R.drawable.wx_d_1,
				R.drawable.wx_d_2,
				R.drawable.wx_d_3,
				R.drawable.wx_d_4,
				R.drawable.wx_d_5,
				R.drawable.wx_d_6,
				R.drawable.wx_d_7,
				R.drawable.wx_d_8,
		};
		desc3.imageResIds = imageResIds3;
		group3.listener = listenerC;
		group3.desc = desc3;

		ArrayList<EmojiGroup> groups = new ArrayList<>();
		groups.add(group1);
		groups.add(group2);
		groups.add(group3);

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
		 mToolbar.toggleEmojiPanel(true);
	}

	@NonNull
	protected void insertEmoji(int emojiId) {
		EditText editText = getEditText();
		int size = editText.getLineHeight();
		// Insert emoji
		int selectionStart = editText.getSelectionStart();
		int selectionEnd = editText.getSelectionEnd();

		Editable editable = editText.getText();
		EmojiSpan span = new EmojiSpan(editText.getContext(), emojiId, size);
		SpannableStringBuilder ssb = new SpannableStringBuilder();
		ssb.append(com.chinalwb.are.Constants.ZERO_WIDTH_SPACE_STR);
		ssb.setSpan(span, 0, ssb.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		editable.replace(selectionStart, selectionEnd, ssb);

		// logAllEmojiSpans(editable);
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
