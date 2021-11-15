package kr.ac.deu.se.wisenote.vo.signup;

import com.google.gson.annotations.SerializedName;

public class SignupResponse {

  @SerializedName("id")
  private String id;

  @SerializedName("email")
  private String email;

  @SerializedName("name")
  private String name;

  @SerializedName("phone")
  private String phone;

  public String getId(){
    return id;
  }

}
