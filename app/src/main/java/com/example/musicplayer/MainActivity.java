package com.example.musicplayer;


import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    ImageView nextIv,playIv,prevIv;
    TextView  singerTv,songTv;
    RecyclerView musicRv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        /*初始化控件*/
        nextIv = (ImageView) findViewById(R.id.local_music_button_iv_next);
        playIv = (ImageView) findViewById(R.id.local_music_button_iv_play);
        prevIv = (ImageView) findViewById(R.id.local_music_button_iv_previous);
        singerTv = (TextView) findViewById(R.id.local_music_button_tv_singer);
        songTv = (TextView) findViewById(R.id.local_music_button_tv_song);
        musicRv = (RecyclerView) findViewById(R.id.local_music_rv);
    }
}