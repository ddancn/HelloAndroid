package com.example.ddancn.helloworld.index.frg;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ddancn.helloworld.R;

import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment {

    private SwipeRefreshLayout swipeRefresh;
    private ListView listView;
    private List<String> mList= new ArrayList<>();
    private MsgAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        for(int i=0;i<10;i++)
            mList.add("initinit");
        adapter = new MsgAdapter(getActivity(),R.layout.item,mList);
        listView = getActivity().findViewById(R.id.listView);
        listView.setAdapter(adapter);
        swipeRefresh = getActivity().findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);//设置刷新进度条的颜色
        swipeRefresh.setOnRefreshListener(() -> new Handler().postDelayed(() -> {
            mList.add(0,"Refresh");
            adapter.notifyDataSetChanged();
            swipeRefresh.setRefreshing(false);
        }, 500));

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    if (view.getLastVisiblePosition() == view.getCount() - 1) {
                        mList.add("load");
                        adapter.notifyDataSetChanged();
                    }
                }
            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });
    }
    class MsgAdapter extends ArrayAdapter<String> {
        private int resourceId;

        public MsgAdapter(Context context, int textViewResourceId, List<String> list){
            super(context, textViewResourceId, list);
            resourceId = textViewResourceId;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            String msg = getItem(position);
            View view;
            ViewHolder viewHolder;
            if (convertView == null) {
                view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.msg =  view.findViewById (R.id.msg);
                view.setTag(viewHolder); // 将ViewHolder存储在View中
            } else {
                view = convertView;
                viewHolder = (ViewHolder) view.getTag(); // 重新获取ViewHolder
            }
            viewHolder.msg.setText(msg);
            return view;
        }

        class ViewHolder {
            TextView msg;
        }
    }
}
