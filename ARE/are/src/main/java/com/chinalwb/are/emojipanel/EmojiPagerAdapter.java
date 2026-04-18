package com.chinalwb.are.emojipanel;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

/**
 * Created by wliu on 2018/3/17.
 */

public class EmojiPagerAdapter extends FragmentStatePagerAdapter {

    private Context mContext;

    private ArrayList<Fragment> mPages = new ArrayList<>();

    private ArrayList<EmojiGroup> mEmojiGroups = new ArrayList<>();

    public EmojiPagerAdapter(Context context, ArrayList<EmojiGroup> emojiGroups, FragmentManager fragmentManager) {
        super(fragmentManager);
        mContext = context;
        mEmojiGroups = emojiGroups;
        initPages();
    }

    private void initPages() {
        if (mEmojiGroups == null || mEmojiGroups.size() == 0) {
            throw new IllegalArgumentException("Emoji Groups cannot be empty!!");
        }
        for (EmojiGroup emojiGroup : mEmojiGroups) {
            EmojiFragment emojiFragment = new EmojiFragment();
            emojiFragment.setListener(emojiGroup.listener);
            Bundle bundle = new Bundle();
            bundle.putSerializable(EmojiFragment.ARG_INPUT_EMOJI_GROUP_DESC, emojiGroup.desc);
            emojiFragment.setArguments(bundle);
            mPages.add(emojiFragment);
        }
    }

    @Override
    public Fragment getItem(int position) {
        return mPages.get(position);
    }

    @Override
    public int getCount() {
        return mPages.size();
    }

    public void setEmojiGroups(ArrayList<EmojiGroup> emojiGroups) {
        this.mEmojiGroups = emojiGroups;
    }
}
