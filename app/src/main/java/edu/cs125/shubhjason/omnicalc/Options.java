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
import java.util.List;

public class Options extends AppCompatActivity {
    private SpeechRecognizer speechrecognizerforoptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
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
            voicebuttonoptions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent voiceintent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                    voiceintent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                    voiceintent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
                    speechrecognizerforoptions.startListening(voiceintent);

                }
            });

        initializeSpeechRecognizer();


    }

    private void initializeSpeechRecognizer() {
        if (SpeechRecognizer.isRecognitionAvailable(this)) {
            speechrecognizerforoptions = SpeechRecognizer.createSpeechRecognizer(this);
            speechrecognizerforoptions.setRecognitionListener(new RecognitionListener() {
                @Override
                public void onReadyForSpeech(Bundle params) {
                    Log.d("Voicee", "ready for speech");
                }

                @Override
                public void onBeginningOfSpeech() {
                    Log.d("Voicee", "begin speech");
                }

                @Override
                public void onRmsChanged(float rmsdB) {

                }

                @Override
                public void onBufferReceived(byte[] buffer) {

                }

                @Override
                public void onEndOfSpeech() {
                    Log.d("Voicee", "done talking");
                }

                @Override
                public void onError(int error) {

                }

                @Override
                public void onResults(Bundle results) {
                    List<String> inputresults = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                    Log.d("Voice!", inputresults.get(0));
                }

                @Override
                public void onPartialResults(Bundle partialResults) {

                }

                @Override
                public void onEvent(int eventType, Bundle params) {

                }
            });
        }
    }
}
