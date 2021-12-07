package kr.ac.deu.se.wisenote.vo.token;

import com.google.gson.annotations.SerializedName;

public class TokenGet {
  @SerializedName("id")
  private String id;
  @SerializedName("email")
  private String email;
  @SerializedName("name")
  private String name;
  @SerializedName("phone")
  private String phone;

  public String getEmail() {
    return email;
  }

  public String getName() {
    return name;
  }

  public String getPhone() {
    return phone;
  }

  public String getId() {
    return id;
  }
}
