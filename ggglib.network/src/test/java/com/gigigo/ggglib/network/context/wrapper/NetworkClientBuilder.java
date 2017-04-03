package com.gigigo.ggglib.network.context.wrapper;

import okhttp3.Interceptor;
import retrofit2.Converter;

/**
 * Created by rui.alonso on 28/3/17.
 */

public interface NetworkClientBuilder {

  NetworkClientBuilder connectTimeout(int seconds);

  NetworkClientBuilder readTimeout(int seconds);

  NetworkClientBuilder writeTimeout(int seconds);

  NetworkClientBuilder headersInterceptor(Interceptor headersInterceptor);

  NetworkClientBuilder loggingInterceptor(Interceptor loggingInterceptor);

  NetworkClientBuilder converterFactory(Converter.Factory converterFactory);

  NetworkClient build();
}
