package kr.ac.deu.se.wisenote.model;


import kr.ac.deu.se.wisenote.model.signin.SigninResponse;
import kr.ac.deu.se.wisenote.model.signup.SignupRequest;
import kr.ac.deu.se.wisenote.model.signup.SignupResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ServiceApi {
  @POST("users")
  Call<SignupResponse> userSignup(@Body SignupRequest signupRequest);
  @FormUrlEncoded
  @POST("/auth/token")
  Call<SigninResponse> userSignin(@Field("username") String username,@Field("password") String password);
}
