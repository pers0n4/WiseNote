package kr.ac.deu.se.wisenote.vo.token;

import com.google.gson.annotations.SerializedName;

public class TokenPost {
  @SerializedName("access_token")
  private String access_token;
  @SerializedName("token_type")
  private String token_type;

  @Override
  public String toString() {
    return "TokenPost{" +
      "access_token='" + access_token + '\'' +
      ", token_type='" + token_type + '\'' +
      '}';
  }

  public String getAccess_token() {
    return access_token;
  }
}
