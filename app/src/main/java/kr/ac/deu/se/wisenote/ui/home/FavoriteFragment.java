package kr.ac.deu.se.wisenote.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import kr.ac.deu.se.wisenote.R;
import kr.ac.deu.se.wisenote.service.NoteService;
import kr.ac.deu.se.wisenote.service.ServiceGenerator;
import kr.ac.deu.se.wisenote.vo.note.Note;
import lombok.SneakyThrows;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoriteFragment extends Fragment {
  private final String token;
  private GridView gridView = null;
  private GridViewAdapter adapter = null;
  private NoteService service = null;

  public FavoriteFragment(String token) { this.token = token; }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_favorite, container, false);
    adapter = new GridViewAdapter();
    gridView = view.findViewById(R.id.gridview);
    ArrayList<Note> itemList = new ArrayList<>();

    service = ServiceGenerator.createService(NoteService.class, token);
    Call<List<Note>> note = service.getNotes();
    note.enqueue(new Callback<List<Note>>() {
      @SneakyThrows
      @Override
      public void onResponse(Call<List<Note>> call, Response<List<Note>> response) {
        if(response.isSuccessful()) {
          List<Note> notes = response.body();
          itemList.addAll(notes);
          adapter.replace(itemList);
          adapter.notifyDataSetChanged();
        }
        else {
          Log.d("notes", response.errorBody().string());
        }
      }

      @Override
      public void onFailure(Call<List<Note>> call, Throwable t) {
        Log.d("fail", t.getCause().toString());
      }
    });

    gridView.setAdapter(adapter);

    return view;
  }
}
