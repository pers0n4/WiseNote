package kr.ac.deu.se.wisenote.ui.home;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Stack;

import kr.ac.deu.se.wisenote.R;
import kr.ac.deu.se.wisenote.vo.note.Note;

public class GridViewAdapter extends BaseAdapter {
  ArrayList<Note> items = new ArrayList<Note>();
  Context context;

  public void addItem(Note item) {
    items.add(item);
  }

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
    Note noteItem = items.get(position);

    if(view == null) {
      LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      view = inflater.inflate(R.layout.activity_home_note_item, viewGroup, false);
    }

    // 배경 색상 랜덤 지정
    RandomColors colors = new RandomColors();
    GradientDrawable drawable =
      (GradientDrawable) context.getDrawable(R.drawable.home_border_radius);
    drawable.setColor(colors.getColor());

    // 텍스트 정보 입력
    TextView titleText = view.findViewById(R.id.note_title);
    TextView descriptionText = view.findViewById(R.id.note_description);
    TextView dateText = view.findViewById(R.id.note_created_date);

    String pattern = "yyyy-MM-dd";
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
    String data = simpleDateFormat.format(noteItem.getCreated_at());

    titleText.setText(noteItem.getTitle());
    descriptionText.setText(noteItem.getSummary());
    dateText.setText(data);

    return view;
  }

  private class RandomColors {
    private Stack<Integer> recycle, colors;

    public RandomColors() {
      colors = new Stack<>();
      recycle = new Stack<>();
      recycle.addAll(Arrays.asList(
        0xfff6cc46, 0xffd49222, 0xffefc58d, 0xffe59e75,
        0xffc87c50, 0xfff1bc4b, 0xffaf7c10, 0xffc56934
      ));
    }

    public int getColor() {
      if(colors.size()==0) {
        while(!recycle.isEmpty())
          colors.push(recycle.pop());
        Collections.shuffle(colors);
      }
      Integer c = colors.pop();
      recycle.push(c);
      return c;
    }
  }
}
