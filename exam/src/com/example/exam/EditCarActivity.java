package com.example.exam;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.*;

import java.util.ArrayList;

public class EditCarActivity extends Activity {
    private TextView model;
    private TextView color;
    private TextView number;
    private TextView phone;
    private TextView timeV;
    private TextView box;
    private Spinner time;
    private DBStorage dbs;
    private ArrayList<Integer> data2InSpin;
    private int times[];
    private Car car;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_car_activity);
        dbs = new DBStorage(getBaseContext());
        times = dbs.getTimeList();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        int boxNumber = sp.getInt(MainActivity.PREFERENCE_ITEM_BOX_NUMBER, 1);
        car = (Car) getIntent().getSerializableExtra("data");
        model = (TextView) findViewById(R.id.text_view_model);
        color = (TextView) findViewById(R.id.text_view_color);
        number = (TextView) findViewById(R.id.text_view_number);
        phone = (TextView) findViewById(R.id.text_view_phone);
        timeV = (TextView) findViewById(R.id.text_view_time);
        box = (TextView) findViewById(R.id.text_view_box);
        time = (Spinner) findViewById(R.id.spinner_time);
        model.setText(car.model);
        color.setText(car.color);
        number.setText(car.number);
        phone.setText(car.phone);
        String g = Integer.toString(8 + car.time / 2);
        if (car.time % 2 == 0)
            timeV.setText(g + " : 00");
        else
            timeV.setText(g + " : 30");
        box.setText(Integer.toString(car.box));
        ArrayList<String> dataInSpin = new ArrayList<String>();
        data2InSpin = new ArrayList<Integer>();
        for (int i = 0; i < times.length; i++) {
            if (times[i] == boxNumber)
                continue;
            g = Integer.toString(8 + i / 2);
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

    public void onClickEdit(View v) {
        int t = data2InSpin.get(time.getSelectedItemPosition());
        int b = times[t] + 1;
        dbs.changeTime(car.id, t, b);
    }

    public void onClickDelete(View v) {
        dbs.deleteCar(car);
        finish();
    }
}
