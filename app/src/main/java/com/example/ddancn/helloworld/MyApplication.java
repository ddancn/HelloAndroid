package com.example.ddancn.helloworld;

import android.app.Application;
import android.content.Context;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ddancn.helloworld.utils.ToastUtil;
import com.guoxiaoxing.phoenix.core.listener.ImageLoader;
import com.guoxiaoxing.phoenix.picker.Phoenix;
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

        Phoenix.config()
                .imageLoader((mContext, imageView, imagePath, type) ->
                        Glide.with(mContext)
                        .load(imagePath)
                        .into(imageView));
    }

    public static Context getContext(){
        return sContext;
    }
}
