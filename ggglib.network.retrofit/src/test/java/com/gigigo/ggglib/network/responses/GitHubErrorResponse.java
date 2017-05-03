package com.gigigo.ggglib.network.responses;

import com.google.gson.annotations.SerializedName;

public class GitHubErrorResponse extends ApiGenericErrorResponse<GitHubErrorData> {

  private GitHubErrorData gitHubErrorData;

  @SerializedName("message") private String message;
  @SerializedName("documentation_url") private String documentationUrl;

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getDocumentationUrl() {
    return documentationUrl;
  }

  public void setDocumentationUrl(String documentationUrl) {
    this.documentationUrl = documentationUrl;
  }

  @Override public GitHubErrorData getError() {
    if (this.gitHubErrorData == null) {
      this.gitHubErrorData = new GitHubErrorData();
      this.gitHubErrorData.setMessage(this.message);
      this.gitHubErrorData.setDocumentationUrl(this.documentationUrl);
    }
    return this.gitHubErrorData;
  }

  @Override public void setError(GitHubErrorData gitHubErrorResponse) {
    this.message = gitHubErrorResponse.getMessage();
    this.documentationUrl = gitHubErrorResponse.getDocumentationUrl();
  }
}
