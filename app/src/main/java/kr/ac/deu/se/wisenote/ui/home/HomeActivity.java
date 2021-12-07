package kr.ac.deu.se.wisenote.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import kr.ac.deu.se.wisenote.R;
import kr.ac.deu.se.wisenote.ui.record.RecordActivity;

public class HomeActivity extends AppCompatActivity {
  private String[] titles = new String[]{"Favorite", "Recent", "Map"};
  String id;
  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home);

    // ViewPager 설정
    ViewPager2 viewPager = findViewById(R.id.view_pager);
    viewPager.setOffscreenPageLimit(3);

    // Fragment 생성
    Fragment favoriteFragment = new FavoriteFragment();
    Fragment recentFragment = new RecentFragment();
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

    Intent intent = getIntent();
    id=intent.getStringExtra("아이디");

    findViewById(R.id.MyPage_btn).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(kr.ac.deu.se.wisenote.ui.home.HomeActivity.this, kr.ac.deu.se.wisenote.ui.mypage.MyPageActivity.class);
        intent.putExtra("아이디",id);
        startActivity(intent);
      }
    });
    findViewById(R.id.Record_btn).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(kr.ac.deu.se.wisenote.ui.home.HomeActivity.this, RecordActivity.class);
        intent.putExtra("아이디",id);
        startActivity(intent);
      }
    });
  }
}
