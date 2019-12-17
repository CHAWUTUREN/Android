package com.example.word;

//单词类
public class Word {

    private String name;
    private String translate;
    private String example;

    public Word(String name, String translate, String example){
        this.name = name;
        this.translate = translate;
        this.example = example;
    }

    //验证用的构造函数
    public Word(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTranslate() {
        return translate;
    }

    public void setTranslate(String translate) {
        this.translate = translate;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }
}
