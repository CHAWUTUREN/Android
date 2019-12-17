package com.example.mytask;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.example.mytask.base.DoingAdapter;
import com.example.mytask.base.MyTask;
import com.example.mytask.baseActivity.ActivityCollector;
import com.example.mytask.baseActivity.BaseActivity;
import com.example.mytask.database.MyDatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class ThridActivity extends BaseActivity implements View.OnClickListener {

    private MyDatabaseHelper myDatabaseHelper;
    private List<MyTask> myTasks = new ArrayList<>();
    private ListView listView;
    private DoingAdapter adapter;
    private Button btn_add;
    private EditText edit_search;
    private String temp = "false";
    private String tableName;
    private Intent nextActivity;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.hello, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_doing:
                Toast.makeText(this, "You choose Doing",
                        Toast.LENGTH_SHORT).show();
                startActivity(nextActivity);
                break;
            case R.id.menu_done:
                Toast.makeText(this, "You are already in this interface",
                        Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_help:
                showHelp();
                break;
            case R.id.menu_quit:
                Toast.makeText(this, "Welcome you next time",
                        Toast.LENGTH_SHORT).show();
                ActivityCollector.finishAll();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thrid);

        String TAG = "Hello";
//        btn_add = findViewById(R.id.btn_add);
//        btn_add.setOnClickListener(this);

//        initFruits("tableName", temp);

        Intent intent = getIntent();

        int version = 0;
        tableName = intent.getStringExtra("tableName");
        version = intent.getIntExtra("version", version);

        nextActivity = new Intent(ThridActivity.this,
                SecondActivity.class);
        nextActivity.putExtra("userName", tableName);
        nextActivity.putExtra("version", version);

        myDatabaseHelper = new MyDatabaseHelper(this, "MyTask.db",
                null, version);

        initFruits("");
        listView = findViewById(R.id.doneTask);
        adapter = new DoingAdapter(ThridActivity.this,
                R.layout.doing_item, myTasks);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                showTask(i);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                MyTask myTask = myTasks.get(i);
                showDelete(myTask, i);
                return false;
            }
        });
    }

    @Override
    public void onClick(View view) {
    }

    //    测试用数据
    private void initFruits(String str){
        myTasks.clear();
        SQLiteDatabase db = myDatabaseHelper.getWritableDatabase();
        Cursor cursor = db.query(tableName, null, "name like '%" + str + "%' and temp like 'false'",
                null, null, null, null);
        if(cursor.moveToFirst()){
            do{
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String temp = cursor.getString(cursor.getColumnIndex("temp"));
                String context = cursor.getString(cursor.getColumnIndex("task"));
                MyTask hello = new MyTask(name, temp, context);
                myTasks.add(hello);
            }while (cursor.moveToNext());
        }
        cursor.close();
    }

    //展示任务的详细信息
    public void showTask(int i){
        final int postion = i;
        final MyTask myTask = myTasks.get(postion);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ThridActivity.this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View layout = inflater.inflate(R.layout.show_layout, null);
        alertDialog.setView(layout);

        TextView view_name = layout.findViewById(R.id.view_name);
        TextView view_context = layout.findViewById(R.id.view_context);

        view_name.setText(myTask.getName());
        view_context.setText(myTask.getContext());

        alertDialog.setPositiveButton("设为未完成", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //null
                changeTemp(myTask, postion);
            }
        });

//        alertDialog.setNegativeButton("更改", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
////                showChange(myTask, postion);
//            }
//        });

        alertDialog.setNeutralButton("删除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                showDelete(myTask, postion);
            }
        });

        AlertDialog dialog = alertDialog.create();

        dialog.show();
    }

    private void showDelete(final MyTask myTask, final int postion){
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(ThridActivity.this);
        alertDialog.setTitle("警告");
        alertDialog.setMessage("确认删除此任务？");

        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                SQLiteDatabase sqLiteDatabase = myDatabaseHelper.getWritableDatabase();

                sqLiteDatabase.delete(tableName, "name = ?",
                        new String[]{myTask.getName()});
                myTasks.remove(postion);
                adapter.notifyDataSetChanged();
                Toast.makeText(ThridActivity.this, "Delete success ",
                        Toast.LENGTH_SHORT).show();
            }
        });

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(ThridActivity.this, "You Cancel the Delete",
                        Toast.LENGTH_SHORT).show();
            }
        });


        AlertDialog dialog = alertDialog.create();

        dialog.show();
    }

    private void changeTemp(final MyTask myTask, final int postion){
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(ThridActivity.this);
        alertDialog.setTitle("警告");
        alertDialog.setMessage("确认将此任务设置为未完成？");

        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                SQLiteDatabase sqLiteDatabase = myDatabaseHelper.getWritableDatabase();

                ContentValues values = new ContentValues();

                values.put("temp", "true");
                sqLiteDatabase.update(tableName, values, "name=?", new String[] {myTask.getName()});

                myTasks.remove(postion);
                adapter.notifyDataSetChanged();
                Toast.makeText(ThridActivity.this, "更改成功 ",
                        Toast.LENGTH_SHORT).show();
            }
        });

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });


        AlertDialog dialog = alertDialog.create();

        dialog.show();
    }

    private void showHelp(){
        AlertDialog.Builder helpDialog = new AlertDialog.Builder(this);
        helpDialog.setTitle("Help");
        helpDialog.setCancelable(true);
        helpDialog.setMessage("There is nothing");
        helpDialog.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        helpDialog.show();

    }
}
