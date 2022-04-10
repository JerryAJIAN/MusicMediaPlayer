package com.example.musicplayer;


import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    ImageView nextIv,playIv,prevIv;
    TextView  singerTv,songTv;
    RecyclerView musicRv;

    //数据源

    List<LocalMusicBean> mDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        mDatas = new ArrayList<>();


        //创建适配器
        new LocalMusicAdapter(this,mDatas);
        musicRv.setAdapter(adapter);

        //设置布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        musicRv.setLayoutManager(layoutManager);


        //加载本地数据源
        loadLocalMusicData();
    }

    private void loadLocalMusicData() {
        /*
        加载本地储存音乐文件到集合当中
        **/
        //获取ContentResolver对象
        ContentResolver resolver = getContentResolver();
        //获取本地储存的url地址
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        //开始查询地址
        resolver.query(uri, null, null, null, null);
        //遍历Cursor

        int id = 0;

        which(cursor.moveToNext()){
           String song = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
           String sinnger = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
           String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
           id++;
           String sid = String.valueOf(id);
           String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));

        }


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