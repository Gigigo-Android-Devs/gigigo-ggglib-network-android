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
import com.gigigo.ggglib.network.responses.ApiGenericErrorResponse;
import com.gigigo.ggglib.network.responses.ApiGenericExceptionResponse;
import com.gigigo.ggglib.network.responses.ApiGenericResponse;
import com.gigigo.ggglib.network.responses.ApiResponseStatus;
import com.gigigo.ggglib.network.responses.HttpResponse;
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

    do {
      Response<ApiGenericResponse> retrofitResponse = null;
      try {
        tries++;
        clonedCall = requestType.clone();
        retrofitResponse = clonedCall.execute();
        apiResponse = parseRetrofitResponseToApi(retrofitResponse);
      } catch (Exception e) {
        exception = e;
        apiResponse = buildApiExceptionResponse(e);

        GGGLogImpl.log(e.getMessage(), LogLevel.ERROR);
      }
    } while (shouldRetry(tries, apiResponse, exception));

    return apiResponse;
  }

  private <ApiResponse extends ApiGenericResponse> ApiGenericResponse parseRetrofitResponseToApi(
      Response<ApiResponse> retrofitResponse) throws IOException {

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

  private <ApiResponse extends ApiGenericResponse> ApiGenericResponse buildApiErrorResponse(
      Response<ApiResponse> retrofitResponse) throws IOException {

    return errorConverter.convert(retrofitResponse.errorBody());
  }

  private ApiGenericResponse buildApiExceptionResponse(Exception exception) {

    return ApiGenericExceptionResponse.getApiGenericExceptionResponseInstance(exception);
  }

  private boolean shouldRetry(int tries, ApiGenericResponse apiGenericResponse, Exception e) {

    boolean retry = false;
    if (apiGenericResponse.getResponseStatus() != ApiResponseStatus.OK) {
      retry = retryPolicyResult(tries, apiGenericResponse, e);
    }

    return retry;
  }

  private boolean retryPolicyResult(int tries, ApiGenericResponse apiResponse, Exception e) {
    if (e != null) {
      return exceptionRetryPolicyResult(tries, e);
    } else {
      return businessErrorRetryPolicyResult(tries, (ApiGenericErrorResponse) apiResponse);
    }
  }

  private boolean exceptionRetryPolicyResult(int tries, Exception e) {
    return retryOnErrorPolicy.shouldRetryOnException(tries, e);
  }

  private boolean businessErrorRetryPolicyResult(int tries, ApiGenericErrorResponse apiResponse) {
    Object businessResponse = apiResponse.getError();
    HttpResponse httpResponse = apiResponse.getHttpResponse();
    return retryOnErrorPolicy.shouldRetryWithErrorAndTries(tries, businessResponse, httpResponse);
  }
}
