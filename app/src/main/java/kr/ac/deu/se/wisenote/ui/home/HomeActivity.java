package kr.ac.deu.se.wisenote.ui.home;

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
import android.widget.TextView;

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
import kr.ac.deu.se.wisenote.ui.notelist.NoteListActivity;
import kr.ac.deu.se.wisenote.ui.record.RecordActivity;
import kr.ac.deu.se.wisenote.vo.notebooks.Notebook;
import kr.ac.deu.se.wisenote.vo.notebooks.NotebookRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {
  private ListView listView;
  private List<Notebook> notebooks;
  private NotebookService service;
  private HamburgerListAdapter adapter;
  private Dialog addDialog;
  private Dialog deleteDialog;
  private EditText folderName;
  private DrawerLayout drawerLayout;

  private final String[] titles = new String[]{"Favorite", "Recent", "Map"};
  private String token;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home);

    // token 정보 가져오기
    SharedPreferences sharedPref = getSharedPreferences("wisenote", Context.MODE_PRIVATE);
    token = sharedPref.getString("token", null);

    // 하단 navigation button 구현
    ImageButton home_button = findViewById(R.id.home_button);
    home_button.setOnClickListener(homeClickListener);
    ImageButton my_page_button = findViewById(R.id.mypage_button);
    my_page_button.setOnClickListener(myPageClickListener);
    ImageButton record_button = findViewById(R.id.Record_btn);
    record_button.setOnClickListener(recordClickListener);

    // 탭 뷰 옆 모든 노트 목록 확인 버튼
    TextView tv_notelist = findViewById(R.id.note_list_button);
    tv_notelist.setOnClickListener(noteListClickListener);

    // ViewPager 설정
    ViewPager2 viewPager = findViewById(R.id.view_pager);
    viewPager.setOffscreenPageLimit(3);

    // Fragment 생성
    Fragment favoriteFragment = new FavoriteFragment(token);
    Fragment recentFragment = new RecentFragment(token);
    Fragment mapFragment = new MapFragment();

    // ViewPagerAdapter 를 이용하여 Fragment 연결
    ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this);
    viewPagerAdapter.addFragment(favoriteFragment);
    viewPagerAdapter.addFragment(recentFragment);
    viewPagerAdapter.addFragment(mapFragment);
    viewPager.setAdapter(viewPagerAdapter);

    // TabLayout 에 ViewPager 연결
    TabLayout tabLayout = findViewById(R.id.tabs);
    new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> tab.setText(titles[position])).attach();

    service = ServiceGenerator.createService(NotebookService.class,token);
    listView = findViewById(R.id.listview);
    getData(token);
    //dialog 초기화
    addDialog = new Dialog(HomeActivity.this);
    addDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    addDialog.setContentView(R.layout.hamburger_add_dialog);
    deleteDialog = new Dialog(HomeActivity.this);
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
    Intent intent = new Intent(kr.ac.deu.se.wisenote.ui.home.HomeActivity.this, kr.ac.deu.se.wisenote.ui.mypage.MyPageActivity.class);
    intent.putExtra("token",token);
    startActivity(intent);
  };
  // Bottom Menu record Button Click Event
  private final View.OnClickListener recordClickListener = view -> {
    Intent intent = new Intent(kr.ac.deu.se.wisenote.ui.home.HomeActivity.this, RecordActivity.class);
    intent.putExtra("token",token);
    startActivity(intent);
  };

  // Hamburger Menu 나오기
  @SuppressLint("RtlHardcoded")
  private final View.OnClickListener hamburgerMenu = view -> {
    drawerLayout = findViewById(R.id.home_draw);
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
    return true;
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
      drawerLayout.closeDrawer(Gravity.LEFT);
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
