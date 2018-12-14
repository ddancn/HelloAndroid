package com.example.ddancn.helloworld.utils.selector.emo;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.ddancn.helloworld.R;

import java.util.ArrayList;
import java.util.List;

public class EmojiVpAdapter extends PagerAdapter {

    private Context mContext;
    private String[] mEmojis;
    private List<View> mPagers;
    private OnEmojiClickListener mEmojiClickListener;

    public EmojiVpAdapter(Context context) {
        this.mContext = context;
        this.mEmojis = getEmojis();
        this.mPagers = getPagerList();
    }

    private String[] getEmojis() {
        return new String[]{"\ud83d\ude02", "\ud83d\ude03", "\ud83d\ude04", "\ud83d\ude05", "\ud83d\ude06",
                "\ud83d\ude07", "\ud83d\ude08", "\ud83d\ude09", "\ud83d\ude0a", "\ud83d\ude0b",
                "\ud83d\ude0c", "\ud83d\ude0d", "\ud83d\ude0e", "\ud83d\ude0f", "\ud83d\ude10",
                "\ud83d\ude11", "\ud83d\ude12", "\ud83d\ude13", "\ud83d\ude14", "\ud83d\ude15",
                "\ud83d\ude16", "\ud83d\ude17", "\ud83d\ude18", "\ud83d\ude19", "\ud83d\ude1a",
                "\ud83d\ude1b", "\ud83d\ude1c", "\ud83d\ude1d", "\ud83d\ude1e", "\ud83d\ude1f",
                "\ud83d\ude01", "\ud83d\ude01", "\ud83d\ude01", "\ud83d\ude01", "\ud83d\ude01",
                "\ud83d\ude01", "\ud83d\ude01", "\ud83d\ude01", "\ud83d\ude01", "\ud83d\ude01",
                "\ud83d\ude01", "\ud83d\ude01", "\ud83d\ude01", "\ud83d\ude01", "\ud83d\ude01",
                "\ud83d\ude01", "\ud83d\ude01", "\ud83d\ude01", "\ud83d\ude01", "\ud83d\ude01",
                "\ud83d\ude01", "\ud83d\ude01", "\ud83d\ude01", "\ud83d\ude01", "\ud83d\ude01"};
    }

    public List<View> getPagerList() {
        List<View> pagers = null;
        if (null != mEmojis && mEmojis.length > 0) {
            pagers = new ArrayList<>();
            int pageCount = (int) Math.ceil((double) mEmojis.length / 20);
            for (int i = 0; i < pageCount; i++) {
                GridView gridView = new GridView(mContext);
                GridView.LayoutParams params = new GridView.LayoutParams(
                        GridView.LayoutParams.MATCH_PARENT, GridView.LayoutParams.MATCH_PARENT);
                gridView.setLayoutParams(params);
                gridView.setNumColumns(7);

                //获取数据源
                String[] eachPageEmojis = new String[21];
                System.arraycopy(mEmojis, i * 20, eachPageEmojis, 0, i != pageCount - 1 ? 20 : mEmojis.length - i * 20);
                eachPageEmojis[20] = "del";//第21是删除按钮,用特殊字符串表示
                gridView.setAdapter(new EmojiGvAdapter(mContext, eachPageEmojis));

                //点击表情监听
                gridView.setOnItemClickListener((parent, view, position, id) -> {
                    //获取选中的表情字符
                    String emoji = (String) parent.getAdapter().getItem(position);
                    if (null != mEmojiClickListener) {
                        mEmojiClickListener.onClick(emoji);
                    }
                });
                pagers.add(gridView);
            }

        }
        return pagers;
    }

    @Override
    public int getCount() {
        return null == mPagers ? 0 : mPagers.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = mPagers.get(position);
        if (null != view) {
            container.addView(view);
        }
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public interface OnEmojiClickListener {
        void onClick(String emoji);
    }

    public void setOnEmojiClickListener(OnEmojiClickListener l) {
        this.mEmojiClickListener = l;
    }

    public void setupWithPagerPoint(ViewPager viewPager, final LinearLayout pointLayout) {
        //初始化表情指示器
        int pageCount = getCount();
        for (int i = 0; i < pageCount; i++) {
            ImageView point = new ImageView(mContext);
            point.setImageResource(R.drawable.dot_unselected);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(25, 25);
            params.rightMargin = 25;
            if (i == 0) {
                point.setImageResource(R.drawable.dot_selected);
            }
            pointLayout.addView(point, params);
        }
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                //切换指示器
                if (null != pointLayout && pointLayout.getChildCount() > 0) {
                    for (int i = 0; i < pointLayout.getChildCount(); i++) {
                        ((ImageView) pointLayout.getChildAt(i)).setImageResource(R.drawable.dot_unselected);
                    }
                    ((ImageView) pointLayout.getChildAt(position)).setImageResource(R.drawable.dot_selected);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }
}
