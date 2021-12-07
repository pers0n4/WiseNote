package kr.ac.deu.se.wisenote.ui.note;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import kr.ac.deu.se.wisenote.R;

public class NoteActivity extends AppCompatActivity {

  boolean Main_toggle=true;
  boolean Text_toggle=true;
  boolean Memo_toggle=true;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_memo);
  }

  public void Main_Focus(View v){
    TextView textView1 = (TextView)findViewById(R.id.Main_Toggle);
    TextView textView2 = (TextView)findViewById(R.id.Text_Toggle);
    TextView textView3 = (TextView)findViewById(R.id.Memo_Toggle);
    TextView Summary_t = (TextView)findViewById(R.id.sum_text);
    TextView Voca_t = (TextView)findViewById(R.id.voca_text);
    TextView textView4 = (TextView)findViewById(R.id.text_text1);
    TextView textView5 = (TextView)findViewById(R.id.text_text2);
    TextView only_text = (TextView)findViewById(R.id.only_text);
    TextView memo_text = (TextView)findViewById(R.id.memo_text);
    if(Main_toggle==true){
      textView1.setTextColor(Color.parseColor("#F5C428"));
      textView2.setTextColor(Color.BLACK);
      textView3.setTextColor(Color.BLACK);
      Main_toggle=false;
      Summary_t.setVisibility(View.VISIBLE);
      Voca_t.setVisibility(View.VISIBLE);
      textView4.setVisibility(View.VISIBLE);
      textView5.setVisibility(View.VISIBLE);
      only_text.setVisibility(View.INVISIBLE);
      memo_text.setVisibility(View.INVISIBLE);
    }else{
      textView1.setTypeface(textView1.getTypeface(), Typeface.NORMAL);
      Main_toggle=true;

    }
  }
  public void Text_Focus(View v){
    TextView textView1 = (TextView)findViewById(R.id.Main_Toggle);
    TextView textView2 = (TextView)findViewById(R.id.Text_Toggle);
    TextView textView3 = (TextView)findViewById(R.id.Memo_Toggle);
    TextView Summary_t = (TextView)findViewById(R.id.sum_text);
    TextView Voca_t = (TextView)findViewById(R.id.voca_text);
    TextView textView4 = (TextView)findViewById(R.id.text_text1);
    TextView textView5 = (TextView)findViewById(R.id.text_text2);
    TextView only_text = (TextView)findViewById(R.id.only_text);
    TextView memo_text = (TextView)findViewById(R.id.memo_text);

    if(Text_toggle==true){
      textView1.setTextColor(Color.BLACK);
      textView2.setTextColor(Color.parseColor("#F5C428"));
      textView3.setTextColor(Color.BLACK);
      Text_toggle=false;
      Summary_t.setVisibility(View.INVISIBLE);
      Voca_t.setVisibility(View.INVISIBLE);
      textView4.setVisibility(View.INVISIBLE);
      textView5.setVisibility(View.INVISIBLE);
      only_text.setVisibility(View.VISIBLE);
      memo_text.setVisibility(View.INVISIBLE);
    }else{
      textView2.setTypeface(textView2.getTypeface(), Typeface.NORMAL);
      Text_toggle=true;
      Log.i("abc", "F");
    }
  }
  public void Memo_Focus(View v){
    TextView textView1 = (TextView)findViewById(R.id.Main_Toggle);
    TextView textView2 = (TextView)findViewById(R.id.Text_Toggle);
    TextView textView3 = (TextView)findViewById(R.id.Memo_Toggle);
    TextView Summary_t = (TextView)findViewById(R.id.sum_text);
    TextView Voca_t = (TextView)findViewById(R.id.voca_text);
    TextView textView4 = (TextView)findViewById(R.id.text_text1);
    TextView textView5 = (TextView)findViewById(R.id.text_text2);
    TextView only_text = (TextView)findViewById(R.id.only_text);
    TextView memo_text = (TextView)findViewById(R.id.memo_text);

    if(Memo_toggle==true){
      textView1.setTextColor(Color.BLACK);
      textView2.setTextColor(Color.BLACK);
      textView3.setTextColor(Color.parseColor("#F5C428"));
      Memo_toggle=false;
      Summary_t.setVisibility(View.INVISIBLE);
      Voca_t.setVisibility(View.INVISIBLE);
      textView4.setVisibility(View.INVISIBLE);
      textView5.setVisibility(View.INVISIBLE);
      only_text.setVisibility(View.INVISIBLE);
      memo_text.setVisibility(View.VISIBLE);
      Log.i("abc", "T");
    }else{
      textView3.setTypeface(textView3.getTypeface(), Typeface.NORMAL);
      Memo_toggle=true;
      Log.i("abc", "F");
    }
  }
}
