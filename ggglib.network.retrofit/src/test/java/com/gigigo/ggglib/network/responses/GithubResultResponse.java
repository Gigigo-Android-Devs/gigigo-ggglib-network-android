package com.gigigo.ggglib.network.responses;

import com.google.gson.annotations.SerializedName;

public class GithubResultResponse
    extends ApiGenericResponse<GithubResultResponse,GithubErrorResponse> {

  @SerializedName("login") private String login;

  @SerializedName("id") private Integer id;

  @SerializedName("avatar_url") private String avatarUrl;

  @SerializedName("name") private String name;

  @SerializedName("email") private String email;

  @SerializedName("bio") private Object bio;

  public String getLogin() {
    return login;
  }

  public void setLogin(String login) {
    this.login = login;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getAvatarUrl() {
    return avatarUrl;
  }

  public void setAvatarUrl(String avatarUrl) {
    this.avatarUrl = avatarUrl;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Object getBio() {
    return bio;
  }

  public void setBio(Object bio) {
    this.bio = bio;
  }

  @Override public GithubResultResponse getResult() {
    return this;
  }

  @Override public GithubErrorResponse getError() {
    return null;
  }

  public void setResult(GithubResultResponse githubResultResponse) {
    this.avatarUrl = githubResultResponse.avatarUrl;
    this.bio = githubResultResponse.bio;
    this.login = githubResultResponse.login;
    this.id = githubResultResponse.id;
    this.email = githubResultResponse.email;
    this.name = githubResultResponse.name;
  }

  @Override public ApiResponseStatus getResponseStatus() {
    return ApiResponseStatus.OK;
  }

  @Override public String toString() {
    return "GitHubResponse{"
        + "login='"
        + login
        + '\''
        + ", id="
        + id
        + ", avatarUrl='"
        + avatarUrl
        + '\''
        + ", name='"
        + name
        + '\''
        + ", email='"
        + email
        + '\''
        + ", bio="
        + bio
        + ", httpResponse="
        + httpResponse
        + '}';
  }
}
