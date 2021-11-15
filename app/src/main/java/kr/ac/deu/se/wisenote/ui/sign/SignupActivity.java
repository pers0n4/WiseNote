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
import kr.ac.deu.se.wisenote.StartActivity;
import kr.ac.deu.se.wisenote.service.ServiceApi;
import kr.ac.deu.se.wisenote.service.ServiceGenerator;
import kr.ac.deu.se.wisenote.vo.signup.SignupRequest;
import kr.ac.deu.se.wisenote.vo.signup.SignupResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity {

  private EditText signupId;
  private EditText signupPassword;
  private EditText signupCheckPassword;
  private ServiceApi service;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_signup);

    signupId = findViewById(R.id.signupId);
    signupPassword = findViewById(R.id.signupPassword);
    signupCheckPassword = findViewById(R.id.signupCheckPassword);
    Button signupButton = findViewById(R.id.signupButton);
    service = ServiceGenerator.createService(ServiceApi.class);

    signupButton.setOnClickListener(view -> check(signupId, signupPassword, signupCheckPassword));
  }

  private void check(EditText signupId, EditText signupPassword,EditText signupCheckPassword){
    String id = signupId.getText().toString();
    String password = signupPassword.getText().toString();
    String check = signupCheckPassword.getText().toString();

    if(password.equals(check)){
      SignupRequest request = new SignupRequest(id, password);
      signUp(request);

    }else{
      Toast.makeText(this,"비밀번호가 일치하지 않습니다.",Toast.LENGTH_SHORT).show();
    }
  }

  public void signUp(SignupRequest request){
    service.userSignup(request).enqueue(new Callback<SignupResponse>() {
      @Override
      public void onResponse(@NonNull Call<SignupResponse> call, @NonNull Response<SignupResponse> response) {

        if(response.code() == 201){
          SignupResponse result = response.body();
          Log.d("Post ","onResponse 성공 결과:"+result.toString());
          Intent intent = new Intent(SignupActivity.this, StartActivity.class);
          startActivity(intent);
        }else{
          Toast.makeText(SignupActivity.this, "ID 형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show();
          Log.d("Post","onResponse 실패:"+response.code());
        }
      }
      @Override
      public void onFailure(Call<SignupResponse> call, Throwable t) {
        Log.d("post",t.getMessage());
        t.printStackTrace();
      }
    });
  }

}
