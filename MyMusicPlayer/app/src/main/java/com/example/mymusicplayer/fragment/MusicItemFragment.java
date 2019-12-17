package com.example.mymusicplayer.fragment;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mymusicplayer.R;
import com.example.mymusicplayer.adapter.MusicItemAdapter;
import com.example.mymusicplayer.base.Music;
import com.example.mymusicplayer.base.MusicItem;
import com.example.mymusicplayer.database.MyDataBaseHelper;
import com.example.mymusicplayer.mymedia.MyMedia;

import java.util.ArrayList;
import java.util.List;

public class MusicItemFragment extends Fragment {

    private String TAG = "MusicItem";

    private Context mContext;
    private MyFragmentManager myFragmentManager;
    private MyDataBaseHelper myDataBaseHelper;
    private MyMedia myMedia;

    //创建歌单列表
    private List<MusicItem> items_list = new ArrayList<>();
    //展示歌单列表用的listview
    private ListView listView;
    private View view;
    private MusicItemAdapter itemAdapter;

    private List<Music> musics_list = new ArrayList<>();
    private MusicFragment musicFragment;

    public MusicItemFragment(){}

    public MusicItemFragment(Context mContext, MyFragmentManager myFragmentManager, MyDataBaseHelper myDataBaseHelper,
                             MyMedia myMedia, List<MusicItem> items_list){
        this.mContext = mContext;
        this.items_list = items_list;
        this.myMedia = myMedia;
        this.myFragmentManager = myFragmentManager;
        this.myDataBaseHelper = myDataBaseHelper;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.music_item_fragment, container, false);

        listView = view.findViewById(R.id.list_music_item);

        //初始化歌单列表的adapter
        itemAdapter = new MusicItemAdapter(mContext, R.layout.music_item_item, items_list);
        listView.setAdapter(itemAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //处理歌单list单击事件
                initMusic(items_list.get(i).getItem_name());
                //musicFragment = new MusicFragment(//content， //音乐列表， //mymedia);
                musicFragment = new MusicFragment(mContext, myMedia, musics_list);
                myFragmentManager.replaceFragment(musicFragment);
            }
        });
        return view;
    }

    //让主界面获取音乐列表
    public List<Music> getMusics_list() {
        return musics_list;
    }

    //让主界面获取到音乐列表的Fragment;
    public MusicFragment getMusicFragment() {
        return musicFragment;
    }

    //刷新歌单列表
    public void refresh(List<MusicItem> items_list){
        this.items_list = items_list;
        itemAdapter.notifyDataSetChanged();
    }

    //初始化歌单的音乐列表
    public void initMusic(String item_name){
        musics_list.clear();
        SQLiteDatabase sqLiteDatabase = myDataBaseHelper.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.query(item_name, null, null,
                null, null, null, null);
        if (cursor.moveToFirst()){
            do{
                String music_name = cursor.getString(cursor.getColumnIndex("musicName"));
                String music_pwd = cursor.getString(cursor.getColumnIndex("musicAdd"));
                int music_time= cursor.getInt(cursor.getColumnIndex("musicTime"));
                Music music = new Music(music_name, music_pwd, music_time);
                musics_list.add(music);
            }while (cursor.moveToNext());
            cursor.close();
        }
    }
}
