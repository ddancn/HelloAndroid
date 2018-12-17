package com.example.ddancn.helloworld.index;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.example.ddancn.helloworld.index.frg.NotificationsFragment;
import com.example.ddancn.helloworld.R;
import com.example.ddancn.helloworld.index.frg.DashboardFragment;
import com.example.ddancn.helloworld.index.frg.HomeFragment;
import com.example.ddancn.helloworld.utils.ImageUtil;
import com.example.ddancn.helloworld.utils.dialog.CommentDialog;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private BottomNavigationView mBottomNavView;
    private MainPagerAdapter adapter;

    public OnPictureChosen onPictureChosen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mViewPager = findViewById(R.id.viewpager);
        mBottomNavView = findViewById(R.id.navigation);
        adapter = new MainPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new HomeFragment());
        adapter.addFragment(new DashboardFragment());
        adapter.addFragment(new NotificationsFragment());
        mViewPager.setAdapter(adapter);

        mBottomNavView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mViewPager.setCurrentItem(0);
                    return true;
                case R.id.navigation_dashboard:
                    mViewPager.setCurrentItem(1);
                    return true;
                case R.id.navigation_notifications:
                    mViewPager.setCurrentItem(2);
                    return true;
            }
            return false;
        });
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mBottomNavView.getMenu().getItem(position).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CommentDialog.CHOOSE_PIC_FROM_ALBUM:
                if (resultCode == RESULT_OK) {
                    onPictureChosen.onChosen(ImageUtil.handleImage(this,data));
                }
                break;
            default:
                break;
        }
    }

    public interface OnPictureChosen{
        void onChosen(String imagePath);
    }

    private class MainPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragments = new ArrayList<>();

        public MainPagerAdapter(FragmentManager fm) {
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
    }
}


