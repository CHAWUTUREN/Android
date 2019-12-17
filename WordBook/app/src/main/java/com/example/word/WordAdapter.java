package com.example.word;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.wordbook.R;

import java.util.List;

public class WordAdapter extends ArrayAdapter<Word> {

    private int resourceId;
    private List<Word> mlist;

    class ViewHolder{
        TextView name;
        TextView translate;
        TextView example;
    }

    public WordAdapter( Context context, int textViewResourceId,
                        List<Word> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
        mlist = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Word word = getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null){
            view = LayoutInflater.from(getContext())
                    .inflate(resourceId, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.name = view.findViewById(R.id.item_name);
            view.setTag(viewHolder);
        }else{
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.name.setText(word.getName());
        return view;
    }

}
