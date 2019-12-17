package com.example.mytask.base;

public class MyTask {

    private String name;
    private String temp;
    private String context;

    public MyTask(){}

    public MyTask(String name, String temp, String context){
        this.name = name;
        this.temp = temp;
        this.context = context;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getName() {
        return name;
    }

    public String getContext() {
        return context;
    }

    public String getTemp() {
        return temp;
    }
}
