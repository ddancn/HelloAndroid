package com.example.ddancn.helloworld.ui.dialog.comment.selector.emo;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.ddancn.helloworld.R;

public class EmojiGvAdapter extends BaseAdapter {

    private Context mContext;
    private String[] mEmojis;

    public EmojiGvAdapter (Context context, String[] eachPageEmojis) {
        this.mContext = context;
        this.mEmojis = eachPageEmojis;
    }

    @Override
    public int getCount() {
        return null == mEmojis ? 0 : mEmojis.length;
    }

    @Override
    public String getItem(int position) {
        return null == mEmojis ? "" : mEmojis[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (null == convertView) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.item_emoji, null);
            holder.emojiTv = convertView.findViewById(R.id.tv_emoji);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (position == 20) {
            //第21个显示删除按钮
            holder.emojiTv.setBackgroundResource(R.drawable.ic_emojis_delete);
        } else {
            holder.emojiTv.setText(getItem(position));
        }
        return convertView;
    }

    private static class ViewHolder {
        private TextView emojiTv;
    }


}

