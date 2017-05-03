package com.gigigo.ggglib.network.retrofit.context.responses;

import com.google.gson.annotations.SerializedName;

public class ApiResultResponseMock extends ApiGenericResultResponse<ApiResultDataMock> {

  @SerializedName("status") private String status;
  @SerializedName("data") private ApiResultDataMock data;

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  @Override public ApiResultDataMock getResult() {
    return this.data;
  }

  @Override public void setResult(ApiResultDataMock data) {
    this.data = data;
  }
}
