package com.gigigo.ggglib.network.mappers;

import com.gigigo.ggglib.core.business.model.BusinessContentType;
import com.gigigo.ggglib.core.business.model.BusinessObject;
import com.gigigo.ggglib.network.context.mappers.BaseTestApiResponseMapper;
import com.gigigo.ggglib.network.context.mappers.TestMapper;
import com.gigigo.ggglib.network.context.responses.ApiDataTestMock;
import com.gigigo.ggglib.network.context.responses.ApiErrorResponseMock;
import com.gigigo.ggglib.network.context.responses.ApiResponseMock;
import com.gigigo.ggglib.network.context.responses.DataTestMock;
import com.gigigo.ggglib.network.executors.ApiServiceExecutor;
import com.gigigo.ggglib.network.responses.ApiGenericExceptionResponse;
import com.gigigo.ggglib.network.responses.ApiGenericResponse;
import com.gigigo.ggglib.network.responses.HttpResponse;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class ApiGenericResponseMapperTest {

  ApiServiceExecutor apiServiceExecutor;
  ApiGenericResponseMapper apiGenericResponseMapper;

  @Before public void setUp() {

    apiServiceExecutor = Mockito.mock(ApiServiceExecutor.class);
    apiGenericResponseMapper = new BaseTestApiResponseMapper(new TestMapper());

    when(
        apiServiceExecutor.executeNetworkServiceConnection(ApiResponseMock.class, "ok")).thenReturn(
        mockApiResponseOkClass());

    when(apiServiceExecutor.executeNetworkServiceConnection(ApiResponseMock.class,
        "error")).thenReturn(mockApiResponseBusinessErrorClass());

    when(apiServiceExecutor.executeNetworkServiceConnection(ApiResponseMock.class,
        "bad")).thenReturn(mockApiResponseExceptionClass());
  }

  @Test public void mapperOkResultTest() throws Exception {
    ApiGenericResponse apiGenericResponse =
        apiServiceExecutor.executeNetworkServiceConnection(ApiResponseMock.class, "ok");

    BusinessObject<DataTestMock> bo =
        apiGenericResponseMapper.mapApiGenericResponseToBusiness(apiGenericResponse);

    assertEquals(bo.getBusinessError().getBusinessContentType(),
        BusinessContentType.NO_ERROR_CONTENT);
    assertEquals(bo.getData().getTest(), "Hello World");
  }

  @Test public void mapperErrorResultTest() throws Exception {
    ApiGenericResponse apiGenericResponse =
        apiServiceExecutor.executeNetworkServiceConnection(ApiResponseMock.class, "error");

    BusinessObject<DataTestMock> bo =
        apiGenericResponseMapper.mapApiGenericResponseToBusiness(apiGenericResponse);

    assertEquals(bo.getBusinessError().getBusinessContentType(),
        BusinessContentType.BUSINESS_ERROR_CONTENT);
    assertEquals(bo.getBusinessError().getMessage(), "bad User");
  }

  @Test public void mapperBadResultTest() throws Exception {
    ApiGenericResponse apiGenericResponse =
        apiServiceExecutor.executeNetworkServiceConnection(ApiResponseMock.class, "bad");

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
