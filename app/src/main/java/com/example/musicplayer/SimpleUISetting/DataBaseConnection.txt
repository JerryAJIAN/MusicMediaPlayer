package com.example.bitroundradio.util;

import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseConnection extends SQLiteOpenHelper {

    public DataBaseConnection(Context ctx) {
        super(ctx, "music.db", null, 1);
    }

    public DataBaseConnection(Context context, String name,
                              CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 建立表
        String sql = "CREATE TABLE music (" +
                "full_path			text			primary key ," +
                "song_name			text			" +
                ");" ;

        db.execSQL(sql);

        //SQLite数据库常见
}
