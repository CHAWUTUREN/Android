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
import com.example.mymusicplayer.base.Music;

import java.util.List;

public class MusicAdapter extends ArrayAdapter<Music> {

    private int resourceId;
    private List<Music> item_list;

    class ViewHolder{

        TextView music_name;
        TextView music_time;

    }

    public MusicAdapter(Context context, int textViewResourceId,
                        List<Music> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
        item_list = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Music music = getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null){
            view = LayoutInflater.from(getContext())
                    .inflate(resourceId, parent,false);
            viewHolder = new ViewHolder();
            viewHolder.music_name = view.findViewById(R.id.music_name);
            viewHolder.music_time = view.findViewById(R.id.music_time);
            view.setTag(viewHolder);
        }else{
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.music_name.setText(music.getMusic_name());
        viewHolder.music_time.setText(timeToStr(music.getMusic_time()));

        return view;
    }

    private String timeToStr(int time)
    {
        String timeStr ;
        int second = time / 1000 ;
        int minute = second / 60 ;
        second = second - minute * 60 ;
        if (minute > 9)
        {
            timeStr = String.valueOf(minute) + ":" ;
        }else
        {
            timeStr = "0" + String .valueOf(minute) + ":" ;
        }
        if (second > 9)
        {
            timeStr += String.valueOf(second) ;
        }else {
            timeStr += "0" + String.valueOf(second) ;
        }
        return timeStr ;
    }

}
