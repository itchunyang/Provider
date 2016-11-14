package com.itchunyang.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by lcy on 2016/11/11.
 */

public class DBOpenHelper extends SQLiteOpenHelper {

    public DBOpenHelper(Context context, String name) {
        this(context, name, null, 1);
    }

    public DBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + ContentData.Table.TABLE_NAME + " ( id INTEGER PRIMARY KEY autoincrement," + ContentData.Table.NAME + " varchar(30)," + ContentData.Table.AGE + " varchar(10)," + ContentData.Table.SEX + " varchar(10)," + ContentData.Table.ADDRESS + " varchar(100))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
