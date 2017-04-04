package com.gigigo.ggglib.network.retrofit.context.responses;

import com.google.gson.annotations.SerializedName;

public class GitHubDataResponse {

  private String login;

  private Integer id;

  private String avatarUrl;

  private String name;

  private String email;

  private Object bio;

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
}
