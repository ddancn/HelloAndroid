package com.example.ddancn.helloworld;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import com.example.ddancn.helloworld.utils.ToastUtil;

public class MyApplication extends Application {

    private static Context sContext;

    @Override
    public void onCreate(){
        super.onCreate();
        sContext = this;
        ToastUtil.init(this);
    }

    public static Context getContext(){
        return sContext;
    }
}
