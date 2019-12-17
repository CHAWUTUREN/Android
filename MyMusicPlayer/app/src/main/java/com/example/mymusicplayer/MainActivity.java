package com.example.mymusicplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mymusicplayer.base.Music;
import com.example.mymusicplayer.base.MusicItem;
import com.example.mymusicplayer.database.MyDataBaseHelper;
import com.example.mymusicplayer.fragment.MusicFragment;
import com.example.mymusicplayer.fragment.MusicItemFragment;
import com.example.mymusicplayer.fragment.MyFragmentManager;
import com.example.mymusicplayer.mymedia.MyMedia;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    //获取菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //处理菜单点击事件
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.menu_search:
                Toast.makeText(MainActivity.this, "在此处将完成对local表的格式化",
                        Toast.LENGTH_SHORT).show();
                initLocal();
                break;
            case R.id.menu_helper:
                Toast.makeText(MainActivity.this, "这是一个待完成的帮助文档",
                        Toast.LENGTH_SHORT).show();
                break;
        }

        return true;
    }

    //此处留作初始化变量
    private String TAG="What";

    private MyDataBaseHelper myDataBaseHelper;
    private MyFragmentManager myFragmentManager;
    private MyMedia myMedia;

    private Button btn_random;/*控制播放模式*/
    private Button btn_start_pause;/*暂定按键与播放按键*/
    private Button btn_add;/*添加歌单或者歌曲的按键*/
    private TextView view_name;/*用来显示歌曲名的窗口*/
    private TextView now_time;/*歌曲现在播放了多长时间*/
    private TextView total_time;/*歌曲的总时长*/
    private SeekBar seekBar;/*歌曲的进度条*/

    private MusicItemFragment musicItemFragment;
    private MusicFragment musicFragment;

    private List<MusicItem> items_list = new ArrayList<>();
    private List<Music> music_list = new ArrayList<>();

    //歌曲位置的中间变量
    private int postion;
    //播放模式变量，1：顺序播放；2：随机播放；3：单曲循环
    private int temp_play = 1;

    //新进程的信息处理
    private Message message;
    private Handler handler = new Handler(){

        @Override
        public void handleMessage(@NonNull Message msg) {
            seekBar.setProgress(msg.what);
            now_time.setText(myMedia.timeToStr(msg.what));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //对UI界面的元素进行初始化
        initUI();

        //动态的申请media的权限
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.
                WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this, new String[]
                    {Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
        myMedia = new MyMedia(seekBar, view_name, total_time);

        //初始化歌单列表
        initList();

        //创建音乐列表Fragment
        musicItemFragment = new MusicItemFragment(MainActivity.this, myFragmentManager,
                myDataBaseHelper, myMedia, items_list);
        myFragmentManager.replaceFragment(musicItemFragment);
        musicFragment = musicItemFragment.getMusicFragment();
        music_list = musicItemFragment.getMusics_list();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try{
                        message = handler.obtainMessage();
                        message.what = myMedia.getMediaPlayer().getCurrentPosition();
                        handler.sendMessage(message);
                        Thread.sleep(1000);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        seekBar.setOnSeekBarChangeListener(this);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try{
                        if(seekBar.getProgress() ==
                                myMedia.getMediaPlayer().getDuration()){
                            switch (temp_play){
                                case 1:
                                    //列表循环
                                    moveTo(1);
                                    break;
                                case 2:
                                    //随机播放
                                    //moveTo();
                                    Random temp_random = new Random();
                                    int temp = temp_random.nextInt(music_list.size());
                                    moveTo(temp);
                                    break;
                                case 3:
                                    //单曲循环
                                    moveTo(0);
                                    break;
                            }
                        }
                        Thread.sleep(1000);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
//            case R.id.btn_create:
//                //创建数据库
//                myDataBaseHelper.getWritableDatabase();
//                break;
//            case R.id.btn_add:
//                //添加
//                break;
            case R.id.btn_save:
                saveLove();
                break;
            case R.id.btn_start_pause:
                //播放与暂停
                myMedia.doPauseStart();
                break;
            case R.id.btn_before:
                //上一曲
                moveTo(-1);
                break;
            case R.id.btn_next:
                //下一曲
                moveTo(1);
                break;
            case R.id.btn_random:
                //随机播放
                switch (temp_play){
                    case 1:
                        btn_random.setText("随机播放");
                        break;
                    case 2:
                        btn_random.setText("单曲循环");
                        break;
                    case 3:
                        btn_random.setText("列表循环");
                        break;
                }
                changePlay();
                break;
            default:
                break;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        if( myMedia.getMediaPlayer().isPlaying()){
            myMedia.doPauseStart();
        }
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        int now = seekBar.getProgress();
        seekBar.setProgress(now);
        myMedia.getMediaPlayer().seekTo(now);
        myMedia.doPauseStart();
    }

    //动态申请权限时的动作
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(MainActivity.this, "申请成功，欢迎使用",
                            Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(MainActivity.this, "拒绝授权将无法使用应用程序",
                            Toast.LENGTH_LONG).show();
                    finish();
                }
                break;
            default:
                break;
        }
    }

    //初始化UI界面
    private void initUI(){
        view_name = findViewById(R.id.view_name);
        total_time = findViewById(R.id.view_total_time);
        now_time = findViewById(R.id.view_now_time);
        seekBar = findViewById(R.id.seek_time);

        Button btn_before = findViewById(R.id.btn_before);
        btn_start_pause = findViewById(R.id.btn_start_pause);
        Button btn_next = findViewById(R.id.btn_next);
        btn_random = findViewById(R.id.btn_random);
        Button btn_save = findViewById(R.id.btn_save);
//        btn_add = findViewById(R.id.btn_add);
//        Button btn_create = findViewById(R.id.btn_create);

        btn_before.setOnClickListener(this);
        btn_start_pause.setOnClickListener(this);
        btn_next.setOnClickListener(this);
        btn_random.setOnClickListener(this);
        btn_save.setOnClickListener(this);
//        btn_add.setOnClickListener(this);
//        btn_create.setOnClickListener(this);

        myDataBaseHelper = new MyDataBaseHelper(this, "MyMusic.db",
                null, 2);
        myFragmentManager = new MyFragmentManager(getSupportFragmentManager());
    }

    //初始化本地歌单的音乐列表
    public void initLocal(){
        int music_number = 0;
        SQLiteDatabase sqLiteDatabase = myDataBaseHelper.getWritableDatabase();
        sqLiteDatabase.delete("musicLocal", null, null);
        ContentValues values = new ContentValues();
        Cursor cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null, null, null,
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        if (cursor.moveToFirst()){
            do{
                String music_name = cursor.getString(
                        cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
                String music_pwd = cursor.getString(
                        cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                int music_time = cursor.getInt(
                        cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
                values.put("musicName", music_name);
                values.put("musicAdd", music_pwd);
                values.put("musicTime", music_time);
                sqLiteDatabase.insert("musicLocal", null, values);
                music_number ++;
                values.clear();
            }while(cursor.moveToNext());
            cursor.close();
        }
        values.put("listName", "musicLocal");
        values.put("number", music_number);
        items_list.get(0).setMusic_numbers(music_number);
        sqLiteDatabase.update("musicList", values, "listName = ?",
                new String[]{ "musicLocal" });
        values.clear();
        musicItemFragment.refresh(items_list);
    }

    //初始化歌单列表
    public void initList(){
        items_list.clear();
        SQLiteDatabase sqLiteDatabase = myDataBaseHelper.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.query("musicList", null, null,
                null, null, null, null);
        if (cursor.moveToFirst()){
            do {
                String item_name = cursor.getString(cursor.getColumnIndex("listName"));
                int music_number = cursor.getInt(cursor.getColumnIndex("number"));
                MusicItem musicItem = new MusicItem(item_name, music_number);
                items_list.add(musicItem);
            }while (cursor.moveToNext());
            cursor.close();
        }
    }

    //收藏到喜欢歌单
    public void saveLove(){
        postion = myMedia.getPostion();

        String loveName;
        String lovePwd;
        int loveTime;

        SQLiteDatabase sqLiteDatabase = myDataBaseHelper.getWritableDatabase();
        Cursor cursorLocal = sqLiteDatabase.query("musicLocal", null,
                "musicName like '" + music_list.get(postion).getMusic_name() + "'",
                null, null, null, null);
        Log.d(TAG, "saveLove: musicName is " + music_list.get(postion).getMusic_name());
        ContentValues values = new ContentValues();
        if (cursorLocal.moveToFirst()){
            do{
                loveName = cursorLocal.getString(cursorLocal.getColumnIndex("musicName"));
                lovePwd = cursorLocal.getString(cursorLocal.getColumnIndex("musicAdd"));
                loveTime = cursorLocal.getInt(cursorLocal.getColumnIndex("musicTime"));
                values.put("musicName", loveName);
                values.put("musicAdd", lovePwd);
                values.put("musicTime", loveTime);
                Log.d(TAG, "saveLove: loveName is " + loveName);
                Log.d(TAG, "saveLove: lovePwd is " + lovePwd);
                Log.d(TAG, "saveLove: loveTime is " + loveTime);
                sqLiteDatabase.insert("musicLove", null, values);
                values.clear();
            }while (cursorLocal.moveToNext());
            cursorLocal.close();
        }

        int music_number = 0;
        Cursor cursorLove = sqLiteDatabase.query("musicList", null, null,
                null, null, null, null);
        if (cursorLove.moveToFirst()){
            do{
                music_number = cursorLove.getInt(cursorLove.getColumnIndex("number"));
            }while (cursorLove.moveToNext());
            cursorLove.close();
        }
        values.put("listName", "musicLove");
        values.put("number", music_number + 1);
        items_list.get(1).setMusic_numbers(music_number + 1);
        sqLiteDatabase.update("musicList", values, "listName = ?",
                new String[]{ "musicLove" });
        values.clear();
        musicItemFragment.refresh(items_list);
    }

    //上一曲与下一曲的处理
    public void moveTo(int change){
        postion = myMedia.getPostion();
        if (postion + change >= music_list.size()){
            postion = 0;
        }else if(postion + change < 0){
            postion = music_list.size() - 1;
        }else{
            postion += change;
        }
        Music music = music_list.get(postion);
        myMedia.setPostion(postion);
        myMedia.resetMediaPlayer(music.getMusic_pwd());
        myMedia.setMusicName(music.getMusic_name());
        myMedia.doPauseStart();
    }

    //播放变量的改变
    public void changePlay(){
        switch (temp_play){
            case 1:
            case 2:
                temp_play += 1;
                break;
            case 3:
                temp_play = 1;
                break;
        }
    }
}
