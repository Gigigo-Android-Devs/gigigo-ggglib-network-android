package com.gigigo.ggglib.network.retrofit.executors;

import com.gigigo.ggglib.network.client.NetworkClient;
import com.gigigo.ggglib.network.executors.NetworkExecutor;
import com.gigigo.ggglib.network.executors.NetworkExecutorBuilder;
import com.gigigo.ggglib.network.retrofit.client.RetrofitNetworkClient;
import com.gigigo.ggglib.network.responses.ApiGenericResponse;
import com.gigigo.ggglib.network.retrofit.converters.DefaultErrorConverterImpl;
import com.gigigo.ggglib.network.retry.DefaultRetryOnErrorPolicyImpl;

/**
 * Created by rui.alonso on 28/3/17.
 */

public class RetrofitNetworkExecutorBuilder extends NetworkExecutorBuilder {
  private RetrofitNetworkClient networkClient;
  private Class<? extends ApiGenericResponse> apiErrorResponse;

  public RetrofitNetworkExecutorBuilder(NetworkClient networkClient,
      Class<? extends ApiGenericResponse> apiErrorResponse) {
    this.networkClient = (RetrofitNetworkClient) networkClient;
    this.apiErrorResponse = apiErrorResponse;
  }

  @Override public NetworkExecutor build() {
    if (defaultErrorConverter == null) {
      defaultErrorConverter =
          new DefaultErrorConverterImpl(networkClient.getRetrofit(), apiErrorResponse);
    }

    if (defaultRetryOnErrorPolicy == null) {
      defaultRetryOnErrorPolicy = new DefaultRetryOnErrorPolicyImpl();
    }

    NetworkExecutor apiServiceExecutor =
        new RetrofitNetworkExecutor(defaultRetryOnErrorPolicy, defaultErrorConverter);

    return apiServiceExecutor;
  }
}
