package com.gigigo.ggglib.network.retrofit.client;

import com.gigigo.ggglib.network.client.NetworkClient;
import com.gigigo.ggglib.network.client.NetworkClientBuilder;
import com.gigigo.ggglib.network.retrofit.RetrofitNetworkInterceptor;
import com.gigigo.ggglib.network.retrofit.converters.RetrofitResponseConverter;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created by rui.alonso on 28/3/17.
 */

public class RetrofitNetworkClientBuilder
    implements NetworkClientBuilder<RetrofitNetworkInterceptor, RetrofitResponseConverter> {
  private final String endpoint;
  private final Class apiClientClass;
  private int connectTimeoutSeconds = 30;
  private int readTimeoutSeconds = 30;
  private int writeTimeoutSeconds = 30;

  private RetrofitNetworkInterceptor defaultHeadersInterceptor;
  private RetrofitNetworkInterceptor defaultLoggingInterceptor;

  private Converter.Factory defaultConverterFactory;

  public RetrofitNetworkClientBuilder(String endpoint, Class apiClientClass) {
    this.endpoint = endpoint;
    this.apiClientClass = apiClientClass;
  }

  @Override public NetworkClientBuilder connectTimeout(int seconds) {
    connectTimeoutSeconds = seconds;
    return this;
  }

  @Override public NetworkClientBuilder readTimeout(int seconds) {
    readTimeoutSeconds = seconds;
    return this;
  }

  @Override public NetworkClientBuilder writeTimeout(int seconds) {
    writeTimeoutSeconds = seconds;
    return this;
  }

  @Override
  public NetworkClientBuilder headersInterceptor(RetrofitNetworkInterceptor headersInterceptor) {
    if (headersInterceptor != null) defaultHeadersInterceptor = headersInterceptor;
    return this;
  }

  @Override
  public NetworkClientBuilder loggingInterceptor(RetrofitNetworkInterceptor loggingInterceptor) {
    if (loggingInterceptor != null) defaultLoggingInterceptor = loggingInterceptor;
    return this;
  }

  @Override public NetworkClientBuilder converterFactory(RetrofitResponseConverter converterFactory) {
    if (converterFactory != null) defaultConverterFactory = converterFactory;
    return this;
  }

  @Override public NetworkClient build() {
    OkHttpClient okHttpClient = createOkHttpClient();
    Retrofit retrofit = createRetrofit(okHttpClient);

    return new RetrofitNetworkClient(retrofit, apiClientClass);
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

    return retrofit;
  }
}
