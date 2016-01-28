package com.huangyuanlove.liaoba.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.huangyuanlove.liaoba.R;
import com.huangyuanlove.liaoba.entity.MusicBean;

import java.util.List;

/**
 * Created by huangyuan on 16-1-20.
 */
public class MusicAdapter extends BaseAdapter {

    private List<MusicBean> datas;
    private Context context;

    public MusicAdapter(List<MusicBean> datas, Context context) {
        this.datas = datas;
        this.context = context;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        MusicBean musicBean = datas.get(position);
        if(convertView == null)
        {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_audio,parent,false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.musicName.setText(musicBean.getMusicName());
        viewHolder.musicTime.setText(musicBean.getMusicTime());


        return convertView;
    }


    class ViewHolder{
        TextView musicName,musicTime;

        public ViewHolder(View view)
        {
            musicName = (TextView) view.findViewById(R.id.nameId);
            musicTime = (TextView) view.findViewById(R.id.timeId);
        }

    }
}
