package com.gigigo.ggglib.network.retrofit;

import com.gigigo.ggglib.network.NetworkClient;
import retrofit2.Retrofit;

/**
 * Created by rui.alonso on 31/3/17.
 */

public class RetrofitNetworkClient<ApiClient> extends NetworkClient {
  private Retrofit retrofit;
  private ApiClient apiClient;

  public RetrofitNetworkClient(Retrofit retrofit, Class apiClientClass) {
    this.retrofit = retrofit;
    this.apiClient = (ApiClient) this.retrofit.create(apiClientClass);
  }

  public Retrofit getRetrofit() {
    return this.retrofit;
  }

  public ApiClient getApiClient() {
    return this.apiClient;
  }
}
