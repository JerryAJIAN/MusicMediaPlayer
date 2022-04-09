package com.example.musicplayer;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;


public class MusicService extends Service {

    private MediaPlayer musicPlayer = new MediaPlayer();
    public static int musicPosition;
    private Timer timer;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "musicservice_onStartCommand",500).show();
        Log.d("MusicService", "onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("MusicService", "_onCreate");
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("action.changesong");
        registerReceiver(updateReceiver, intentFilter);
        timer = new Timer();
    }

    //根据播放信息准备播放音乐
    private void prepareMusic(Intent intent) {
        Log.d("MusicService", "prepareMusic");
        if (MainActivity.MSG == MainActivity.MSG_PLAY ) {
            play();//从头开始播放一首歌
        } else if (MainActivity.MSG == MainActivity.MSG_PAUSE) {
            pause();//暂停播放当前歌曲
        } else if (MainActivity.MSG == MainActivity.MSG_CONTINUEPLAYING) {
            continuePlaying();//继续播放未完的歌曲
        }
    }

    //继续播放
    private void continuePlaying() {
        Log.d("MusicService", "continuePlaying");
        musicPlayer.start();
    }
    //从头开始播放一首歌
    private  void play() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (musicPlayer.isPlaying()) {
                    musicPlayer.stop();
                }
                try {
                    musicPosition = 0;
                    musicPlayer.reset();
                    musicPlayer.setDataSource(MainActivity.currentMusicUrl);
                    musicPlayer.prepare();
                    musicPlayer.start();
                    musicPlayer.setOnCompletionListener(new OnCompletionListener() {

                        @Override
                        public void onCompletion(MediaPlayer arg0) {
                            Intent completeIntent = new Intent();
                            completeIntent.setAction("action.nextsong");
                            sendBroadcast(completeIntent);
                            Log.d("MusicService", "sendBroadcast");
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(MusicService.this, "加载音乐失败，请检查网络链接！",500).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        }).start();

    }

    //暂停播放
    private void pause() {
        Log.d("MusicService", "pause");
        musicPlayer.pause();
        musicPosition = musicPlayer.getCurrentPosition();
        timer.cancel();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (musicPlayer != null) {
            if (musicPlayer.isPlaying()) timer.cancel();
            musicPlayer.stop();
            musicPlayer.release();
        }
        this.unregisterReceiver(updateReceiver);
    }


    private BroadcastReceiver updateReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("action.changesong")) {
                prepareMusic(intent);
            }
        }

    };


}