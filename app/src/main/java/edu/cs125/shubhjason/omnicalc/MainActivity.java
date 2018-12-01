package edu.cs125.shubhjason.omnicalc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    Timer timer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
            Intent OptionsIntent = new Intent(MainActivity.this, Options.class);
            startActivity(OptionsIntent);
            finish();
            }
        }, 4000);

    }

}
