package com.gigigo.ggglib.network.executors;

import com.gigigo.ggglib.network.converters.ErrorConverter;
import com.gigigo.ggglib.network.defaultelements.RetryOnErrorPolicy;

/**
 * Created by rui.alonso on 28/3/17.
 */

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
