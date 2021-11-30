package kr.ac.deu.se.wisenote.ui.notelist;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import kr.ac.deu.se.wisenote.R;
import kr.ac.deu.se.wisenote.vo.note.Note;

public class NoteListViewAdapter extends BaseAdapter {
  ArrayList<Note> items = new ArrayList<Note>();
  Context context;

  public void addItem(Note item) {
    items.add(item);
  }

  public void replace(ArrayList<Note> items) {
    this.items = items;
  }

  @Override
  public int getCount() { return items.size(); }

  @Override
  public Note getItem(int position) { return items.get(position); }

  @Override
  public long getItemId(int position) { return position; }

  @Override
  public View getView(int position, View view, ViewGroup viewGroup) {
    context = viewGroup.getContext();
    ViewHolder holder;

    if(view == null) {
      holder = new ViewHolder();

      LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      view = inflater.inflate(R.layout.activity_note_list_item, viewGroup, false);

      holder.titleText = view.findViewById(R.id.note_name);
      holder.descriptionText = view.findViewById(R.id.note_description);
      holder.dateText = view.findViewById(R.id.note_created_date);

      view.setTag(holder);
    } else {
      holder = (ViewHolder) view.getTag();
    }

    Note noteItem = getItem(position);

    // 텍스트 정보 입력
    String pattern = "EEE, MMM dd, yyyy";
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
    String data = simpleDateFormat.format(noteItem.getCreated_at());

    holder.titleText.setText(noteItem.getTitle());
    holder.descriptionText.setText(noteItem.getSummary());
    holder.dateText.setText(data);

    return view;
  }

  private class ViewHolder {
    TextView titleText;
    TextView descriptionText;
    TextView dateText;
  }
}
