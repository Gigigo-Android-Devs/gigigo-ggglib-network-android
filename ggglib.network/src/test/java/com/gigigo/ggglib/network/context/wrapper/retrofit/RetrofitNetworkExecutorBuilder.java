package com.gigigo.ggglib.network.context.wrapper.retrofit;

import com.gigigo.ggglib.network.context.collaborators.DefaultErrorConverterImpl;
import com.gigigo.ggglib.network.context.collaborators.DefaultRetryOnErrorPolicyImpl;
import com.gigigo.ggglib.network.context.wrapper.NetworkClient;
import com.gigigo.ggglib.network.context.wrapper.NetworkExecutor;
import com.gigigo.ggglib.network.context.wrapper.NetworkExecutorBuilder;
import com.gigigo.ggglib.network.executors.ApiServiceExecutor;
import com.gigigo.ggglib.network.executors.RetrofitApiServiceExecutor;

/**
 * Created by rui.alonso on 28/3/17.
 */

public class RetrofitNetworkExecutorBuilder extends NetworkExecutorBuilder {
  private RetrofitNetworkClient networkClient;
  private Class apiErrorResponse;

  public RetrofitNetworkExecutorBuilder(NetworkClient networkClient, Class apiErrorResponse) {
    this.networkClient = (RetrofitNetworkClient) networkClient;
    this.apiErrorResponse = apiErrorResponse;
  }

  @Override public NetworkExecutor build() {
    ApiServiceExecutor apiServiceExecutor = createApiServiceExecutor();
    return new RetrofitNetworkExecutor(apiServiceExecutor);
  }

  private ApiServiceExecutor createApiServiceExecutor() {
    if (defaultErrorConverter == null) {
      defaultErrorConverter =
          new DefaultErrorConverterImpl(networkClient.getRetrofit(), apiErrorResponse);
    }

    if (defaultRetryOnErrorPolicy == null) {
      defaultRetryOnErrorPolicy = new DefaultRetryOnErrorPolicyImpl();
    }

    ApiServiceExecutor apiServiceExecutor =
        new RetrofitApiServiceExecutor.Builder().errorConverter(defaultErrorConverter)
            .retryOnErrorPolicy(defaultRetryOnErrorPolicy)
            .build();

    return apiServiceExecutor;
  }
}
