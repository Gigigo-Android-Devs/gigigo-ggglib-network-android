package com.gigigo.ggglib.network.retrofit.context.responses;

public class GithubErrorResponse extends ApiGenericErrorResponse<GithubErrorData> {

  private GithubErrorData error;

  @Override public GithubErrorData getError() {
    return this.error;
  }

  @Override public void setError(GithubErrorData error) {
    this.error = error;
  }
}
