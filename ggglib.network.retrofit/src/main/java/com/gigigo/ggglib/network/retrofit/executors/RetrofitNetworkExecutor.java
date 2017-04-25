/*
 * Created by Gigigo Android Team
 *
 * Copyright (C) 2016 Gigigo Mobile Services SL
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gigigo.ggglib.network.retrofit.executors;

import com.gigigo.ggglib.logger.GGGLogImpl;
import com.gigigo.ggglib.logger.LogLevel;
import com.gigigo.ggglib.network.converters.ErrorConverter;
import com.gigigo.ggglib.network.defaultelements.RetryOnErrorPolicy;
import com.gigigo.ggglib.network.executors.NetworkExecutor;
import com.gigigo.ggglib.network.retrofit.context.responses.ApiGenericExceptionResponse;
import com.gigigo.ggglib.network.retrofit.context.responses.ApiGenericResponse;
import com.gigigo.ggglib.network.retrofit.context.responses.HttpResponse;
import java.io.IOException;
import retrofit2.Call;
import retrofit2.Response;

public class RetrofitNetworkExecutor implements NetworkExecutor<Call<ApiGenericResponse>> {

  private RetryOnErrorPolicy retryOnErrorPolicy;
  private ErrorConverter errorConverter;

  public RetrofitNetworkExecutor(RetryOnErrorPolicy retryOnErrorPolicy,
      ErrorConverter errorConverter) {
    this.retryOnErrorPolicy = retryOnErrorPolicy;
    this.errorConverter = errorConverter;
  }

  @Override public ApiGenericResponse call(Call<ApiGenericResponse> requestType) {

    ApiGenericResponse apiResponse;
    Call<ApiGenericResponse> clonedCall;
    int tries = 0;
    Exception exception = null;
    boolean success = false;

    do {
      Response<ApiGenericResponse> retrofitResponse = null;
      try {
        tries++;
        clonedCall = requestType.clone();
        retrofitResponse = clonedCall.execute();
        success = retrofitResponse.isSuccessful();
        apiResponse = parseRetrofitResponseToApi(retrofitResponse);
      } catch (Exception e) {
        exception = e;
        apiResponse = ApiGenericExceptionResponse.getApiGenericExceptionResponseInstance(e);

        GGGLogImpl.log(e.getMessage(), LogLevel.ERROR);
      }
    } while (shouldRetry(tries, apiResponse, success, exception));

    return apiResponse;
  }

  private ApiGenericResponse parseRetrofitResponseToApi(
      Response<ApiGenericResponse> retrofitResponse) throws IOException {

    ApiGenericResponse apiResponse;

    if (retrofitResponse.isSuccessful()) {
      apiResponse = retrofitResponse.body();
    } else {
      apiResponse = buildApiErrorResponse(retrofitResponse);
    }

    apiResponse.setHttpResponse(
        new HttpResponse(retrofitResponse.code(), retrofitResponse.message()));
    return apiResponse;
  }

  private ApiGenericResponse buildApiErrorResponse(
      Response<ApiGenericResponse> retrofitResponse) throws IOException {
    return (ApiGenericResponse) errorConverter.convert(retrofitResponse.errorBody());
  }

  private boolean shouldRetry(int tries, ApiGenericResponse apiGenericResponse, boolean success,
      Exception e) {

    if (success) {
      return false;
    } else {
      return retryPolicyResult(tries, apiGenericResponse, e);
    }
  }

  private boolean retryPolicyResult(int tries, ApiGenericResponse apiResponse, Exception e) {
    if (e != null) {
      return exceptionRetryPolicyResult(tries, e);
    } else {
      return businessErrorRetryPolicyResult(tries, apiResponse);
    }
  }

  private boolean businessErrorRetryPolicyResult(int tries, ApiGenericResponse apiResponse) {
    Object businessResponse = apiResponse.getBusinessError();
    HttpResponse httpResponse = apiResponse.getHttpResponse();
    return retryOnErrorPolicy.shouldRetryWithErrorAndTries(tries, businessResponse, httpResponse);
  }

  private boolean exceptionRetryPolicyResult(int tries, Exception e) {
    return retryOnErrorPolicy.shouldRetryOnException(tries, e);
  }
}
