package kr.ac.deu.se.wisenote.ui.mypage;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import kr.ac.deu.se.wisenote.R;
import kr.ac.deu.se.wisenote.service.RetrofitService;
import kr.ac.deu.se.wisenote.vo.token.TokenGet;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MyPageActivity extends AppCompatActivity {
  TokenGet tokenGetreulst;
  String token;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_mypage);
    TextView textView =(TextView)findViewById(R.id.NameId);
    String resultText ="[NULL]";
//    TextView textView2 =(TextView)findViewById(R.id.PwId);
    TextView textView3 =(TextView)findViewById(R.id.EmailId);
    TextView textView4 =(TextView)findViewById(R.id.PhoneId);
    TextView textView5 =(TextView)findViewById(R.id.NId);

    Intent intent = getIntent();
    token=intent.getStringExtra("token");

    Retrofit retrofit = new Retrofit.Builder()
      .baseUrl("http://13.125.179.157/")
      .addConverterFactory(GsonConverterFactory.create())
      .build();

    RetrofitService retrofitService = retrofit.create(RetrofitService.class);
    Call<TokenGet> tokenGetCall = retrofitService.TestToken("Bearer "+token);
    tokenGetCall.enqueue(new Callback<TokenGet>() {
      @Override
      public void onResponse(Call<TokenGet> call, Response<TokenGet> response) {
        if(response.isSuccessful()){
          tokenGetreulst = response.body();
          Log.d("zz","onResponse: 성공, 결과\n"+tokenGetreulst.toString());
          textView.setText(tokenGetreulst.getName());
          textView3.setText(tokenGetreulst.getEmail());
          textView4.setText(tokenGetreulst.getPhone());
          textView5.setText(tokenGetreulst.getName());
        }else{
          Log.d("zz","onResponse: 실패"+response.code());
        }
      }

      @Override
      public void onFailure(Call<TokenGet> call, Throwable t) {
        Log.d("zz","onResponse: 인터넷문제");
        t.printStackTrace();
      }
    });

//    RetrofitService service = retrofit.create(RetrofitService.class);
//    Call<PostResult> call = service.getPosts(id);
//    call.enqueue(new Callback<PostResult>() {
//      @Override
//      public void onResponse(Call<PostResult> call, Response<PostResult> response) {
//        Log.d("hi","onResponse: 1233");
//        if(response.isSuccessful()){
//          PostResult result = response.body();
//          Log.d("hi","onResponse: 성공, 결과\n"+result.toString());
//          textView.setText(result.getName());
//          textView3.setText(result.getEmail());
//          textView4.setText(result.getPhone());
//          textView5.setText(result.getName());
//        }else{
//          Log.d("hi","onResponse: 실패"+response.code());
//        }
//      }
//
//      @Override
//      public void onFailure(Call<PostResult> call, Throwable t) {
//        Log.d("hi","onResponse: 인터넷문제");
//        t.printStackTrace();
//      }
//    });
  }

  private void deleteToken() {
    Retrofit retrofit = new Retrofit.Builder()
      .baseUrl("http://13.125.179.157/")
      .addConverterFactory(GsonConverterFactory.create())
      .build();
    RetrofitService service = retrofit.create(RetrofitService.class);
    Log.d("hiqq","delete: 실패, 결과\n"+token+" "+tokenGetreulst.getId());
    Call<Void> call2 = service.deleteToken("Bearer "+token,tokenGetreulst.getId());
    call2.enqueue(new Callback<Void>() {
      @Override
      public void onResponse(Call<Void> call2, Response<Void> response) {
        Toast.makeText(getApplicationContext(), "리스폰스", Toast.LENGTH_SHORT).show();

        if (!response.isSuccessful()) {
          Log.d("hiqq","delete: 실패, 결과\n"+response.message()+"//"+response.code());
          return ;
        }
        if(response.code()==204)
          Log.d("hiqq","delete2: \n");
//        testToken(postResponse.getAccess_token());
      }

      @Override
      public void onFailure(Call<Void> call2, Throwable t) {
        Toast.makeText(getApplicationContext(), "삭제했데~", Toast.LENGTH_SHORT).show();
        Log.d("hiqq","delete: 삭제성공~//"+t.toString()+"//");
      }
    });
//    Call<TokenPost> call = service.createToken("user@example.com","string");
//    call.enqueue(new Callback<TokenPost>() {
//      @Override
//      public void onResponse(Call<TokenPost> call, Response<TokenPost> response) {
//        if (!response.isSuccessful()) {
//          Log.d("hi","auth: 실패, 결과\n"+response.toString());
//          return;
//        }
//        TokenPost postResponse = response.body();
//        Log.d("hi","delete1: 성공, 결과\n"+postResponse.getAccess_token());
//      }
//
//      @Override
//      public void onFailure(Call<TokenPost> call, Throwable t) {
//        Log.d("hi","auth: 인터넷문제");
//        t.printStackTrace();
//      }
//    });
  }

  public void logout_btn(View v){
    AlertDialog.Builder ad = new AlertDialog.Builder(MyPageActivity.this);
    ad.setIcon(R.mipmap.ic_launcher);
    ad.setMessage("정말로 로그아웃 하시겠습니까?");

    ad.setPositiveButton("네", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) { ;
        dialog.dismiss();
        Intent intent = new Intent(kr.ac.deu.se.wisenote.ui.mypage.MyPageActivity.this, kr.ac.deu.se.wisenote.ui.sign.SigninActivity.class);
        startActivity(intent);
      }
    });

    ad.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        dialog.dismiss();
      }
    });
    ad.show();
  }
  public void delete_btn(View v){
    AlertDialog.Builder ad = new AlertDialog.Builder(MyPageActivity.this);
    ad.setIcon(R.mipmap.ic_launcher);
    ad.setMessage("정말로 계정을 삭제 하시겠습니까?");

    ad.setPositiveButton("네", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) { ;
        dialog.dismiss();
        deleteToken();
        Intent intent = new Intent(kr.ac.deu.se.wisenote.ui.mypage.MyPageActivity.this, kr.ac.deu.se.wisenote.ui.sign.SigninActivity.class);
        startActivity(intent);
      }
    });

    ad.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        dialog.dismiss();
      }
    });
    ad.show();
  }

  public void change_btn(View v){
    AlertDialog.Builder ad = new AlertDialog.Builder(MyPageActivity.this);
    ad.setIcon(R.mipmap.ic_launcher);
    ad.setTitle("비밀번호 변경");
    ad.setMessage("변경할 비밀번호를 입력해주세요");

    final EditText et = new EditText(MyPageActivity.this);
    ad.setView(et);

    ad.setPositiveButton("변경", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) { ;
        dialog.dismiss();
      }
    });

    ad.setNegativeButton("취소", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        dialog.dismiss();
      }
    });
    ad.show();
  }


}
