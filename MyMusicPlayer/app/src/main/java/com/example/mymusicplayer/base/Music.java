package com.example.mymusicplayer.base;

public class Music {

    private String music_name;
    private String music_pwd;
    private int music_time;

    public Music(){}

    public Music(String music_name, int music_time){
        this.music_name = music_name;
        this.music_time = music_time;
    }

    public Music(String music_name, String music_pwd, int music_time){
        this.music_name = music_name;
        this.music_pwd = music_pwd;
        this.music_time = music_time;
    }

    public int getMusic_time() {
        return music_time;
    }

    public String getMusic_name() {
        return music_name;
    }

    public String getMusic_pwd() {
        return music_pwd;
    }

    public void setMusic_name(String music_name) {
        this.music_name = music_name;
    }

    public void setMusic_pwd(String music_pwd) {
        this.music_pwd = music_pwd;
    }

    public void setMusic_time(int music_time) {
        this.music_time = music_time;
    }
}
