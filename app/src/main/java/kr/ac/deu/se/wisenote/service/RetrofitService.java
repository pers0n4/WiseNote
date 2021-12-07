package kr.ac.deu.se.wisenote.service;


import kr.ac.deu.se.wisenote.vo.mypage.PostResult;
import kr.ac.deu.se.wisenote.vo.note.NoteCreateResponse;
import kr.ac.deu.se.wisenote.vo.note.NotePost;
import kr.ac.deu.se.wisenote.vo.token.TokenGet;
import kr.ac.deu.se.wisenote.vo.token.TokenPost;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RetrofitService {

  @GET("users/{user_id}")
  Call<PostResult> getPosts(@Path("user_id") String post);

  @FormUrlEncoded
  @POST("auth/token")
  Call<TokenPost> createToken(@Field("username") String username, @Field("password") String password);

  @DELETE("users/{user_id}")
  Call<Void> deleteToken(@Header("Authorization") String authorization, @Path("user_id") String user_id);

  @GET("auth/token")
  Call<TokenGet> TestToken(@Header("Authorization") String authorization);


  @POST("notes")
  Call<NoteCreateResponse> createNote(@Header("Authorization") String authorization,
                                      @Body NotePost notePost);
}
