package com.example.musicplayer;

public class LocalMusicBean {
    private String id;//歌曲ID
    private String song;//歌曲名称
    private String sinnger;//歌手
    private String album;//专辑
    private String duration;//歌曲时长
    private String path;//歌曲存储位置


    public LocalMusicBean() {
    }


    public LocalMusicBean(String id, String song, String sinnger, String album, String duration, String path) {
        this.id = id;
        this.song = song;
        this.sinnger = sinnger;
        this.album = album;
        this.duration = duration;
        this.path = path;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSong() {
        return song;
    }

    public void setSong(String song) {
        this.song = song;
    }

    public String getSinnger() {
        return sinnger;
    }

    public void setSinnger(String sinnger) {
        this.sinnger = sinnger;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
