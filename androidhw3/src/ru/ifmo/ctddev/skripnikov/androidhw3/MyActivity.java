package ru.ifmo.ctddev.skripnikov.androidhw3;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class MyActivity extends Activity {

    private EditText word;
    private LinearLayout ll;
    ImagesRenderer iGT;

    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.enter);
        word = (EditText) findViewById(R.id.word);
        ll = (LinearLayout) findViewById(R.id.ll);
    }

    public void onSubmitButton(View v) {
        if(iGT != null)
            iGT.cancel(true);
        iGT = new ImagesRenderer(this, ll, word.getText().toString());
        iGT.execute();
    }
}
