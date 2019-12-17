package com.example.calculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Stack;

public class MainActivity extends BaseActivity {

    //创建视窗对象
    private TextView[] textViews;

    //创建按键对象
    private Button[] buttons;

    private String input_1, input_2, input_t;
    private Boolean Blean_Del = true;//用于判断是否能够回退
    private Boolean Blean_Pio = true;//用于判断小数点
    private Boolean Blean_Equ = true;
    private int tri = 0;

    //获取菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    //处理菜单的单击事件
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        Intent intent;

        switch (item.getItemId()){
            case R.id.menu_Cal:
                Toast.makeText(this, "You are already in this interface",
                        Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_Dec:
                Toast.makeText(this, "You choose Decimal",
                        Toast.LENGTH_SHORT).show();
                intent = new Intent(MainActivity.this, Decimal.class);
                startActivity(intent);
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

    //初始化各种参数
    private void initial(){
        //初始化视窗
        textViews = new TextView[2];
        textViews[0] = (TextView) findViewById(R.id.textView_M_2);
        textViews[1] = (TextView) findViewById(R.id.textView_M_1);

        //初始化按键
        buttons = new Button[23];
        buttons[0] = (Button) findViewById(R.id.button_M_N0);
        buttons[1] = (Button) findViewById(R.id.button_M_N1);
        buttons[2] = (Button) findViewById(R.id.button_M_N2);
        buttons[3] = (Button) findViewById(R.id.button_M_N3);
        buttons[4] = (Button) findViewById(R.id.button_M_N4);
        buttons[5] = (Button) findViewById(R.id.button_M_N5);
        buttons[6] = (Button) findViewById(R.id.button_M_N6);
        buttons[7] = (Button) findViewById(R.id.button_M_N7);
        buttons[8] = (Button) findViewById(R.id.button_M_N8);
        buttons[9] = (Button) findViewById(R.id.button_M_N9);
        buttons[10] = (Button) findViewById(R.id.button_M_Add);
        buttons[11] = (Button) findViewById(R.id.button_M_Sub);
        buttons[12] = (Button) findViewById(R.id.button_M_Mul);
        buttons[13] = (Button) findViewById(R.id.button_M_Div);
        buttons[14] = (Button) findViewById(R.id.button_Bra_L);
        buttons[15] = (Button) findViewById(R.id.button_Bra_R);
        buttons[16] = (Button) findViewById(R.id.button_M_AC);
        buttons[17] = (Button) findViewById(R.id.button_M_Del);
        buttons[18] = (Button) findViewById(R.id.button_M_P);
        buttons[19] = (Button) findViewById(R.id.button_M_E);
        buttons[20] = (Button) findViewById(R.id.button_Sin);
        buttons[21] = (Button) findViewById(R.id.button_Cos);
        buttons[22] = (Button) findViewById(R.id.button_Tan);

        MyOnClickListener myOnClickListener = new MyOnClickListener();

        for(Button i : buttons)
            i.setOnClickListener(myOnClickListener);

    }


    //处理点击事件，以及运算结果
    private class MyOnClickListener implements View.OnClickListener {

        //输入的最终表达式
        private String expression;

        //数字符集合
        private String str_Num = "0123456789.";

        //操作符集合
        private String str_Opt = "+-*/()#";

        //操作符的栈
        private Stack<Character> opStack = new Stack<>();
        //操作数的栈
        private Stack<Double> numStack = new Stack<>();
        //获取将要进栈的操作符
        private char nowOption;
        //获取栈顶的操作符
        private char topOption;

        //设置运算符优先级
        private int compOpt(char c){
            switch(c){
                case '+':
                case '-':
                    return 1;
                case '*':
                case '/':
                    return 2;
                default:
                    return 0;
            }
        }

        //处理运算结果
        private double getAns(double a, double b, char c){
            double ret = 0;//保存结果
            switch (c) {//运算符
                case '+': {//加法
                    ret = a + b;
                    break;
                }
                case '-': {//减法
                    ret = a - b;
                    break;
                }
                case '*': {//乘法
                    ret = a * b;
                    break;
                }
                case '/': {//除法
                    ret = a / b;
                    break;
                }
            }
            return ret;//返回结果
        }

        //寻找运算符在表达式中的位置
        private int findOpt(String opt, String exp, int start ){
            int index = exp.length();
            int temp;
            for(int i = 0; i < opt.length(); i ++){
                temp = exp.indexOf(opt.charAt(i), start);
                if (temp != -1 && temp < index){
                    index = temp;
                }
            }
            if (index == exp.length()){
                index = -1;
            }
            return index;
        }

        //读取运算表达式
        private void calculator() {

            char nowOp;
            char topOp;
            int start = 0;
            int end;
            Boolean cut = true;
            int cut_i = 0;
            double end_num = 0;

            String exmp = textViews[0].getText().toString();

            String texp = exmp + "#";
            opStack.add('#');

            while(start < texp.length()) {

                if(cut_i == 1) {
                    cut = true;
                }

                end = findOpt(str_Opt, texp, start);

                nowOp = texp.charAt(end);

                cut_i = 1;
                if(nowOp == '(') {
                    opStack.add(nowOp);
                    start = end + 1;
                }else if(nowOp == ')') {
                    topOp = opStack.peek();

                    if (cut) {
                        numStack.add(Double.valueOf(texp.substring(start, end)));
                    }

                    while(topOp != '(') {
                        double b = numStack.pop();
                        double a = numStack.pop();

                        double ret = getAns(a, b, topOp);
                        numStack.add(ret);
                        opStack.pop();
                        topOp = opStack.peek();
                    }
                    opStack.pop();
                    start = end + 1;
                    cut_i = 0;
                    cut = false;
                }else {
                    topOp = opStack.peek();

                    if (cut) {
                        numStack.add(Double.valueOf(texp.substring(start, end)));
                    }

                    while (!(compOpt(topOp) == 0 && compOpt(nowOp) == 0)) {
                        if (compOpt(nowOp) > compOpt(topOp)) {
                            opStack.add(nowOp);
                            start = end + 1;
                            break;
                        }else {
                            double b = numStack.pop();
                            double a = numStack.pop();

                            double ret = getAns(a, b, topOp);
                            numStack.add(ret);
                            opStack.pop();
                            topOp = opStack.peek();
                        }
                    }

                    if(compOpt(topOp) == 0 && compOpt(nowOp) == 0) {
                        opStack.pop();
                        break;
                    }
                }
            }

            if (numStack.size() == 1 && opStack.empty()) {
                end_num = numStack.pop();
            }

            input_1 = Double.toString(end_num);
            input_2 = exmp;

            textViews[1].setText(input_2);
            textViews[0].setText("=" + input_1);

            Blean_Del = false;
            Blean_Equ = false;
        }

        //计算三角函数
        private void tri_calculator(int temp){
            double degree = Math.toRadians(Double.valueOf(textViews[0].getText().toString()));
            double ret = 0;

            switch (temp){
                case 1:
                    ret = Math.sin(degree);
                    break;
                case 2:
                    ret = Math.cos(degree);
                    break;
                case 3:
                    ret = Math.tan(degree);
                default:
                    break;
            }

            input_2 = textViews[0].getText().toString();
            input_1 = Double.toString(ret);

            textViews[0].setText("=" + input_1);
            textViews[1].setText(input_2);
        }

        //设置单击按键时的触发事件
        @Override
        public void onClick(View v) {

            switch (v.getId()){
                case R.id.button_M_N0:
                case R.id.button_M_N1:
                case R.id.button_M_N2:
                case R.id.button_M_N3:
                case R.id.button_M_N4:
                case R.id.button_M_N5:
                case R.id.button_M_N6:
                case R.id.button_M_N7:
                case R.id.button_M_N8:
                case R.id.button_M_N9:
                case R.id.button_M_P:
                    //处理数字键与小数点的输入问题
                    input_1 = textViews[0].getText().toString();
                    if(input_1.equals("0") && v.getId() == R.id.button_M_N0){
                        textViews[0].setText(input_1);
                    }else if(input_1.equals("0") && v.getId() != R.id.button_M_N0 &&
                        v.getId() != R.id.button_M_P){
                        textViews[0].setText(((Button)v).getText());
                    }else if(v.getId() == R.id.button_M_P && !Blean_Pio){
                        textViews[0].setText(input_1);
                    }else{
                        if(((Button) v).getText().toString().equals("."))
                            Blean_Pio = false;
                        textViews[0].setText(input_1 + ((Button) v).getText());
                    }
                    break;
                case R.id.button_M_AC:
                    //设置清空键
                    input_1 = "0";
                    input_2 = "";
                    textViews[0].setText(input_1);
                    textViews[1].setText(input_2);
                    Blean_Pio = true;
                    Blean_Del = true;
                    Blean_Equ = true;
                    break;
                case R.id.button_M_Del:
                    //设置回退键
                    input_1 = textViews[0].getText().toString();
                    input_t = input_1.substring(input_1.length() - 1);
                    if (Blean_Del){//Blean_Del是回退键是否能够运行的一个判定
                        if(input_1.length() - 1 != 0){
                            //将原字符串的长度减一，重新赋值
                            input_1 = input_1.substring(0, input_1.length() - 1);
                            if(input_t.equals(buttons[10].getText().toString()) ||
                                input_t.equals((buttons[11].getText().toString())) ||
                                input_t.equals((buttons[12].getText().toString())) ||
                                input_t.equals((buttons[13].getText().toString()))){
                                Blean_Pio = false;
                            }else if(input_t.equals("."))
                                Blean_Pio = true;
                        }else{
                            input_1 = "0";
                        }
                    }else
                        break;
                    textViews[0].setText(input_1);
                    break;
                case R.id.button_M_Add:
                case R.id.button_M_Sub:
                case R.id.button_M_Mul:
                case R.id.button_M_Div:
                    //处理“+-*/”符号键，以及在符号键输入后小数点的问题
                    Blean_Pio = true;
                    input_1 = textViews[0].getText().toString();
                    input_t = input_1.substring(input_1.length() - 1);
                    if(input_1.indexOf(((Button) v).getText().toString()) != -1
                        && input_t.equals(((Button) v).getText().toString())){
                        textViews[0].setText(input_1);
                    }else if(input_1.indexOf(((Button) v).getText().toString()) == -1
                        && (input_t.equals(buttons[10].getText().toString()) ||
                            input_t.equals((buttons[11].getText().toString())) ||
                            input_t.equals((buttons[12].getText().toString())) ||
                            input_t.equals((buttons[13].getText().toString())))){
                        textViews[0].setText(input_1);
                    }else if(input_1.substring(0, 1).equals("=")){
                        textViews[0].setText(input_1.substring(1) + ((Button) v).getText());
                        textViews[1].setText("");
                        Blean_Equ = true;
                    }else{
                        textViews[0].setText(input_1 + ((Button) v).getText());
                    }
                    break;
                case R.id.button_Bra_L:
                case R.id.button_Bra_R:
                    input_1 = textViews[0].getText().toString();
                    if(!input_1.equals("0")){
                        input_1 = input_1 + ((Button) v).getText().toString();
                    }else{
                        input_1 = ((Button) v).getText().toString();
                    }
                    textViews[0].setText(input_1);
                    break;
                case R.id.button_Sin:
                    tri = 1;
                    tri_calculator(tri);
                    break;
                case R.id.button_Cos:
                    tri = 2;
                    tri_calculator(tri);
                    break;
                case R.id.button_Tan:
                    tri = 3;
                    tri_calculator(tri);
                    break;
                case R.id.button_M_E:
                    if(Blean_Equ){
                        calculator();
                    }
                    break;
                default:
                    break;
            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
