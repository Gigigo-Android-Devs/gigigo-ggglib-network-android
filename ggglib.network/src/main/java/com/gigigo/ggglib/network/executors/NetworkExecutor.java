package com.gigigo.ggglib.network.executors;

import com.gigigo.ggglib.network.retrofit.context.responses.ApiGenericResponse;

/**
 * Created by rui.alonso on 28/3/17.
 */

public interface NetworkExecutor<Request> {
  ApiGenericResponse executeNetworkServiceConnection(
      Class<? extends ApiGenericResponse> responseType, Request requestType);
}
