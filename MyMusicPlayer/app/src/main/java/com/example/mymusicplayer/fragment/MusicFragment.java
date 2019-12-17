package com.example.mymusicplayer.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mymusicplayer.R;
import com.example.mymusicplayer.adapter.MusicAdapter;
import com.example.mymusicplayer.base.Music;
import com.example.mymusicplayer.mymedia.MyMedia;

import java.util.ArrayList;
import java.util.List;

public class MusicFragment extends Fragment {

    private String TAG = "MusicItem";

    private Context mContext;
    private MyFragmentManager myFragmentManager;
    private MyMedia myMedia;

    private ListView listView;
    private View view;
    private MusicAdapter musicAdapter;
    private List<Music> musics_list = new ArrayList<>();


    public MusicFragment(){}

    public MusicFragment(Context mContext, MyMedia myMedia, List<Music> musics_list){
        this.mContext = mContext;
        this.myMedia = myMedia;
        this.musics_list = musics_list;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.music_item_fragment, container, false);

        listView = view.findViewById(R.id.list_music_item);

        musicAdapter = new MusicAdapter(mContext, R.layout.music_item, musics_list);
        listView.setAdapter(musicAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                myMedia.setPostion(i);
                myMedia.resetMediaPlayer(musics_list.get(i).getMusic_pwd());
                myMedia.setMusicName(musics_list.get(i).getMusic_name());
                Log.d(TAG, "onItemClick: postion is " + myMedia.getPostion());
                myMedia.doPauseStart();
            }
        });
        return view;
    }

    public void reflesh(List<Music> musics_list){
        this.musics_list = musics_list;
        musicAdapter.notifyDataSetChanged();
    }
}
