package com.gigigo.ggglib.network.context.wrapper;

import com.gigigo.ggglib.network.converters.ErrorConverter;
import com.gigigo.ggglib.network.defaultelements.RetryOnErrorPolicy;
import okhttp3.Interceptor;
import retrofit2.Converter;

/**
 * Created by rui.alonso on 28/3/17.
 */

public interface NetworkBuilder {

  NetworkBuilder connectTimeout(int seconds);

  NetworkBuilder readTimeout(int seconds);

  NetworkBuilder writeTimeout(int seconds);

  NetworkBuilder headersInterceptor(Interceptor headersInterceptor);

  NetworkBuilder loggingInterceptor(Interceptor loggingInterceptor);

  NetworkBuilder converterFactory(Converter.Factory converterFactory);

  NetworkBuilder errorConverter(ErrorConverter errorConverter);

  NetworkBuilder retryOnErrorPolicy(RetryOnErrorPolicy retryOnErrorPolicy);

  NetworkProvider build();
}
