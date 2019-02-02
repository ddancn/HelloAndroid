package com.example.ddancn.helloworld.index.net;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface TestService {

    @GET("https://wwww.baidu.com")
    Call<ResponseBody> testCall();

    @GET("repos/{owner}/{repo}/contributors")
    Call<ResponseBody> simpleGet(@Path("owner") String owner,
                           @Path("repo") String repo);
}
