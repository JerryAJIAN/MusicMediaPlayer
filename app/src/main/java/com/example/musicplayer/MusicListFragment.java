package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.util.Map;

import com.example.comfortmusic_1.MainActivity;
import com.example.comfortmusic_1.R;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AbsListView.LayoutParams;
import android.widget.AdapterView.OnItemClickListener;

public class MusicListFragment extends Fragment {

    private Callbacks mCallbacks;
    public static ListView musicList;//音乐列表
    BaseAdapter musicListAdapter;//音乐列表适配器

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("action.refreshmusiclistFragment");
        this.getActivity().registerReceiver(refreshReceiver, intentFilter);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.musiclist_main, container, false);
        musicList = (ListView) rootView.findViewById(R.id.musiclist);
        musicListAdapter = getAdapter();
        musicList.setAdapter(musicListAdapter);
        musicList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,long id) {//播放选中歌曲
                MainActivity.currentMusicPosition = position;
                MainActivity.isPlaying = true;
                MainActivity.MSG = MainActivity.MSG_PLAY;
                mCallbacks.onItemSelected(position);//回调实现Callbacks的MainActivity.onItemSelected(position);

            }
        });
        musicList.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                // TODO Auto-generated method stub
                return false;
            }

        });

        return rootView;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (!(activity instanceof Callbacks)) {
            throw new IllegalStateException("activity 必须实现Callbacks接口!");
        }
        mCallbacks = (Callbacks)activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }



    public interface Callbacks {
        public void onItemSelected(Integer id);
    }


    public BaseAdapter getAdapter() {

        BaseAdapter adapter = new BaseAdapter() {

            @Override
            public int getCount() {
                return MainActivity.dbMusic.size();
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View view, ViewGroup parent) {
                Map<String, Object> item = MainActivity.dbMusic.get(position);
                LinearLayout line = new LinearLayout(MusicListFragment.this.getActivity());
                line.setOrientation(LinearLayout.VERTICAL);

                LinearLayout upLayout = new LinearLayout(MusicListFragment.this.getActivity());
                upLayout.setOrientation(LinearLayout.HORIZONTAL);

                ImageView musicTag = new ImageView(MusicListFragment.this.getActivity());
                musicTag.setImageResource(R.drawable.music);
                musicTag.setLayoutParams(new LayoutParams(80,80));


                TextView musicTitle = new TextView(MusicListFragment.this.getActivity());
                TextView musicArtist = new TextView(MusicListFragment.this.getActivity());
                TextView musicDuration = new TextView(MusicListFragment.this.getActivity());

                musicTitle.setText((String)item.get("title"));
                musicTitle.setWidth(MainActivity.screenWidth/2);
                musicTitle.setTextSize(18);
                musicTitle.setTextColor(Color.MAGENTA);
                musicTitle.setMaxLines(1);

                musicDuration.setText((String)item.get("duration"));
                musicDuration.setWidth(MainActivity.screenWidth/2);
                musicDuration.setGravity(Gravity.RIGHT);
                musicDuration.setTextColor(Color.MAGENTA);
                musicDuration.setPaddingRelative(0, 0, 50, 0);
                musicDuration.setMaxLines(1);

                musicArtist.setText((String)item.get("artist"));
//              musicArtist.setWidth(MainActivity.screenWidth/4);
                musicArtist.setMaxLines(1);
//              musicArtist.setTextColor(Color.argb(50, 255, 100, 0));
                musicArtist.setPaddingRelative(80, 0, 0, 0);

                upLayout.addView(musicTag);
                upLayout.addView(musicTitle);
                upLayout.addView(musicDuration);
                line.addView(upLayout);
                line.addView(musicArtist);

                return line;
            }

        };
        return adapter;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.getActivity().unregisterReceiver(refreshReceiver);
    }

    private BroadcastReceiver refreshReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("action.refreshmusiclistFragment"))  {
                Log.d("MusicListFragment", "receive refresh music list!");
//                  Toast.makeText(MusicListFragment.this.getActivity(), "receive update progress!", 200).show();
                musicList.setAdapter(getAdapter());
            }
        }
    };

}
