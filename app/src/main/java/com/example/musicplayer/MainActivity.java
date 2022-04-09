package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import com.example.comfortmusic_1.fragment.DownloadFragment;
import com.example.comfortmusic_1.fragment.MusicListFragment;
import com.example.comfortmusic_1.fragment.NetFragment;
import com.example.comfortmusic_1.service.MusicService;


import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ActionBar.Tab;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.app.FragmentTransaction;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RemoteViews;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements MusicListFragment.Callbacks {



    public static boolean isPlaying = false;//全局变量，表征当前是否在播放
    /**
     * 当前播放音乐的信息
     */
    public static int currentMusicPosition;
    public static String currentMusicTitle;
    public static String currentMusicArtist;
    public static String currentMusicDuration;
    public static String currentMusicUrl;
    /**
     * 循环控制－CYCLE表示当前循环模式
     */
    public static final int CYCLE_LIST = 1;
    public static final int CYCLE_RANDOM = 2;
    public static final int CYCLE_SINGLE = 3;
    public static int CYCLE = CYCLE_LIST;
    /**
     * 发送给MusicService的播放信息MSG：从头开始播放／暂停／继续播放／换歌
     */
    public static final int MSG_PLAY = 0;
    public static final int MSG_PAUSE = 1;
    public static final int MSG_CONTINUEPLAYING = 2;
    public static final int MSG_CHANGESONG = 3;
    public static int MSG = MSG_PLAY;

    //屏幕的长度和宽度
    public static int screenWidth;
    public static int screenHeight;
    //上一首歌曲位置，用来判断是从头播放还是继续播放，若prePosition＝currentMusicPosition则继续播放，反之从头播放
    public static int prePosition;
    //程序启动时获取音频数据库中的音乐信息并保存到dbMusic中
    public static List<Map<String, Object>> dbMusic = new ArrayList<Map<String, Object>>();
    public static String TAG = "MainActivity";

    //activity切换fragment的工具
    SectionsPagerAdapter mSectionsPagerAdapter;
    ViewPager mViewPager;

    ActionBar actionBar;
    //tv1, tv2, tv3分别显示三个fragment的标题
    TextView tv1, tv2, tv3, currentMusic;
    //三个fragment的标题下动态表征切换页面的红条，红条宽度－bottomLine＝screenWidth／页面数
    LinearLayout bottomLine;
    int bottomLineWidth;
    //下方播放区域，音乐图标，上一曲，播放／暂停／下一曲
    LinearLayout playArea;
    ImageView playTag;
    ImageButton buttonPre, buttonPlayAndPause, buttonNext;

    //启动MusicService的intent
    Intent ServiceIntent = null;
    //当前页面/fragment
    int currentItem = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Toast.makeText(MainActivity.this, "CYCLE=" + CYCLE, 300).show();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        actionBar = getActionBar();
        actionBar.hide();
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        getScreenWidth();
        initTab();
        initView();
        bottomLine = (LinearLayout) findViewById(R.id.bottomline);
        bottomLine.setX(bottomLineWidth);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }

            @Override
            public void onPageScrolled(int item, float percent, int offset) {
                bottomLine.setX(item * bottomLineWidth +  offset/3);
            }

            @Override
            public void onPageSelected(int position) {
                currentItem = position;
                changeTabState(position);
            }
        });

        mViewPager.setCurrentItem(0);
        registerMyReceiver();
        ServiceIntent = new Intent(this, MusicService.class);
        startService(ServiceIntent);
    }

    private void registerMyReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("action.nextsong");
        intentFilter.addAction("action.play");
        intentFilter.addAction("action.pause");
        intentFilter.addAction("action.pre");
        intentFilter.addAction("action.next");
        registerReceiver(completeReceiver, intentFilter);
    }

    //获取屏幕的长度和高度
    private void getScreenWidth() {
        DisplayMetrics dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
        bottomLineWidth = dm.widthPixels / (mSectionsPagerAdapter.getCount());
    }

    //标红当前fragment标题并添加监听点击即可跳转到对应fragment
    private void initTab() {
        tv1 = (TextView) findViewById(R.id.tab1);
        tv1.setTextColor(Color.RED);
        tv2 = (TextView) findViewById(R.id.tab2);
        tv3 = (TextView) findViewById(R.id.tab3);
        tv1.setOnClickListener(new MyOnClickListener(0));
        tv2.setOnClickListener(new MyOnClickListener(1));
        tv3.setOnClickListener(new MyOnClickListener(2));
    }


    public void initView() {

        playArea = (LinearLayout) findViewById(R.id.playarea);
        currentMusic = (TextView) findViewById(R.id.currentmusic);
        currentMusic.setTextColor(Color.RED);
        buttonPre = (ImageButton) findViewById(R.id.pre);
        buttonPlayAndPause = (ImageButton) findViewById(R.id.playandpause);
        buttonNext = (ImageButton) findViewById(R.id.next);
        playArea.setOnClickListener(new MyPlayListener());
        playArea.setPaddingRelative(0, 0, 0, 0);
        playTag = (ImageView)findViewById(R.id.playtag);
        buttonPre.setOnClickListener(new MyPlayListener());
        buttonPlayAndPause.setOnClickListener(new MyPlayListener());
        buttonNext.setOnClickListener(new MyPlayListener());
        getMusicFromDb();
        //开始默认将dbMusic中的第一首歌设置为当前歌曲
        if(dbMusic.size() != 0) {
            Map<String, Object> map = dbMusic.get(currentMusicPosition);
            currentMusicTitle = (String)map.get("title");
            currentMusicArtist = (String)map.get("artist");
            currentMusicUrl = (String)map.get("url");
            currentMusicDuration =(String)map.get("duration");
            currentMusic.setText(currentMusicTitle + "—" + currentMusicArtist);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu");
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch (position) {
                case 0:
                    fragment = new MusicListFragment();
                    break;
                case 1:
                    fragment = new NetFragment();
                    break;
                case 2:
                    fragment = new DownloadFragment();
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);//R.string.title_section1 = "本地音乐"
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);//R.string.title_section1 = "网络音乐"
                case 2:
                    return getString(R.string.title_section3).toUpperCase(l);//R.string.title_section1 = "下载列表"
            }
            return null;
        }
    }


    //从手机音乐数据库中提取音乐并保存到dbMusic中
    private void getMusicFromDb() {
        if (dbMusic.size() > 0) dbMusic.clear();
        //从sd卡读取歌曲
        Cursor musicCursor1 = this.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        getMusic(musicCursor1);
        //从手机内存中读取歌曲
        Cursor musicCursor2 = this.getContentResolver().query(MediaStore.Audio.Media.INTERNAL_CONTENT_URI,
                null, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        getMusic(musicCursor2);
    }

    private void getMusic(Cursor musicCursor) {
        while (musicCursor.moveToNext()) {
            Map<String, Object> item = new HashMap<String, Object>();
            long id = musicCursor.getLong(musicCursor.getColumnIndex(MediaStore.Audio.Media._ID));
            String title = musicCursor.getString(musicCursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
            String artist = musicCursor.getString(musicCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
            if (artist != null && artist.equals("<unknown>")) {
                continue;
            }
            long duration = musicCursor.getLong(musicCursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
            long size = musicCursor.getLong(musicCursor.getColumnIndex(MediaStore.Audio.Media.SIZE));
            String url = musicCursor.getString(musicCursor.getColumnIndex(MediaStore.Audio.Media.DATA));
            int isMusic = musicCursor.getInt(musicCursor.getColumnIndex(MediaStore.Audio.Media.IS_MUSIC));
            if (isMusic != 0) {
                item.put("id", id);
                item.put("title", title);
                item.put("artist", artist);
                item.put("duration", formatDuration(duration));
                item.put("size", size);
                item.put("url", url);
                Log.d("MainActivity", "MusicTitle = " + title);
                Log.d("MainActivity", "MusicArtist = " + artist);
                Log.d("MainActivity", "MusicUrl = " + url);
                dbMusic.add(item);
            }

        }

    }

    //将毫秒形式的音乐时长信息转化为00:00形式
    public static String formatDuration(long dur) {
        long totalSecond = dur / 1000;
        String minute = totalSecond / 60 + "";
        if (minute.length() < 2) minute = "0" + minute ;
        String second = totalSecond % 60 + "";
        if (second.length() < 2) second = "0" + second;
        return minute + ":" + second;
    }


    //音乐控制按钮的监听
    private class MyPlayListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.pre:
                    pre();
                    break;
                case R.id.playandpause:
                    if (isPlaying) {
                        pause();
                    } else {
                        play();
                    }
                    break;
                case R.id.next:
                    next();
                    break;
                case R.id.playarea:
                    break;
                default:
                    break;
            }
        }
    }


    //通信后台MusicService更新播放信息
    public void notifyPlayService() {
        Log.d("MainActivity", "notifyPlayService");
        Toast.makeText(this, "notify play service" + "position = " + currentMusicPosition , 300).show();
        Log.d("MainActivity", "dbMusic.size() = " + dbMusic.size());
        if (dbMusic.size() == 0) return;
        Map<String, Object> map = dbMusic.get(currentMusicPosition);
        currentMusicTitle = (String)map.get("title");
        currentMusicArtist = (String)map.get("artist");
        currentMusicUrl = (String)map.get("url");
        Log.d("MainActivity", "url = " + currentMusicUrl);
        currentMusicDuration =(String)map.get("duration");

        Intent musicIntent = new Intent();
        musicIntent.setAction("action.changesong");
        this.sendBroadcast(musicIntent);

        refreshPlayState();
    }

    //上一首
    public void pre() {
        Log.d("MainActivity", "pre");
        if (dbMusic.size() == 0) return;

        if (CYCLE == CYCLE_RANDOM) {
            currentMusicPosition = new Random().nextInt(dbMusic.size() - 1);
        }  else if (CYCLE == CYCLE_SINGLE|| CYCLE == CYCLE_LIST) {
            if (currentMusicPosition == 0) {
                currentMusicPosition = dbMusic.size() - 1;
            } else {
                currentMusicPosition--;
            }
        }
        isPlaying = true;
        MSG = MSG_PLAY;
        notifyPlayService();
    }

    //播放
    public void play() {
        Log.d("MainActivity", "play");
        if (prePosition != 0 && prePosition == currentMusicPosition) {
            MSG = MSG_CONTINUEPLAYING;
        } else {
            MSG = MSG_PLAY;
        }
        isPlaying = true;
        notifyPlayService();
    }

    //暂停
    public void pause() {
        Log.d("MainActivity", "pause");
        MSG = MSG_PAUSE;
        prePosition = currentMusicPosition;
        isPlaying = false;
        notifyPlayService();
    }

    //下一首
    public void next() {
        Log.d("MainActivity", "next");
        if (dbMusic.size() == 0) return;
        if (CYCLE == CYCLE_RANDOM) {
            currentMusicPosition = new Random().nextInt(dbMusic.size() - 1);
        }  else if (CYCLE == CYCLE_SINGLE|| CYCLE == CYCLE_LIST) {
            if (currentMusicPosition == dbMusic.size() - 1) {
                currentMusicPosition = 0;
            } else {
                currentMusicPosition++;
            }
        }
        isPlaying = true;
        MSG = MSG_PLAY;
        notifyPlayService();
    }

    //接收到MusicService播放完之后的广播后自动播放下一首
    private void autoChangeSong() {
        Log.d("MainActivity", "autoChangeSong");
        Toast.makeText(MainActivity.this, "MainActivity_autoChangeSong_CYCLE = " + CYCLE, 300).show();
        if (CYCLE == CYCLE_RANDOM) {
            currentMusicPosition = new Random().nextInt(dbMusic.size() - 1);
        }  else if (CYCLE == CYCLE_LIST) {
            if (currentMusicPosition == dbMusic.size() - 1) {
                currentMusicPosition = 0;
            } else {
                currentMusicPosition++;
            }
        }
        isPlaying = true;
        MSG = MSG_PLAY;
        notifyPlayService();
    }

    //更新播放界面状态
    public void refreshPlayState() {
        Log.d("MainActivity", "refreshPlayState");
        currentMusic.setText(currentMusicTitle + "—" + currentMusicArtist);
        currentMusic.requestFocus();
        currentMusic.setMovementMethod(ScrollingMovementMethod.getInstance());
        if (isPlaying) {
            buttonPlayAndPause.setImageResource(android.R.drawable.ic_media_pause);
            //animation
            TranslateAnimation animation= new TranslateAnimation(-playTag.getWidth()-currentMusic.getWidth()
                    ,-currentMusic.getWidth(),0,  0);
            animation.setDuration(10000);
            animation.setRepeatCount(Animation.INFINITE);
            animation.setRepeatMode(Animation.RESTART);
//          currentMusic.startAnimation(animation);
        } else {
            buttonPlayAndPause.setImageResource(android.R.drawable.ic_media_play);
            //clear animation
            currentMusic.clearAnimation();
        }
    }

    //页面/fragment标题的监听类
    public class MyOnClickListener implements View.OnClickListener {

        int item;

        public MyOnClickListener(int item) {
            this.item = item;
        }
        @Override
        public void onClick(View v) {
            mViewPager.setCurrentItem(item);
            changeTabState(item);
            currentItem = item;
        }

    }

    //标红当前页面标题
    public void changeTabState(int item) {
//      Toast.makeText(this, "current item = " + currentItem, 300).show();
        switch (item) {
            case 0:
                tv1.setTextColor(Color.RED);
                tv2.setTextColor(Color.BLACK);
                tv3.setTextColor(Color.BLACK);
                break;
            case 1:
                tv2.setTextColor(Color.RED);
                tv3.setTextColor(Color.BLACK);
                tv1.setTextColor(Color.BLACK);
                break;
            case 2:
                tv3.setTextColor(Color.RED);
                tv1.setTextColor(Color.BLACK);
                tv2.setTextColor(Color.BLACK);
                break;
        }
//      Toast.makeText(this, "item = " + item, 300).show();

    }

    @Override
    public void onItemSelected(Integer id) {//实现MusicListFragment的回调接口
        Log.d("MainActivity", "onItemSelected");
        Log.d("MainActivity", "item" + id + " clicked!");
        notifyPlayService();
    }

    //接受广播并改变播放状态
    private BroadcastReceiver completeReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("action.nextsong")) {
                autoChangeSong();
            } else if (action.equals("action.pre")) {
                pre();
            } else if (action.equals("action.play")) {
                play();
            } else if (action.equals("action.pause")) {
                pause();
            } else if (action.equals("action.next")) {
                next();
            } else if (action.equals("action.exit")) {
                closeApplication();
            }
        }

    };


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {//返回按键监听
        if (keyCode == KeyEvent.KEYCODE_BACK  && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (mViewPager.getCurrentItem() != 0) {
                mViewPager.setCurrentItem(mViewPager.getCurrentItem()-1);
                return false;
            } else {
                closeApplication();
                return false;
            }

        }
        return super.onKeyDown(keyCode, event);
    }

    private void closeApplication() {
        MainActivity.this.finish();
    }
    //转到手机桌面
    public void toHomeTable(){
        Intent intent = new Intent();
        intent.setAction("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.HOME");
        startActivity(intent);
    }

    @Override
    protected void onStop() {
        Log.d("MainActivity", "onStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d("MainActivity", "onStop");
        this.unregisterReceiver(completeReceiver);
        MainActivity.this.stopService(ServiceIntent);
        isPlaying = false;
        super.onDestroy();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

}
