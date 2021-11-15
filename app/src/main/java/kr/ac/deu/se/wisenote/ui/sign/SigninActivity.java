package kr.ac.deu.se.wisenote.ui.sign;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import kr.ac.deu.se.wisenote.R;
import kr.ac.deu.se.wisenote.service.ServiceApi;
import kr.ac.deu.se.wisenote.service.ServiceGenerator;
import kr.ac.deu.se.wisenote.ui.home.HomeActivity;
import kr.ac.deu.se.wisenote.vo.signin.SigninResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SigninActivity extends AppCompatActivity{
  private ServiceApi service;
  private EditText signinId;
  private EditText signinPassword;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_signin);

    signinId = findViewById(R.id.signinId);
    signinPassword = findViewById(R.id.signinPassword);
    Button signinButton = findViewById(R.id.signinButton);
    service = ServiceGenerator.createService(ServiceApi.class);

    signinButton.setOnClickListener(view -> {
      Log.d("test ","버큰클릭 확인");
      check(signinId,signinPassword);
    });
  }

  public void check(EditText signinId,EditText signinPassword){
    String id = signinId.getText().toString();
    String password = signinPassword.getText().toString();
    if(id.isEmpty() || password.isEmpty()){
      Toast.makeText(this, "빈칸을 입력하세요", Toast.LENGTH_SHORT).show();
    }else{
      Log.d("test","check()메소드 실행"+id);
      signIn(id,password);
    }
  }


  public void signIn(String id, String password){
    service.userSignin(id,password).enqueue(new Callback<SigninResponse>() {
      @Override
      public void onResponse(@NonNull Call<SigninResponse> call, @NonNull Response<SigninResponse> response) {
        Log.d("test","연결성공"+response.code());
        if(response.code() == 200){
          SigninResponse result = response.body();
          result.setCode(response.code());
          Log.d("test","token:"+result.getAccess_token()+"code:"+result.getCode());
          Intent intent = new Intent(SigninActivity.this, HomeActivity.class);
          startActivity(intent);
        }else if(response.code() == 401){
          Toast.makeText(SigninActivity.this,"ID 또는 PASSWORD 를 잘못입력하셨습니다.",Toast.LENGTH_SHORT).show();
        }
      }

      @Override
      public void onFailure(@NonNull Call<SigninResponse> call, @NonNull Throwable t) {
        t.printStackTrace();
      }
    });

  }
}