package edu.cs125.shubhjason.omnicalc;

import android.content.Intent;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class Options extends AppCompatActivity implements RecognitionListener {
    private SpeechRecognizer speechrecognizerforoptions = null;
    private Intent voiceintent;
    private int id;
    private String num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        speechrecognizerforoptions = SpeechRecognizer.createSpeechRecognizer(this);
        speechrecognizerforoptions.setRecognitionListener(this);


        final Button normiebutton = findViewById(R.id.normie);
        normiebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Standardintent = new Intent(Options.this, StandardCalc.class);
                startActivity(Standardintent);
            }
        });
        final Button algebrabutton = findViewById(R.id.Algebra);
        algebrabutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Algebraintent = new Intent(Options.this, AlgebraCalc.class);
                startActivity(Algebraintent);
            }
        });
        final Button graphingbutton = findViewById(R.id.graphing);
        graphingbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Graphingintent = new Intent(Options.this, GraphingCalc.class);
                startActivity(Graphingintent);
            }
        });
        final FloatingActionButton voicebuttonoptions = findViewById(R.id.VoiceButton);
        voiceintent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);


                }
    public void start(View view)
    {
        num = Integer.toString(++id);
        speechrecognizerforoptions.startListening(voiceintent);
        Log.d("Voices", "Starting");
    }

    public void stop(View view)
    {
        speechrecognizerforoptions.stopListening();
        Log.d("Voices", "Stopped");
    }
    @Override public void onReadyForSpeech(Bundle params){
        Log.d("Voices", "Ready for Speech");
    }
    @Override public void onBeginningOfSpeech(){
        Log.d("Voices", "Beginning of speech");
    }
    @Override public void onRmsChanged(float rms_dB){

    }
    @Override public void onBufferReceived(byte[] buffer){

    }
    @Override public void onEndOfSpeech(){
        Log.d("Voices", "End of speech");
    }
    @Override public void onResults(Bundle results){
        List <String> inputresults = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
    }
    @Override public void onPartialResults(Bundle partialResults){

    }
    @Override public void onEvent(int eventType, Bundle params){

    }
    @Override public void onError(int error){

    }
            }


