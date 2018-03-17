package com.chinalwb.are.styles.emoji;

import android.app.Activity;
import android.content.Context;
import android.media.MediaDataSource;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.chinalwb.are.R;

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
            Fragment emojiFragment = new EmojiFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(EmojiFragment.ARG_INPUT_RES_ID, emojiGroup.imageResId);
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

    //    @Override
//    public int getCount() {
//        return mPages.size();
//    }
//
//    @Override
//    public boolean isViewFromObject(View view, Object object) {
//        return view == object;
//    }
//
//    @Override
//    public Object instantiateItem(ViewGroup container, int position) {
//        View view = mPages.get(position);
//        container.addView(view);
//        return view;
//    }
//
//    @Override
//    public void destroyItem(ViewGroup container, int position, Object object) {
//        View view = mPages.get(position);
//        container.removeView(view);
//    }
}
