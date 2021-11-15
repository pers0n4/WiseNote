package kr.ac.deu.se.wisenote.ui.sign;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import kr.ac.deu.se.wisenote.MainActivity;
import kr.ac.deu.se.wisenote.R;
import kr.ac.deu.se.wisenote.service.RetrofitClient;
import kr.ac.deu.se.wisenote.service.ServiceApi;
import kr.ac.deu.se.wisenote.vo.signin.SigninResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SigninActivity extends AppCompatActivity{
  private ServiceApi service;
  private EditText signinId;
  private EditText signinPassword;
  private Button signinButton;
  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);


    setContentView(R.layout.activity_signin);

    signinId = (EditText)findViewById(R.id.signinId);
    signinPassword = (EditText)findViewById(R.id.signinPassword);
    signinButton = (Button) findViewById(R.id.signinButton);
    service = RetrofitClient.getClient().create(ServiceApi.class);


    signinButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Log.d("test ","버큰클릭 확인");
        check(signinId,signinPassword);
      }
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
      public void onResponse(Call<SigninResponse> call, Response<SigninResponse> response) {
        Log.d("test","연결성공"+response.code());
        if(response.code() == 200){
          SigninResponse result = response.body();
          result.setCode(response.code());
          Log.d("test","token:"+result.getAccess_token()+"code:"+result.getCode());
          Intent intent = new Intent(SigninActivity.this, MainActivity.class);
          startActivity(intent);
        }else if(response.code() == 401){
          Toast.makeText(SigninActivity.this,"ID 또는 PASSOWRD를 잘못입력하셨습니다.",Toast.LENGTH_SHORT).show();
        }
      }

      @Override
      public void onFailure(Call<SigninResponse> call, Throwable t) {
        t.printStackTrace();
      }
    });

  }
}
