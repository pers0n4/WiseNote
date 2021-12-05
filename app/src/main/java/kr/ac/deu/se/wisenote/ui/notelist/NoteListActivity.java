package kr.ac.deu.se.wisenote.ui.notelist;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import kr.ac.deu.se.wisenote.R;
import kr.ac.deu.se.wisenote.service.NoteService;
import kr.ac.deu.se.wisenote.service.ServiceGenerator;
import kr.ac.deu.se.wisenote.ui.home.HomeActivity;
import kr.ac.deu.se.wisenote.vo.note.Note;
import lombok.SneakyThrows;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NoteListActivity extends AppCompatActivity {
  private NoteListViewAdapter adapter;
  private TextView tv_title;
  private TextView tv_description;
  private Intent intent;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_note_list);

    tv_title = (TextView) findViewById(R.id.notebook_title);
    tv_description = (TextView) findViewById(R.id.notebook_description);

    adapter = new NoteListViewAdapter();
    ListView listView = findViewById(R.id.note_list_view);
    ArrayList<Note> itemList = new ArrayList<>();

    // token 정보 가져오기
    SharedPreferences sharedPref = getSharedPreferences("wisenote", Context.MODE_PRIVATE);
    String token = sharedPref.getString("token", null);

    // 전달 정보에 따른 노트 생성
    intent = getIntent();
    if (!TextUtils.isEmpty(intent.getStringExtra("NotebookTitle"))) {
      Log.d("check", "get notebook data");
    } else {
      setTextView("All Notes", "Every notes you wrote.");
      // 모든 노트에 대한 목록 생성
      // service 생성
      NoteService service = ServiceGenerator.createService(NoteService.class, token);
      Call<List<Note>> note = service.getNotes();
      note.enqueue(new Callback<List<Note>>() {
        @SneakyThrows
        @Override
        public void onResponse(Call<List<Note>> call, Response<List<Note>> response) {
          if(response.isSuccessful()) {
            List<Note> notes = response.body();
            itemList.addAll(notes);
            adapter.replace(itemList);
            adapter.notifyDataSetChanged();
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

    listView.setAdapter(adapter);

    // 하단 navigation button 구현
    ImageButton home_button = (ImageButton) findViewById(R.id.home_button);
    home_button.setOnClickListener(homeClickListener);
  }

  private void setTextView(String title, String description) {
    tv_title.setText(title);
    tv_description.setText(description);
  }

  private View.OnClickListener homeClickListener = view -> {
    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
    startActivity(intent);
  };
}
