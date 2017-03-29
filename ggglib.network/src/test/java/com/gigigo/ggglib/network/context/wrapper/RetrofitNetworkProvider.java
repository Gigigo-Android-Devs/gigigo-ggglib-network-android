package com.gigigo.ggglib.network.context.wrapper;

import com.gigigo.ggglib.network.executors.ApiServiceExecutor;
import com.gigigo.ggglib.network.responses.ApiGenericResponse;

/**
 * Created by rui.alonso on 28/3/17.
 */

public class RetrofitNetworkProvider implements NetworkProvider {
  private Class apiServiceClass;
  private ApiServiceExecutor apiServiceExecutor;

  public RetrofitNetworkProvider(Class apiServiceClass, ApiServiceExecutor apiServiceExecutor) {
    this.apiServiceClass = apiServiceClass;
    this.apiServiceExecutor = apiServiceExecutor;
  }

  public Class getApiClient() {
    return apiServiceClass;
  }

  @Override
  public <ApiResponse extends ApiGenericResponse, Request> ApiGenericResponse executeNetworkServiceConnection(
      Class<ApiResponse> responseType, Request requestType) {
    ApiGenericResponse apiGenericResponse =
        apiServiceExecutor.executeNetworkServiceConnection(responseType, requestType);
    return apiGenericResponse;
  }
}
