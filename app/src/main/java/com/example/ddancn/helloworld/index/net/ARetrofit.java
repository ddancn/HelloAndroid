package com.example.ddancn.helloworld.index.net;

import retrofit2.Retrofit;

public class ARetrofit {

    public static Retrofit getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static final Retrofit INSTANCE = new Retrofit.Builder()
                .build();
    }


}
