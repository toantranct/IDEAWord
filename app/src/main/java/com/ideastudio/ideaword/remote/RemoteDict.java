package com.ideastudio.ideaword.remote;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RemoteDict {


    @GET("/cdn/json/2/{word}.jgz")
    Call<String> getWords2(@Path("word") String word);

    @GET("/cdn/json/3/{word}.jgz")
    Call<String> getWords3(@Path("word") String word);

    @GET("/cdn/json/4/{word}.jgz")
    Call<String> getWords4(@Path("word") String word);

}