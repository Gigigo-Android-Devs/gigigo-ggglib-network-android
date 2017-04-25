package com.gigigo.ggglib.network.client;

import com.gigigo.ggglib.network.converters.ResponseConverter;
import com.gigigo.ggglib.network.interceptors.NetworkInterceptor;

/**
 * Created by rui.alonso on 28/3/17.
 */

public interface NetworkClientBuilder<Interceptor extends NetworkInterceptor, Converter extends ResponseConverter> {

  NetworkClientBuilder connectTimeout(int seconds);

  NetworkClientBuilder readTimeout(int seconds);

  NetworkClientBuilder writeTimeout(int seconds);

  NetworkClientBuilder headersInterceptor(Interceptor headersInterceptor);

  NetworkClientBuilder loggingInterceptor(Interceptor loggingInterceptor);

  NetworkClientBuilder converterFactory(Converter converterFactory);

  NetworkClient build();
}
