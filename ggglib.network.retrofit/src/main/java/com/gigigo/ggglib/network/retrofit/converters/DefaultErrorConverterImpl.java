package com.gigigo.ggglib.network.retrofit.converters;

import com.gigigo.ggglib.network.converters.ErrorConverter;
import com.gigigo.ggglib.network.retrofit.context.responses.ApiGenericResponse;
import java.io.IOException;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;

public class DefaultErrorConverterImpl<ErrorResponse extends ApiGenericResponse>
    implements ErrorConverter<ResponseBody> {

  RetrofitErrorConverter retrofitErrorConverter;

  public DefaultErrorConverterImpl(Retrofit retrofit, Class<ApiGenericResponse> errorResponse) {
    this.retrofitErrorConverter = new RetrofitErrorConverter(retrofit, errorResponse);
  }

  @Override public ErrorResponse convert(ResponseBody value) throws IOException {
    return (ErrorResponse) retrofitErrorConverter.convert(value);
  }
}
