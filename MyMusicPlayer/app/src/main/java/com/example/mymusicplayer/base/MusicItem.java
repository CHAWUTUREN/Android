package com.example.mymusicplayer.base;

public class MusicItem {

    private String item_name;
    private int music_numbers;

    public MusicItem(){
    }

    public MusicItem(String item_name, int music_numbers){
        this.item_name = item_name;
        this.music_numbers = music_numbers;
    }

    public int getMusic_numbers() {
        return music_numbers;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public void setMusic_numbers(int music_numbers) {
        this.music_numbers = music_numbers;
    }
}
