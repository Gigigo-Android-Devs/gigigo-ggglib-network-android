package com.gigigo.ggglib.network.retrofit;

import com.gigigo.ggglib.network.interceptors.NetworkInterceptor;
import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * Created by rui.alonso on 20/4/17.
 */

public abstract class RetrofitNetworkInterceptor
    implements NetworkInterceptor<Response, Interceptor.Chain>, Interceptor {

}
