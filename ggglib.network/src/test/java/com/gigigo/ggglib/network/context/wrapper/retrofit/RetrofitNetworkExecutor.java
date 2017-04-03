package com.gigigo.ggglib.network.context.wrapper.retrofit;

import com.gigigo.ggglib.network.context.wrapper.NetworkExecutor;
import com.gigigo.ggglib.network.executors.ApiServiceExecutor;
import com.gigigo.ggglib.network.responses.ApiGenericResponse;
import retrofit2.Call;

/**
 * Created by rui.alonso on 28/3/17.
 */

public class RetrofitNetworkExecutor implements NetworkExecutor<Call<?>> {
  private ApiServiceExecutor apiServiceExecutor;

  public RetrofitNetworkExecutor(ApiServiceExecutor apiServiceExecutor) {
    this.apiServiceExecutor = apiServiceExecutor;
  }

  @Override
  public <ApiResponse extends ApiGenericResponse> ApiGenericResponse executeNetworkServiceConnection(
      Class<ApiResponse> responseType, Call<?> requestType) {
    ApiGenericResponse apiGenericResponse =
        apiServiceExecutor.executeNetworkServiceConnection(responseType, requestType);
    return apiGenericResponse;
  }
}
