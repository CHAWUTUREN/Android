package com.example.mytask.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.mytask.R;

import java.util.List;

public class DoingAdapter extends ArrayAdapter<MyTask> {

    private int resourceId;
    private List<MyTask> mlist;

    class ViewHolder{
        TextView name;
    }

    public DoingAdapter(@NonNull Context context, int resource, @NonNull List<MyTask> objects) {
        super(context, resource, objects);
        resourceId = resource;
        mlist = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        MyTask task = getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null){
            view = LayoutInflater
                    .from(getContext()).inflate(resourceId, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.name = view.findViewById(R.id.doing_name);
            view.setTag(viewHolder);
        }else{
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.name.setText(task.getName());
        return view;
    }

}
