package com.example.wordbook;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fragment.AddFragment;
import com.example.fragment.EmptyFragment;
import com.example.fragment.ListViewFragment;
import com.example.fragment.ShowFragment;
import com.example.sqlite.MyDataBaseHelper;
import com.example.word.Word;
import com.example.word.WordAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private MyDataBaseHelper dbHelper;
    private List<Word> words = new ArrayList<>();
    private ListView listView;
    private WordAdapter adapter;
    private EditText edit_search;
    private int land_postion;

    private int land_update_temp = 0;
    private int land_delete_temp = 0;
    private Button btn_land_add;
    private Button btn_land_update;
    private Button btn_land_delete;
    private AddFragment addFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new MyDataBaseHelper(this, "WordBook.db",
                        null, 2);

        edit_search = findViewById(R.id.edit_search);
        Button btn_add = findViewById(R.id.btn_add);
        Button btn_search = findViewById(R.id.btn_search);

        btn_land_add = findViewById(R.id.btn_land_add);
        btn_land_update = findViewById(R.id.btn_land_update);
        btn_land_delete = findViewById(R.id.btn_land_delete);

//创建数据库，与更新数据库的按键
//        Button btn_create = findViewById(R.id.create_database);
//        Button btn_update = findViewById(R.id.update_database);
//        btn_create.setOnClickListener(this);
//        btn_update.setOnClickListener(this);

        initFruits("");
        adapter = new WordAdapter(MainActivity.this,
                R.layout.word_item, words);

        Configuration cf = this.getResources().getConfiguration();
        int ori = cf.orientation;

        if (ori == cf.ORIENTATION_LANDSCAPE){
            btn_land_add.setOnClickListener(this);
            btn_land_delete.setOnClickListener(this);
            btn_land_update.setOnClickListener(this);

            listView = findViewById(R.id.list_land_view);
            listView.setAdapter(adapter);
            replaceFragment(new EmptyFragment());
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    land_postion = i;
                    landShowWord();
                }
            });
        }else if(ori == cf.ORIENTATION_PORTRAIT){
            btn_add.setOnClickListener(this);
            listView = findViewById(R.id.list_view);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    showWord(i);
                }
            });
        }
        btn_search.setOnClickListener(this);

//        replaceFragment(new ListViewFragment());

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
//            case R.id.create_database:
//                dbHelper.getWritableDatabase();
//                break;
//            case R.id.update_database:
//                dbHelper = new MyDataBaseHelper(MainActivity.this, "WordBook.db",
//                        null, 2);
//                break;
            case R.id.btn_add:
                showAdd();
                break;
            case R.id.btn_search:
                select();
                break;
            case R.id.btn_land_add:
                landShowAdd();
                break;
            case R.id.btn_land_update:
                switch (land_update_temp){
                    case 0:
                        landShowChange();
                        break;
                    case 1:
//                        judgeLandShow(land_edit_word, land_edit_translate, land_edit_example);
                        judgeLandShow(addFragment);
                        break;
                    case 2:
                        judgeLandChange(addFragment);
                        break;
                    default:
                        break;
                }
                break;
            case R.id.btn_land_delete:
                switch (land_delete_temp){
                    case 0:
                        landDelete();
                        break;
                    case 1:
                        chageBack();
                        Toast.makeText(this, "You Cancel this add",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        chageBack();
                        Toast.makeText(this, "You Cancel this Change",
                                Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
                break;
            default:
                Toast.makeText(MainActivity.this, "Hello",
                        Toast.LENGTH_SHORT).show();
                break;
        }
    }

    //展示单词的详细信息
    public void showWord(int i){
        final int postion = i;
        final Word word = words.get(postion);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View layout = inflater.inflate(R.layout.show_layout, null);
        alertDialog.setView(layout);

        //通过数据库精准查询，获取点击单词的详细信息
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("Book", null, "word = '" + word.getName() + "'",
                null, null, null, null);

        if(cursor.moveToFirst()){
            do{
                String translate = cursor.getString(cursor.getColumnIndex("translate"));
                String example = cursor.getString(cursor.getColumnIndex("example"));
                word.setTranslate(translate);
                word.setExample(example);
            }while (cursor.moveToNext());
        }
        cursor.close();

        TextView view_word = layout.findViewById(R.id.view_name);
        TextView view_translate = layout.findViewById(R.id.view_translate);
        TextView view_example = layout.findViewById(R.id.view_example);

        view_word.setText(word.getName());
        view_translate.setText(word.getTranslate());
        view_example.setText(word.getExample());

        alertDialog.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //null
            }
        });

        alertDialog.setNegativeButton("Change", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                showChange(word, postion);
            }
        });

        alertDialog.setNeutralButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                delete(word, postion);
                Toast.makeText(MainActivity.this, "Delete success ",
                        Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog dialog = alertDialog.create();

        dialog.show();

        Window dialogWindow = dialog.getWindow();
        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams p = dialogWindow.getAttributes();

        p.height = (int) (d.getHeight() * 0.4);
        p.width = (int) (d.getWidth() * 1.0);

        dialogWindow.setAttributes(p);
    }

    private void landShowWord(){
        Word word = words.get(land_postion);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("Book", null, "word = '" + word.getName() + "'",
                null, null, null, null);

        if(cursor.moveToFirst()){
            do{
                String translate = cursor.getString(cursor.getColumnIndex("translate"));
                String example = cursor.getString(cursor.getColumnIndex("example"));
                word.setTranslate(translate);
                word.setExample(example);
            }while (cursor.moveToNext());
        }
        cursor.close();
        replaceFragment(new ShowFragment(word.getName(), word.getTranslate(), word.getExample()));
    }

    //show Add
    private void showAdd(){
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        final LayoutInflater inflater = LayoutInflater.from(this);
        final View layout = inflater.inflate(R.layout.add_layout, null);
        alertDialog.setView(layout);
        alertDialog.setCancelable(false);

        final EditText edit_word = layout.findViewById(R.id.add_name);
        final EditText edit_translate = layout.findViewById(R.id.add_translate);
        final EditText edit_example = layout.findViewById(R.id.add_example);

        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //数据库操作，存储。
                String word = edit_word.getText().toString();
                String translate = edit_translate.getText().toString();
                String example = edit_example.getText().toString();
                Word new_word = new Word(word);

                if (!word.isEmpty() && !translate.isEmpty() && !example.isEmpty()){
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put("word", word);
                    values.put("translate", translate);
                    values.put("example", example);
                    db.insert("Book", null, values);
                    values.clear();
                    words.add(new_word);
//                    initFruits();
                    adapter.notifyDataSetChanged();
//                    listView.setAdapter(adapter);
                }else{
                    Toast.makeText(MainActivity.this, "You must write something",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(MainActivity.this, "You Cancel the add",
                        Toast.LENGTH_SHORT).show();
            }
        });


        AlertDialog dialog = alertDialog.create();

        dialog.show();

        Window dialogWindow = dialog.getWindow();
        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams p = dialogWindow.getAttributes();

        p.height = (int) (d.getHeight() * 0.5);
        p.width = (int) (d.getWidth() * 1.0);

        dialogWindow.setAttributes(p);
    }

    private void landShowAdd(){
        addFragment = new AddFragment();
        replaceFragment(addFragment);
//        land_edit_word = addFragment.getEdit_word();
//        land_edit_translate = addFragment.getEdit_translate();
//        land_edit_example = addFragment.getEdit_example();
        land_delete_temp = 1;
        land_update_temp = 1;
        chageTo();
    }

    private void judgeLandShow(AddFragment fragment){

        String word = fragment.getWord();
        String translate = fragment.getTranslate();
        String example = fragment.getExample();

        Word new_word = new Word(word);
        if (!word.isEmpty() && !translate.isEmpty() && !example.isEmpty()){
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("word", word);
            values.put("translate", translate);
            values.put("example", example);
            db.insert("Book", null, values);
            values.clear();
            words.add(new_word);
//                    initFruits();
            adapter.notifyDataSetChanged();
//                    listView.setAdapter(adapter);
            chageBack();
        }else{
            Toast.makeText(MainActivity.this, "Something is null",
                    Toast.LENGTH_SHORT).show();
        }
    }

    //change
    private void showChange(final Word word, int i){
        final int postion = i;
        final String name = word.getName();
        AlertDialog.Builder change_dialog = new AlertDialog.Builder(MainActivity.this);
        final LayoutInflater inflater = LayoutInflater.from(this);
        View layout = inflater.inflate(R.layout.add_layout, null);
        change_dialog.setView(layout);

        final EditText edit_word = layout.findViewById(R.id.add_name);
        final EditText edit_translate = layout.findViewById(R.id.add_translate);
        final EditText edit_example = layout.findViewById(R.id.add_example);

        edit_word.setText(word.getName());
        edit_translate.setText(word.getTranslate());
        edit_example.setText(word.getExample());

        change_dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //数据库操作，更改
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                String new_word = edit_word.getText().toString();
                String new_translate = edit_translate.getText().toString();
                String new_example = edit_example.getText().toString();

                values.put("word", new_word);
                values.put("translate", new_translate);
                values.put("example", new_example);
                words.get(postion).setName(new_word);

                db.update("Book", values, "word=?", new String[] {name});
                values.clear();
//                initFruits();
                adapter.notifyDataSetChanged();
            }
        });

        change_dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(MainActivity.this, "You Cancel this action",
                        Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog change = change_dialog.create();

        change.show();

        Window dialogWindow = change.getWindow();
        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams p = dialogWindow.getAttributes();

        p.height = (int) (d.getHeight() * 0.5);
        p.width = (int) (d.getWidth() * 1.0);

        dialogWindow.setAttributes(p);
    }

    private void landShowChange(){
        Word word = words.get(land_postion);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("Book", null, "word = '" + word.getName() + "'",
                null, null, null, null);

        if(cursor.moveToFirst()){
            do{
                String translate = cursor.getString(cursor.getColumnIndex("translate"));
                String example = cursor.getString(cursor.getColumnIndex("example"));
                word.setTranslate(translate);
                word.setExample(example);
            }while (cursor.moveToNext());
        }
        cursor.close();
        addFragment = new AddFragment(word.getName(), word.getTranslate(), word.getExample());

        replaceFragment(addFragment);
        land_update_temp = 2;
        land_delete_temp = 2;
        chageTo();
    }

    private void judgeLandChange(AddFragment fragment){
        String word = fragment.getWord();
        String translate = fragment.getTranslate();
        String example = fragment.getExample();

        if (!word.isEmpty() && !translate.isEmpty() && !example.isEmpty()){
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("word", word);
            values.put("translate", translate);
            values.put("example", example);

            db.update("Book", values, "word=?",
                    new String[] {words.get(land_postion).getName()});
            words.get(land_postion).setName(word);
//                    initFruits();
            values.clear();
            adapter.notifyDataSetChanged();
//                    listView.setAdapter(adapter);
            chageBack();
        }else{
            Toast.makeText(MainActivity.this, "Something is null",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void delete(Word word, int postion){
        SQLiteDatabase db =dbHelper.getWritableDatabase();
        db.delete("Book", "word = ?", new String[] {word.getName()});
        words.remove(postion);
        adapter.notifyDataSetChanged();
    }

    private void landDelete(){
        SQLiteDatabase db =dbHelper.getWritableDatabase();
        String word = words.get(land_postion).getName();
        db.delete("Book", "word = ?", new String[] {word});
        words.remove(land_postion);
        adapter.notifyDataSetChanged();
        replaceFragment(new EmptyFragment());
    }

    //Select
    private void select(){
        //通过获取edit_search的数据，开始查询数据库，查询完毕刷新界面。

        Configuration cf = this.getResources().getConfiguration();
        int ori = cf.orientation;

        String str_search = edit_search.getText().toString();
        initFruits(str_search);
        adapter.notifyDataSetChanged();

        if(ori == cf.ORIENTATION_LANDSCAPE){
            replaceFragment(new EmptyFragment());
        }
    }

    //测试用数据
    private void initFruits(String str){
        words.clear();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("Book", null, "word like '%" + str + "%'",
                null, null, null, null);
        if(cursor.moveToFirst()){
            do{
                String name = cursor.getString(cursor.getColumnIndex("word"));
                Word hello = new Word(name);
                words.add(hello);
            }while (cursor.moveToNext());
        }
        cursor.close();
    }

    //展示listView
    public void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.right_land_layout, fragment);
        transaction.commit();
    }

    //更改update以及delete按键的键值并设置监听事件
    private void chageTo(){
        btn_land_update.setText("Ok");
        btn_land_delete.setText("Cancel");
        btn_land_update.setOnClickListener(this);
        btn_land_delete.setOnClickListener(this);
    }

    //恢复update以及delete的键值以及其自身中间变量的值
    private void chageBack(){
        btn_land_update.setText("Update");
        btn_land_delete.setText("Delete");
        land_update_temp = 0;
        land_delete_temp = 0;
        replaceFragment(new EmptyFragment());
    }
}
