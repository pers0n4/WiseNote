package kr.ac.deu.se.wisenote.vo.note;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;
public class NoteCreateResponse {
  @SerializedName("id")
  String id;
  @SerializedName("title")
  String title;
  @SerializedName("content")
  String content;
  @SerializedName("user_id")
  String user_id;
  @SerializedName("notebook_id")
  String notebook_id;
  @SerializedName("summary")
  String summary;
  @SerializedName("memo")
  String memo;
  @SerializedName("is_favorite")
  boolean is_favorite;
  @SerializedName("latitude")
  float latitude;
  @SerializedName("longitude")
  float longitude;
  @SerializedName("email")
  String email;
  @SerializedName("created_at")
  String created_at;
  @SerializedName("updated_at")
  String updated_at;

}
