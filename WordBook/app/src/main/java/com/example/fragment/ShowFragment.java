package com.example.fragment;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.wordbook.R;

import org.w3c.dom.Text;

public class ShowFragment extends Fragment {

    private String word;
    private String translate;
    private String example;

    public ShowFragment(){
    }

    public ShowFragment(String word, String translate, String example){
        this.word = word;
        this.translate = translate;
        this.example = example;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Configuration cf = this.getResources().getConfiguration();
        int ori = cf.orientation;

        if (ori == cf.ORIENTATION_LANDSCAPE){
            View view = inflater.inflate(R.layout.show_land_layout, container, false);

            TextView view_word = view.findViewById(R.id.show_land_word);
            TextView view_translate = view.findViewById(R.id.show_land_translate);
            TextView view_example = view.findViewById(R.id.show_land_example);

            view_word.setText(word);
            view_translate.setText(translate);
            view_example.setText(example);

            return view;
        }else{
            return null;
        }
    }
}
