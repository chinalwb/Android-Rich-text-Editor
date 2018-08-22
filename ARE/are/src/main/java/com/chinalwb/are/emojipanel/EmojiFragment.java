package com.chinalwb.are.emojipanel;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.chinalwb.are.R;

/**
 * Created by wliu on 2018/3/17.
 */

public class EmojiFragment extends Fragment {

    private Context mContext;

    private EmojiGroupDesc mEmojiGroupDesc;

    public static final String ARG_INPUT_EMOJI_GROUP_DESC = "EMOJI_GROUP_DESC";

    private AdapterView.OnItemClickListener mListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
        mEmojiGroupDesc = (EmojiGroupDesc) getArguments().get(ARG_INPUT_EMOJI_GROUP_DESC);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (mEmojiGroupDesc == null) {
            return null;
        }
        GridView gridView = (GridView) inflater.inflate(R.layout.are_emoji_panel, container, false);
        gridView.setNumColumns(mEmojiGroupDesc.numColumns);
        EmojiGridViewAdapter adapter = new EmojiGridViewAdapter(mContext, mEmojiGroupDesc);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(mListener);
        return gridView;
    }

    public void setListener(AdapterView.OnItemClickListener listener) {
        this.mListener = listener;
    }
}
