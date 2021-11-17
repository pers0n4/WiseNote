package kr.ac.deu.se.wisenote.service;

import java.util.List;

import kr.ac.deu.se.wisenote.vo.notebooks.Notebook;
import retrofit2.Call;
import retrofit2.http.GET;

public interface NotebookService {
  @GET("/notebooks")
  Call<List<Notebook>> getNotebooks();
}
