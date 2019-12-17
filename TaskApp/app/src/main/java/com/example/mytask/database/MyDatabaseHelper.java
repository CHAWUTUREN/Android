package com.example.mytask.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    public static final String CREATE_USER = "create table Users (" +
            "id integer primary key autoincrement," +
            "user text," +
            "pwd text)";

    public static final String CREATE = "create table ";

    public static final String TABLE = " (id integer primary key autoincrement," +
            "name text," +
            "temp text," +
            "task text)";

    private static String tableName;

    private Context mContext;

    public MyDatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_USER);
        Toast.makeText(mContext, "Create Success", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(CREATE + tableName + TABLE);
    }

    public void update(SQLiteDatabase sqLiteOpenHelper, String tableName, int i, int il){
        this.tableName = tableName;
        onUpgrade(sqLiteOpenHelper, i, il);
    }
}
