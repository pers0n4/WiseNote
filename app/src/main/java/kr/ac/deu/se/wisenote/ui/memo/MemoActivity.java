package kr.ac.deu.se.wisenote.ui.memo;

import android.app.Dialog;
import android.content.Intent;
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
import android.widget.Toast;

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

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_memo);

    Intent intent = getIntent();
    String auth_token = intent.getStringExtra("token");
    service = ServiceGenerator.createService(NotebookService.class,auth_token);
    listView = (ListView) findViewById(R.id.listview);
    getData(auth_token);

    addDialog = new Dialog(MemoActivity.this);
    addDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    addDialog.setContentView(R.layout.hamburger_add_dialog);
    deleteDialog = new Dialog(MemoActivity.this);
    deleteDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    deleteDialog.setContentView(R.layout.hamburger_delete_dialog);

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

    //햄버거메뉴 나오기
    hamburger = (ImageButton) findViewById(R.id.hamButton);
    hamburger.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        drawerLayout = (DrawerLayout) findViewById(R.id.memo_draw);
        if (!drawerLayout.isDrawerOpen(Gravity.LEFT)) {
          getData(auth_token);
          drawerLayout.openDrawer(Gravity.LEFT);
        }
      }
    });
    // 햄버거 메뉴 플러스 버튼
    addFolder = (ImageButton) findViewById(R.id.addFolder);
    addFolder.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        addDialog(auth_token);
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
        deleteDialog(i,auth_token);
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
      }
      @Override
      public void onFailure(Call<List<Notebook>> call, Throwable t) {

      }
    });
  }

}
