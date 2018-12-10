package edu.cs125.shubhjason.omnicalc;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
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
import java.util.Locale;


public class StandardCalc extends AppCompatActivity {
    private final int Reqcodespeechinput = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_standard_calc);

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
                Expression newExpr = new Expression(newText);
                Double result = newExpr.calculate();
                String theAns;
                if (result.isNaN()) {
                    theAns = "Error.";
                } else {
                    theAns = result.toString();
                }
                final  TextView answer = findViewById(R.id.textView3);
                answer.setText(theAns);
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
                    String bc = answerThing.getText().toString();
                    Expression ab = new Expression(bc);
                    Double xyz = ab.calculate();
                    String theAns;
                    if (xyz.isNaN()) {
                        theAns = "Error.";
                    } else {
                        theAns = xyz.toString();
                    }
                    final TextView answer = findViewById(R.id.textView3);
                    answer.setText(theAns);
                    break;
                }
            }
        }
    }
}
