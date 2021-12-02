package kr.ac.deu.se.wisenote.vo.notebooks;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import kr.ac.deu.se.wisenote.vo.note.Note;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Notebook {
  @SerializedName("id")
  private String id;

  @SerializedName("name")
  private String name;

  @SerializedName("user_id")
  private String user_id;

  @SerializedName("notes")
  private List<Note> notes = null;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getUser_id() {
    return user_id;
  }

  public void setUser_id(String user_id) {
    this.user_id = user_id;
  }

  public List<Note> getNotes() {
    return notes;
  }

  public void setNotes(List<Note> notes) {
    this.notes = notes;
  }
}
