package kr.ac.deu.se.wisenote.ui.memo;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import kr.ac.deu.se.wisenote.R;
import kr.ac.deu.se.wisenote.service.NoteService;
import kr.ac.deu.se.wisenote.service.ServiceGenerator;
import kr.ac.deu.se.wisenote.vo.note.Note;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainFragment extends Fragment {
  private Note note;
  private String noteId;
  private final String token;
  private TextView summary;
  private NoteService service;

  MainFragment(String noteId,String token){
    this.noteId = noteId;
    this.token = token;
  }
  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_main,container,false);
    summary = view.findViewById(R.id.summary);
    summary.setMovementMethod(new ScrollingMovementMethod());
    service = ServiceGenerator.createService(NoteService.class,token);
    service.getNote(noteId).enqueue(new Callback<Note>() {
      @Override
      public void onResponse(Call<Note> call, Response<Note> response) {
        note = response.body();
        summary.setText(note.getSummary());
      }

      @Override
      public void onFailure(Call<Note> call, Throwable t) {

      }
    });


    return view;


  }
}
