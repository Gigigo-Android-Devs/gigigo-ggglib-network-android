package com.gigigo.ggglib.network.retrofit;

import com.gigigo.ggglib.network.converters.ErrorConverter;
import com.gigigo.ggglib.network.retry.RetryOnErrorPolicy;
import com.gigigo.ggglib.network.executors.NetworkExecutor;
import com.gigigo.ggglib.network.retrofit.client.RetrofitNetworkClient;
import com.gigigo.ggglib.network.retrofit.context.BaseApiClient;
import com.gigigo.ggglib.network.responses.ApiErrorDataMock;
import com.gigigo.ggglib.network.responses.ApiErrorResponseMock;
import com.gigigo.ggglib.network.responses.ApiGenericExceptionResponse;
import com.gigigo.ggglib.network.responses.ApiGenericResponse;
import com.gigigo.ggglib.network.responses.ApiResponseStatus;
import com.gigigo.ggglib.network.responses.ApiResultDataMock;
import com.gigigo.ggglib.network.responses.HttpResponse;
import com.gigigo.ggglib.network.responses.utils.ResponseUtils;
import com.gigigo.ggglib.network.retrofit.converters.DefaultErrorConverterImpl;
import com.gigigo.ggglib.network.retrofit.executors.RetrofitNetworkExecutorBuilder;
import com.gigigo.ggglib.network.retry.DefaultRetryOnErrorPolicyImpl;
import com.gigigo.ggglib.network.retry.NoExceptionRetryOnErrorPolicyImpl;
import com.squareup.okhttp.mockwebserver.Dispatcher;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.squareup.okhttp.mockwebserver.RecordedRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.Mockito.when;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class NetworkExecutorTest {

  private final static int ERROR_RESPONSE_CODE = 500;
  @Mock RetrofitNetworkClient networkClient;
  private MockWebServer server;
  private Retrofit retrofit;
  private BaseApiClient apiClient;

  @Before public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
    this.server = new MockWebServer();
    server.setDispatcher(getMockwebserverDispatcherInstance());
    apiClient = initializeApiclient();

    when(networkClient.getRetrofit()).thenReturn(retrofit);
  }

  private Dispatcher getMockwebserverDispatcherInstance() {
    return new Dispatcher() {
      @Override public MockResponse dispatch(RecordedRequest request) throws InterruptedException {
        try {

          if (request.getPath().equals("/ok/")) {
            return new MockResponse().setResponseCode(200)
                .setBody(ResponseUtils.getContentFromFile("testOkHttp.json", this));
          } else if (request.getPath().equals("/error/")) {
            return new MockResponse().setStatus("HTTP/1.1 500 KO")
                .setBody(ResponseUtils.getContentFromFile("testErrorHttp.json", this));
          } else {
            return new MockResponse().setResponseCode(ERROR_RESPONSE_CODE)
                .setBody(ResponseUtils.getContentFromFile("testBadHttp.json", this));
          }
        } catch (Exception e) {
          e.printStackTrace();
          return new MockResponse().setResponseCode(ERROR_RESPONSE_CODE).setBody("");
        }
      }
    };
  }

  private BaseApiClient initializeApiclient() {
    Retrofit retrofit = new Retrofit.Builder().baseUrl(server.url("/").toString())
        .addConverterFactory(GsonConverterFactory.create())
        .build();

    this.retrofit = retrofit;
    return retrofit.create(BaseApiClient.class);
  }

  /*
  @Test(expected = IllegalStateException.class) public void serviceExecutorInstanceBuilderTest1()
      throws Exception {
    new RetrofitNetworkExecutorBuilder(networkClient).errorConverter(null)
        .retryOnErrorPolicy(null)
        .build();
  }
  */

  /*
  @Test(expected = IllegalStateException.class) public void serviceExecutorInstanceBuilderTest2()
      throws Exception {
    new RetrofitNetworkExecutorBuilder(networkClient).errorConverter(buildErrorConverterInstance())
        .retryOnErrorPolicy(null)
        .build();
  }
  */

  @Test public void apiServiceOKExecutorTest() throws Exception {
    NetworkExecutor apiServiceExecutor = getServiceExecutorInstance();

    ApiGenericResponse apiGenericResponse =
        apiServiceExecutor.call(apiClient.testHttpConnection("ok"));

    ApiResultDataMock testResponse = (ApiResultDataMock) apiGenericResponse.getResult();

    assertEquals(testResponse.getTest(), "Hello World");
    assertEquals(apiGenericResponse.getHttpResponse().getHttpStatus(), 200);
  }

  @Test public void apiServiceErrorExecutorTest() throws Exception {
    NetworkExecutor apiServiceExecutor = getServiceExecutorInstance();

    ApiGenericResponse apiGenericResponse =
        apiServiceExecutor.call(apiClient.testHttpConnection("error"));

    ApiErrorDataMock testResponse = (ApiErrorDataMock) apiGenericResponse.getError();

    assertEquals(apiGenericResponse.getResponseStatus(), ApiResponseStatus.ERROR);
    assertEquals(testResponse.getMessage(), "Error.");
    assertEquals(apiGenericResponse.getHttpResponse().getHttpStatus(), ERROR_RESPONSE_CODE);
  }

  @Test(expected = Exception.class) public void apiServiceBadExecutorTest() throws Exception {
    NetworkExecutor apiServiceExecutor = getServiceExecutorInstance();

    apiServiceExecutor.call(apiClient.testHttpConnection("bad"));
  }

  @Test public void apiServiceBadExecutorExceptionErrorBusinessTest() throws Exception {
    NetworkExecutor apiServiceExecutor = getServiceExecutorExceptionHandlerInstance();

    ApiGenericResponse apiGenericResponse =
        apiServiceExecutor.call(apiClient.testHttpConnection("bad"));

    Exception exception = (Exception) apiGenericResponse.getError();
    HttpResponse httpResponse = apiGenericResponse.getHttpResponse();
    assertNotNull(exception);
    assertEquals(httpResponse.getHttpStatus(), ApiGenericExceptionResponse.HTTP_EXCEPTION_CODE);
  }

  @After public void tearDown() throws Exception {
    server.shutdown();
  }

  private NetworkExecutor getServiceExecutorExceptionHandlerInstance() {
    return new RetrofitNetworkExecutorBuilder(networkClient,
        ApiErrorResponseMock.class).retryOnErrorPolicy(
        buildErrorPolicyInstanceWithoutThrowException()).build();
  }

  private NetworkExecutor getServiceExecutorInstance() {
    return new RetrofitNetworkExecutorBuilder(networkClient,
        ApiErrorResponseMock.class).retryOnErrorPolicy(buildErrorPolicyInstance()).build();
  }

  private RetryOnErrorPolicy buildErrorPolicyInstance() {
    return new DefaultRetryOnErrorPolicyImpl();
  }

  private RetryOnErrorPolicy buildErrorPolicyInstanceWithoutThrowException() {
    return new NoExceptionRetryOnErrorPolicyImpl();
  }

  private ErrorConverter buildErrorConverterInstance() {
    return new DefaultErrorConverterImpl(retrofit, ApiErrorResponseMock.class);
  }
}