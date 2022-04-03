package com.ideastudio.ideaword.remote;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RemoteDict {


    @GET("cdn/json/3/{word}.jgz")
    Call<String> getWords(@Path("word") String word);

}
