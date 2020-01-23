package com.example.filenvoice;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import java.io.File;
import java.util.List;

public class StartVoiceRecognition extends Activity {
    private final int REQUEST_SPEECH_RECOGNIZER = 3000;
    private TextView mTextView;
    private final String mQuestion = "Which company is the largest online retailer on the planet?";
    private String mAnswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_voice_recognition);
        mTextView = findViewById(R.id.textView);
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

                File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+mAnswer+".jpg");
                Log.i("path",Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+mAnswer+".jpg");
                if(file.exists()){
                    Log.i("message","File exists");

                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    Uri path = Uri.fromFile(file);
                    Uri path = FileProvider.getUriForFile(StartVoiceRecognition.this,StartVoiceRecognition.this.getApplicationContext().getPackageName()+".provider" ,file);
                    intent.setDataAndType(path, "image/jpg");

//                    try {
                        startActivity(intent);

//                    }
//                    catch (ActivityNotFoundException e) {
//                        Toast.makeText(StartVoiceRecognition.this,"Exception!!!",Toast.LENGTH_SHORT).show();
//                        Log.i("message","File can't be opened");
//
//                    }
                }else{
                    Toast.makeText(StartVoiceRecognition.this,"File doesn't exist",Toast.LENGTH_LONG).show();
                    System.out.println("File doesn't exist");
                    Log.i("message","File doesn't exist");
                }


            }
        }
    }
}