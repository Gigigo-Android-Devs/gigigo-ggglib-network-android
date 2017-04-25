package com.gigigo.ggglib.network.interceptors;

import java.io.IOException;

/**
 * Created by rui.alonso on 20/4/17.
 */

public interface NetworkInterceptor<Response, Chain> {
  Response intercept(Chain chain) throws IOException;
}
