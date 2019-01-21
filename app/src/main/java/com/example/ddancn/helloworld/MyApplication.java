package com.example.ddancn.helloworld;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import com.example.ddancn.helloworld.utils.ToastUtil;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

public class MyApplication extends Application {

    private static Context sContext;

    @Override
    public void onCreate(){
        super.onCreate();
        sContext = this;
        ToastUtil.init(this);
        Iconify.with(new FontAwesomeModule());
        Logger.addLogAdapter(new AndroidLogAdapter());
    }

    public static Context getContext(){
        return sContext;
    }
}
