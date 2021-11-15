package kr.ac.deu.se.wisenote.ui.home;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import kr.ac.deu.se.wisenote.R;

public class HomeActivity extends AppCompatActivity {
  private String[] titles = new String[]{"Favorite", "Recent", "Map"};

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home);

    // ViewPager 설정
    ViewPager2 viewPager = findViewById(R.id.view_pager);
    viewPager.setOffscreenPageLimit(3);

    // Fragment 생성
    FavoriteFragment favoriteFragment = new FavoriteFragment();
    RecentFragment recentFragment = new RecentFragment();
    MapFragment mapFragment = new MapFragment();

    // ViewPagerAdapter 를 이용하여 Fragment 연결
    ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this);
    viewPagerAdapter.addFragment(favoriteFragment);
    viewPagerAdapter.addFragment(recentFragment);
    viewPagerAdapter.addFragment(mapFragment);
    viewPager.setAdapter(viewPagerAdapter);

    // TabLayout 에 ViewPager 연결
    TabLayout tabLayout = findViewById(R.id.tabs);
    new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> tab.setText(titles[position])).attach();
  }
}
