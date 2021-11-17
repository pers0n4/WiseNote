package kr.ac.deu.se.wisenote.vo.note;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Note {
  @SerializedName("id")
  private String id;

  @SerializedName("title")
  private String title;

  @SerializedName("body")
  private String content;

  @SerializedName("user_id")
  private String user_id;

  @SerializedName("notebook_id")
  private String notebook_id;

  @SerializedName("summary")
  private String summary;

  @SerializedName("memo")
  private String memo;

  @SerializedName("is_favorite")
  private String is_favorite;

  @SerializedName("created_at")
  private Date created_at;

  @SerializedName("updated_at")
  private Date updated_at;
}
