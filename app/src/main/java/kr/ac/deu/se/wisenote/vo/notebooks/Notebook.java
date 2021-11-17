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
}
