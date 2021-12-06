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

  @SerializedName("content")
  private String content;

  @SerializedName("user_id")
  private String user_id;

  @SerializedName("notebook_id")
  private String notebook_id;

  @SerializedName("summary")
  private String summary;

  @SerializedName("memo")
  private String memo;

  @SerializedName("notebook")
  private String notebook;

  @SerializedName("is_favorite")
  private Boolean is_favorite;

  @SerializedName("created_at")
  private Date created_at;

  @SerializedName("updated_at")
  private Date updated_at;

  public String getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getContent() {
    return content;
  }

  public String getUser_id() {
    return user_id;
  }

  public String getNotebook_id() {
    return notebook_id;
  }

  public void setNotebook_id(String notebook_id) {
    this.notebook_id = notebook_id;
  }

  public String getSummary() {
    return summary;
  }

  public String getMemo() {
    return memo;
  }

  public void setMemo(String memo) {
    this.memo = memo;
  }

  public Boolean getIs_favorite() {
    return is_favorite;
  }

  public Date getCreated_at() {
    return created_at;
  }

  public Date getUpdated_at() {
    return updated_at;
  }

  public String getNotebook() {
    return notebook;
  }

  public void setNotebook(String notebook) {
    this.notebook = notebook;
  }
}
