package com.gigigo.ggglib.network.retrofit.context;

import com.gigigo.ggglib.network.retrofit.context.responses.GithubResultResponse;
import retrofit2.Call;
import retrofit2.http.GET;

public interface GitHubApiClient {

  @GET("users/Gigigo-Android-Devs") Call<GithubResultResponse> getOneUser();

}
