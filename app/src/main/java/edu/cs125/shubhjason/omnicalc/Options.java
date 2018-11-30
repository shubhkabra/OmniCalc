package edu.cs125.shubhjason.omnicalc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Options extends AppCompatActivity {

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

    }
}
