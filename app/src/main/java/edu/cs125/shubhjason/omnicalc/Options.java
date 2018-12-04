package edu.cs125.shubhjason.omnicalc;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Options extends AppCompatActivity {
    private TextView textoutput;
    private final int Reqcodespeechinput = 100;

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
        FloatingActionButton voicebuttonoptions = findViewById(R.id.VoiceButton);
        textoutput = findViewById(R.id.Statustext);
        voicebuttonoptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptspeechinput();
            }
        });
    }

    private void promptspeechinput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, Reqcodespeechinput);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(), getString(R.string.speech_not_supported), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case Reqcodespeechinput: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    textoutput.setText(result.get(0));
                    if (result.contains("algebra") || result.contains("Algebra")) {
                        Intent Algebraintent = new Intent(Options.this, AlgebraCalc.class);
                        startActivity(Algebraintent);
                    } else if (result.contains("Standard") || result.contains("standard")) {
                        Intent standardintent = new Intent(Options.this, StandardCalc.class);
                        startActivity(standardintent);
                    } else if (result.contains("graphing") || result.contains("Graphing")) {
                        Intent graphingintent = new Intent(Options.this, GraphingCalc.class);
                        startActivity(graphingintent);
                    }

                    break;
                }
            }

        }
    }

}


