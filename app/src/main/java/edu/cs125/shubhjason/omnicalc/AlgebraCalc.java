package edu.cs125.shubhjason.omnicalc;

import android.arch.core.util.Function;
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

import net.objecthunter.exp4j.tokenizer.UnknownFunctionOrVariableException;

import org.mariuszgromada.math.mxparser.Expression;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import javax.xml.xpath.XPathExpression;

public class AlgebraCalc extends AppCompatActivity {
    private final int Reqcodespeechinput = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_algebra_calc);
        final FloatingActionButton infoalgebra = findViewById(R.id.infobuttonalgebra);
        infoalgebra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AlgebraCalc.this, infoforalgebra.class));
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
                String fullExpression = answerThing.getText().toString();
                String newExpr = makeMathy(fullExpression);
                boolean triggy = hasTrig(newExpr);
                final TextView answerBox = findViewById(R.id.textView3);
                answerBox.setText(doAlgebra(newExpr, triggy));
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

                    Log.d("Algebra", "Input: " + result.get(0));
                    String fullExpression = result.get(0);
                    String newExpr = makeMathy(fullExpression);
                    boolean triggy = hasTrig(newExpr);
                    Log.d("Algebra", "Transform: " + newExpr);
                    answerThing.setText(newExpr);
                    final TextView answerBox = findViewById(R.id.textView3);
                    answerBox.setText(doAlgebra(newExpr, triggy));
                    break;
                }
            }

        }
    }

    public static String makeMathy(final String tooWordy) {
        HashMap<String, String> keyWords = new HashMap<>();
        keyWords.put("equals", "=");
        keyWords.put("plus", "+");
        keyWords.put("minus", "-");
        keyWords.put("times", "*");
        keyWords.put("divided by ", "/");
        keyWords.put(" squared", "^2");
        keyWords.put(" cubed", "^3");
        keyWords.put(" to the", "^");
        keyWords.put(" second", "2");
        keyWords.put(" third", "3");
        keyWords.put(" fourth", "4");
        keyWords.put(" fifth", "5");
        keyWords.put(" sixth", "6");
        keyWords.put(" seventh", "7");
        keyWords.put(" eighth", "8");
        String lessWordy = new String(tooWordy);
        for (String keyWord: keyWords.keySet()) {
            int ind = lessWordy.indexOf(keyWord);
            Log.d("Transform", "equ: " + lessWordy + " key: " + keyWord + " ind: " + ind);
            if (ind != -1) {
                lessWordy = lessWordy.replace(keyWord, keyWords.get(keyWord));
            }
        }
        for (int i = 0; i < lessWordy.length(); i++) {
            int charVal = (int)lessWordy.charAt(i);
            Log.d("AlgFormat", "char:" + lessWordy.charAt(i));
            if (charVal >= 65 && charVal <= 90) {
                lessWordy = lessWordy.replace(lessWordy.charAt(i), (char) (charVal + 32));
                Log.d("AlgFormat", "replaced:" + lessWordy.charAt(i));
            }
        }
        return lessWordy;
    }

    private static String doAlgebra(String newExpr, boolean trig) {
        if (newExpr.indexOf("=") == -1) {
            //Not an equation
            return "Error. Please input an equation";
        }
        char variable = 'x';
        int varVal = 0;
        char[] equ = newExpr.toCharArray();
        if (!trig) {
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
        }
        String[] sides = newExpr.split("=");
        ArrayList<Double> solutions;
        String printSolns = "No Solution";
        try {
            solutions = AlgebraStuff.solve(sides, variable, trig);
        } catch (Exception e) {
            printSolns = "Error. Check your equation.";
            solutions = null;
        }
        //Log.d("Algebra", "numSoln" + solutions.size());
        if (printSolns.equals("No Solution")) {
            // No error
            if (solutions != null && solutions.size() > 0) {
                // There is a solution
                if (solutions.size() > 40) {
                    // Probably Infinite Solutions
                    printSolns = variable + " = all real numbers";
                } else {
                    printSolns = variable + " = " + solutions.get(0);
                    for (int i = 1; i < solutions.size(); i++) {
                        printSolns += ", " + solutions.get(i);
                    }
                }
            }
        }
        return printSolns;
    }

    private static boolean hasTrig(String expr) {
        String[] TRIG_FUNCTIONS = {"sin", "cos", "tan"};
        for (String trig: TRIG_FUNCTIONS) {
            if (expr.contains(trig)) {
                return true;
            }
        }
        return false;
    }
}
