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

import kr.ac.deu.se.wisenote.R;
import kr.ac.deu.se.wisenote.StartActivity;
import kr.ac.deu.se.wisenote.service.RetrofitClient;
import kr.ac.deu.se.wisenote.service.ServiceApi;
import kr.ac.deu.se.wisenote.vo.signup.SignupRequest;
import kr.ac.deu.se.wisenote.vo.signup.SignupResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity {

  private EditText signupId;
  private EditText signupPassword;
  private EditText signupCheckPassword;
  private Button signupButton;
  private ServiceApi service;
  private SignupRequest request;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_signup);

    signupId = (EditText) findViewById(R.id.signupId);
    signupPassword = (EditText) findViewById(R.id.signupPassword);
    signupCheckPassword = (EditText) findViewById(R.id.signupCheckPassword);
    signupButton = (Button) findViewById(R.id.signupButton);
    service = RetrofitClient.getClient().create(ServiceApi.class);
    signupButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        check(signupId, signupPassword, signupCheckPassword);
      }
    });
  }

  private void check(EditText signupId, EditText signupPassword,EditText signupCheckPassword){
    String id = signupId.getText().toString();
    String password = signupPassword.getText().toString();
    String check = signupCheckPassword.getText().toString();

    if(password.equals(check)){
      request = new SignupRequest(id,password);
      signUp(request);

    }else{
      Toast.makeText(this,"비밀번호가 일치하지 않습니다.",Toast.LENGTH_SHORT).show();
    }
  }

  public void signUp(SignupRequest request){
    service.userSignup(request).enqueue(new Callback<SignupResponse>() {
      @Override
      public void onResponse(Call<SignupResponse> call, Response<SignupResponse> response) {

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
