package com.example.mymusicplayer.mymedia;

import android.media.MediaPlayer;
import android.widget.SeekBar;
import android.widget.TextView;

public class MyMedia {

    private MediaPlayer mediaPlayer = new MediaPlayer();
    //一个是最大时长的显示框，一个是歌曲名的显示，一个是进度条的最大
    private SeekBar seekBar;
    private TextView musicName;
    private TextView musicTotalTime;
    private int postion;

    public MyMedia(){}

    public MyMedia(SeekBar seekBar, TextView musicName, TextView musicTotalTime){
        this.seekBar = seekBar;
        this.musicName = musicName;
        this.musicTotalTime = musicTotalTime;
    }

    //初始化
    public void initMediaPlayer(String pwd){
        try{
            mediaPlayer.setDataSource(pwd);
            mediaPlayer.prepare();
            //设置进度条的最大长度
            seekBar.setMax(mediaPlayer.getDuration());
            //设置音乐最大播放时长
            musicTotalTime.setText(timeToStr(mediaPlayer.getDuration()));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //暂停与播放
    public void doPauseStart(){

        if (mediaPlayer.isPlaying()){
            mediaPlayer.pause();
        }else{
            mediaPlayer.start();
        }
    }

    //格式化音乐播放器
    public void resetMediaPlayer(String pwd){
        mediaPlayer.reset();
        initMediaPlayer(pwd);
    }

    //设置正在播放的歌曲的名字
    public void setMusicName(String music_name){
        musicName.setText(music_name);
    }

    public int getPostion() {
        return postion;
    }

    public void setPostion(int postion) {
        this.postion = postion;
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public void setMediaPlayer(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
    }

    public String timeToStr(int time){
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
