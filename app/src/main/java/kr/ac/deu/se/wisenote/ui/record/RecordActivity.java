package kr.ac.deu.se.wisenote.ui.record;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import kr.ac.deu.se.wisenote.MainActivity;
import kr.ac.deu.se.wisenote.R;
import kr.ac.deu.se.wisenote.vo.note.NoteCreateResponse;
import kr.ac.deu.se.wisenote.vo.note.NotePost;
import kr.ac.deu.se.wisenote.service.RetrofitService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecordActivity extends AppCompatActivity {
  private static final int PERMISSION_REQUEST_RECORD = 0;
  private final int REQUEST_SPEECH = 1000;
  TextView textView;
  MediaRecorder recorder;
  String filename;
  Long time;
  MediaPlayer player;
  Uri audioUri = null;
  int pause_count = 0;
  public Thread timeThread = null;
  View mainLayout;
  private long pauseOffset;
  boolean i = true;
  long TimeStop;
  Dialog dialog;
  String token, tv, sp;
  double longitude, latitude;
  FusedLocationProviderClient fusedLocationClient;
  LocationListener locationListener;
  LocationManager locationManager;

  public void showDialog(LocationManager lm) {
    dialog.show();

    String[] items = {"All Notes", "abcd"};
    TextView textView = (TextView) dialog.findViewById(R.id.note_title);
    Spinner spinner = (Spinner) dialog.findViewById(R.id.note_content);
    tv = textView.getText().toString();
    ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, items);
    spinner.setAdapter(adapter);
    sp = spinner.getSelectedItem().toString();

//    locationListener = new LocationListener() {
//      @Override
//      public void onLocationChanged(@NonNull Location location) {
//        longitude = location.getLongitude();
//        latitude = location.getLatitude();
//        Log.d("asdf", "auth: ??????~~\n");
//      }
//    };
//    String locationProvider;
//    locationProvider = LocationManager.GPS_PROVIDER;
//    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//      return;
//    }
//    lm.requestLocationUpdates(locationProvider, 1, 1, (android.location.LocationListener) locationListener);
//    locationProvider = LocationManager.NETWORK_PROVIDER;
//    lm.requestLocationUpdates(locationProvider, 1, 1, (android.location.LocationListener) locationListener);


    dialog.findViewById(R.id.record_ok).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Retrofit retrofit = new Retrofit.Builder()
          .baseUrl("http://13.125.179.157/")
          .addConverterFactory(GsonConverterFactory.create())
          .build();
        RetrofitService service = retrofit.create(RetrofitService.class);
        NotePost notePost = new NotePost(tv, sp, "string", true, "string", 0, 0);
        Call<NoteCreateResponse> call = service.createNote("Bearer " + token, notePost);
        call.enqueue(new Callback<NoteCreateResponse>() {
          @Override
          public void onResponse(Call<NoteCreateResponse> call, Response<NoteCreateResponse> response) {
            Log.d("asdf", "auth: ??????\n" + token + " " + tv + " " + sp);
            dialog.dismiss();
          }

          @Override
          public void onFailure(Call<NoteCreateResponse> call, Throwable t) {
            Log.d("asdf", "auth: ??????\n" + token);
          }
        });
      }
    });
  }
  /**
   * ???????????? ????????? ??????
   */
//  private Location getMyLocation() {
//    Location currentLocation = null;
//    // Register the listener with the Location Manager to receive location updates
//    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//      System.out.println("////////////??????????????? ????????? ???????????????");
//      ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},2 );
//      getMyLocation(); //?????? ???????????? ????????? ?????????, ??? ?????? ???????????? ?????? ????????? ??????????????? ????????????!
//
//    }
//    else {
//      System.out.println("////////////???????????? ????????????");
//
//      // ???????????? ?????? ?????????
//      String locationProvider = LocationManager.GPS_PROVIDER;
//
//      locationManager.requestLocationUpdates(locationProvider,1,1, (android.location.LocationListener) locationListener);
//      locationProvider=LocationManager.NETWORK_PROVIDER;
//      locationManager.requestLocationUpdates(locationProvider,1,1, (android.location.LocationListener) locationListener);
//      currentLocation = locationManager.getLastKnownLocation(locationProvider);
//      if (currentLocation != null) {
//        double lng = currentLocation.getLongitude();
//        double lat = currentLocation.getLatitude();
//      }else{
//      }
//    }
//    return currentLocation;
//  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    textView = (TextView) findViewById(R.id.timeText);
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_record);
    layout = findViewById(R.id.recordlayout);
    permissionCheck();
    ImageView imageView = findViewById(R.id.imageView);
    final Chronometer chronometer = (Chronometer) findViewById(R.id.timeText);
    MainActivity mainActivity = new MainActivity();

    fusedLocationClient = LocationServices.getFusedLocationProviderClient(RecordActivity.this);
    Intent intent = getIntent();
    token = intent.getStringExtra("token");
    dialog = new Dialog(RecordActivity.this);       // Dialog ?????????
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // ????????? ??????
    dialog.setContentView(R.layout.recording_create);


//    int REQUEST_CODE_LOCATION = 2;
//    locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
//    Location userLocation = getMyLocation();
//    if(userLocation!=null){
//      latitude=userLocation.getLatitude();
//      longitude=userLocation.getLongitude();
//      Log.d("asdf", "auth: ??????~~44\n"+latitude+" "+longitude);
//    }else{
//      Log.d("asdf", "auth: ??????~~5\n");
//    }
//    public void onStatusChanged(String provider, int status, Bundle extras) { }
//    public void onProviderEnabled(String provider) { }
//    public void onProviderDisabled(String provider) { }


    findViewById(R.id.recordbtn).setOnClickListener(new View.OnClickListener() {
      ImageButton imageButton = findViewById(R.id.recordbtn);

      @Override
      public void onClick(View view) {
        if (i==true){
          imageButton.setImageResource(R.drawable.ic_change_pause);
          imageView.setImageResource(R.drawable.ic_record);
          chronometer.setBase(SystemClock.elapsedRealtime());
          chronometer.start();
          recordAudio();
          i=false;
        }else{
          imageButton.setImageResource(R.drawable.ic_record);
          imageView.setImageResource(R.drawable.ic_change_pause);
          stopRecording();
          chronometer.stop();
          chronometer.setBase(SystemClock.elapsedRealtime());
          i=true;
          showDialog(locationManager);
        }
      }
    });

    findViewById(R.id.close_btn).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        finish();
      }
    });

    findViewById(R.id.pause_btn).setOnClickListener(new View.OnClickListener() {
      @RequiresApi(api = Build.VERSION_CODES.N)
      @Override
      public void onClick(View v) {
        if  (i==false){
          recorder.pause();
          chronometer.stop();
          TimeStop=chronometer.getBase()-SystemClock.elapsedRealtime();
          pause_count++;
          i=true;
        }
        else if (i==true && pause_count>=1){
          recorder.resume();
          chronometer.setBase(SystemClock.elapsedRealtime()+TimeStop);
          chronometer.start();
          i=false;
        }
      }
    });
  }

  private void recordAudio() {
    String recordPath = getExternalFilesDir("/").getAbsolutePath();
    // ?????? ?????? ????????? ?????? ????????? ??????????????? ?????????. ??? ????????? ????????? ???????????? ????????? ?????? ????????? ?????? ???????????? ?????? ??????????????? ???.
    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
    filename = recordPath + "/" +"RecordExample_" + timeStamp + "_"+"audio.mp4";

    recorder = new MediaRecorder();
    recorder.setAudioSource(MediaRecorder.AudioSource.MIC); // ???????????? ?????? ???????????? ?????? ?????????
    recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP); // ?????? ?????? ??????
    recorder.setOutputFile(filename);
    recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

    try {
      recorder.prepare();
      recorder.start();
      time=System.currentTimeMillis();


      Toast.makeText(this, "?????? ?????????.", Toast.LENGTH_SHORT).show();
    } catch (IOException e) {
      e.printStackTrace();
      Toast.makeText(this, "?????? ??????.", Toast.LENGTH_SHORT).show();
    }
  }
  private void stopRecording() {
    // ?????? ?????? ??????
    recorder.stop();
    recorder.release();
    recorder = null;

    // ?????? ??????(String) ?????? Uri??? ???????????? ??????
    //      - Why? : ????????????????????? ???????????? ArrayList??? Uri??? ????????? ??????
    //      - File Path??? ?????? File???  ??????????????? ????????? ????????? ??? ?????? ??????
    audioUri = Uri.parse(filename);
    Log.d("hi",filename);
  }
  public void closePlayer(){
    if(player!=null){
      player.release();
      player=null;
    }
  }
  public void permissionCheck(){
    if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
      || ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
      ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO}, 1);
    }
  }

  private View layout;

  private final RecognitionListener listener =
    new RecognitionListener() {
      @Override
      public void onReadyForSpeech(Bundle bundle) {
        Log.d("Speech", "onBeginningOfSpeech");
      }

      @Override
      public void onBeginningOfSpeech() {
        Log.d("Speech", "onBeginningOfSpeech");
      }

      @Override
      public void onRmsChanged(float v) {
        Log.d("Speech", "onRmsChanged");
      }

      @Override
      public void onBufferReceived(byte[] bytes) {
        Log.d("Speech", "onBufferReceived");
      }

      @Override
      public void onEndOfSpeech() {
        Log.d("Speech", "onEndOfSpeech");
      }

      @Override
      public void onError(int i) {
        Log.d("Speech", "onError");
      }

      @Override
      public void onResults(Bundle bundle) {
        Log.d("Speech", "onResults");
        ArrayList<String> words = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

        StringBuilder sentence = new StringBuilder();
        for (String word : words) {
          Log.d("Speech", "result=" + word);
          sentence.append(word);
        }
      }

      @Override
      public void onPartialResults(Bundle bundle) {
        Log.d("Speech", "onPartialResults");
      }

      @Override
      public void onEvent(int i, Bundle bundle) {
        Log.d("Speech", "onEvent");
      }
    };

  private final ActivityResultLauncher<String> requestPermissionLauncher =
    registerForActivityResult(
      new ActivityResultContracts.RequestPermission(),
      isGranted -> {
        if (isGranted) {
          startSpeech();
        } else {
          Snackbar.make(layout, "denied", Snackbar.LENGTH_SHORT).show();
        }
      });

  @RequiresApi(api = Build.VERSION_CODES.M)
  private void requestAudioPermission() {
    if (shouldShowRequestPermissionRationale(Manifest.permission.RECORD_AUDIO)) {
      Snackbar.make(layout, "permission required", Snackbar.LENGTH_INDEFINITE)
        .setAction(
          "OK",
          view -> {
            requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO);
          })
        .show();
    } else {
      Snackbar.make(layout, "permission not available", Snackbar.LENGTH_LONG)
        .setAction(
          "OK",
          view -> {
            requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO);
          })
        .show();
    }
  }

  private void startSpeech() {
    Toast.makeText(this, "start", Toast.LENGTH_SHORT).show();

    Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
    intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");

    SpeechRecognizer recognizer = SpeechRecognizer.createSpeechRecognizer(getApplicationContext());
    recognizer.setRecognitionListener(listener);
    recognizer.startListening(intent);
  }


}
