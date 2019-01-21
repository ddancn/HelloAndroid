package com.example.ddancn.helloworld.ui.dialog.comment.selector.at;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.ddancn.helloworld.R;

import java.util.List;

public class AtUserRvAdapter extends BaseQuickAdapter<UserInfo, BaseViewHolder> {

    public AtUserRvAdapter(int layoutResId, @Nullable List<UserInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, UserInfo item) {
        helper.setText(R.id.tv_username, item.getUsername());
        helper.setText(R.id.tv_title, item.getTitle());
        helper.addOnClickListener(R.id.checkbox);
    }
}
