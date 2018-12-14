package edu.cs125.shubhjason.omnicalc;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

        final FloatingActionButton infoDeriv = findViewById(R.id.infobuttonDerivative);
        infoDeriv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GraphingCalc.this, infoforDerivative.class));
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
                String mathExpr = AlgebraCalc.makeMathy(newText);
                answerThing.setText(mathExpr);
                String theAns = doDerivatives(mathExpr);
                final TextView answer = findViewById(R.id.textView3);
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
                    String mathExpr = AlgebraCalc.makeMathy(result.get(0));
                    answerThing.setText(mathExpr);
                    String theAns = doDerivatives(mathExpr);
                    final TextView answer = findViewById(R.id.textView3);
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
        int degree = 0;
        if (theExpr.indexOf(variable) != -1) {
            degree = 1;
        }
        String[] findDegree = theExpr.split(Character.toString(variable));
        Log.d("Derivative", "leng:" + findDegree.length);
        for (int i = 1; i < findDegree.length; i++) {
            Log.d("Derivative", "str:" + findDegree[i]);
            if (findDegree[i].length() >= 2 && findDegree[i].charAt(0) == '^') {
                String degExp = findDegree[i].substring(1,2);
                int ind = 2;
                while (ind < findDegree[i].length()) {
                    int charVal = (int) findDegree[i].charAt(ind);
                    if (charVal >= 48 && charVal <= 57) {
                        degExp += findDegree[i].substring(ind,ind + 1);
                        ind++;
                    } else {
                        break;
                    }
                }
                int thisDeg = Integer.parseInt(degExp);
                if (thisDeg > degree) {
                    degree = thisDeg;
                }
            }
        }
        Log.d("Derivative", "deg:" + degree);
        String deriv = "d/d" + variable + " = ";
        try {
            deriv += AlgebraStuff.findDerivative(theExpr, variable, degree);
        } catch (Exception e) {
            deriv = "Error. Check your equation.";
        }
        return deriv;
    }
}
