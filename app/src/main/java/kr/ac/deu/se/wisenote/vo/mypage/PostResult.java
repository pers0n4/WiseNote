package kr.ac.deu.se.wisenote.vo.mypage;


import com.google.gson.annotations.SerializedName;

public class PostResult {

  @SerializedName("id")
  private String id;
  @SerializedName("email")
  private String email;
  @SerializedName("name")
  private String name;
  @SerializedName("phone")
  private String phone;
  @Override
  public String toString() {
    return "PostResult{" +
      "id=" + id +
      ", email=" + email +
      ", name='" + name + '\'' +
      ", phone='" + phone + '\'' +
      '}';
  }

  public String getId() {
    return id;
  }

  public String getEmail() {
    return email;
  }

  public String getName() {
    return name;
  }

  public String getPhone() {
    return phone;
  }
}
