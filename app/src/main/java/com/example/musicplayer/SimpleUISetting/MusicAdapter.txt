package com.example.bitroundradio.Adapter;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.liky.music.R;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MusicAdapter extends BaseAdapter {

    private List<View> allViews = new ArrayList<View>();

    private Context ctx;

    public MusicAdapter(List<Map<String, Object>> allValues, Context ctx) {
        this.ctx = ctx;
        // 循环建立View
        Iterator<Map<String, Object>> iter = allValues.iterator();
        while (iter.hasNext()) {
            Map<String, Object> map = iter.next();

            View v = LayoutInflater.from(ctx).inflate(R.layout.song_line, null);
            TextView songName = (TextView) v.findViewById(R.id.song_name);
            songName.setText(map.get("songName").toString());

            allViews.add(v);
        }
    }

    public void setSelectedBackground(int index) {
        // 其他的索引全部清空成为白色
        for (int i = 0; i < allViews.size(); i++) {
            if (i == index) {
                allViews.get(index).setBackgroundColor(Color.RED);
            } else {
                allViews.get(i).setBackgroundColor(Color.WHITE);
            }
        }
    }

    @Override
    public int getCount() {
        return allViews.size();
    }

    @Override
    public Object getItem(int position) {
        return allViews.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return allViews.get(position);
    }

}