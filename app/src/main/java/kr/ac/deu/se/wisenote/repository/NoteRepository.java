package kr.ac.deu.se.wisenote.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import kr.ac.deu.se.wisenote.service.NoteService;
import kr.ac.deu.se.wisenote.vo.note.Note;

public class NoteRepository {
  private NoteService service;
  private String token;

  public NoteRepository(String token) { this.token=token; }

  public List<Note> getAllNotes() {
//    NoteService noteservice = ServiceGenerator.createService(NoteService.class, token);
//    Call<List<Note>> notes = noteservice.getNotes();
//    notes.enqueue(new Callback<List<Note>>() {
//      @SneakyThrows
//      @Override
//      public void onResponse(Call<List<Note>> call, Response<List<Note>> response) {
//        if(response.isSuccessful()) {
//          Log.d("notes", response.body().toString());
//        }
//        else {
//          Log.d("notes", response.errorBody().string());
//        }
//      }
//
//      @Override
//      public void onFailure(Call<List<Note>> call, Throwable t) {
//        Log.d("fail", "Failed");
//      }
//    });

    ArrayList<Note> notes = new ArrayList<Note>();

    for(int i = 0 ; i < 20 ; i++){
      Note data = new Note();
      data.setId("testId"+i);
      data.setTitle("Test Title");
      data.setContent("Test content");
      data.setUser_id("Test User ID");
      data.setNotebook_id("Test Notebook ID");
      data.setSummary("Test Summary");
      data.setMemo("Test Memo");
      data.setIs_favorite(true);
      data.setCreated_at(new Date());
      data.setUpdated_at(new Date());

      notes.add(data);
    }

    return notes;
  }
}
