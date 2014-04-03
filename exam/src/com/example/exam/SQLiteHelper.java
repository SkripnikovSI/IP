package com.example.exam;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "com.example.exam.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_CARS = "cars";

    public static final String DROP_TABLE_CARS = "drop table if exists " + TABLE_CARS;

    private static final String CREATE_CARS_QUERY = "create table "
            + TABLE_CARS + "("
            + CarCols.ID + " integer not null primary key autoincrement, "
            + CarCols.MODEL + " text not null, "
            + CarCols.COLOR + " text not null, "
            + CarCols.NUMBER + " text not null, "
            + CarCols.PHONE + " text not null, "
            + CarCols.TIME + " integer not null, "
            + CarCols.BOX + " integer not null)";

    public static final class CarCols {
        public static final String ID = "_id";
        public static final String MODEL = "model";
        public static final String COLOR = "color";
        public static final String NUMBER = "number";
        public static final String PHONE = "phone";
        public static final String TIME = "time";
        public static final String BOX = "box";
    }

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CARS_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE_CARS);
        onCreate(db);
    }
}