package com.example.bitroundradio.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.database.Cursor;

public class MusicDAOUtils {

    public static void insertData(String fullPath) {
        // 先判断数据库中是否有这条数据
        String sql = "SELECT song_name FROM music WHERE full_path = ?";
        Cursor c = Globals.dbc.getReadableDatabase().rawQuery(sql,
                new String[] { fullPath });
        if (!c.moveToFirst()) {
            // 之前没有添加过,需要添加新数据
            sql = "INSERT INTO music VALUES (?,?)";
            Globals.dbc.getWritableDatabase().execSQL(
                    sql,
                    new Object[] { fullPath,
                            Globals.allSongNameMap.get(fullPath) });
        }
        c.close();
    }

    public static List<Map<String, Object>> listData() {
        String sql = "SELECT full_path,song_name FROM music";
        Cursor c = Globals.dbc.getReadableDatabase().rawQuery(sql, null);

        List<Map<String, Object>> allValues = new ArrayList<Map<String, Object>>();
        c.moveToFirst();
        while (!c.isAfterLast()) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("fullPath", c.getString(0));
            map.put("songName", c.getString(1));
            allValues.add(map);

            c.moveToNext();
        }

        c.close();

        return allValues;
    }
}