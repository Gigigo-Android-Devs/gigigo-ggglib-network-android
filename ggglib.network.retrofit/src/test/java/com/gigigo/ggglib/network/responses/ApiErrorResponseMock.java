package com.gigigo.ggglib.network.responses;

import com.google.gson.annotations.SerializedName;

public class ApiErrorResponseMock extends ApiGenericErrorResponse<ApiErrorDataMock> {

  @SerializedName("status") private String status;
  @SerializedName("error") private ApiErrorDataMock error;

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  @Override public ApiErrorDataMock getError() {
    return this.error;
  }

  @Override public void setError(ApiErrorDataMock error) {
    this.error = error;
  }
}
