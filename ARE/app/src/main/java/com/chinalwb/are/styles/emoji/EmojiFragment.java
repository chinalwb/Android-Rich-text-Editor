package com.chinalwb.are.styles.emoji;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.chinalwb.are.R;

/**
 * Created by wliu on 2018/3/17.
 */

public class EmojiFragment extends Fragment {

    private int mImageResId;

    public static final String ARG_INPUT_RES_ID = "RES_ID";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mImageResId = getArguments().getInt(ARG_INPUT_RES_ID);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.are_emoji, container, false);
        ImageView imageView = viewGroup.findViewById(R.id.areEmojiImage);
        imageView.setImageResource(mImageResId);
        return viewGroup;
    }
}
