package com.gigigo.ggglib.network.retrofit.context.responses;

import com.google.gson.annotations.SerializedName;

public class GitHubErrorResponse {

  //Error fields
  @SerializedName("message") private String message;
  @SerializedName("documentation_url") private String documentationUrl;

  private HttpResponse httpResponse;

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
}
