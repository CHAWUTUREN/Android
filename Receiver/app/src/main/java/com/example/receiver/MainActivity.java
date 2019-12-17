package com.example.receiver;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final  String TAG = "MainActivity.test";
    private String newId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn_add = findViewById(R.id.btn_add);
        Button btn_delete = findViewById(R.id.btn_delete);
        Button btn_update = findViewById(R.id.btn_update);
        Button btn_query = findViewById(R.id.btn_query);

        btn_add.setOnClickListener(this);
        btn_delete.setOnClickListener(this);
        btn_update.setOnClickListener(this);
        btn_query.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        Uri uri = null;
        switch (view.getId()){
            case R.id.btn_add:
                uri = Uri.parse("content://com.example.wordbook.provider/book");
                ContentValues values = new ContentValues();
//                values.put("name", "A Clash of Kings");
//                values.put("author", "George Martin");
//                values.put("pages", 1040);
//                values.put("price", 22.85);

                values.put("word", "translate");
                values.put("translate", "FanYi");
                values.put("example", "Translate it");

                Uri newUri = getContentResolver().insert(uri, values);
                newId = newUri.getPathSegments().get(1);
                break;
            case R.id.btn_delete:
                uri = Uri.parse("content://com.example.wordbook.provider/book/"+newId);
                getContentResolver().delete(uri, null, null);
                break;
            case R.id.btn_update:
                uri = Uri.parse("content://com.example.wordbook.provider/book/" + newId);
                ContentValues newValues = new ContentValues();
//                newValues.put("name", "A Storm of Swords");
//                newValues.put("pages", 1216);
//                newValues.put("price", 24.05);

                newValues.put("word", "translate");
                newValues.put("example", "Translation");
                getContentResolver().update(uri, newValues, null, null);
                break;
            case R.id.btn_query:
                uri = Uri.parse("content://com.example.wordbook.provider/book");
                Cursor cursor = getContentResolver().query(uri, null, null,
                        null, null, null);
                if (cursor != null){
                    while (cursor.moveToNext()){
                        String word = cursor.getString(cursor.getColumnIndex("word"));
                        String translate = cursor.getString(cursor.getColumnIndex("translate"));
                        String example = cursor.getString(cursor.getColumnIndex("example"));
//                        int pages = cursor.getInt(cursor.getColumnIndex("pages"));
//                        double price = cursor.getDouble(cursor.getColumnIndex("price"));
                        Log.d(TAG, "word:\t" + word + "\ntranslate\t" + translate +
                                "\nexample\t" + example + "\n");
                    }
                }
                break;
            default:
                break;
        }
    }
}
