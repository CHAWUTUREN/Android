package com.example.mytask;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.ContactsContract;
import android.util.EventLog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mytask.database.MyDatabaseHelper;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private MyDatabaseHelper myDatabaseHelper;

    private EditText userName;
    private EditText password;
    private Button btn_new;
    private Button btn_login;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myDatabaseHelper = new MyDatabaseHelper(this, "MyTask.db",
                    null, 1);

        initUI();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.btn_new:
                showNew();
                break;
            case R.id.btn_login:
                userLogin();
                break;
        }

    }

    private void initUI(){
        userName = findViewById(R.id.user_name);
        password = findViewById(R.id.user_pass);
        btn_login = findViewById(R.id.btn_login);
        btn_new = findViewById(R.id.btn_new);

        btn_new.setOnClickListener(this);
        btn_login.setOnClickListener(this);
    }

    //注册窗口
    private void showNew(){
        final AlertDialog.Builder dia_login = new AlertDialog.Builder(MainActivity.this);
        dia_login.setCancelable(false);
        dia_login.setTitle("Login");
        final LayoutInflater inflater = LayoutInflater.from(this);
        final View layout = inflater.inflate(R.layout.new_user_layout, null);
        dia_login.setView(layout);
        dia_login.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                TextView user = layout.findViewById(R.id.userName);
                TextView pwd = layout.findViewById(R.id.passWord);

                String name = user.getText().toString();
                String pass = pwd.getText().toString();

                Cursor cursor = getCursor(name);
                if (cursor.moveToFirst()){
                    Toast.makeText(MainActivity.this, "用户名已存在",
                            Toast.LENGTH_SHORT).show();
                }else{
                    if ( !name.equals("") && !pass.equals("")){

                        SQLiteDatabase sqLiteDatabase = myDatabaseHelper.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        values.put("user", name);
                        values.put("pwd", pass);
                        sqLiteDatabase.insert("Users", null, values);
                        myDatabaseHelper.update(sqLiteDatabase, name, sqLiteDatabase.getVersion(),
                                sqLiteDatabase.getVersion() + 1);
                        Toast.makeText(MainActivity.this, "注册成功，请登入",
                                Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(MainActivity.this, "用户名或密码不能为空",
                                Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
        dia_login.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(MainActivity.this, "您取消了注册",
                        Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog dialog = dia_login.create();
        dialog.show();
    }

    private void userLogin(){

        String TAG = "Hello";
        String name = userName.getText().toString();
        String pass = password.getText().toString();
        String temp_pass = "";
        SQLiteDatabase sqLiteDatabase = myDatabaseHelper.getWritableDatabase();

        Intent intent = new Intent("com.example.mytask.USER_DOING");

        if ( !name.equals("") && !pass.equals("")){
            Cursor cursor = getCursor(name);
            if(cursor.moveToFirst()){
                temp_pass = cursor.getString(cursor.getColumnIndex("pwd"));
                if(pass.equals(temp_pass)){
                    Toast.makeText(MainActivity.this, "登入成功",
                            Toast.LENGTH_SHORT).show();
                    userName.setText("");
                    password.setText("");
                    intent.putExtra("userName", name);
                    intent.putExtra("version", sqLiteDatabase.getVersion());
                    Log.d(TAG, "onCreate: userName is " + name);
                    Log.d(TAG, "onCreate: version is " + sqLiteDatabase.getVersion());
                    startActivity(intent);
                }else{
                    Toast.makeText(MainActivity.this, "密码错误",
                            Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(MainActivity.this, "用户名不存在",
                        Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(MainActivity.this, "用户名或密码不能为空",
                    Toast.LENGTH_SHORT).show();
        }

    }

    //查询窗口
    private Cursor getCursor(String name){
        SQLiteDatabase sqLiteDatabase = myDatabaseHelper.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.query("Users", null,
                "user = '" + name + "'",
                null, null, null, null);
        return cursor;
    }

}
