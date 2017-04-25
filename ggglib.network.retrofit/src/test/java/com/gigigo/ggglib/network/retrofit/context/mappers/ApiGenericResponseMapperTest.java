package com.gigigo.ggglib.network.retrofit.context.mappers;

import com.gigigo.ggglib.core.business.model.BusinessContentType;
import com.gigigo.ggglib.core.business.model.BusinessObject;
import com.gigigo.ggglib.network.executors.NetworkExecutor;
import com.gigigo.ggglib.network.retrofit.context.responses.ApiDataTestMock;
import com.gigigo.ggglib.network.retrofit.context.responses.ApiErrorResponseMock;
import com.gigigo.ggglib.network.retrofit.context.responses.ApiGenericExceptionResponse;
import com.gigigo.ggglib.network.retrofit.context.responses.ApiGenericResponse;
import com.gigigo.ggglib.network.retrofit.context.responses.ApiResponseMock;
import com.gigigo.ggglib.network.retrofit.context.responses.HttpResponse;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class ApiGenericResponseMapperTest {

  NetworkExecutor apiServiceExecutor;
  ApiGenericResponseMapper apiGenericResponseMapper;

  @Before public void setUp() {

    apiServiceExecutor = Mockito.mock(NetworkExecutor.class);
    apiGenericResponseMapper = new BaseTestApiResponseMapper(new TestMapper());

    when(apiServiceExecutor.call("ok")).thenReturn(mockApiResponseOkClass());

    when(apiServiceExecutor.call("error")).thenReturn(mockApiResponseBusinessErrorClass());

    when(apiServiceExecutor.call("bad")).thenReturn(mockApiResponseExceptionClass());
  }

  @Test public void mapperOkResultTest() throws Exception {
    ApiGenericResponse apiGenericResponse = apiServiceExecutor.call("ok");

    BusinessObject<DataTestMock> bo =
        apiGenericResponseMapper.mapApiGenericResponseToBusiness(apiGenericResponse);

    assertEquals(bo.getBusinessError().getBusinessContentType(),
        BusinessContentType.NO_ERROR_CONTENT);
    assertEquals(bo.getData().getTest(), "Hello World");
  }

  @Test public void mapperErrorResultTest() throws Exception {
    ApiGenericResponse apiGenericResponse = apiServiceExecutor.call("error");

    BusinessObject<DataTestMock> bo =
        apiGenericResponseMapper.mapApiGenericResponseToBusiness(apiGenericResponse);

    assertEquals(bo.getBusinessError().getBusinessContentType(),
        BusinessContentType.BUSINESS_ERROR_CONTENT);
    assertEquals(bo.getBusinessError().getMessage(), "bad User");
  }

  @Test public void mapperBadResultTest() throws Exception {
    ApiGenericResponse apiGenericResponse = apiServiceExecutor.call("bad");

    BusinessObject<DataTestMock> bo =
        apiGenericResponseMapper.mapApiGenericResponseToBusiness(apiGenericResponse);

    assertEquals(bo.getBusinessError().getBusinessContentType(),
        BusinessContentType.EXCEPTION_CONTENT);
  }

  private ApiGenericResponse mockApiResponseOkClass() {
    ApiDataTestMock apiDataTestMock = new ApiDataTestMock();
    apiDataTestMock.setTest("Hello World");

    HttpResponse httpResponse = new HttpResponse(200, "OK");

    ApiResponseMock mockApiResponse = new ApiResponseMock();
    mockApiResponse.setData(apiDataTestMock);
    mockApiResponse.setHttpResponse(httpResponse);

    return mockApiResponse;
  }

  private ApiGenericResponse mockApiResponseBusinessErrorClass() {
    ApiErrorResponseMock apiErrorResponseMock = new ApiErrorResponseMock(1550, "bad User");
    HttpResponse httpResponse = new HttpResponse(500, "KO");

    ApiResponseMock mockApiResponse = new ApiResponseMock();
    mockApiResponse.setBusinessError(apiErrorResponseMock);
    mockApiResponse.setHttpResponse(httpResponse);

    return mockApiResponse;
  }

  private ApiGenericResponse mockApiResponseExceptionClass() {
    Exception e = new Exception("This is a mock exception");
    ApiGenericExceptionResponse apiGenericExceptionResponse = new ApiGenericExceptionResponse(e);

    return apiGenericExceptionResponse;
  }
}
