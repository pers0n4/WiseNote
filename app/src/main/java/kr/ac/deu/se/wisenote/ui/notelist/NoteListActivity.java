package kr.ac.deu.se.wisenote.ui.notelist;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import java.util.ArrayList;
import java.util.List;

import kr.ac.deu.se.wisenote.R;
import kr.ac.deu.se.wisenote.service.NoteService;
import kr.ac.deu.se.wisenote.service.NotebookService;
import kr.ac.deu.se.wisenote.service.ServiceGenerator;
import kr.ac.deu.se.wisenote.ui.hamburger.HamburgerListAdapter;
import kr.ac.deu.se.wisenote.ui.home.HomeActivity;
import kr.ac.deu.se.wisenote.vo.note.Note;
import kr.ac.deu.se.wisenote.vo.notebooks.Notebook;
import kr.ac.deu.se.wisenote.vo.notebooks.NotebookRequest;
import lombok.SneakyThrows;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NoteListActivity extends AppCompatActivity {
  private ListView listView;
  private List<Notebook> notebooks;
  private NotebookService service;
  private HamburgerListAdapter adapter;
  private Dialog addDialog;
  private Dialog deleteDialog;
  private EditText folderName;

  private NoteListViewAdapter noteListViewAdapter;
  private TextView tv_title;
  private TextView tv_description;
  private String token;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_note_list);

    // token 정보 가져오기
    SharedPreferences sharedPref = getSharedPreferences("wisenote", Context.MODE_PRIVATE);
    token = sharedPref.getString("token", null);

    // 하단 navigation button 구현
    ImageButton home_button = findViewById(R.id.home_button);
    home_button.setOnClickListener(homeClickListener);
    ImageButton my_page_button = findViewById(R.id.mypage_button);
    my_page_button.setOnClickListener(myPageClickListener);

    tv_title = findViewById(R.id.notebook_title);
    tv_description = findViewById(R.id.notebook_description);

    noteListViewAdapter = new NoteListViewAdapter();
    ListView noteListView = findViewById(R.id.note_list_view);

    // 전달 정보에 따른 노트 생성
    Intent intent = getIntent();
    if (!TextUtils.isEmpty(intent.getStringExtra("NotebookTitle"))) {
      getNoteInfo(intent.getStringExtra("NotebookTitle"));
    } else {
      setTextView("All Notes", "Every notes you wrote.");
      // 모든 노트에 대한 목록 생성
      getNoteInfo();
    }

    noteListView.setAdapter(noteListViewAdapter);

    // 햄버거 메뉴 생성
    service = ServiceGenerator.createService(NotebookService.class,token);
    listView = findViewById(R.id.listview);
    getData(token);
    //dialog 초기화
    addDialog = new Dialog(NoteListActivity.this);
    addDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    addDialog.setContentView(R.layout.hamburger_add_dialog);
    deleteDialog = new Dialog(NoteListActivity.this);
    deleteDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    deleteDialog.setContentView(R.layout.hamburger_delete_dialog);

    // 햄버거메뉴 나오기
    ImageButton hamburger = findViewById(R.id.hamButton);
    hamburger.setOnClickListener(hamburgerMenu);

    // 햄버거 메뉴 플러스 버튼
    ImageButton addFolder = findViewById(R.id.addFolder);
    addFolder.setOnClickListener(addFolderClickEvent);

    // Hamburger Menu All note view Button
    LinearLayout all_notes = findViewById(R.id.all_notes);
    all_notes.setOnClickListener(noteListClickListener);

    // Hamburger Menu Folder Item Click Event
    listView.setOnItemClickListener(folderItemClickEvent);
    listView.setOnItemLongClickListener(folderItemLongClickEvent);

    LinearLayout favorite = findViewById(R.id.favorite);
    favorite.setOnClickListener(favoriteNoteListClickListener);
  }

  // Get All Note Information
  private void getNoteInfo() {
    NoteService service = ServiceGenerator.createService(NoteService.class, token);
    Call<List<Note>> note = service.getNotes();
    note.enqueue(new Callback<List<Note>>() {
      @SneakyThrows
      @Override
      public void onResponse(Call<List<Note>> call, Response<List<Note>> response) {
        if(response.isSuccessful()) {
          List<Note> notes = response.body();
          ArrayList<Note> itemList = new ArrayList<>();
          itemList.addAll(notes);
          noteListViewAdapter.replace(itemList);
          noteListViewAdapter.notifyDataSetChanged();
        }
        else {
          Log.d("notes", response.errorBody().string());
        }
      }

      @Override
      public void onFailure(Call<List<Note>> call, Throwable t) {
        Log.d("fail", t.getCause().toString());
      }
    });
  }

  // Get Note Information By Note Name
  private void getNoteInfo(String notebook_name) {
    if (notebook_name.equals("favorite")) {
      setTextView("Favorite", "Favorite notes you wrote.");
      NoteService service = ServiceGenerator.createService(NoteService.class, token);
      Call<List<Note>> note = service.getNotes();
      note.enqueue(new Callback<List<Note>>() {
        @SneakyThrows
        @Override
        public void onResponse(Call<List<Note>> call, Response<List<Note>> response) {
          if(response.isSuccessful()) {
            List<Note> notes = response.body();
            for(Note note : notes) {
              if(note.getIs_favorite()) {
                noteListViewAdapter.addItem(note);
                noteListViewAdapter.notifyDataSetChanged();
              }
            }
          }
        }

        @Override
        public void onFailure(Call<List<Note>> call, Throwable t) {
          Log.d("fail", t.getCause().toString());
        }
      });
    } else {
      NotebookService service = ServiceGenerator.createService(NotebookService.class, token);
      Call<Notebook> notebook = service.readNotebook(notebook_name);
      notebook.enqueue(new Callback<Notebook>() {
        @Override
        public void onResponse(Call<Notebook> call, Response<Notebook> response) {
          if(response.isSuccessful()) {
            Notebook data = response.body();
            setTextView(data.getName(), "The notes that you want");
            ArrayList<Note> itemList = new ArrayList<>();
            itemList.addAll(data.getNotes());
            noteListViewAdapter.replace(itemList);
            noteListViewAdapter.notifyDataSetChanged();
          }
        }

        @Override
        public void onFailure(Call<Notebook> call, Throwable t) {
          Log.d("fail", t.getCause().toString());
        }
      });
    }
  }

  // Edit Notebook List View Title
  private void setTextView(String title, String description) {
    tv_title.setText(title);
    tv_description.setText(description);
  }

  // All Note List View Button Click Event
  private final View.OnClickListener noteListClickListener = view -> {
    Intent intent = new Intent(getApplicationContext(), NoteListActivity.class);
    startActivity(intent);
  };

  // Favorite Note List View Button Click Event
  private final View.OnClickListener favoriteNoteListClickListener = view -> {
    Intent intent = new Intent(getApplicationContext(), NoteListActivity.class);
    intent.putExtra("NotebookTitle", "favorite");
    startActivity(intent);
  };

  // Bottom Menu Home Button Click Event
  private View.OnClickListener homeClickListener = view -> {
    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
    startActivity(intent);
  };

  // Bottom Menu My Page Button Click Event
  private View.OnClickListener myPageClickListener = view -> {
    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
    startActivity(intent);
  };

  // Hamburger Menu 나오기
  @SuppressLint("RtlHardcoded")
  private final View.OnClickListener hamburgerMenu = view -> {
    DrawerLayout drawerLayout = findViewById(R.id.note_list_draw);
    if (!drawerLayout.isDrawerOpen(Gravity.LEFT)) {
      getData(token);
      drawerLayout.openDrawer(Gravity.LEFT);
    }
  };

  // Add Folder Click Event
  private final View.OnClickListener addFolderClickEvent = view -> {
    addDialog(token);
  };

  // Folder Item Click Event
  @SuppressLint("RtlHardcoded")
  private final AdapterView.OnItemClickListener folderItemClickEvent = (adapterView, view, i, l) ->  {
    Intent intent = new Intent(getApplicationContext(), NoteListActivity.class);
    intent.putExtra("NotebookTitle", adapter.getFolderName(i));
    startActivity(intent);
  };

  // Folder Item Long Click Event
  private final AdapterView.OnItemLongClickListener folderItemLongClickEvent = (adapterView, view, i, l) -> {
    deleteDialog(i,token);
    return false;
  };

  // 폴더 추가
  public void addDialog(String token) {
    addDialog.show();
    addDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

    folderName = addDialog.findViewById(R.id.editText);
    Button cancel = addDialog.findViewById(R.id.cancel);
    Button create = addDialog.findViewById(R.id.create);

    cancel.setOnClickListener(view -> addDialog.dismiss());

    create.setOnClickListener(view -> {
      String text = folderName.getText().toString();
      NotebookRequest request = new NotebookRequest(text);
      adapter.addItem(request);
      folderName.setText("");
      addDialog.dismiss();
      getData(token);
      adapter.notifyDataSetChanged();
    });
  }

  // 폴더 삭제
  public void deleteDialog(int i,String auth_token) {
    String text = adapter.getFolderName(i);
    deleteDialog.show();
    deleteDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    Button cancel = deleteDialog.findViewById(R.id.button2);
    cancel.setOnClickListener(view -> deleteDialog.dismiss());
    Button delete = deleteDialog.findViewById(R.id.button3);
    delete.setOnClickListener(view -> {
      Log.d("notebooks delete",text);
      adapter.remove(i);
      deleteDialog.dismiss();
      getData(auth_token);
      adapter.notifyDataSetChanged();
    });
  }

  // 햄버거메뉴 폴더 리스트 가져오기
  public void getData(String token) {
    service.getNotebooks().enqueue(new Callback<List<Notebook>>() {
      @Override
      public void onResponse(Call<List<Notebook>> call, Response<List<Notebook>> response) {
        Log.d("notebooks","list code :"+ response.code());
        notebooks = response.body();
        adapter = new HamburgerListAdapter(notebooks,token);
        listView.setAdapter(adapter);
      }
      @Override
      public void onFailure(Call<List<Notebook>> call, Throwable t) {

      }
    });
  }
}
