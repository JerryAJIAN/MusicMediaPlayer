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

        /*点击事件*/
        nextIv.setOnClickListener(this);
        playIv.setOnClickListener(this);
        prevIv.setOnClickListener(this);



    }

    public void onClick(View v) {
        switch (v.getId()){
            case R.id.local_music_button_iv_previous:

                break;
            case R.id.local_music_button_iv_play:

                break;
            case R.id.local_music_button_iv_next:

                break;
        }


    }
}