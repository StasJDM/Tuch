package com.technologies.tuch.tuch.SQLite;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLiteCreater extends SQLiteOpenHelper{

    public SQLiteCreater(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("MyLogs", "--- onCreate database ---");
        db.execSQL("create table Messages ("
                + "id integer primary key,"
                + "last_message_text text,"
                + "last_message_time text,"
                + "friend text,"
                + "quantity int"
                + ");");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
