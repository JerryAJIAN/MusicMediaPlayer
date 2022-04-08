package com.example.bitroundradio.util;

public class Globals {


    public static int SCREEN_WIDTH;
    public static int SCREEN_HEIGHT;
    // 建立一个Map集合, 里面封装了所有扩展名对应的图标图片, 以便进行文件图标的显示
    public static Map<String, Integer> allIconImgs = new HashMap<String, Integer>();

    public static Map<String, String> allSongNameMap = new HashMap<String, String>();

    // 初始化数据库连接
    public static DataBaseConnection dbc;

    public static void init(Activity a) {
        SCREEN_WIDTH = a.getWindowManager().getDefaultDisplay().getWidth();
        SCREEN_HEIGHT = a.getWindowManager().getDefaultDisplay().getHeight();

        dbc = new DataBaseConnection(a);

        // 初始化一些歌曲名称
        allSongNameMap.put("/storage/sdcard/a.mp3", "Fly Me To The Moon");
        allSongNameMap.put("/storage/sdcard/b.mp3", "时间都去哪儿了");
        allSongNameMap.put("/storage/sdcard/c.mp3", "卷珠帘");

        // 初始化所有扩展名和图片的对应关系
        allIconImgs.put("txt", R.drawable.txt_file);
        allIconImgs.put("mp3", R.drawable.mp3_file);
        allIconImgs.put("mp4", R.drawable.mp4_file);
        allIconImgs.put("bmp", R.drawable.image_file);
        allIconImgs.put("gif", R.drawable.image_file);
        allIconImgs.put("png", R.drawable.image_file);
        allIconImgs.put("jpg", R.drawable.image_file);
        allIconImgs.put("dir_open", R.drawable.open_dir);
        allIconImgs.put("dir_close", R.drawable.close_dir);

}
