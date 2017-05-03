package com.gigigo.ggglib.network.context.responses;

import com.gigigo.ggglib.network.responses.HttpResponse;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

public class HttpResponseTest {

  @Test public void createExceptionResponseWithStaticMethod() {

    Exception e = new Exception("Hello Exception World");

    HttpResponse httpResponse = HttpResponse.getHttpResponseExceptionInstance(-222, e.getLocalizedMessage());

    assertNotNull(httpResponse);
    assertEquals(httpResponse.getHttpStatus(), -222);
    assertEquals(httpResponse.getStatusMessage(), "Hello Exception World");
  }
}
