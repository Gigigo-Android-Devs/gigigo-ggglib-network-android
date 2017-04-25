package com.gigigo.ggglib.network.retrofit.context.interceptor;

import com.gigigo.ggglib.network.retrofit.RetrofitNetworkInterceptor;
import java.io.IOException;
import okhttp3.Response;

/**
 * Created by rui.alonso on 21/4/17.
 */

public class LoggingInterceptor extends RetrofitNetworkInterceptor {

  @Override public Response intercept(Chain chain) throws IOException {
/*
    Request original = chain.request();

    Request.Builder builder = original.newBuilder();

    String appCountry = "";
    if (woahPreferences.getCountryCode() != null) {
      builder = builder.header(ACCEPT_LANGUAGE, Locale.getDefault().toString());
      appCountry = woahPreferences.getCountryCode();
    }

    if (appCountry != null) {
      builder = builder.header(X_APP_COUNTRY, appCountry);
    }

    StringBuilder woahAuth = new StringBuilder();
    woahAuth.append(BuildConfig.WOAH_API_KEY).append(":").append(BuildConfig.WOAH_API_SECRET);

    String cidUserId = UserData.UID;
    String orxAccessToken = OrchextraCredentialsData.TOKEN;
    if ((cidUserId != null && !cidUserId.isEmpty()) && (orxAccessToken != null
        && !orxAccessToken.isEmpty())) {
      woahAuth.append(":").append(cidUserId).append(":").append(orxAccessToken);
    }

    builder = builder.header(AUTHORIZATION,
        "Bearer " + Base64.encodeToString(woahAuth.toString().getBytes(), Base64.NO_WRAP));

    builder = builder.header(X_APP_VERSION, BuildConfig.WOAH_APP_VERSION);

    if (BuildConfig.DEBUG && BuildConfig.DEMO_ID != "") {
      builder = builder.header(X_DEMO_ID, BuildConfig.DEMO_ID);
    }

    Request request = builder.method(original.method(), original.body()).build();

    return chain.proceed(request);
*/
    return null;
  }
}
