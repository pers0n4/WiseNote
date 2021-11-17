package kr.ac.deu.se.wisenote.vo.signin;

import com.google.gson.annotations.SerializedName;

public class SigninResponse {

  @SerializedName("access_token")
  private String access_token;

  @SerializedName("token_type")
  private String token_type;

  private SigninResponse() {};

  private static class LazyHolder {
    private static final SigninResponse INSTANCE = new SigninResponse();
  }

  private static SigninResponse getInstance() {
    return LazyHolder.INSTANCE;
  }

  private int code;

  public int getCode(){
    return code;
  }

  public void setCode(int code){
    this.code = code;

  }
  public String getAccess_token() {
    return access_token;
  }
}
