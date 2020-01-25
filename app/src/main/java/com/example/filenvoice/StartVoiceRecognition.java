package com.example.filenvoice;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.util.List;

public class StartVoiceRecognition extends Activity {
    private final int REQUEST_SPEECH_RECOGNIZER = 3000;
    private TextView mTextView;
    private final String mQuestion = "Which company is the largest online retailer on the planet?";
    private String mAnswer;
    private ImageView imageView;
    private int MY_PERMISSIONS_REQUEST_READ_STORAGE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_voice_recognition);
        mTextView = findViewById(R.id.textView);
        imageView = findViewById(R.id.imageView);
        startSpeechRecognizer();
    }

    private void startSpeechRecognizer() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, mQuestion);
        startActivityForResult(intent, REQUEST_SPEECH_RECOGNIZER);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_SPEECH_RECOGNIZER) {
            if (resultCode == RESULT_OK) {
                List<String> results = data.getStringArrayListExtra
                        (RecognizerIntent.EXTRA_RESULTS);
                mAnswer = results.get(0);
                Toast.makeText(StartVoiceRecognition.this,mAnswer,Toast.LENGTH_LONG);

                if(ContextCompat.checkSelfPermission(StartVoiceRecognition.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(StartVoiceRecognition.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            MY_PERMISSIONS_REQUEST_READ_STORAGE);
                }

                String str = Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+mAnswer+".jpg";
                File file = new File(str);
                Log.i("path_simple",str);

                if(file.exists()){
                    Log.i("message","File exists");

                    Uri path = FileUtils.getUri(file);

                    Intent intent = new Intent(Intent.ACTION_VIEW);

                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

//                    Uri uri = FileManagerProvider.getUriForFile(path);
                    Log.i("path_provided",path.toString());

                    intent.setDataAndType(path, "image/jpg");
                    imageView.setImageURI(path);


                    PackageManager pm = StartVoiceRecognition.this.getPackageManager();
                    if (intent.resolveActivity(pm) != null) {
                        startActivity(intent);
                    }

                }else{
                    Toast.makeText(StartVoiceRecognition.this,"File doesn't exist",Toast.LENGTH_LONG).show();
                    System.out.println("File doesn't exist");
                    Log.i("message","File doesn't exist");
                }


            }
        }
    }
}