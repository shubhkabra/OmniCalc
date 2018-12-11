package edu.cs125.shubhjason.omnicalc;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.mariuszgromada.math.mxparser.Expression;
import org.mariuszgromada.math.mxparser.mathcollection.Calculus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class StandardCalc extends AppCompatActivity {
    private final int Reqcodespeechinput = 100;

    private Map<String, String> trig = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_standard_calc);

        trig.put("sign", "sin(");
        trig.put("sine", "sin(");
        trig.put("cosine", "cos(");
        trig.put("tangent", "tan(");
        final FloatingActionButton infostandard = findViewById(R.id.infobuttonstandard);
        infostandard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StandardCalc.this, infoforstandard.class));
            }
        });
        final Button voiceButton = findViewById(R.id.voiceButton);
        voiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptspeechinput();
            }
        });
        final EditText answerThing = findViewById(R.id.edittext);
        Button updatebutton = findViewById(R.id.updatebutton);
        updatebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newText = answerThing.getText().toString();
                final  TextView answer = findViewById(R.id.textView3);
                answer.setText(simpleCalc(newText));
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
                    final EditText answerThing = findViewById(R.id.edittext);
                    answerThing.setText(result.get(0));
                    String input = answerThing.getText().toString();
                    final TextView answer = findViewById(R.id.textView3);
                    answer.setText(simpleCalc(input));
                    break;
                }
            }
        }
    }

    private String simpleCalc(String input) {
        String mathInput = AlgebraCalc.makeMathy(input);
        final EditText answerThing = findViewById(R.id.edittext);
        answerThing.setText(mathInput);
        Expression newExpr = new Expression(mathInput);
        Double result = newExpr.calculate();
        String theAns;
        if (result.isNaN()) {
            theAns = "Error. Invalid Input.";
        } else {
            theAns = result.toString();
        }
        return theAns;
    }

    private String mathy(String input) {
        String better = input;
        Log.d("StanFormat", "Init:" + input);
        for (String spec: trig.keySet()) {
            int sInd = input.indexOf(spec);
            if (sInd != -1) {
                int termEndIndex = better.indexOf(" ", sInd + spec.length() + 1);
                better = better.substring(0, sInd) + trig.get(spec)
                        + better.substring(sInd + spec.length(), termEndIndex) + ")"
                        + better.substring(termEndIndex);

            }
        }
        Log.d("StanFormat", "Fin:" + better);
        return better;
    }
}
