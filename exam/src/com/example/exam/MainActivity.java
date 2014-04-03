package com.example.exam;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class MainActivity extends Activity {
    public static final String PREFERENCE_ITEM_NAME = "name";
    public static final String PREFERENCE_ITEM_BOX_NUMBER = "box_number";
    private String name = "";
    private SharedPreferences sp;
    private ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        if (firstRunCheck()) {
            Intent intent = new Intent(this, FirstRunActivity.class);
            startActivity(intent);
        }
        listView = (ListView) findViewById(R.id.list_view);
        init();
    }

    private void init() {
        setTitle(name);
        DBStorage dbs = new DBStorage(getBaseContext());
        CarsListAdapter cla = new CarsListAdapter(this, dbs.getCars());
        dbs.destroy();
        listView.setAdapter(cla);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position, long id) {
                Intent intent = new Intent(getBaseContext(), EditCarActivity.class);
                Car car = (Car)listView.getAdapter().getItem(position);
                intent.putExtra("data",car);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onRestart() {
        super.onRestart();
        if (firstRunCheck()) {
            finish();
        }
        init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_car:
                Intent intent = new Intent(this, AddCarActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean firstRunCheck() {
        name = sp.getString(PREFERENCE_ITEM_NAME, "");
        return "".equals(name);
    }
}
