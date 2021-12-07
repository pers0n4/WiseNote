package kr.ac.deu.se.wisenote.vo.notebooks;

import com.google.gson.annotations.SerializedName;

public class NotebookRequest {
  @SerializedName("name")
  String name;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
  public NotebookRequest(String name){
    this.name = name;
  }
}
