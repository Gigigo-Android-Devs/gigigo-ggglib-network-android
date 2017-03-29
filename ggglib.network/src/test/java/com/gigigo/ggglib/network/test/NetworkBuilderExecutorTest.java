package com.gigigo.ggglib.network.test;

import com.gigigo.ggglib.network.context.BaseApiClient;
import com.gigigo.ggglib.network.context.responses.ApiDataTestMock;
import com.gigigo.ggglib.network.context.responses.ApiErrorResponseMock;
import com.gigigo.ggglib.network.context.responses.ApiResponseMock;
import com.gigigo.ggglib.network.context.responses.ResponseUtils;
import com.gigigo.ggglib.network.context.wrapper.NetworkProvider;
import com.gigigo.ggglib.network.context.wrapper.RetrofitNetworkBuilder;
import com.gigigo.ggglib.network.context.wrapper.RetrofitNetworkProvider;
import com.gigigo.ggglib.network.responses.ApiGenericResponse;
import com.squareup.okhttp.mockwebserver.Dispatcher;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.squareup.okhttp.mockwebserver.RecordedRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import retrofit2.converter.gson.GsonConverterFactory;

import static junit.framework.Assert.assertEquals;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class NetworkBuilderExecutorTest {

  private final static int ERROR_RESPONSE_CODE = 500;

  private MockWebServer server;

  @Before public void setUp() throws Exception {
    this.server = new MockWebServer();
    server.setDispatcher(getMockwebserverDispatcherInstance());
  }

  @Test public void apiServiceOKExecutorTest() throws Exception {
    NetworkProvider networkProvider =
        new RetrofitNetworkBuilder(BaseApiClient.class, server.url("/").toString(),
            ApiErrorResponseMock.class).converterFactory(GsonConverterFactory.create()).build();

    /*
    BaseApiClient apiClient = ((RetrofitNetworkProvider)networkProvider).getApiClient();

    ApiGenericResponse apiGenericResponse =
        networkProvider.executeNetworkServiceConnection(ApiResponseMock.class,
            apiClient.testHttpConnection("ok"));

    ApiDataTestMock testResponse = (ApiDataTestMock) apiGenericResponse.getResult();

    assertEquals(testResponse.getTest(), "Hello World");
    assertEquals(apiGenericResponse.getHttpResponse().getHttpStatus(), 200);
    */
  }

  @After public void tearDown() throws Exception {
    server.shutdown();
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
}