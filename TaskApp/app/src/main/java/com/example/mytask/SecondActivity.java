package com.example.mytask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
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

import com.example.mytask.base.DoingAdapter;
import com.example.mytask.base.MyTask;
import com.example.mytask.baseActivity.ActivityCollector;
import com.example.mytask.baseActivity.BaseActivity;
import com.example.mytask.database.MyDatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class SecondActivity extends BaseActivity implements View.OnClickListener {

    private MyDatabaseHelper myDatabaseHelper;
    private List<MyTask> myTasks = new ArrayList<>();
    private ListView listView;
    private DoingAdapter adapter;
    private Button btn_add;
    private EditText edit_search;
    private String temp = "true";
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
                Toast.makeText(this, "You are already in this interface",
                        Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_done:
                Toast.makeText(this, "You choose Done",
                        Toast.LENGTH_SHORT).show();
                startActivity(nextActivity);
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
        setContentView(R.layout.activity_second);

        String TAG = "Hello";
        btn_add = findViewById(R.id.btn_add);
        btn_add.setOnClickListener(this);

//        initFruits("tableName", temp);

        Intent intent = getIntent();

        int version = 0;
        tableName = intent.getStringExtra("userName");
        version = intent.getIntExtra("version", version);

        nextActivity = new Intent(SecondActivity.this,
                ThridActivity.class);
        nextActivity.putExtra("tableName", tableName);
        nextActivity.putExtra("version", version);

        myDatabaseHelper = new MyDatabaseHelper(this, "MyTask.db",
                    null, version);

        initFruits("");
        listView = findViewById(R.id.doingTask);
        adapter = new DoingAdapter(SecondActivity.this,
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
        switch (view.getId()){
            case R.id.btn_add:
                showAdd();
                Toast.makeText(SecondActivity.this, "New Task",
                        Toast.LENGTH_SHORT).show();
                break;
        }
    }

//    测试用数据
    private void initFruits(String str){
        myTasks.clear();
        SQLiteDatabase db = myDatabaseHelper.getWritableDatabase();
        Cursor cursor = db.query(tableName, null, "name like '%" + str + "%' and temp like 'true'",
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
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(SecondActivity.this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View layout = inflater.inflate(R.layout.show_layout, null);
        alertDialog.setView(layout);

        TextView view_name = layout.findViewById(R.id.view_name);
        TextView view_context = layout.findViewById(R.id.view_context);

        view_name.setText(myTask.getName());
        view_context.setText(myTask.getContext());

        alertDialog.setPositiveButton("已完成", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //null
                changeTemp(myTask, postion);
            }
        });

        alertDialog.setNegativeButton("更改", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                showChange(myTask, postion);
            }
        });

        alertDialog.setNeutralButton("删除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                showDelete(myTask, postion);
            }
        });

        AlertDialog dialog = alertDialog.create();

        dialog.show();
    }

    //show Add
    private void showAdd(){
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(SecondActivity.this);
        final LayoutInflater inflater = LayoutInflater.from(this);
        final View layout = inflater.inflate(R.layout.add_layout, null);
        alertDialog.setView(layout);
        alertDialog.setCancelable(false);

        final EditText edit_name = layout.findViewById(R.id.add_name);
        final EditText edit_context = layout.findViewById(R.id.add_context);

        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //数据库操作，存储。
                String name = edit_name.getText().toString();
                String task = edit_context.getText().toString();
                MyTask newTask = new MyTask(name, temp, task);

                if (!name.isEmpty() && !task.isEmpty()){
                    SQLiteDatabase db = myDatabaseHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put("name", name);
                    values.put("temp", temp);
                    values.put("task", task);
                    db.insert(tableName, null, values);
                    values.clear();
                    myTasks.add(newTask);
//                    initFruits();
                    adapter.notifyDataSetChanged();
//                    listView.setAdapter(adapter);
                }else{
                    Toast.makeText(SecondActivity.this, "You must write something",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(SecondActivity.this, "You Cancel the add",
                        Toast.LENGTH_SHORT).show();
            }
        });


        AlertDialog dialog = alertDialog.create();

        dialog.show();
    }

    private void showDelete(final MyTask myTask, final int postion){
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(SecondActivity.this);
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
                Toast.makeText(SecondActivity.this, "Delete success ",
                        Toast.LENGTH_SHORT).show();
            }
        });

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(SecondActivity.this, "You Cancel the Delete",
                        Toast.LENGTH_SHORT).show();
            }
        });


        AlertDialog dialog = alertDialog.create();

        dialog.show();
    }

    //change
    private void showChange(final MyTask myTask, int i){
        final int postion = i;
        final String name = myTask.getName();
        AlertDialog.Builder change_dialog = new AlertDialog.Builder(SecondActivity.this);
        final LayoutInflater inflater = LayoutInflater.from(this);
        View layout = inflater.inflate(R.layout.add_layout, null);
        change_dialog.setView(layout);
        change_dialog.setCancelable(false);

        final EditText edit_name = layout.findViewById(R.id.add_name);
        final EditText edit_context = layout.findViewById(R.id.add_context);

        edit_name.setText(myTask.getName());
        edit_context.setText(myTask.getContext());

        change_dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //数据库操作，更改
                SQLiteDatabase db = myDatabaseHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                String new_task = edit_name.getText().toString();
                String new_context = edit_context.getText().toString();

                values.put("name", new_task);
                values.put("task", new_context);
                myTasks.get(postion).setName(new_task);
                myTasks.get(postion).setContext(new_context);

                db.update(tableName, values, "name=?", new String[] {name});
                values.clear();
                adapter.notifyDataSetChanged();
            }
        });

        change_dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(SecondActivity.this, "You Cancel this action",
                        Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog change = change_dialog.create();

        change.show();
    }

    private void changeTemp(final MyTask myTask, final int postion){
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(SecondActivity.this);
        alertDialog.setTitle("警告");
        alertDialog.setMessage("确认此任务已完成？");

        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                SQLiteDatabase sqLiteDatabase = myDatabaseHelper.getWritableDatabase();

                ContentValues values = new ContentValues();

                values.put("temp", "false");
                sqLiteDatabase.update(tableName, values, "name=?", new String[] {myTask.getName()});

                myTasks.remove(postion);
                adapter.notifyDataSetChanged();
                Toast.makeText(SecondActivity.this, "任务已完成 ",
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
