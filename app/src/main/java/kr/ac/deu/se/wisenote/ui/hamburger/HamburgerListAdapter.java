package kr.ac.deu.se.wisenote.ui.hamburger;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

import kr.ac.deu.se.wisenote.R;
import kr.ac.deu.se.wisenote.service.NotebookService;
import kr.ac.deu.se.wisenote.service.ServiceGenerator;
import kr.ac.deu.se.wisenote.vo.notebooks.Notebook;
import kr.ac.deu.se.wisenote.vo.notebooks.NotebookRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HamburgerListAdapter extends BaseAdapter {
  NotebookService service;
  List<Notebook> lists = new ArrayList<Notebook>();


  public HamburgerListAdapter(List<Notebook> lists,String token){
    service =  ServiceGenerator.createService(NotebookService.class,token);
    this.lists = lists;
  }

  @Override
  public int getCount() {
    return lists.size();
  }

  @Override
  public Object getItem(int i) {
    return lists.get(i);
  }

  @Override
  public long getItemId(int i) {
    return i;
  }

  public String getFolderName(int i){
    return lists.get(i).getName();
  }

  @Override
  public View getView(int i, View view, ViewGroup viewGroup) {
    Context context = viewGroup.getContext();
    if (view == null){
      LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      view = inflater.inflate(R.layout.hamburger_list_item,viewGroup,false);
    }

    TextView folderName = (TextView) view.findViewById(R.id.foldername);

    Notebook listItem = lists.get(i);

    folderName.setText(listItem.getName());
    return view;
  }
  public void addItem(NotebookRequest request){
    //service = ServiceGenerator.createService(NotebookService.class,token);
    service.createNotebooks(request).enqueue(new Callback<Notebook>() {
      @Override
      public void onResponse(Call<Notebook> call, Response<Notebook> response) {
        Log.d("notebooksadd ","code"+response.code());
      }

      @Override
      public void onFailure(Call<Notebook> call, Throwable t) {

      }
    });
  }
  public void remove(int i){
    //service = ServiceGenerator.createService(NotebookService.class,token);
    String notebooks_name = lists.get(i).getName();
    service.deleteNotebooks(notebooks_name).enqueue(new Callback<Void>() {
      @Override
      public void onResponse(Call<Void> call, Response<Void> response) {
        Log.d("notebooksdelete","code"+response.code());

      }
      @Override
      public void onFailure(Call<Void> call, Throwable t) {
      }
    });

  }

}
