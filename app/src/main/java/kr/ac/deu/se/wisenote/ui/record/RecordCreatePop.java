//package kr.ac.deu.se.wisenote.ui.record;
//
//import android.app.Dialog;
//import android.content.Context;
//import android.content.Intent;
//
//public class RecordCreatePop{
//  private Dialog context;
////  Intent intent = getIntent();
////  String id=intent.getStringExtra("아이디");
//  public static RecordCreatePop recordCreatePop;
//  public RecordCreatePop(Dialog context){
//    this.context = context;
//  }
//  String[] facilityList = {
//    "공급배관", "사용자배관", "공급밸브", "정압기", "테스트박스",
//    "로케이트박스", "정류기", "배류기", "수취기", "검지공"
//  };
//  public static RecordCreatePop getInstance(Dialog context){
//    recordCreatePop = new RecordCreatePop(context);
//
//    return recordCreatePop;
//  }
//
//  public  void showDefaultDialog(){
////    final Dialog dialog = new Dialog(context);
////    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
////    dialog.setContentView(R.layout.recording_create);
//    dialog.show();
//
//    TextView textView = (TextView)dialog.findViewById(R.id.note_title);
//    Spinner spinner = (Spinner) dialog.findViewById(R.id.note_content);
//    String tv = textView.getText().toString();
//
//    ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.layout.recording_create,facilityList);
//    String sp = spinner.getSelectedItem().toString();
//
//    Retrofit retrofit = new Retrofit.Builder()
//      .baseUrl("http://13.125.179.157/")
//      .addConverterFactory(GsonConverterFactory.create())
//      .build();
//    RetrofitService service = retrofit.create(RetrofitService.class);
//    dialog.findViewById(R.id.record_ok).setOnClickListener(new View.OnClickListener() {
//      @Override
//      public void onClick(View v) {
//        Retrofit retrofit = new Retrofit.Builder()
//          .baseUrl("http://13.125.179.157/")
//          .addConverterFactory(GsonConverterFactory.create())
//          .build();
//        RetrofitService service = retrofit.create(RetrofitService.class);
//        Call<NotePost> call = service.createNote("a",tv,sp,"string",true,"string","0","0");
//        call.enqueue(new Callback<NotePost>() {
//
//          @Override
//          public void onResponse(Call<NotePost> call, Response<NotePost> response) {
////            Log.d("asdf","auth: 성공\n"+ id);
//            Log.d("asdf","auth: 성공\n");
//          }
//
//          @Override
//          public void onFailure(Call<NotePost> call, Throwable t) {
////            Log.d("asdf","auth: 실패\n"+ id);
//            Log.d("asdf","auth: 실패\n");
//          }
//        });
//      }
//    });
//  }
//}
////public class RecordCreatePop extends AppCompatActivity {
////  public void callRecordPop(){
////    setContentView(R.layout.recording_create);
////
////    TextView textView = (TextView)findViewById(R.id.note_title);
////    Spinner spinner = (Spinner) findViewById(R.id.note_content);
////    String tv = textView.getText().toString();
////    String sp = spinner.getSelectedItem().toString();
////
////    Intent intent = getIntent();
////    String id=intent.getStringExtra("아이디");
////    Toast.makeText(RecordCreatePop.this,"abc",Toast.LENGTH_SHORT).show();
////    findViewById(R.id.record_ok).setOnClickListener(new View.OnClickListener() {
////      @Override
////      public void onClick(View v) {
////        Retrofit retrofit = new Retrofit.Builder()
////          .baseUrl("http://13.125.179.157/")
////          .addConverterFactory(GsonConverterFactory.create())
////          .build();
////        RetrofitService service = retrofit.create(RetrofitService.class);
////        Call<NotePost> call = service.createNote(id,tv,sp,"string",true,"string","0","0");
////        call.enqueue(new Callback<NotePost>() {
////
////          @Override
////          public void onResponse(Call<NotePost> call, Response<NotePost> response) {
////            Log.d("asdf","auth: 성공\n"+ id);
////            Toast.makeText(RecordCreatePop.this,"abcd",Toast.LENGTH_SHORT).show();
////          }
////
////          @Override
////          public void onFailure(Call<NotePost> call, Throwable t) {
////            Log.d("asdf","auth: 실패\n"+ id);
////            Toast.makeText(RecordCreatePop.this,"abce",Toast.LENGTH_SHORT).show();
////          }
////        });
////      }
////    });
////  }
////}
////public class RecordCreatePop extends AppCompatActivity {
////    String[] facilityList = {
////    "공급배관", "사용자배관", "공급밸브", "정압기", "테스트박스",
////    "로케이트박스", "정류기", "배류기", "수취기", "검지공"
////  };
////  @Override
////  protected void onCreate(@Nullable Bundle savedInstanceState) {
////    super.onCreate(savedInstanceState);
////    setContentView(R.layout.recording_create);
////    TextView textView = (TextView)findViewById(R.id.note_title);
////    Spinner spinner = (Spinner) findViewById(R.id.note_content);
////    String tv = textView.getText().toString();
////    spinner.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) RecordCreatePop.this);
////    ArrayAdapter<String> adapter = new ArrayAdapter<String>(RecordCreatePop.this, R.layout.recording_create, facilityList);
////    spinner.setAdapter(adapter);
////    Toast.makeText(RecordCreatePop.this,"성공",Toast.LENGTH_SHORT).show();
////    String sp = spinner.getSelectedItem().toString();
//
////    Intent intent = getIntent();
////    String id=intent.getStringExtra("아이디");
////    Toast.makeText(RecordCreatePop.this,"abc",Toast.LENGTH_SHORT).show();
////    findViewById(R.id.record_ok).setOnClickListener(new View.OnClickListener() {
////      @Override
////      public void onClick(View v) {
////        Retrofit retrofit = new Retrofit.Builder()
////          .baseUrl("http://13.125.179.157/")
////          .addConverterFactory(GsonConverterFactory.create())
////          .build();
////        RetrofitService service = retrofit.create(RetrofitService.class);
////        Call<NotePost> call = service.createNote(id,tv,sp,"string",true,"string","0","0");
////        call.enqueue(new Callback<NotePost>() {
////
////          @Override
////          public void onResponse(Call<NotePost> call, Response<NotePost> response) {
////            Log.d("asdf","auth: 성공\n"+ id);
////            Toast.makeText(RecordCreatePop.this,"abcd",Toast.LENGTH_SHORT).show();
////          }
////
////          @Override
////          public void onFailure(Call<NotePost> call, Throwable t) {
////            Log.d("asdf","auth: 실패\n"+ id);
////            Toast.makeText(RecordCreatePop.this,"abce",Toast.LENGTH_SHORT).show();
////          }
////        });
////      }
////    });
////  }
////
////}
