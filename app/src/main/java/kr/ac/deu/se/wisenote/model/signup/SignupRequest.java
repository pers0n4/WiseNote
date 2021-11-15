package kr.ac.deu.se.wisenote.model.signup;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class SignupRequest {
  @SerializedName("email")
  String email;

  @SerializedName("password")
  String password;

  @SerializedName("name")
  String name;

  @SerializedName("phone")
  String phone;

  public SignupRequest(String email,String password){
    this.email = email;
    this.password = password;
    this.name = " ";
    this.phone = " ";
  }

  @NonNull
  @Override
  public String toString() {
    String test = email  + "," + password;
    return test;
  }
}
