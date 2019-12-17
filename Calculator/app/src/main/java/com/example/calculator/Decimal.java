package com.example.calculator;

import androidx.annotation.AnimatorRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class Decimal extends BaseActivity {

    //创建视图对应的对象
    private TextView[] textViews;

    //创建按键对应的对象
    private Button[] buttons;

    //当前输入的值
    private String input = "0";

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        Intent intent;

        switch (item.getItemId()){
            case R.id.menu_Cal:
                Toast.makeText(this, "You choose Calculator",
                        Toast.LENGTH_SHORT).show();
                intent = new Intent(Decimal.this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.menu_Dec:
                Toast.makeText(this, "You are already in this interface",
                        Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_help:
                showHelp();
                break;
            case R.id.quit:
                Toast.makeText(this, "Welcome you next time",
                        Toast.LENGTH_SHORT).show();
                ActivityCollector.finishAll();
                break;
            default:
                break;
        }
        return true;
    }

    private void initial(){

        textViews = new TextView[4];
        buttons = new Button[13];

        //创建视窗
        textViews[0] = (TextView) findViewById(R.id.textV_D_0);
        textViews[1] = (TextView) findViewById(R.id.textV_D_1);
        textViews[2] = (TextView) findViewById(R.id.textV_D_2);
        textViews[3] = (TextView) findViewById(R.id.textV_D_3);

        //创建按钮
        buttons[0] = (Button) findViewById(R.id.buttonN_D_0);
        buttons[1] = (Button) findViewById(R.id.buttonN_D_1);
        buttons[2] = (Button) findViewById(R.id.buttonN_D_2);
        buttons[3] = (Button) findViewById(R.id.buttonN_D_3);
        buttons[4] = (Button) findViewById(R.id.buttonN_D_4);
        buttons[5] = (Button) findViewById(R.id.buttonN_D_5);
        buttons[6] = (Button) findViewById(R.id.buttonN_D_6);
        buttons[7] = (Button) findViewById(R.id.buttonN_D_7);
        buttons[8] = (Button) findViewById(R.id.buttonN_D_8);
        buttons[9] = (Button) findViewById(R.id.buttonN_D_9);
        buttons[10] = (Button) findViewById(R.id.button_D_AC);
        buttons[11] = (Button) findViewById(R.id.button_D_Del);
//        buttons[12] = (Button) findViewById(R.id.button_P);
//        buttons[13] = (Button) findViewById(R.id.button_C);
        buttons[12] = (Button) findViewById(R.id.button_D_End);

        MyOnClickHandler myOnClickHandler = new MyOnClickHandler();

        for (Button i : buttons){
            i.setOnClickListener(myOnClickHandler);
        }

    }

    private class MyOnClickHandler implements View.OnClickListener {

        private void calculator(){
            input = textViews[0].getText().toString();

            int N_input = Integer.valueOf(input);
            String N_2_output, N_8_output, N_16_output;

            N_2_output = Integer.toBinaryString(N_input);
            N_8_output = Integer.toOctalString(N_input);
            N_16_output = Integer.toHexString(N_input);

            textViews[1].setText(N_2_output);
            textViews[2].setText(N_8_output);
            textViews[3].setText(N_16_output);

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.buttonN_D_0:
                case R.id.buttonN_D_1:
                case R.id.buttonN_D_2:
                case R.id.buttonN_D_3:
                case R.id.buttonN_D_4:
                case R.id.buttonN_D_5:
                case R.id.buttonN_D_6:
                case R.id.buttonN_D_7:
                case R.id.buttonN_D_8:
                case R.id.buttonN_D_9:
//                case R.id.button_P:
                    input = textViews[0].getText().toString();
                    if (textViews[0].getText().toString().equals("0") &&
                            v.getId() == R.id.buttonN_D_0){
                        textViews[0].setText(input);
                    }else if(textViews[0].getText().toString().equals("0") &&
                            v.getId() != R.id.buttonN_D_0){
                        textViews[0].setText(((Button)v).getText());
                    }
//                    else if(v.getId() == R.id.button_P && input.indexOf(".") != -1){
//                        textViews[0].setText(input);
//                    }
                    else{
                        textViews[0].setText(input + ((Button) v).getText());
                    }
                    break;
                case R.id.button_D_AC:
                    input = "0";
                    for (TextView textView : textViews){
                        textView.setText(input);
                    }
                    break;
                case R.id.button_D_Del:
                    input = textViews[0].getText().toString();
                    if (input.length() - 1 != 0)
                        input = input.substring(0,input.length()-1);
                    else
                        input = "0";
                    for (TextView textView : textViews){
                        textView.setText("0");
                    }
                    textViews[0].setText(input);
                    break;
                case R.id.button_D_End:
                    calculator();
                    break;
                default:
                    break;
            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.decimal_layout);

        initial();
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
