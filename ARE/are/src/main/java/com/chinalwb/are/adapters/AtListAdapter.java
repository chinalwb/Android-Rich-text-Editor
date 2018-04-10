package com.chinalwb.are.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinalwb.are.R;
import com.chinalwb.are.models.AtItem;

import java.util.ArrayList;

/**
 * Created by wliu on 2018/2/4.
 */

public class AtListAdapter extends BaseAdapter {

    private Context mContext;

    private ArrayList<AtItem> mItemsList;

    private LayoutInflater mLayoutInflater;

    public AtListAdapter(Context context, ArrayList<AtItem> itemsList) {
        this.mContext = context;
        this.mItemsList = itemsList;
        this.mLayoutInflater = LayoutInflater.from(this.mContext);
    }

    public void setData(ArrayList<AtItem> itemsList) {
        this.mItemsList = itemsList;
    }

    @Override
    public int getCount() {
        return this.mItemsList == null ? 0 : this.mItemsList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public AtItem getItem(int position) {
        return (this.mItemsList != null && this.mItemsList.size() > position)
                ? this.mItemsList.get(position) : null;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.are_adapter_at_item_view, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.imageView = convertView.findViewById(R.id.are_view_at_item_image);
            viewHolder.textView = convertView.findViewById(R.id.are_view_at_item_name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        AtItem item = getItem(position);
        viewHolder.imageView.setImageResource(item.mIconId);
        viewHolder.textView.setText(item.mName);


        return convertView;
    }

    class ViewHolder {
        ImageView imageView;
        TextView textView;
    }

}
