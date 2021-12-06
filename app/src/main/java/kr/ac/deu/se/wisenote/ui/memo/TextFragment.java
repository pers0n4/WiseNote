package kr.ac.deu.se.wisenote.ui.memo;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
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

public class TextFragment extends Fragment {
  private Note note;
  private String noteId;
  private final String token;
  private TextView text;
  private NoteService service;

  TextFragment(String notdId,String token){
    this.noteId = notdId;
    this.token = token;
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_text,container,false);
    text = view.findViewById(R.id.textView19);
    text.setMovementMethod(new ScrollingMovementMethod());
    service = ServiceGenerator.createService(NoteService.class,token);
    service.getNote(noteId).enqueue(new Callback<Note>() {
      @Override
      public void onResponse(Call<Note> call, Response<Note> response) {
        note = response.body();
        Log.d("Textcontent","content:"+note.getContent());
        text.setText(note.getContent());
      }
      @Override
      public void onFailure(Call<Note> call, Throwable t) {

      }
    });
    return view;
  }




}
