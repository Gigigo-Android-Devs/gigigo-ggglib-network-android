package com.gigigo.ggglib.network.retrofit.context.interceptor;

import com.gigigo.ggglib.network.retrofit.RetrofitNetworkInterceptor;
import java.io.IOException;
import java.util.Locale;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by rui.alonso on 21/4/17.
 */

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
