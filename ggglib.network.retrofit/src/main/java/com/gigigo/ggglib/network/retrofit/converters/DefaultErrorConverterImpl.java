package com.gigigo.ggglib.network.retrofit.converters;

import com.gigigo.ggglib.network.converters.ErrorConverter;
import com.gigigo.ggglib.network.responses.ApiGenericErrorResponse;
import java.io.IOException;
import java.lang.annotation.Annotation;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

public class DefaultErrorConverterImpl<ErrorResponse extends ApiGenericErrorResponse>
    implements ErrorConverter<ResponseBody> {

  private Converter<ResponseBody, ErrorResponse> converter;

  public DefaultErrorConverterImpl(Retrofit retrofit,
      Class<ApiGenericErrorResponse> errorResponse) {
    converter = retrofit.responseBodyConverter(errorResponse, new Annotation[0]);
  }

  @Override public ErrorResponse convert(ResponseBody value) throws IOException {
    return converter.convert(value);
  }
}
