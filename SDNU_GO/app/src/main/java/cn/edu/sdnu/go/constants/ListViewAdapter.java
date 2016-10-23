package cn.edu.sdnu.go.constants;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

import cn.edu.sdnu.go.R;

/**
 * Created by jsj1996m on 2016/8/31.
 */
public class ListViewAdapter extends BaseAdapter {
    private Context context;
    private List list;
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView= LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, null);
        }
        return convertView;
    }
}
