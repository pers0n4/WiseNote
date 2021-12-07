package kr.ac.deu.se.wisenote.vo.note;
import com.google.gson.annotations.SerializedName;

public class NotePost {
  @SerializedName("title")
  private String title;
  @SerializedName("content")
  private String content;
  @SerializedName("memo")
  private String memo;
  @SerializedName("is_favorite")
  private boolean is_favorite;
  @SerializedName("notebook")
  private String notebook;
  @SerializedName("latitude")
  private float latitude;
  @SerializedName("longitude")
  private float longitude;

  public NotePost(String title, String content, String memo, boolean is_favorite, String notebook, float latitude, float longitude) {
    this.title = title;
    this.content = content;
    this.memo = memo;
    this.is_favorite = is_favorite;
    this.notebook = notebook;
    this.latitude = latitude;
    this.longitude = longitude;
  }
}
