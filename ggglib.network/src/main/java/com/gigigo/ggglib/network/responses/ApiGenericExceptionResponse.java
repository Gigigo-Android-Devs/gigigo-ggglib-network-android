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

package com.gigigo.ggglib.network.responses;

public class ApiGenericExceptionResponse extends ApiGenericResponse<Object, Exception> {

  public static final int HTTP_EXCEPTION_CODE = -500;

  private Exception exception;

  public ApiGenericExceptionResponse(Exception exception) {
    this.exception = exception;
    this.httpResponse = HttpResponse.getHttpResponseExceptionInstance(HTTP_EXCEPTION_CODE,
        exception.getLocalizedMessage());
  }

  public static ApiGenericExceptionResponse getApiGenericExceptionResponseInstance(Exception e) {
    return new ApiGenericExceptionResponse(e);
  }

  @Override public Object getResult() {
    return null;
  }

  @Override public Exception getError() {
    return this.exception;
  }

  @Override public ApiResponseStatus getResponseStatus() {
    return ApiResponseStatus.EXCEPTION;
  }
}
