package kr.ac.deu.se.wisenote.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import kr.ac.deu.se.wisenote.vo.notebooks.Notebook;

public class GridViewAdapter extends BaseAdapter {
  ArrayList<Notebook> items = new ArrayList<Notebook>();
  Context context;

  @Override
  public int getCount() {
    return items.size();
  }

  @Override
  public Object getItem(int position) {
    return items.get(position);
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public View getView(int position, View view, ViewGroup viewGroup) {
    context = viewGroup.getContext();
    Notebook notebookItem = items.get(position);

    if(view == null) {
      LayoutInflater inflater =
        (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    return null;
  }
}
