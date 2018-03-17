package com.chinalwb.are.styles.emoji;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.chinalwb.are.R;

/**
 * Created by wliu on 2018/3/17.
 */

public class EmojiGridViewAdapter extends BaseAdapter {

    private Context mContext;

    private int[] mImageResIds;

    public EmojiGridViewAdapter(Context context, int[] imageResIds) {
        mContext = context;
        mImageResIds = imageResIds;
    }

    @Override
    public int getCount() {
        if (mImageResIds != null) {
            return mImageResIds.length;
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return mImageResIds[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null || convertView.getTag() == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.are_emoji_item, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.imageView = (ImageView) convertView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.imageView.setImageResource(mImageResIds[position]);
        viewHolder.resId = mImageResIds[position];

        return convertView;
    }

    public class ViewHolder {
        public ImageView imageView;
        public int resId;
    }
}
