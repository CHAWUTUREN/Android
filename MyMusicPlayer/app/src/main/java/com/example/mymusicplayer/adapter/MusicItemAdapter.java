package com.example.mymusicplayer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.mymusicplayer.R;
import com.example.mymusicplayer.base.MusicItem;

import java.util.List;

public class MusicItemAdapter extends ArrayAdapter<MusicItem> {
    
    private int resourceId;
    private List<MusicItem> item_list;

    class ViewHolder{

        TextView music_item_name;
        TextView music_numbers;

    }

    public MusicItemAdapter(Context context, int textViewResourceId,
                            List<MusicItem> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
        item_list = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        MusicItem music_item = getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null){
            view = LayoutInflater.from(getContext())
                    .inflate(resourceId, parent,false);
            viewHolder = new ViewHolder();
            viewHolder.music_item_name = view.findViewById(R.id.music_item_name);
            viewHolder.music_numbers = view.findViewById(R.id.music_number);
            view.setTag(viewHolder);
        }else{
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.music_item_name.setText(music_item.getItem_name());
        viewHolder.music_numbers.setText(Integer.toString(music_item.getMusic_numbers()));

        return view;
    }
}
