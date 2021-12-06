package kr.ac.deu.se.wisenote.ui.memo;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.CertPathTrustManagerParameters;

import kr.ac.deu.se.wisenote.R;
import kr.ac.deu.se.wisenote.vo.notebooks.Notebook;

public class MemoSpinnerAdapter extends BaseAdapter {
  List<Notebook> lists = new ArrayList<Notebook>();
  LayoutInflater inflater;

  MemoSpinnerAdapter( List<Notebook> lists){
    this.lists = lists;
    Log.d("spinner","name: test");
  }

  @Override
  public int getCount() {
    return 0;
  }

  @Override
  public Object getItem(int i) {
    return lists.get(i);
  }

  @Override
  public long getItemId(int i) {
    return i;
  }

  @Override
  public View getView(int i, View view, ViewGroup viewGroup) {
    Context context = viewGroup.getContext();
    if(view == null){
      LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      view = inflater.inflate(R.layout.spinner_list_item, viewGroup,false);
    }

      Notebook notebook = lists.get(i);

      TextView title = (TextView)view.findViewById(R.id.textView21);

      title.setText(notebook.getName());
      Log.d("spinner","name2:"+notebook.getName());

    return view;
  }

  @Override
  public View getDropDownView(int position, View convertView, ViewGroup parent) {
    Context context = parent.getContext();
    if(convertView==null){
      LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      convertView = inflater.inflate(R.layout.spinner_dropdown_item,parent,false);
    }
    Notebook notebook = lists.get(position);

    TextView title = (TextView)convertView.findViewById(R.id.textView22);

    title.setText(notebook.getName());
    Log.d("spinner","name3:"+notebook.getName());

    return convertView;
  }


}
