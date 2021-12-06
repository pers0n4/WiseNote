package kr.ac.deu.se.wisenote.service;


import java.util.List;

import kr.ac.deu.se.wisenote.vo.note.Note;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.Path;

/*
 * REST API access points
 */
public interface NoteService {
  @GET("/notes")
  Call<List<Note>> getNotes();

  @GET("/notes/{note_id}")
  Call<Note> getNote(@Path("note_id")String note_id);

  @PATCH("/notes/{note_id}")
  Call<Note> setNote(@Path("note_id")String note_id, @Body Note note);

}
