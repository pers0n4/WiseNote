package kr.ac.deu.se.wisenote;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import kr.ac.deu.se.wisenote.ui.sign.SigninActivity;
import kr.ac.deu.se.wisenote.ui.sign.SignupActivity;

public class StartActivity extends AppCompatActivity {


  private Button goSignin;
  private Button goSignup;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_start);

    goSignup = (Button) findViewById(R.id.goSignup);
    goSignin = (Button) findViewById(R.id.goSignin);
    goSignin.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent(StartActivity.this, SigninActivity.class);
        startActivity(intent);
      }
    });

    goSignup.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent(StartActivity.this, SignupActivity.class);
        startActivity(intent);
      }
    });

  }

}
