package com.example.exam;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class AddCarActivity extends Activity {
    private EditText model;
    private EditText color;
    private EditText number;
    private EditText phone;
    private Spinner time;
    private DBStorage dbs;
    private ArrayList<Integer> data2InSpin;
    private int times[];

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_car_activity);
        dbs = new DBStorage(getBaseContext());
        times = dbs.getTimeList();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        int boxNumber = sp.getInt(MainActivity.PREFERENCE_ITEM_BOX_NUMBER, 1);
        model = (EditText) findViewById(R.id.edit_text_model);
        color = (EditText) findViewById(R.id.edit_text_color);
        number = (EditText) findViewById(R.id.edit_text_number);
        phone = (EditText) findViewById(R.id.edit_text_phone);
        time = (Spinner) findViewById(R.id.spinner_time);

        ArrayList<String> dataInSpin = new ArrayList<String>();
        data2InSpin = new ArrayList<Integer>();
        for (int i = 0; i < times.length; i++) {
            if (times[i] == boxNumber)
                continue;
            String g = Integer.toString(8 + i / 2);
            if (i % 2 == 0)
                dataInSpin.add(g + " : 00");
            else
                dataInSpin.add(g + " : 30");
            data2InSpin.add(i);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dataInSpin);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        time.setAdapter(adapter);
    }

    public void onClickCancel(View v) {
        dbs.destroy();
        finish();
    }

    public void onClickOk(View v) {
        String m = "";
        String c = "";
        String n = "";
        String p = "";
        int t = -1;
        int b = -1;
        try {
            m = model.getText().toString();
            c = color.getText().toString();
            n = number.getText().toString();
            p = phone.getText().toString();
            t = data2InSpin.get(time.getSelectedItemPosition());
            b = times[t] + 1;
        } catch (Exception ignored) {
        }
        if (!"".equals(m) && !"".equals(c) && !"".equals(n) && !"".equals(p)) {
            dbs.addCar(m, c, n, p, t, b);
            dbs.destroy();
            finish();
        } else {
            Toast.makeText(this, "data fail", Toast.LENGTH_SHORT).show();
        }
    }
}
