package com.example.mymusicplayer.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class MyDataBaseHelper extends SQLiteOpenHelper {

    private static final String CREATE_MUSICLIST = "create table musicList (" +
            "id integer primary key autoincrement," +
            "listName text," +
            "number integer)";

    private static final String CREATE_LOCAL = "create table musicLocal (" +
            "id integer primary key autoincrement," +
            "musicName text," +
            "musicAdd text," +
            "musicTime integer)";

    private static final String CREATE_LOVE = "create table musicLove (" +
            "id integer primary key autoincrement," +
            "musicName text," +
            "musicAdd text," +
            "musicTime integer)";

    private static final String CREAT = "create table ";

    private static final String TABLE = " ( id integer primary key autoincrement," +
            "musicName text," +
            "musicAdd text," +
            "musicTime integer)";

    private Context mContext;
    //要创建的歌单的名字
    private String list_name;
    //判定歌单是创建还是删除
    private Boolean list_temp = true;

    public MyDataBaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_MUSICLIST);
        sqLiteDatabase.execSQL(CREATE_LOCAL);
        sqLiteDatabase.execSQL(CREATE_LOVE);
        //执行数据库操作，将local表的数据插入
        sqLiteDatabase.execSQL("insert into musicList values(0, 'musicLocal', 0)");
        sqLiteDatabase.execSQL("insert into musicList values(1, 'musicLove', 0)");
        Toast.makeText(mContext, "Create Success", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        if (list_temp){
            sqLiteDatabase.execSQL(CREAT + list_name + TABLE);
        }else{
            sqLiteDatabase.execSQL("Drop table if exists " + list_name);
        }

    }

    public void update(SQLiteDatabase sqLiteOpenHelper, String list_name, Boolean list_temp,
                       int i, int il){
        this.list_name = list_name;
        this.list_temp = list_temp;
        onUpgrade(sqLiteOpenHelper, i, il);
    }
}

