package com.gigigo.ggglib.network.retrofit.converters;

import com.gigigo.ggglib.network.converters.ErrorConverter;
import java.io.IOException;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;

public class DefaultErrorConverterImpl<ErrorResponse>
    implements ErrorConverter<ErrorResponse, ResponseBody> {

  RetrofitErrorConverter retrofitErrorConverter;

  public DefaultErrorConverterImpl(Retrofit retrofit, Class<ErrorResponse> errorResponse) {
    this.retrofitErrorConverter = new RetrofitErrorConverter(retrofit, errorResponse);
  }

  @Override public ErrorResponse convert(ResponseBody value) throws IOException {
    return (ErrorResponse) retrofitErrorConverter.convert(value);
  }
}
