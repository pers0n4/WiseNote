package kr.ac.deu.se.wisenote.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import kr.ac.deu.se.wisenote.R;
import kr.ac.deu.se.wisenote.service.NoteService;
import kr.ac.deu.se.wisenote.service.ServiceGenerator;
import kr.ac.deu.se.wisenote.ui.memo.MemoActivity;
import kr.ac.deu.se.wisenote.vo.note.Note;
import lombok.SneakyThrows;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecentFragment extends Fragment {
  private final String token;
  private GridView gridView = null;
  private GridViewAdapter adapter = null;
  private NoteService service = null;

  public RecentFragment(String token) { this.token = token; }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_recent, container, false);
    adapter = new GridViewAdapter();
    gridView = view.findViewById(R.id.gridview_recent);
    ArrayList<Note> itemList = new ArrayList<>();

    service = ServiceGenerator.createService(NoteService.class, token);
    Call<List<Note>> note = service.getNotes();
    note.enqueue(new Callback<List<Note>>() {
      @SneakyThrows
      @Override
      public void onResponse(Call<List<Note>> call, Response<List<Note>> response) {
        if(response.isSuccessful()) {
          List<Note> notes = response.body();
          Log.d("recent", notes.toString());
          itemList.addAll(notes);
          adapter.replace(itemList);
          adapter.notifyDataSetChanged();
        }
        else {
          Toast.makeText(container.getContext() , response.errorBody().string(), Toast.LENGTH_LONG).show();
        }
      }

      @Override
      public void onFailure(Call<List<Note>> call, Throwable t) {
        Toast.makeText(container.getContext(), t.getCause().toString(), Toast.LENGTH_LONG).show();
      }
    });

    gridView.setAdapter(adapter);
    gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        //Toast.makeText(view.getContext(), "test"+itemList.get(i).getTitle(),Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(view.getContext(), MemoActivity.class);
        intent.putExtra("notdId",itemList.get(i).getId());
        startActivity(intent);
      }
    });
    return view;
  }

}
