package kr.ac.deu.se.wisenote.service;


import kr.ac.deu.se.wisenote.vo.signin.SigninResponse;
import kr.ac.deu.se.wisenote.vo.signup.SignupRequest;
import kr.ac.deu.se.wisenote.vo.signup.SignupResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ServiceApi {
  @POST("/users")
  Call<SignupResponse> userSignup(@Body SignupRequest signupRequest);

  @FormUrlEncoded
  @POST("/auth/token")
  Call<SigninResponse> userSignin(@Field("username") String username,@Field("password") String password);
}
