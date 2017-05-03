package com.gigigo.ggglib.network.context.responses;

import com.gigigo.ggglib.network.retrofit.context.responses.ApiGenericExceptionResponse;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

public class ApiGenericExceptionResponseTest {

  @Test public void createExceptionResponseWithStaticMethod() {

    Exception e = new Exception("Hello Exception World");

    ApiGenericExceptionResponse apiGenericExceptionResponse =
        ApiGenericExceptionResponse.getApiGenericExceptionResponseInstance(e);

    assertNotNull(apiGenericExceptionResponse);
    assertNotNull(apiGenericExceptionResponse.getError());
    assertNotNull(apiGenericExceptionResponse.getHttpResponse());
    assertNull(apiGenericExceptionResponse.getResult());
    assertEquals(apiGenericExceptionResponse.getError().getMessage(),
        "Hello Exception World");
  }
}
