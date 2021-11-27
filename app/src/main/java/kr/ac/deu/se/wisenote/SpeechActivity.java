package kr.ac.deu.se.wisenote;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class SpeechActivity extends AppCompatActivity {

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

          ((TextView) findViewById(R.id.speech_text_view)).setText(sentence);
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

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_speech);

    layout = findViewById(R.id.speech_layout);

    findViewById(R.id.speech_button)
        .setOnClickListener(
            view -> {
              if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                  == PackageManager.PERMISSION_GRANTED) {
                startSpeech();
              } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestAudioPermission();
              }
            });
  }

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
