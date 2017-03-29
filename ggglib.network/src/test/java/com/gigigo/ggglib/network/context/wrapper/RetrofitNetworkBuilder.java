package com.gigigo.ggglib.network.context.wrapper;

import com.gigigo.ggglib.network.context.collaborators.DefaultErrorConverterImpl;
import com.gigigo.ggglib.network.context.collaborators.DefaultRetryOnErrorPolicyImpl;
import com.gigigo.ggglib.network.converters.ErrorConverter;
import com.gigigo.ggglib.network.defaultelements.RetryOnErrorPolicy;
import com.gigigo.ggglib.network.executors.ApiServiceExecutor;
import com.gigigo.ggglib.network.executors.RetrofitApiServiceExecutor;
import java.util.concurrent.TimeUnit;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created by rui.alonso on 28/3/17.
 */

public class RetrofitNetworkBuilder implements NetworkBuilder {
  private final String endpoint;
  private Class apiServiceClass;
  private Class apiErrorResponseClass;
  private int connectTimeoutSeconds = 30;
  private int readTimeoutSeconds = 30;
  private int writeTimeoutSeconds = 30;

  private Interceptor defaultHeadersInterceptor;
  private Interceptor defaultLoggingInterceptor;

  private Converter.Factory defaultConverterFactory;

  private ErrorConverter defaultErrorConverter;
  private RetryOnErrorPolicy defaultRetryOnErrorPolicy;

  public RetrofitNetworkBuilder(Class apiServiceClass, String endpoint,
      Class apiErrorResponseClass) {
    this.apiServiceClass = apiServiceClass;
    this.endpoint = endpoint;
    this.apiErrorResponseClass = apiErrorResponseClass;
  }

  @Override public NetworkBuilder connectTimeout(int seconds) {
    connectTimeoutSeconds = seconds;
    return this;
  }

  @Override public NetworkBuilder readTimeout(int seconds) {
    readTimeoutSeconds = seconds;
    return this;
  }

  @Override public NetworkBuilder writeTimeout(int seconds) {
    writeTimeoutSeconds = seconds;
    return this;
  }

  @Override public NetworkBuilder headersInterceptor(Interceptor headersInterceptor) {
    if (headersInterceptor != null) defaultHeadersInterceptor = headersInterceptor;
    return this;
  }

  @Override public NetworkBuilder loggingInterceptor(Interceptor loggingInterceptor) {
    if (loggingInterceptor != null) defaultLoggingInterceptor = loggingInterceptor;
    return this;
  }

  @Override public NetworkBuilder converterFactory(Converter.Factory converterFactory) {
    if (converterFactory != null) defaultConverterFactory = converterFactory;
    return this;
  }

  @Override public NetworkBuilder errorConverter(ErrorConverter errorConverter) {
    if (errorConverter != null) defaultErrorConverter = errorConverter;
    return this;
  }

  @Override public NetworkBuilder retryOnErrorPolicy(RetryOnErrorPolicy retryOnErrorPolicy) {
    if (retryOnErrorPolicy != null) defaultRetryOnErrorPolicy = retryOnErrorPolicy;
    return this;
  }

  @Override public NetworkProvider build() {
    OkHttpClient okHttpClient = createOkHttpClient();
    Retrofit retrofit = createRetrofit(okHttpClient);
    ApiServiceExecutor apiServiceExecutor =
        createApiServiceExecutor(retrofit, apiErrorResponseClass);

    return new RetrofitNetworkProvider(apiServiceClass, apiServiceExecutor);
  }

  private OkHttpClient createOkHttpClient() {
    OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();

    if (defaultHeadersInterceptor != null) {
      okHttpClientBuilder.addInterceptor(defaultHeadersInterceptor);
    }

    if (defaultLoggingInterceptor != null) {
      okHttpClientBuilder.addInterceptor(defaultLoggingInterceptor);
    }

    OkHttpClient okHttpClient =
        okHttpClientBuilder.connectTimeout(connectTimeoutSeconds, TimeUnit.SECONDS)
            .readTimeout(readTimeoutSeconds, TimeUnit.SECONDS)
            .writeTimeout(writeTimeoutSeconds, TimeUnit.SECONDS)
            .build();

    return okHttpClient;
  }

  private Retrofit createRetrofit(OkHttpClient okHttpClient) {
    if (defaultConverterFactory == null) {
      defaultConverterFactory = retrofit2.converter.gson.GsonConverterFactory.create();
    }

    Retrofit retrofit = new Retrofit.Builder().baseUrl(endpoint)
        .client(okHttpClient)
        .addConverterFactory(defaultConverterFactory)
        .build();

    retrofit.create(apiServiceClass);

    return retrofit;
  }

  private ApiServiceExecutor createApiServiceExecutor(Retrofit retrofit, Class errorResponseClass) {
    if (defaultErrorConverter == null) {
      defaultErrorConverter = new DefaultErrorConverterImpl(retrofit, errorResponseClass);
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
