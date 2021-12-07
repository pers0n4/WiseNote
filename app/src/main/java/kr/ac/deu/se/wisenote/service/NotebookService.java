package kr.ac.deu.se.wisenote.service;

import java.util.List;

import kr.ac.deu.se.wisenote.vo.notebooks.Notebook;
import kr.ac.deu.se.wisenote.vo.notebooks.NotebookRequest;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;


public interface NotebookService {
  @GET("/notebooks")
  Call<List<Notebook>> getNotebooks();

  @POST("/notebooks")
  Call<Notebook> createNotebooks(@Body NotebookRequest notebookRequest);

  @GET("/notebooks/{notebook_name}")
  Call<Notebook> readNotebook(@Path("notebook_name")String notebook_name);

  @DELETE("/notebooks/{notebook_name}")
  Call<Void> deleteNotebooks(@Path("notebook_name")String notebook_name );

}
