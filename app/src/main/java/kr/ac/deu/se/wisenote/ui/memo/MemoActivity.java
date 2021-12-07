package kr.ac.deu.se.wisenote.ui.memo;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import kr.ac.deu.se.wisenote.R;
import kr.ac.deu.se.wisenote.service.NoteService;
import kr.ac.deu.se.wisenote.service.NotebookService;
import kr.ac.deu.se.wisenote.service.ServiceGenerator;
import kr.ac.deu.se.wisenote.ui.hamburger.HamburgerListAdapter;
import kr.ac.deu.se.wisenote.ui.home.HomeActivity;
import kr.ac.deu.se.wisenote.ui.home.ViewPagerAdapter;
import kr.ac.deu.se.wisenote.ui.mypage.MyPageActivity;
import kr.ac.deu.se.wisenote.ui.notelist.NoteListActivity;
import kr.ac.deu.se.wisenote.ui.record.RecordActivity;
import kr.ac.deu.se.wisenote.vo.note.Note;
import kr.ac.deu.se.wisenote.vo.notebooks.Notebook;
import kr.ac.deu.se.wisenote.vo.notebooks.NotebookRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MemoActivity extends AppCompatActivity {
  private ListView listView;
  private List<Notebook> notebooks;
  private TextView title;
  private TextView date;
  private LinearLayout setMemo;
  private Note note;
  private NotebookService service;
  private HamburgerListAdapter adapter;
  private NoteService noteService;
  private Button edit;
  private Dialog addDialog;
  private Dialog deleteDialog;
  private Dialog editDialog;
  private Button ok;
  private EditText folderName;
  private EditText editTitle;
  private DrawerLayout drawerLayout;
  private Spinner spinner;

  private final String[] titles = new String[]{"Main","Text","Memo"};
  private String token;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_memo);

    // token 정보 가져오기
    SharedPreferences sharedPref = getSharedPreferences("wisenote", Context.MODE_PRIVATE);
    token = sharedPref.getString("token", null);

    Intent intent = getIntent();
    String noteId = intent.getStringExtra("NoteId");

    // 하단 navigation button 구현
    ImageButton home_button = findViewById(R.id.home_button);
    home_button.setOnClickListener(homeClickListener);
    ImageButton my_page_button = findViewById(R.id.mypage_button);
    my_page_button.setOnClickListener(myPageClickListener);
    ImageButton record_button = findViewById(R.id.Record_btn);
    record_button.setOnClickListener(recordClickListener);

    ViewPager2 viewPager = findViewById(R.id.view_pager_memo);
    viewPager.setOffscreenPageLimit(3);

    Fragment memoFragment = new MemoFragment(noteId,token);
    Fragment textFragment = new TextFragment(noteId,token);
    Fragment mainFragment = new MainFragment(noteId,token);

    ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this);
    viewPagerAdapter.addFragment(mainFragment);
    viewPagerAdapter.addFragment(textFragment);
    viewPagerAdapter.addFragment(memoFragment);
    viewPager.setAdapter(viewPagerAdapter);

    TabLayout tabLayout = findViewById(R.id.tabs_memo);
    new TabLayoutMediator(tabLayout,viewPager,(tab, position) -> tab.setText(titles[position])).attach();

    // Hamburger Menu
    service = ServiceGenerator.createService(NotebookService.class,token);
    listView = findViewById(R.id.listview);
    getData(token);

    noteService = ServiceGenerator.createService(NoteService.class,token);

    getNote(noteId);

    addDialog = new Dialog(MemoActivity.this);
    addDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    addDialog.setContentView(R.layout.hamburger_add_dialog);
    deleteDialog = new Dialog(MemoActivity.this);
    deleteDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    deleteDialog.setContentView(R.layout.hamburger_delete_dialog);
    editDialog = new Dialog(MemoActivity.this);
    editDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    editDialog.setContentView(R.layout.memo_edit_dialog);

    editTitle = (EditText) editDialog.findViewById(R.id.editNoteTitle);
    spinner = (Spinner) editDialog.findViewById(R.id.spinner);

    //햄버거메뉴 나오기
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

    setMemo = (LinearLayout) findViewById(R.id.setMemo);
    setMemo.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        EditText memo = (EditText) findViewById(R.id.memo);
        //키보드 내리기
        InputMethodManager manager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        if(manager.isAcceptingText()){
          manager.hideSoftInputFromWindow(memo.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
        memo.clearFocus();
        String text = memo.getText().toString();
        note.setMemo(text);
        Log.d("text",text);
        noteService.setNote(noteId,note).enqueue(new Callback<Note>() {
          @Override
          public void onResponse(Call<Note> call, Response<Note> response) {
            Log.d("text","code"+ response.code());
            note = response.body();

          }

          @Override
          public void onFailure(Call<Note> call, Throwable t) {

          }
        });
      }
    });
    edit = (Button) findViewById(R.id.edit);
    edit.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        editDialog(noteId);

      }
    });
    ok = (Button)editDialog.findViewById(R.id.ok);
    ok.setOnClickListener(new View.OnClickListener() {
      String folderId;
      Notebook notebook = new Notebook();
      @Override
      public void onClick(View view) {
        notebook = (Notebook) spinner.getSelectedItem();
        folderId = notebook.getId();
        note.setTitle(editTitle.getText().toString());
        note.setNotebook(folderId);
        Log.d("editFolder","folder id"+ folderId);
        noteService.setNote(noteId,note).enqueue(new Callback<Note>() {
          @Override
          public void onResponse(Call<Note> call, Response<Note> response) {
            note = response.body();
            Log.d("editNote","title  :  "+note.getTitle()+"  folderId : "+note.getNotebook_id()+"code:"+response.code());
            getNote(noteId);
          }

          @Override
          public void onFailure(Call<Note> call, Throwable t) {

          }
        });
        editDialog.dismiss();
      }
    });

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
  private final View.OnClickListener homeClickListener = view -> {
    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
    startActivity(intent);
  };

  // Bottom Menu My Page Button Click Event
  private final View.OnClickListener myPageClickListener = view -> {
    Intent intent = new Intent(getApplicationContext(), MyPageActivity.class);
    intent.putExtra("token",token);
    startActivity(intent);
  };

  // Bottom Menu record Button Click Event
  private final View.OnClickListener recordClickListener = view -> {
    Intent intent = new Intent(getApplicationContext(), RecordActivity.class);
    intent.putExtra("token",token);
    startActivity(intent);
  };

  // Hamburger Menu 나오기
  @SuppressLint("RtlHardcoded")
  private final View.OnClickListener hamburgerMenu = view -> {
    drawerLayout = findViewById(R.id.memo_draw);
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
      drawerLayout.closeDrawer(Gravity.LEFT);
    });
  }
  public void editDialog(String noteid){
    editDialog.show();
    editDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    editTitle.setText(note.getTitle());
    MemoSpinnerAdapter memoAdapter = new MemoSpinnerAdapter(notebooks);
    spinner.setAdapter(memoAdapter);
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
  public void getNote(String noteId){
    noteService.getNote(noteId).enqueue(new Callback<Note>() {
      @Override
      public void onResponse(Call<Note> call, Response<Note> response) {
        note = response.body();
        Log.d("note","note title:"+note.getTitle());
        Log.d("note","note title:"+note.getUpdated_at());
        title = (TextView) findViewById(R.id.textView15);
        title.setText(note.getTitle());

        date = (TextView) findViewById(R.id.textView16);
        SimpleDateFormat sdf = new SimpleDateFormat("E, MMM dd, y",new Locale("en", "US"));
        String sdate = sdf.format(note.getUpdated_at());
        date.setText(sdate);

      }
      @Override
      public void onFailure(Call<Note> call, Throwable t) {

      }
    });

  }




}
