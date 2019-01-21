package com.example.ddancn.helloworld.utils;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Rect;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;

import com.example.ddancn.helloworld.MyApplication;

public class DimenUtil {

    public static final String SOFT_INPUT_HEIGHT = "softInputHeight";
    public static final int SOFT_INPUT_DEFAULT_HEIGHT = 787;

    private static final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext());


    /**
     * 从SP文件中获取软键盘的高度
     *
     * @return 软键盘高度
     */
    public static int getSoftInputHeightFromPref() {
        return pref.getInt(SOFT_INPUT_HEIGHT, SOFT_INPUT_DEFAULT_HEIGHT);
    }

    /**
     * 计算软键盘高度
     *
     * @return 软键盘高度
     */
    public static int calculateSoftInputHeight(Activity activity) {
        //通过decorView获取到程序显示的区域，包括标题栏，但不包括状态栏。
        Rect rect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        //获取屏幕的高度
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        //计算软键盘的高度
        int softInputHeight = metrics.heightPixels - rect.bottom;
        if (softInputHeight <= 0)
            softInputHeight = SOFT_INPUT_DEFAULT_HEIGHT;
        //存到SP
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(SOFT_INPUT_HEIGHT, softInputHeight);
        editor.apply();
        return softInputHeight;
    }

    public static int dp2px(final float dpValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int px2dp(final float pxValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
