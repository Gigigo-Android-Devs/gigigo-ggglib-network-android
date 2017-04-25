package com.gigigo.ggglib.network.retrofit.client;

import com.gigigo.ggglib.network.client.NetworkClient;
import retrofit2.Retrofit;

/**
 * Created by rui.alonso on 31/3/17.
 */

public class RetrofitNetworkClient<ApiClient> implements NetworkClient<ApiClient> {
  private Retrofit retrofit;
  private ApiClient apiClient;

  public RetrofitNetworkClient(Retrofit retrofit, Class<ApiClient> apiClientClass) {
    this.retrofit = retrofit;
    this.apiClient = this.retrofit.create(apiClientClass);
  }

  public Retrofit getRetrofit() {
    return this.retrofit;
  }

  @Override public ApiClient getApiClient() {
    return this.apiClient;
  }
}
