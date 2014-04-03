package com.example.exam;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.exam.SQLiteHelper.*;

import java.util.ArrayList;

public class DBStorage {
    private SQLiteDatabase database;

    public DBStorage(Context context) {
        SQLiteHelper helper = new SQLiteHelper(context);
        database = helper.getWritableDatabase();
    }

    public void destroy() {
        database.close();
    }

    public Car[] getCars() {
        Cursor cursor = null;
        try {
            cursor = database.query(SQLiteHelper.TABLE_CARS,
                    new String[]{CarCols.ID, CarCols.MODEL, CarCols.COLOR, CarCols.NUMBER,
                            CarCols.PHONE, CarCols.TIME, CarCols.BOX},
                    null, null, null, null, null);
            ArrayList<Car> answer = new ArrayList<Car>();
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                answer.add(new Car(cursor.getLong(0), cursor.getString(1), cursor.getString(2),
                        cursor.getString(3), cursor.getString(4), cursor.getInt(5), cursor.getInt(6)));
                cursor.moveToNext();
            }
            return answer.toArray(new Car[answer.size()]);
        } finally {
            if (cursor != null)
                cursor.close();
        }
    }

    public long addCar(String m, String c, String n, String p, int t, int b) {
        ContentValues values = new ContentValues();
        values.put(CarCols.MODEL, m);
        values.put(CarCols.COLOR, c);
        values.put(CarCols.NUMBER, n);
        values.put(CarCols.PHONE, p);
        values.put(CarCols.TIME, t);
        values.put(CarCols.BOX, b);
        return database.insert(SQLiteHelper.TABLE_CARS, null, values);
    }

    public int[] getTimeList() {
        Cursor cursor = null;
        int times[] = new int[28];
        try {
            cursor = database.query(SQLiteHelper.TABLE_CARS, new String[]{CarCols.TIME}, null, null, null, null, null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                times[cursor.getInt(0)]++;
                cursor.moveToNext();
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return times;
    }

    public boolean changeTime(long id, int t, int b) {
        String where = CarCols.ID + " = " + id;
        ContentValues values = new ContentValues();
        values.put(CarCols.TIME, t);
        values.put(CarCols.BOX, b);
        return database.update(SQLiteHelper.TABLE_CARS, values, where, null) > 0;
    }

    public boolean deleteCar(Car car) {
        String where = CarCols.ID + " = " + car.id;
        return database.delete(SQLiteHelper.TABLE_CARS, where, null) > 0;
    }
}
