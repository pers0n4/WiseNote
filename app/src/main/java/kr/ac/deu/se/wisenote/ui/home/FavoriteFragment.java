package kr.ac.deu.se.wisenote.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import kr.ac.deu.se.wisenote.R;
import kr.ac.deu.se.wisenote.service.NotebookService;

public class FavoriteFragment extends Fragment {
  private GridView gridView = null;
  private GridViewAdapter adapter = null;
  private NotebookService service = null;

  public FavoriteFragment() {}

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_favorite, container, false);

    adapter = new GridViewAdapter();

    return view;
  }
}
