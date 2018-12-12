package edu.cs125.shubhjason.omnicalc;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class GraphingCalc extends AppCompatActivity {
    private final int Reqcodespeechinput = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graphing_calc);

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
                String mathExpr = AlgebraCalc.makeMathy(newText);
                String theAns = doDerivatives(mathExpr);
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
                    String mathExpr = AlgebraCalc.makeMathy(result.get(0));
                    String theAns = doDerivatives(mathExpr);
                    final  TextView answer = findViewById(R.id.textView3);
                    answer.setText(theAns);
                    break;
                }
            }

        }
    }

    private String doDerivatives(String theExpr) {
        char variable = 'x';
        int varVal = 0;
        char[] equ = theExpr.toCharArray();
        for (char thing : equ) {
            int val = (int) thing;
            if (val >= 97 && val <= 122) {
                if (varVal != 0) {
                    if (val != varVal) {
                        return "Error. Only use 1 variable.";
                    }
                } else {
                    varVal = val;
                    variable = thing;
                }
            }
        }
        // find degree
        String deriv;
        try {
            deriv = AlgebraStuff.findDerivative(theExpr, variable, 2);
        } catch (Exception e) {
            deriv = "Error. Check your equation.";
        }
        return deriv;
    }
}
