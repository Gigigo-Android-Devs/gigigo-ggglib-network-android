# Gigigo GGGLib-Network

[![Build Status](https://travis-ci.org/Gigigo-Android-Devs/gigigo-ggglib-network-library-android.svg?branch=master)](https://travis-ci.org/Gigigo-Android-Devs/gigigo-ggglib-network-library-android) 
[![](https://jitpack.io/v/Gigigo-Android-Devs/gigigo-ggglib-network-library-android.svg)](https://jitpack.io/#Gigigo-Android-Devs/gigigo-ggglib-network-library-android)

Network library for developing network request fast in Gigigo.
It provides a Java interface to abstract develop and a Retrofit2 implementation.

How to use
==========

Create a network client to configure server URL and client interface
```
NetworkClient networkClient = new RetrofitNetworkClientBuilder("https://api.github.com", GitHubApiClient.class).build();
```

Create an executor for network client:
```
NetworkExecutor networkExecutor = new RetrofitNetworkExecutorBuilder(networkClient, GitHubErrorResponse.class).retryOnErrorPolicy(
                            new GithubRetryOnErrorPolicyImpl()).build();
```

Get api client and call requests with executor:
```
GitHubApiClient apiClient = (GitHubApiClient) networkClient.getApiClient();
ApiGenericResponse apiGenericResponse = networkExecutor.call(apiClient.getOneUser());
```        

Obtain result/error from response:
```
GitHubResultData githubData = (GitHubResultData) apiGenericResponse.getResult();
```          

Client
------

Use *NetworkClientBuilder*:
```
public interface NetworkClientBuilder<Interceptor extends NetworkInterceptor, Converter extends ResponseConverter> {

  NetworkClientBuilder connectTimeout(int seconds);

  NetworkClientBuilder readTimeout(int seconds);

  NetworkClientBuilder writeTimeout(int seconds);

  NetworkClientBuilder headersInterceptor(Interceptor headersInterceptor);

  NetworkClientBuilder loggingInterceptor(Interceptor loggingInterceptor);

  NetworkClientBuilder converterFactory(Converter converterFactory);

  NetworkClient build();
}
```
implementation **RetrofitNetworkClientBuilder** creates new one that configures *OkHttpClient*, *Interceptor* and *Converters* by simple api.


### Interceptors

*NetworkClientBuilder* use *NetworkInterceptors* interface for header or logger interception: 
```
public interface NetworkInterceptor<Response, Chain> {
  Response intercept(Chain chain) throws IOException;
}
```
Retrofit2 implementation default interceptor:
```
public abstract class RetrofitNetworkInterceptor
    implements NetworkInterceptor<Response, Interceptor.Chain>, Interceptor {
}
```

now create interceptor that extends that implementation:
```
public class LoggingInterceptor extends RetrofitNetworkInterceptor {
  @Override public Response intercept(Chain chain) throws IOException {
    Request original = chain.request();
    
    Request.Builder builder = original.newBuilder();
    builder = builder.header("LANGUAGE", Locale.getDefault().toString());
    builder = builder.header("AUTHORIZATION", "Bearer 123456");
    
    Request request = builder.method(original.method(), original.body()).build();
    return chain.proceed(request);
  }
}

```

### Converters

*NetworkClientBuilder* use *ResponseConverter* interface.
```
public interface ResponseConverter {}
```

Retrofit2 implementation:
```
 public class RetrofitResponseConverter extends Converter.Factory implements ResponseConverter {
 }
```
and default response converter:
```
RetrofitResponseConverter defaultConverterFactory = retrofit2.converter.gson.GsonConverterFactory.create();
```


Executor
--------

Use *NetworkExecutorBuilder*:
```
public abstract class NetworkExecutorBuilder {
  protected ErrorConverter defaultErrorConverter;
  protected RetryOnErrorPolicy defaultRetryOnErrorPolicy;

  public NetworkExecutorBuilder errorConverter(ErrorConverter errorConverter) {
    if (errorConverter != null) defaultErrorConverter = errorConverter;
    return this;
  }

  public NetworkExecutorBuilder retryOnErrorPolicy(RetryOnErrorPolicy retryOnErrorPolicy) {
    if (retryOnErrorPolicy != null) defaultRetryOnErrorPolicy = retryOnErrorPolicy;
    return this;
  }

  public abstract NetworkExecutor build();
}
```

implementation **RetrofitNetworkExecutorBuilder** creates one that configures retry policy and error convertion

### Error

For errors use *ErrorConverter* interface:
```
public interface ErrorConverter<ResponseBody> {
  ApiGenericResponse convert(ResponseBody value) throws IOException;
}
```

by default, error converter:
```
public class DefaultErrorConverterImpl<ErrorResponse extends ApiGenericErrorResponse>
    implements ErrorConverter<ResponseBody> {

  private Converter<ResponseBody, ErrorResponse> converter;

  public DefaultErrorConverterImpl(Retrofit retrofit, Class<ApiGenericErrorResponse> errorResponse) {
    converter = retrofit.responseBodyConverter(errorResponse, new Annotation[0]);
  }

  @Override public ErrorResponse convert(ResponseBody value) throws IOException {
    return converter.convert(value);
  }
}
```


Responses
---------

Result,error and exceptions responses extends from *ApiGenericResponse*:
```
public abstract class ApiGenericResponse<Result, Error> {

  protected HttpResponse httpResponse;

  public abstract Result getResult();

  public abstract Error getError();

  public HttpResponse getHttpResponse() {
    return this.httpResponse;
  }

  public void setHttpResponse(HttpResponse httpResponse) {
    this.httpResponse = httpResponse;
  }

  public abstract ApiResponseStatus getResponseStatus();
}
```

for simplify all responses are generic responses:
```
ApiGenericResponse apiGenericResponse = networkExecutor.call(apiClient.getOneUser());
GitHubResultData githubData = (GitHubResultData) apiGenericResponse.getResult();
```
and check *ApiResponseStatus* to know if it's successful or not


### Result

```
public abstract class ApiGenericResultResponse<Result> extends ApiGenericResponse<Result, Object> {

  public abstract Result getResult();

  public abstract void setResult(Result result);

  @Override public Object getError() {
    return null;
  }

  @Override public ApiResponseStatus getResponseStatus() {
    return ApiResponseStatus.OK;
  }
}
```

create result response extending from *ApiGenericResultResponse*:
```
public class ApiResultResponseMock extends ApiGenericResultResponse<ApiResultDataMock> {

  @SerializedName("status") private String status;
  @SerializedName("data") private ApiResultDataMock data;

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  @Override public ApiResultDataMock getResult() {
    return this.data;
  }

  @Override public void setResult(ApiResultDataMock data) {
    this.data = data;
  }
}
```


### Error

```
public abstract class ApiGenericErrorResponse<Error> extends ApiGenericResponse<Object, Error> {

  @Override public Object getResult() {
    return null;
  }

  public abstract Error getError();

  public abstract void setError(Error error);

  public ApiResponseStatus getResponseStatus() {
    return ApiResponseStatus.ERROR;
  }
}
```

create result response extending from *ApiGenericErrorResponse*:
```
public class ApiErrorResponseMock extends ApiGenericErrorResponse<ApiErrorDataMock> {

  @SerializedName("status") private String status;
  @SerializedName("error") private ApiErrorDataMock error;

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  @Override public ApiErrorDataMock getError() {
    return this.error;
  }

  @Override public void setError(ApiErrorDataMock error) {
    this.error = error;
  }
}
```

### Exception

```
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
```

Dependencies
============

Add dependency to *gradle* file:
```
compile 'com.github.Gigigo-Android-Devs.gigigo-ggglib-network-library-android:ggglib.network.retrofit:1.0.0'
```          
  
Library internally depends on:
```
retrofit          : 'com.squareup.retrofit2:retrofit:2.1.0',
gsonConverter     : 'com.squareup.retrofit2:converter-gson:2.1.0',
okHttp            : 'com.squareup.okhttp3:okhttp:3.5.0',
```
and other Gigigo libraries:
```
gggLibCore        : 'com.github.Gigigo-Android-Devs.gigigo-ggglib-library-android:ggglib.core:1.0.5',
gggLibLogger      : 'com.github.Gigigo-Android-Devs.gigigo-ggglib-library-android:ggglib.logger:1.0.5',
gggLibMappers     : 'com.github.Gigigo-Android-Devs.gigigo-ggglib-library-android:ggglib.mappers:1.0.5'
```


License
=======

    Copyright 2015 Gigigo Android Team

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
