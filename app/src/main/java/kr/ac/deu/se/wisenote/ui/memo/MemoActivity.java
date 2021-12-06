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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.List;

import kr.ac.deu.se.wisenote.R;
import kr.ac.deu.se.wisenote.service.NotebookService;
import kr.ac.deu.se.wisenote.service.ServiceGenerator;
import kr.ac.deu.se.wisenote.ui.hamburger.HamburgerListAdapter;
import kr.ac.deu.se.wisenote.ui.home.HomeActivity;
import kr.ac.deu.se.wisenote.ui.home.ViewPagerAdapter;
import kr.ac.deu.se.wisenote.ui.notelist.NoteListActivity;
import kr.ac.deu.se.wisenote.vo.notebooks.Notebook;
import kr.ac.deu.se.wisenote.vo.notebooks.NotebookRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MemoActivity extends AppCompatActivity {
  private LinearLayout favorite;
  private LinearLayout recycle;
  ListView listView;
  List<Notebook> notebooks;
  private NotebookService service;
  HamburgerListAdapter adapter;
  private ImageButton hamburger;
  private ImageButton addFolder;
  private Dialog addDialog;
  private Dialog deleteDialog;
  private Button cancel;
  private Button create;
  private EditText folderName;
  DrawerLayout drawerLayout;

  private String[] titles = new String[]{"Main","Text","Memo"};
  private String token;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_memo);

    // token 정보 가져오기
    SharedPreferences sharedPref = getSharedPreferences("wisenote", Context.MODE_PRIVATE);
    token = sharedPref.getString("token", null);

    ViewPager2 viewPager = findViewById(R.id.view_pager_memo);
    viewPager.setOffscreenPageLimit(3);

    Fragment memoFragment = new MemoFragment();
    Fragment textFragment = new TextFragment();
    Fragment mainFragment = new MainFragment();

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

    addDialog = new Dialog(MemoActivity.this);
    addDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    addDialog.setContentView(R.layout.hamburger_add_dialog);
    deleteDialog = new Dialog(MemoActivity.this);
    deleteDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    deleteDialog.setContentView(R.layout.hamburger_delete_dialog);

    //햄버거메뉴 나오기
    hamburger = findViewById(R.id.hamButton);
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
    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
    startActivity(intent);
  };

  // Hamburger Menu 나오기
  @SuppressLint("RtlHardcoded")
  private final View.OnClickListener hamburgerMenu = view -> {
    DrawerLayout drawerLayout = findViewById(R.id.memo_draw);
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
    cancel = addDialog.findViewById(R.id.cancel);
    create = addDialog.findViewById(R.id.create);


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
