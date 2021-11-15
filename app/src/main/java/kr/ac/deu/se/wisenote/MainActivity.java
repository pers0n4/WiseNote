package kr.ac.deu.se.wisenote;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity
    implements ActivityCompat.OnRequestPermissionsResultCallback {

  private static final int PERMISSION_REQUEST_RECORD = 0;
  private final int REQUEST_SPEECH = 1000;
  private Button bt3;
  private View mainLayout;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    mainLayout = findViewById(R.id.main_layout);

    findViewById(R.id.button).setOnClickListener(view -> prepareSpeech());

    bt3 = (Button) findViewById(R.id.bt3);
    bt3.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {

            Intent intent = new Intent(MainActivity.this, StartActivity.class);

            startActivity(intent);

            finish();
          }
        });
  }

  @Override
  public void onRequestPermissionsResult(
      int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    if (requestCode == PERMISSION_REQUEST_RECORD) {
      if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        Snackbar.make(mainLayout, R.string.record_permission_granted, Snackbar.LENGTH_SHORT).show();
      } else {
        Snackbar.make(mainLayout, R.string.record_permission_denied, Snackbar.LENGTH_SHORT).show();
      }
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (requestCode == REQUEST_SPEECH) {
      if (resultCode == RESULT_OK && data != null) {
        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText(data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).get(0));
      }
    }
  }

  private void prepareSpeech() {
    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
        == PackageManager.PERMISSION_GRANTED) {
      startSpeech();
    } else {
      requestRecordPermission();
    }
  }

  private void requestRecordPermission() {
    if (ActivityCompat.shouldShowRequestPermissionRationale(
        this, Manifest.permission.RECORD_AUDIO)) {
      Snackbar.make(mainLayout, R.string.record_permission_required, Snackbar.LENGTH_INDEFINITE)
          .setAction(
              "OK",
              view ->
                  ActivityCompat.requestPermissions(
                      MainActivity.this,
                      new String[] {Manifest.permission.RECORD_AUDIO},
                      PERMISSION_REQUEST_RECORD))
          .show();
    } else {
      Snackbar.make(mainLayout, R.string.record_permission_not_available, Snackbar.LENGTH_SHORT)
          .show();
      ActivityCompat.requestPermissions(
          this, new String[] {Manifest.permission.RECORD_AUDIO}, PERMISSION_REQUEST_RECORD);
    }
  }

  private void startSpeech() {
    Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
    intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-kr");

    try {
      startActivityForResult(intent, REQUEST_SPEECH);
    } catch (ActivityNotFoundException e) {
      Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
    }
  }
}
