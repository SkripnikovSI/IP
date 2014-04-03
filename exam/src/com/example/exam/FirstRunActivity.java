package com.example.exam;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class FirstRunActivity extends Activity {
    private SharedPreferences sp;
    private EditText name;
    private EditText boxNumber;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_run_activity);
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        name = (EditText) findViewById(R.id.edit_text_name);
        boxNumber = (EditText) findViewById(R.id.edit_text_box_number);
    }

    public void onClickCancel(View v) {
        finish();
    }

    public void onClickOk(View v) {
        String n = "";
        int b = 0;
        try {
            n = name.getText().toString();
            b = Integer.valueOf(boxNumber.getText().toString());
        } catch (Exception ignored) {
        }
        if (!"".equals(n) && b > 0) {
            SharedPreferences.Editor editor = sp.edit();
            editor.putString(MainActivity.PREFERENCE_ITEM_NAME, n);
            editor.putInt(MainActivity.PREFERENCE_ITEM_BOX_NUMBER, b);
            editor.apply();
            finish();
        } else {
            Toast.makeText(this, "data fail", Toast.LENGTH_SHORT).show();
        }
    }
}
