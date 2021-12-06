package kr.ac.deu.se.wisenote.ui.notelist;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import kr.ac.deu.se.wisenote.R;
import kr.ac.deu.se.wisenote.ui.home.HomeActivity;

public class NoteActivity extends AppCompatActivity {
  private ImageButton home_button;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_note_list);

    // 하단 navigation button 구현
    home_button = (ImageButton) findViewById(R.id.home_button);
    home_button.setOnClickListener(homeClickListener);
  }

  private View.OnClickListener homeClickListener = view -> {
    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
    startActivity(intent);
  };
}
