package kr.ac.deu.se.wisenote.service;


import java.util.List;

import kr.ac.deu.se.wisenote.vo.note.NotePost;
import retrofit2.Call;
import retrofit2.http.GET;

/*
 * REST API access points
 */
public interface NoteService {
  @GET("/notes")
  Call<List<NotePost>> getNotes();
}
