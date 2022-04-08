package com.example.bitroundradio.Adapter;


//创建adapter,两个简单的adapter,处理文件，和音乐

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.liky.music.R;
import org.liky.music.util.Globals;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class FileAdapter extends BaseAdapter {

    private Context ctx;
    private List<Map<String, Object>> allValues = new ArrayList<Map<String, Object>>();

    public FileAdapter(Context ctx, List<Map<String, Object>> allValues) {
        this.ctx = ctx;
        this.allValues = allValues;
    }

    @Override
    public int getCount() {
        return allValues.size();
    }

    @Override
    public Object getItem(int arg0) {
        return allValues.get(arg0);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(ctx).inflate(R.layout.file_line,
                    null);
            // 设置高度
            convertView.setLayoutParams(new LayoutParams(
                    LayoutParams.MATCH_PARENT, Globals.SCREEN_HEIGHT / 9));
        }

        // 取得组件
        TextView fileImg = (TextView) convertView.findViewById(R.id.file_img);

        fileImg.getLayoutParams().height = Globals.SCREEN_HEIGHT / 9;

        TextView fileName = (TextView) convertView.findViewById(R.id.file_name);

        // 取得数据,设置到组件里
        Map<String, Object> map = allValues.get(position);

        // 设置内容, 文字
        fileName.setText(map.get("fileName").toString());

        // 图片要根据扩展名取得
        String extName = map.get("extName").toString();
        // 取得图片的id
        int imgId = Globals.allIconImgs.get(extName);
        // 设置图片
        fileImg.setBackgroundResource(imgId);

        return convertView;
    }

}