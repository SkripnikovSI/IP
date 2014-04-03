package ru.ifmo.ctddev.skripnikov.lesson6;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.view.Window;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
    }

    public void onClickStart(View v) {
        startService(new Intent(this, PlayService.class));
    }

    public void onClickStop(View v) {
        stopService(new Intent(this, PlayService.class));
    }
}
