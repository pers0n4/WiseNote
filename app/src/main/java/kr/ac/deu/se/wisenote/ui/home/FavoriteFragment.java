package kr.ac.deu.se.wisenote.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.List;

import kr.ac.deu.se.wisenote.R;
import kr.ac.deu.se.wisenote.repository.NoteRepository;
import kr.ac.deu.se.wisenote.vo.note.Note;

public class FavoriteFragment extends Fragment {
  private final String token;
  private NoteRepository repository;
  private GridView gridView = null;
  private GridViewAdapter adapter = null;

  public FavoriteFragment(String token) { this.token = token; }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_favorite, container, false);
    repository = new NoteRepository(token);
    gridView = (GridView) view.findViewById(R.id.gridview);
    adapter = new GridViewAdapter();

    List<Note> allNotes = repository.getAllNotes();

    for(Note note : allNotes) {
      if(note.getIs_favorite() == true){
        adapter.addItem(note);
      }
    }

    gridView.setAdapter(adapter);

    return view;
  }
}
