package com.example.ddancn.helloworld.ui.dialog.comment.selector.file;

import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.widget.ImageButton;

import com.example.ddancn.helloworld.R;

import java.util.ArrayList;
import java.util.List;

public class FileSelectorActivity extends AppCompatActivity {

    public static String FILE_CHOSEN = "fileChosen";

    private ImageButton btnBack;
    private ViewPager mViewPager;
    private TabLayout mTab;
    private FilePagerAdapter mAdapter;

    public static String[] TYPE_NAMES = {"DOC", "XLS", "PPT", "PDF", "其他"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_selector);

        btnBack = findViewById(R.id.btn_back);
        mViewPager = findViewById(R.id.vp_file);
        mTab = findViewById(R.id.tl_file);

        btnBack.setOnClickListener(v -> this.finish());
        mAdapter = new FilePagerAdapter(getSupportFragmentManager());

        for (int i = 0; i < FileSelectorFragment.TYPES.length; i++)
            mAdapter.addFragment(FileSelectorFragment.newInstance(i));

        List<String> list = new ArrayList<>();
        for(int i=0;i<TYPE_NAMES.length;i++)
            list.add(TYPE_NAMES[i]);
        mAdapter.setTitles(list);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(5);
        mTab.setupWithViewPager(mViewPager);
    }


    public class FilePagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragments = new ArrayList<>();

        private List<String> titles = new ArrayList<>();

        public FilePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        public void addFragment(Fragment fragment) {
            fragments.add(fragment);
        }

        public void setTitles(List<String> titles) {
            this.titles = titles;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }
}
