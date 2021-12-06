package kr.ac.deu.se.wisenote.ui.memo;

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
import kr.ac.deu.se.wisenote.ui.home.ViewPagerAdapter;
import kr.ac.deu.se.wisenote.vo.note.Note;
import kr.ac.deu.se.wisenote.vo.notebooks.Notebook;
import kr.ac.deu.se.wisenote.vo.notebooks.NotebookRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MemoActivity extends AppCompatActivity {
  private LinearLayout favorite;
  private LinearLayout recycle;
  private TextView title;
  private TextView date;
  private LinearLayout setMemo;
  private ListView listView;
  private List<Notebook> notebooks;
  private Note note;
  private NotebookService service;
  private NoteService noteService;
  private HamburgerListAdapter adapter;
  private ImageButton hamburger;
  private ImageButton addFolder;
  private Button edit;
  private Dialog addDialog;
  private Dialog deleteDialog;
  private Dialog editDialog;
  private Button cancel;
  private Button create;
  private Button ok;
  private EditText folderName;
  private EditText editTitle;
  private DrawerLayout drawerLayout;

  private String[] titles = new String[]{"Main","Text","Memo"};

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_memo);

    // token 정보 가져오기
    SharedPreferences sharedPref = getSharedPreferences("wisenote", Context.MODE_PRIVATE);
    String token = sharedPref.getString("token", null);
    service = ServiceGenerator.createService(NotebookService.class,token);
    listView = (ListView) findViewById(R.id.listview);

    getData(token);

    noteService = ServiceGenerator.createService(NoteService.class,token);
    Intent intent = getIntent();
    String noteId = intent.getStringExtra("notdId");

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

    //햄버거메뉴 나오기
    hamburger = (ImageButton) findViewById(R.id.hamButton);
    hamburger.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        drawerLayout = (DrawerLayout) findViewById(R.id.memo_draw);
        if (!drawerLayout.isDrawerOpen(Gravity.LEFT)) {
          getData(token);
          drawerLayout.openDrawer(Gravity.LEFT);
        }
      }
    });
    // 햄버거 메뉴 플러스 버튼
    addFolder = (ImageButton) findViewById(R.id.addFolder);
    addFolder.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        addDialog(token);
      }
    });
    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Toast.makeText(MemoActivity.this, "폴더클릭"+adapter.getFolderName(i), Toast.LENGTH_SHORT).show();
        drawerLayout.closeDrawer(Gravity.LEFT);
      }
    });
    listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
      @Override
      public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        deleteDialog(i,token);
        return false;
      }
    });

    favorite = (LinearLayout) findViewById(R.id.favorite);
    recycle = (LinearLayout) findViewById(R.id.recycle);
    favorite.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Toast.makeText(MemoActivity.this,"favorite 클릭",Toast.LENGTH_SHORT).show();
        drawerLayout.closeDrawer(Gravity.LEFT);
      }
    });
    recycle.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Toast.makeText(MemoActivity.this, "recycle 클릭", Toast.LENGTH_SHORT).show();
        drawerLayout.closeDrawer(Gravity.LEFT);
      }
    });
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
        @Override
        public void onClick(View view) {
          note.setTitle(editTitle.getText().toString());
          Log.d("editTitle","title: "+ editTitle.getText().toString());
          noteService.setNote(noteId,note).enqueue(new Callback<Note>() {
            @Override
            public void onResponse(Call<Note> call, Response<Note> response) {
              Log.d("editTitle","title:"+note.getTitle()+"code:"+response.code());
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
  // 폴더 추가
  public void addDialog(String auth_token) {
    addDialog.show();
    addDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


    folderName = addDialog.findViewById(R.id.editText);
    cancel = addDialog.findViewById(R.id.cancel);
    create = addDialog.findViewById(R.id.create);


    cancel.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        addDialog.dismiss();
      }
    });

    create.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        String text = folderName.getText().toString();
        NotebookRequest request = new NotebookRequest(text);
        adapter.addItem(request);
        folderName.setText("");
        addDialog.dismiss();
        getData(auth_token);
        //adapter.notifyDataSetChanged();
      }
    });
  }
  // 폴더 삭제
  public void deleteDialog(int i,String auth_token) {
    String text = adapter.getFolderName(i).toString();
    deleteDialog.show();
    deleteDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    Button cancel = deleteDialog.findViewById(R.id.button2);
    cancel.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        deleteDialog.dismiss();
      }
    });
    Button delete = deleteDialog.findViewById(R.id.button3);
    delete.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Log.d("notebooks delete",text);
        adapter.remove(i);
        deleteDialog.dismiss();
        getData(auth_token);
        //adapter.notifyDataSetChanged();
      }
    });
  }
  public void editDialog(String noteid){
    editDialog.show();
    editDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    editTitle.setText(note.getTitle());
  }
  // 햄버거메뉴 폴더 리스트 가져오기
  public void getData(String token) {
    //listView = (ListView) findViewById(R.id.listview);
    service.getNotebooks().enqueue(new Callback<List<Notebook>>() {
      @Override
      public void onResponse(Call<List<Notebook>> call, Response<List<Notebook>> response) {
        Log.d("notebooks","list code :"+ response.code());
        notebooks = response.body();
        adapter = new HamburgerListAdapter(notebooks,token);
        listView.setAdapter(adapter);
        //memoAdapter = new MemoSpinnerAdapter(notebooks);
        //spinner.setAdapter(memoAdapter);
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
