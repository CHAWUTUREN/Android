package com.example.fragment;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.wordbook.R;

public class AddFragment extends Fragment {

    private EditText edit_word;
    private EditText edit_translate;
    private EditText edit_example;

    private String word;
    private String translate;
    private String example;

    public AddFragment(){
    }

    public AddFragment(String word, String translate, String example){
        this.word = word;
        this.translate = translate;
        this.example = example;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        //获取当前屏幕状态的变量
        Configuration cf = this.getResources().getConfiguration();
        int ori = cf.orientation;

        if (ori == cf.ORIENTATION_LANDSCAPE){
            View view = inflater.inflate(R.layout.add_land_layout, container, false);
            edit_word = view.findViewById(R.id.land_edit_word);
            edit_translate = view.findViewById(R.id.land_edit_translate);
            edit_example = view.findViewById(R.id.land_edit_example);

            edit_word.setText(word);
            edit_translate.setText(translate);
            edit_example.setText(example);

            return view;
        }else{
            return null;
        }
    }

    public EditText getEdit_word() {
        return edit_word;
    }

    public EditText getEdit_translate() {
        return edit_translate;
    }

    public EditText getEdit_example() {
        return edit_example;
    }

    public String getWord() {
        word = edit_word.getText().toString();
        return word;
    }

    public String getTranslate() {
        translate = edit_translate.getText().toString();
        return translate;
    }

    public String getExample() {
        example = edit_example.getText().toString();
        return example;
    }
}
